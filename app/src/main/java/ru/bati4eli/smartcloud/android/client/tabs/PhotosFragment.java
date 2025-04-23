package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import ru.bati4eli.smartcloud.android.client.MainActivity;
import ru.bati4eli.smartcloud.android.client.databinding.TabPhotosBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.service.observers.AdapterItemsObserver;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoAdapter;

public class PhotosFragment extends Fragment implements OnBackPressedListener, OnItemClickListener {
    private TabPhotosBinding binding;
    private PhotoAdapter adapter;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabPhotosBinding.inflate(inflater, container, false);
        //activity = (MainActivity) getActivity();
        adapter = new PhotoAdapter(this);
        binding.swipeRefreshLayout.setOnRefreshListener(this::updateSubFiles);
        int spanCount = calculateSpanCount(); // Метод для определения количества колонок
        binding.recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
        binding.recyclerViewPhotos.setAdapter(adapter);
        binding.recyclerViewPhotos.setHasFixedSize(true);
        binding.recyclerViewPhotos.setItemViewCacheSize(100);
        updateSubFiles();
        return binding.getRoot();
    }

    private void updateSubFiles() {
        adapter.clear();
        binding.recyclerViewPhotos.setAdapter(adapter);
        grpcService.getPhotos(new AdapterItemsObserver<>(adapter, binding.swipeRefreshLayout));
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {

    }

    @Override
    public void onItemClick(int position) {

    }


    private int calculateSpanCount() {
        // Получаем ширину дисплея в пикселях
        int displayWidth = getResources().getDisplayMetrics().widthPixels;
        // Конвертируем ширину плитки из dp в пиксели
        int tileWidthDp = 80; // ширина плитки в dp
        float density = getResources().getDisplayMetrics().density; // получаем плотность экрана
        // Высчитываем ширину плитки в пикселях
        int tileWidthPx = (int) (tileWidthDp * density);
        // Определяем количество колонок
        return Math.max(1, displayWidth / tileWidthPx);
    }
}
