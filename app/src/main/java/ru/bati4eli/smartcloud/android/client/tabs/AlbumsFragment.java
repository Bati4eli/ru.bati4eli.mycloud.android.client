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
import ru.bati4eli.mycloud.repo.ShortFaceDto;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.TabAlbumsBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumCardModel;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.AlbumInterface;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.FaceAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.albumsHelper.FaceCardModel;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.FAConstants;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;
import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateItemSize;

public class AlbumsFragment extends Fragment implements OnBackPressedListener, OnItemClickListener<AlbumInterface> {

    private static final int ALBUM_SPAN_COUNT = 3;
    private static final int FACE_SPAN_COUNT = 4;

    private TabAlbumsBinding binding;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabAlbumsBinding.inflate(inflater, container, false);

        int albumSize = calculateItemSize(this, getAlbumSpacing(), ALBUM_SPAN_COUNT);
        int faceSize = calculateItemSize(this, getFaceSpacing(), FACE_SPAN_COUNT);

        try {
            // Your albums (favorite, photos, videos)
            AlbumAdapter adapter = new AlbumAdapter(this)
                    .setPixelSize(albumSize);
            binding.recyclerYourAlbums.setAdapter(adapter);
            binding.recyclerYourAlbums.setLayoutManager(new GridLayoutManager(getContext(), ALBUM_SPAN_COUNT));

            grpcService.getAlbums(AlbumType.AT_FAVORITE)
                    .ifPresent(album -> addAlbum(adapter, album, FAConstants.FA_GOLD_STAR, "Favorite"));
            grpcService.getAlbums(AlbumType.AT_PHOTO)
                    .ifPresent(album -> addAlbum(adapter, album, FAConstants.FA_CAMERA_COLOR_OCEAN, "Photos"));
            grpcService.getAlbums(AlbumType.AT_VIDEO)
                    .ifPresent(album -> addAlbum(adapter, album, FAConstants.FA_VIDEO_CAMERA_COLOR_OCEAN, "Videos"));

            // Background in the photo
            AlbumAdapter backgroundAdapter = new AlbumAdapter(this)
                    .setPixelSize(albumSize);
            binding.recyclerBackground.setAdapter(backgroundAdapter);
            binding.recyclerBackground.setLayoutManager(new GridLayoutManager(getContext(), ALBUM_SPAN_COUNT));
            grpcService.getAlbums(AlbumType.AT_BACKGROUND)
                    .forEach(albumInfo ->
                            addAlbum(backgroundAdapter, albumInfo, "", albumInfo.getAlbumName())
                    );

            // Cameras
            AlbumAdapter camerasAdapter = new AlbumAdapter(this)
                    .setPixelSize(albumSize);
            binding.recyclerCameras.setAdapter(camerasAdapter);
            binding.recyclerCameras.setLayoutManager(new GridLayoutManager(getContext(), ALBUM_SPAN_COUNT));
            grpcService.getAlbums(AlbumType.AT_CAMERAS)
                    .forEach(albumInfo ->
                            addAlbum(camerasAdapter, albumInfo, "", albumInfo.getAlbumId() + "\n" + albumInfo.getAlbumName())
                    );

            // Faces
            FaceAdapter facesAdapter = new FaceAdapter(this)
                    .setPixelSize(faceSize);
            binding.recyclerFaces.setAdapter(facesAdapter);
            binding.recyclerFaces.setLayoutManager(new GridLayoutManager(getContext(), FACE_SPAN_COUNT));
            grpcService.getFaces().forEach(faceDto -> addFace(facesAdapter, faceDto));
        } catch (Throwable e) {
            Log.e(TAG, e.getMessage());
        }

        return binding.getRoot();
    }

    private void addAlbum(AlbumAdapter adapter, RespAlbumInfo albumInfo, String fontAwesomeIcon, String label) {
        if (albumInfo != null) {
            AlbumCardModel albumCardModel = new AlbumCardModel(albumInfo, fontAwesomeIcon, label);
            adapter.add(albumCardModel);
        }
    }

    private void addFace(FaceAdapter adapter, ShortFaceDto faceDto) {
        if (faceDto != null) {
            adapter.add(new FaceCardModel(faceDto));
        }
    }

    /**
     * отступы между плитками.
     */
    private int getAlbumSpacing() {
        return super.getResources().getDimensionPixelSize(R.dimen.album_spacing);
    }

    /**
     * отступы между плитками.
     */
    private int getFaceSpacing() {
        return super.getResources().getDimensionPixelSize(R.dimen.face_spacing);
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) { /* Ваш код */ }

    @Override
    public void onItemClick(int position, AlbumInterface o) {
        if (o instanceof FaceCardModel) {
            FaceCardModel model = (FaceCardModel) o;
            Toast.makeText(getActivity(), "Id лица:" + model.getFaceId(), Toast.LENGTH_SHORT).show();
            return;
        }
        if (o instanceof AlbumCardModel) {
            AlbumCardModel model = (AlbumCardModel) o;
            Toast.makeText(getActivity(), "Album: " + model.getAlbumName(), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getActivity(), "А ты рукастый =)", Toast.LENGTH_SHORT).show();
    }
}
