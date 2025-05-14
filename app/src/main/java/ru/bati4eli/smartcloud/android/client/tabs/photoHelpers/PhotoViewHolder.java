package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.ItemPhotoLayoutBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class PhotoViewHolder extends AbstractViewHolder<ShortMediaInfoDto> {
    private final ItemPhotoLayoutBinding binding;

    public PhotoViewHolder(@NonNull ViewGroup parent, int size) {
        super(parent, R.layout.item_photo_layout);
        binding = ItemPhotoLayoutBinding.bind(super.itemView);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(size, size);
        } else {
            params.width = size;
            params.height = size;
        }
        itemView.setLayoutParams(params);
    }

    /**
     * Биндинг для каждого фото
     */
    @Override
    public void bind(ShortMediaInfoDto item, OnItemClickListener<ShortMediaInfoDto> listener) {
        try {
            setupOnClickListener(binding.getRoot(), item, listener);
            setupIcon(ShortInfo.of(item), binding.photoIcon, DownloadType.PREVIEW_SQUARE);
        } catch (Throwable e) {
            Log.e(TAG, "PhotoViewHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

}
