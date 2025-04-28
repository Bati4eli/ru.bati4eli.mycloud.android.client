package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import java.util.Iterator;

public class AlbumsFragment extends Fragment {
    private TabAlbumsBinding binding;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabAlbumsBinding.inflate(inflater, container, false);

        // Создание и отображение карточек альбомов
        displayAlbum(AlbumType.AT_FAVORITE, "Favorites", binding.albumsWrapperFavorites);
        displayAlbum(AlbumType.AT_PHOTO, "Photos", binding.albumsWrapperPhotos);
        displayAlbum(AlbumType.AT_VIDEO, "Videos", binding.albumsWrapperVideos);

        // Дополнительные блоки
        displayBackgroundAlbums(binding.albumsWrapperBackground);
        displayCameras(binding.albumsWrapperCameras);
        displayFaces(binding.albumsWrapperFaces);

        return binding.getRoot();
    }

    private void displayAlbum(AlbumType albumType, String label, LinearLayout wrapper) {
        Iterator<RespAlbumInfo> iterator = grpcService.getAlbums(albumType);
        RespAlbumInfo albumInfo = iterator.next();
        AlbumCardModel model = AlbumCardModel.of(albumInfo).setLabel(label).setAlbumImage(android.R.drawable.ic_menu_camera);
        AlbumCardLayout child = new AlbumCardLayout(getContext(), null, model);
        wrapper.addView(child);
    }

    private void displayBackgroundAlbums(LinearLayout wrapper) {
        // Код для отображения альбомов с фоновыми фотографиями
    }

    private void displayCameras(LinearLayout wrapper) {
        // Код для отображения камер
    }

    private void displayFaces(LinearLayout wrapper) {
        // Код для отображения лиц на фотографиях
    }
}
