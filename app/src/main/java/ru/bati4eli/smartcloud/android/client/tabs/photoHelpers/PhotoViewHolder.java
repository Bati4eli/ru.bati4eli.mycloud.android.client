package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.ItemPhotoLayoutBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.fileHelpers.AbstractViewHolder;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class PhotoViewHolder extends AbstractViewHolder<ShortMediaInfoDto> {
    private final ItemPhotoLayoutBinding binding;

    public PhotoViewHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_layout, parent, false));
        binding = ItemPhotoLayoutBinding.bind(super.itemView);
    }

    /**
     * Биндинг для каждого фото
     */
    @Override
    public void bind(ShortMediaInfoDto item, OnItemClickListener listener) {
        try {
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
            setupIcon(ShortInfo.of(item), binding.photoIcon, DownloadType.PREVIEW_SQUARE);
        } catch (Throwable e) {
            Log.e(TAG, "PhotoViewHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

}
