package ru.bati4eli.smartcloud.android.client.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.var;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.TabPhotosBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.MonthFastScrollerAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoObserver;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.MonthBucket;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.PhotoItem;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.ItemTypes.VT_HEADER;
import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateItemSize;

public class PhotosFragment extends Fragment implements OnBackPressedListener, OnItemClickListener<PhotoItem> {

    private static final int SPAN_COUNT = 4;
    private static final int PREFETCH_THRESHOLD = 40;   // подгрузить следующий месяц заранее
    private boolean isLoadingMonth = false;
    private int nextMonthIndexToLoad = 0;
    private TabPhotosBinding binding;
    private PhotoAdapter adapter;
    private RecyclerView photosRecyclerView;
    private RecyclerView monthFastScrollRv;             // быстрый скролл по месяцам (горизонтальный)
    private TextView stickyMonthTextView;               // плавающий текущий месяц
    private GridLayoutManager gridLayoutManager;        // быстрый скролл по месяцам (горизонтальный)
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);
    private final List<MonthBucket> monthBuckets = new ArrayList<>();
    private final Map<YearMonth, Integer> headerAdapterPositions = new LinkedHashMap<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isViewActive = false;
    private int targetMonthIndex = -1;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabPhotosBinding.inflate(inflater, container, false);
        photosRecyclerView = binding.photosRecyclerView;
        monthFastScrollRv = binding.monthFastScrollRv;
        stickyMonthTextView = binding.stickyMonthTextView;

        int spacing = getSpacing();
        int itemSize = calculateItemSize(this, spacing, SPAN_COUNT);
        adapter = new PhotoAdapter()
                .setPixelSize(itemSize)
                //.setSwipeRefreshLayout(binding.swipeRefreshLayout)
                .setListener(this);
        gridLayoutManager = new GridLayoutManager(requireContext(), SPAN_COUNT);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int viewType = adapter.getItemViewType(position);
                return viewType == VT_HEADER ? SPAN_COUNT : 1;
            }
        });

        photosRecyclerView.setLayoutManager(gridLayoutManager);
        photosRecyclerView.setAdapter(adapter);
        isViewActive = true;

        setupScrollListeners();
        loadMonthCounters();

        // binding.swipeRefreshLayout.setOnRefreshListener(this::updateSubFiles);
        // updateSubFiles();
        return binding.getRoot();
    }

    /**
     * отступы между плитками.
     */
    private int getSpacing() {
        return super.getResources().getDimensionPixelSize(R.dimen.photo_spacing);
    }

    @SuppressLint("NewApi")
    private void updateSubFiles() {
        //todo  adapter.start();
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {

    }

    @Override
    public void onItemClick(int position, PhotoItem item) {
        ShortMediaInfoDto photo = item.getPhoto();
        Toast.makeText(getActivity(), "Ты кликнул FileId: " + photo.getFileId(), Toast.LENGTH_SHORT).show();
    }

    // ----------------------------------------
    // Загрузка данных по месяцам
    // ----------------------------------------
    @SuppressLint("NewApi")
    private void loadMonthCounters() {
        // Получаем итератор счетчиков месяцев
        grpcService.getMonthCounters().forEachRemaining(counter -> monthBuckets.add(new MonthBucket(counter)));
        // Преобразуем в YearMonth + amount, сортируем по убыванию даты (новые сначала)
        monthBuckets.sort(Comparator.comparing(MonthBucket::getYearMonth).reversed());
        // Инициализируем быстрый скроллер по месяцам
        setupMonthFastScroller();
        // Загружаем первый месяц
        loadNextMonthIfNeeded();
    }

    @SuppressLint("NewApi")
    private void loadNextMonthIfNeeded() {
        if (isLoadingMonth) return;
        if (nextMonthIndexToLoad >= monthBuckets.size()) return;

        isLoadingMonth = true;
        MonthBucket bucket = monthBuckets.get(nextMonthIndexToLoad);
        YearMonth ym = bucket.getYearMonth();

        // Добавить заголовок секции в адаптер (если его ещё нет)
        if (!headerAdapterPositions.containsKey(ym)) {
            int headerPos = adapter.addHeader(ym);
            headerAdapterPositions.put(ym, headerPos);
        }

        // Подгрузка фоток этого месяца
        var observer = new PhotoObserver(ym, this::observeOnNext, this::observeOnCompleted, this::observeOnError);
        grpcService.getPhotosByDate(bucket.getStartFilter(), bucket.getEndFilter(), observer);
    }

    public void observeOnNext(YearMonth yearMonth, ShortMediaInfoDto item) {
        // ВАЖНО: observer будет звать add в фоне — переносим в главный поток
        mainHandler.post(() -> {
            if (!isViewActive || adapter == null) return;
            adapter.addPhoto(yearMonth, item);
        });
    }

    public void observeOnCompleted() {
        mainHandler.post(() -> {
            if (!isViewActive) return;
            // отмечаем, что месяц загружен, двигаемся к следующему
            nextMonthIndexToLoad++;
            isLoadingMonth = false;
            // если выбрана цель догрузки до конкретного месяца — продолжаем
            if (targetMonthIndex != -1 && nextMonthIndexToLoad <= targetMonthIndex) {
                loadNextMonthIfNeeded();
            } else {
                targetMonthIndex = -1;
                // если пользователь уже близко к концу — загрузим следующий
                maybePrefetchNext();
            }
        });
    }

    public void observeOnError(Throwable t) {
        mainHandler.post(() -> {
            isLoadingMonth = false;
            if (isViewActive && getContext() != null) {
                Toast.makeText(getContext(), "Ошибка загрузки фото: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void maybePrefetchNext() {
        if (!isViewActive || adapter == null) return;
        int lastVisible = gridLayoutManager.findLastVisibleItemPosition();
        int total = adapter.getItemCount();
        if (total <= 0 || lastVisible == RecyclerView.NO_POSITION) return;
        if (total - lastVisible < PREFETCH_THRESHOLD) {
            loadNextMonthIfNeeded();
        }
    }

    // ----------------------------------------
    // Скролл, липкий хедер, быстрый скролл
    // ----------------------------------------
    private void setupScrollListeners() {
        photosRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView rv, int dx, int dy) {
                super.onScrolled(rv, dx, dy);
                maybePrefetchNext();
                updateStickyMonth();
            }
        });
    }

    private void updateStickyMonth() {
        if (!isViewActive || adapter == null) return;
        int firstVisiblePos = gridLayoutManager.findFirstVisibleItemPosition();
        if (firstVisiblePos == RecyclerView.NO_POSITION) return;
        YearMonth ym = adapter.getMonthForPosition(firstVisiblePos);
        if (ym != null) {
            stickyMonthTextView.setText(formatMonth(ym));
        }
    }

    private void setupMonthFastScroller() {
        MonthFastScrollerAdapter fastAdapter = new MonthFastScrollerAdapter(monthBuckets, ym -> {
            // Прокрутить к заголовку этого месяца
            Integer headerPos = headerAdapterPositions.get(ym);
            if (headerPos != null) {
                photosRecyclerView.scrollToPosition(headerPos);
                return;
            }
            // Если не загружен — проскроллим к ближайшему и инициируем догрузку
            int idx = indexOfMonth(ym);
            if (idx >= 0) {
                // Если этот месяц ещё не загрузили, последовательно догружаем до него
                targetMonthIndex = idx;
                if (nextMonthIndexToLoad <= idx) {
                    loadNextMonthIfNeeded();
                }
                // После добавления заголовка map заполнится; сделаем пост-скролл
                mainHandler.postDelayed(() -> {
                    Integer hp = headerAdapterPositions.get(ym);
                    if (hp != null) photosRecyclerView.scrollToPosition(hp);
                }, 100);
            }
        });
        monthFastScrollRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        monthFastScrollRv.setAdapter(fastAdapter);
    }

    private int indexOfMonth(YearMonth ym) {
        for (int i = 0; i < monthBuckets.size(); i++) {
            if (monthBuckets.get(i).getYearMonth().equals(ym)) {
                return i;
            }
        }
        return -1;
    }

    @SuppressLint("NewApi")
    private String formatMonth(YearMonth ym) {
        // Формат MM.YYYY
        int m = ym.getMonthValue();
        int y = ym.getYear();
        return String.format(Locale.getDefault(), "%02d.%04d", m, y);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewActive = false;
        if (photosRecyclerView != null) {
            photosRecyclerView.clearOnScrollListeners();
            photosRecyclerView.setAdapter(null);
        }
        if (monthFastScrollRv != null) {
            monthFastScrollRv.setAdapter(null);
        }
        adapter = null;
        binding = null;
    }

}
