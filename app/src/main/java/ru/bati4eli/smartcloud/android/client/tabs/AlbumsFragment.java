package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import ru.bati4eli.mycloud.repo.AlbumType;
import ru.bati4eli.mycloud.repo.RespAlbumInfo;
import ru.bati4eli.smartcloud.android.client.databinding.TabAlbumsBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumCardModel;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import java.util.Iterator;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;
import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateSpanCount;

public class AlbumsFragment extends Fragment implements OnBackPressedListener, OnItemClickListener<AlbumCardModel> {
    private TabAlbumsBinding binding;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabAlbumsBinding.inflate(inflater, container, false);
        int spanCount = calculateSpanCount(this, 200);
        try {
            // Your albums (favorite, photos, videos)
            AlbumAdapter adapter = new AlbumAdapter(this);
            binding.recyclerYourAlbums.setAdapter(adapter);
            binding.recyclerYourAlbums.setLayoutManager(new GridLayoutManager(getContext(), spanCount));

            grpcService.getAlbums(AlbumType.AT_FAVORITE)
                    .ifPresent(album -> addAlbum(adapter, album, "{fa-star #ffe600}", "Favorite"));
            grpcService.getAlbums(AlbumType.AT_PHOTO)
                    .ifPresent(album -> addAlbum(adapter, album, "{fa-camera @color/ocean}", "Photos"));
            grpcService.getAlbums(AlbumType.AT_VIDEO)
                    .ifPresent(album -> addAlbum(adapter, album, "{fa-video-camera @color/ocean}", "Videos"));

            // Background in the photo
            AlbumAdapter backgroundAdapter = new AlbumAdapter(this);
            binding.recyclerBackground.setAdapter(backgroundAdapter);
            binding.recyclerBackground.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
            grpcService.getAlbums(AlbumType.AT_BACKGROUND)
                    .forEach(albumInfo ->
                            addAlbum(backgroundAdapter, albumInfo, "", albumInfo.getAlbumName())
                    );

            // Cameras
            AlbumAdapter camerasAdapter = new AlbumAdapter(this);
            binding.recyclerCameras.setAdapter(camerasAdapter);
            binding.recyclerCameras.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
            grpcService.getAlbums(AlbumType.AT_CAMERAS)
                    .forEach(albumInfo ->
                            addAlbum(camerasAdapter, albumInfo, "", albumInfo.getAlbumId() + "\n" + albumInfo.getAlbumName())
                    );
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }

/*
        // Faces on your photos
        Iterator<ShortFaceDto> facesIterator = grpcService.getFaces(); // замените на свой источник
        facesAdapter = new FaceCardAdapter( face -> {
            // обработка клика
        });
        binding.recyclerFaces.setAdapter(facesAdapter);
*/
        return binding.getRoot();
    }

    private void addAlbum(AlbumAdapter adapter, RespAlbumInfo albumInfo, String fontAwesomeIcon, String label) {
        if (albumInfo != null) {
            AlbumCardModel albumCardModel = new AlbumCardModel(albumInfo, fontAwesomeIcon, label);
            adapter.add(albumCardModel);
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
    public void onItemClick(int position, AlbumCardModel model) {
        Toast.makeText(getActivity(), "Ты кликнул: " + model.getAlbumName(), Toast.LENGTH_SHORT).show();
    }
}
