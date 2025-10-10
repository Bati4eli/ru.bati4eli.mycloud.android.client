package ru.bati4eli.smartcloud.android.client.tabs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private final int SPAN_COUNT = 3;
    private TabPhotosBinding binding;
    private PhotoAdapter adapter;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabPhotosBinding.inflate(inflater, container, false);

        int spacing = getSpacing();
        int itemSize = calculateItemSize(this, spacing, SPAN_COUNT);
        adapter = new PhotoAdapter(this).setItemSize(itemSize);

        binding.recyclerViewPhotos.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        binding.recyclerViewPhotos.setAdapter(adapter);
        binding.swipeRefreshLayout.setOnRefreshListener(this::updateSubFiles);

        updateSubFiles();
        return binding.getRoot();
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
