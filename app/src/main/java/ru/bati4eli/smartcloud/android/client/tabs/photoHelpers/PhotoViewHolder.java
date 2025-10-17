package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.ItemPhotoLayoutBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.PhotoItem;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;
import static ru.bati4eli.smartcloud.android.client.utils.FAConstants.FA_VIDEO_CAMERA;

public class PhotoViewHolder extends AbstractViewHolder<PhotoItem> {
    private final ItemPhotoLayoutBinding binding;

    public PhotoViewHolder(@NonNull ViewGroup parent, int pixelSize) {
        super(parent, R.layout.item_photo_layout);
        binding = ItemPhotoLayoutBinding.bind(super.itemView);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(pixelSize, pixelSize);
        } else {
            params.width = pixelSize;
            params.height = pixelSize;
        }
        itemView.setLayoutParams(params);
    }

    /**
     * Биндинг для каждого фото
     */
    @Override
    public void bind(PhotoItem item, OnItemClickListener<PhotoItem> listener) {
        try {
            ShortMediaInfoDto photo = item.getPhoto();
            binding.videoInfoTxt.setVisibility(photo.getMediaType() == TypeOfFile.VIDEO ? View.VISIBLE : View.INVISIBLE);
            binding.videoInfoTxt.setText(FA_VIDEO_CAMERA + " " + MyUtils.formatSeconds(photo.getVideoDurationSec())); //  @color/white
            Log.d(TAG, "Binding PhotoItem FileId: " + photo.getFileId());
            setOnClick(item, listener);
            setupIcon(ShortInfo.of(photo), binding.photoIcon, DownloadType.PREVIEW_SQUARE);
        } catch (Throwable e) {
            Log.e(TAG, "PhotoViewHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

    private void setOnClick(PhotoItem item, OnItemClickListener<PhotoItem> listener) {
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(getAdapterPosition(), item);
            }
        });
    }

}
