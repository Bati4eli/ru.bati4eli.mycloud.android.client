package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.bati4eli.mycloud.repo.AlbumType;
import ru.bati4eli.mycloud.repo.RespAlbumInfo;
import ru.bati4eli.smartcloud.android.client.databinding.TabAlbumsBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumCardLayout;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumCardModel;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import java.util.Iterator;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class AlbumsFragment extends Fragment implements OnBackPressedListener, OnItemClickListener {
    private TabAlbumsBinding binding;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabAlbumsBinding.inflate(inflater, container, false);

        // Your albums (favorite, photos, videos) - как единичные items
        try {
            addAlbum(getFirst(grpcService.getAlbums(AlbumType.AT_FAVORITE)), "{faw-heart}","Favorite");
            addAlbum(getFirst(grpcService.getAlbums(AlbumType.AT_PHOTO)), "{faw-heart}","Photos");
            addAlbum(getFirst(grpcService.getAlbums(AlbumType.AT_VIDEO)),"{fa-video-camera}" ,"Videos");
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }
/*
        //albumsAdapter = new AlbumCardAdapter(  this).addAll(yourAlbums);
        binding.recyclerYourAlbums.setAdapter(albumsAdapter);

        // Background in the photo
        Iterator<RespAlbumInfo> bgIterator = grpcService.getAlbums(AlbumType.AT_BACKGROUND);
        backgroundAdapter = new AlbumCardAdapter( album -> {
            // обработка клика
        });
        binding.recyclerBackground.setAdapter(backgroundAdapter);

        // Cameras
        Iterator<RespAlbumInfo> camIterator = grpcService.getAlbums(AlbumType.AT_CAMERAS);
        camerasAdapter = new AlbumCardAdapter( album -> {
            // обработка клика
        });
        binding.recyclerCameras.setAdapter(camerasAdapter);

        // Faces on your photos
        Iterator<ShortFaceDto> facesIterator = grpcService.getFaces(); // замените на свой источник
        facesAdapter = new FaceCardAdapter( face -> {
            // обработка клика
        });
        binding.recyclerFaces.setAdapter(facesAdapter);
*/
        return binding.getRoot();
    }

    private void addAlbum(RespAlbumInfo albumInfo, String fontAwesomeIcon, String label) {
        if (albumInfo != null) {
            AlbumCardLayout child = new AlbumCardLayout(getContext(), null, AlbumCardModel.of(albumInfo, fontAwesomeIcon, label));
            binding.recyclerYourAlbums.addView(child);
        }
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) { /* Ваш код */ }

    private RespAlbumInfo getFirst(Iterator<RespAlbumInfo> it) {
        if (it != null && it.hasNext())
            return it.next();
        return null;
    }

    @Override
    public void onItemClick(int position) {

    }
}
