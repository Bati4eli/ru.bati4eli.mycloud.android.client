package ru.bati4eli.smartcloud.android.client.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.TabPhotosBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.service.observers.AdapterItemsObserver;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoAdapter;

import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateItemSize;
import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateSpanCount;

public class PhotosFragment extends Fragment implements OnBackPressedListener, OnItemClickListener<ShortMediaInfoDto> {
    private int tileWidthDp = 80;
    private TabPhotosBinding binding;
    private PhotoAdapter adapter;
    private ScaleGestureDetector scaleDetector;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabPhotosBinding.inflate(inflater, container, false);

        int spacing = getSpacing();
        int spanCount = calculateSpanCount(this, tileWidthDp);
        int itemSize =    calculateItemSize(this, spacing, spanCount);

        adapter = new PhotoAdapter(this).setItemSize(itemSize);

        scaleDetector = getScaleDetector();
        binding.recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        binding.recyclerViewPhotos.setAdapter(adapter);
        binding.recyclerViewPhotos.setHasFixedSize(true);
        binding.recyclerViewPhotos.setOnTouchListener((v, event) -> {
            scaleDetector.onTouchEvent(event); // Только жесты зума
            return true; // не перехватывай остальные тапы, чтобы скролл работал!
        });

        binding.swipeRefreshLayout.setOnRefreshListener(this::updateSubFiles);

        updateSubFiles();
        return binding.getRoot();
    }

    /**
     * Обработка "щупа" для изменения размера фоток.
     */
    @NotNull
    private ScaleGestureDetector getScaleDetector() {
        return new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scaleFactor = detector.getScaleFactor();
                // Определяем увеличивать или уменьшать
                if (scaleFactor > 1.05f) {
                    changeTileWidth(+10);
                } else if (scaleFactor < 0.95f) {
                    changeTileWidth(-10);
                }
                return true;
            }
        });
    }

    private void changeTileWidth(int delta) {
        int newWidth = tileWidthDp + delta;
        if (newWidth < 80) newWidth = 80;
        if (newWidth > 148) newWidth = 148;
        if (newWidth == tileWidthDp) return; // Нет изменений

        tileWidthDp = newWidth;

        int spacing = getSpacing();
        int spanCount = calculateSpanCount(this, tileWidthDp);
        int itemSize =  calculateItemSize(this, spacing, spanCount);

        // Обновляем LayoutManager и размер плиток
        GridLayoutManager glm = new GridLayoutManager(getContext(), spanCount);
        binding.recyclerViewPhotos.setLayoutManager(glm);
        adapter.setItemSize(itemSize); // тут notifyDataSetChanged не нужен, есть внутри setItemSize() по твоей структуре.
    }

    /**
     * отступы между плитками.
     */
    private int getSpacing() {
        return getResources().getDimensionPixelSize(R.dimen.photo_spacing);
    }

    private void updateSubFiles() {
        adapter.clear();
        grpcService.getPhotos(new AdapterItemsObserver<>(adapter, null /*binding.swipeRefreshLayout*/));
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {

    }

    @Override
    public void onItemClick(int position, ShortMediaInfoDto model) {
        Toast.makeText(getActivity(), "Ты кликнул FileId: " + model.getFileId(), Toast.LENGTH_SHORT).show();
    }

}
