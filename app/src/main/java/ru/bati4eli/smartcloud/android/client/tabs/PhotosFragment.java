package ru.bati4eli.smartcloud.android.client.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import lombok.var;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.TabPhotosBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoObserver;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.MonthBucket;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.PhotoItem;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.ItemTypes.VT_HEADER;
import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateItemSize;

public class PhotosFragment extends Fragment implements OnBackPressedListener, OnItemClickListener<PhotoItem> {
    private static final int SPAN_COUNT = 4;
    private TabPhotosBinding binding;
    private PhotoAdapter adapter;                       // адаптер фото
    private RecyclerView photosRecyclerView;            // фото вью
    private TextView stickyMonthTextView;               // плавающий текущий месяц
    private GridLayoutManager gridLayoutManager;        // менеджер фото
    private final GrpcService grpcService = MiserableDI.get(GrpcService.class);
    private final List<MonthBucket> monthBuckets = new ArrayList<>();
    private final Map<YearMonth, Integer> headerAdapterPositions = new LinkedHashMap<>();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private boolean isViewActive = false;

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabPhotosBinding.inflate(inflater, container, false);
        photosRecyclerView = binding.photosRecyclerView;
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

        loadMonthCounters();
        setupScrollListeners();

        return binding.getRoot();
    }

    /**
     * отступы между плитками.
     */
    private int getSpacing() {
        return super.getResources().getDimensionPixelSize(R.dimen.photo_spacing);
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
        // Инициализация пустого массива фоток и заголовков. Заполнение monthBuckets индексами
        adapter.initListFromCounters(monthBuckets);
        // Инициализируем headerAdapterPositions
        monthBuckets.forEach(bucket ->
                headerAdapterPositions.put(bucket.getYearMonth(), bucket.getStartIndexPhoto())
        );
        // Загружаем первый месяц
        loadNextMonthIfNeeded();
    }

    @SuppressLint("NewApi")
    private void loadNextMonthIfNeeded() {
        int firstVisible = gridLayoutManager.findFirstVisibleItemPosition();
        firstVisible = Math.max(firstVisible, 0);
        int lastVisible = gridLayoutManager.findLastVisibleItemPosition();
        // Сбор всех бакетов которые отображаются на экране сейчас и загрузка их!
        IntStream.rangeClosed(firstVisible, lastVisible)
                .mapToObj(i -> adapter.getBucketByIndex(i)).collect(Collectors.toSet())
                .stream()
                .filter(bucket -> !bucket.isLoaded()) // только те бакеты что не были загружены
                .forEach(bucket -> {
                    // Подгрузка фоток этого месяца
                    // TODO Нужно организовать очередь задач на подгрузку!!
                    //TODO Сейчас все еще дублируются запросы на дозагрузку!
                    var observer = new PhotoObserver(bucket, this::observeOnNext, this::observeOnCompleted, this::observeOnError);
                    grpcService.getPhotosByDate(bucket.getStartFilter(), bucket.getEndFilter(), observer);
                });
    }

    public void observeOnNext(MonthBucket bucket, ShortMediaInfoDto item) {
        // ВАЖНО: observer будет звать add в фоне — переносим в главный поток
        mainHandler.post(() -> {
            if (!isViewActive || adapter == null) return;
            adapter.addPhoto(bucket, item);
        });
    }

    public void observeOnCompleted() {

    }

    public void observeOnError(Throwable t) {
        mainHandler.post(() -> {
            //isLoadingMonth = false;
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
        Log.d("INDEX", "maybePrefetchNext: lastVisible=" + lastVisible + ", total=" + total);
        //if (total - lastVisible < PREFETCH_THRESHOLD) {
        loadNextMonthIfNeeded();
        //}
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
            stickyMonthTextView.setText(MyUtils.getLabelYearAndMonth(ym));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewActive = false;
        if (photosRecyclerView != null) {
            photosRecyclerView.clearOnScrollListeners();
            photosRecyclerView.setAdapter(null);
        }
        adapter = null;
        binding = null;
    }

}
