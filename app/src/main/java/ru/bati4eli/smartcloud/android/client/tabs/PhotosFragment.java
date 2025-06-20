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

public class PhotosFragment extends Fragment implements OnBackPressedListener, OnItemClickListener<ShortMediaInfoDto> {
    private int spanCount = 5;
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
        int itemSize = calculateItemSize(this, spacing, spanCount);
        scaleDetector = getScaleDetector();
        adapter = new PhotoAdapter(this).setItemSize(itemSize);

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
                    changeTileWidth(-1);
                } else if (scaleFactor < 0.95f) {
                    changeTileWidth(+1);
                }
                return true;
            }
        });
    }

    private void changeTileWidth(int delta) {
        int newSpanCount = spanCount + delta;
        if (newSpanCount < 2) newSpanCount = 2;
        if (newSpanCount > 6) newSpanCount = 6;
        if (newSpanCount == spanCount) return; // Нет изменений

        spanCount = newSpanCount;

        int spacing = getSpacing();
        //int spanCount = calculateSpanCount(this, tileWidthDp);
        int itemSize = calculateItemSize(this, spacing, spanCount);

        // Обновляем LayoutManager и размер плиток
        adapter.setItemSize(itemSize);
        binding.recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
    }

    /**
     * отступы между плитками.
     */
    private int getSpacing() {
        return getResources().getDimensionPixelSize(R.dimen.photo_spacing);
    }

    private void updateSubFiles() {
        adapter.clear();
        grpcService.getPhotos(new AdapterItemsObserver<>(adapter, binding.swipeRefreshLayout));
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {

    }

    @Override
    public void onItemClick(int position, ShortMediaInfoDto model) {
        Toast.makeText(getActivity(), "Ты кликнул FileId: " + model.getFileId(), Toast.LENGTH_SHORT).show();
    }

}
