package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.AlbumCardBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class AlbumViewHolder extends AbstractViewHolder<AlbumCardModel> {

    private AlbumCardBinding binding;

    public AlbumViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.album_card);
        binding = AlbumCardBinding.bind(super.itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void bind(AlbumCardModel item, OnItemClickListener<AlbumCardModel> listener) {
        try {
            binding.labelText.setText(item.getLabel());
            binding.awesomeIcon.setText(item.getFontAwesomeIcon());
            binding.amountText.setText(item.getAmount().toString());
            setupIcon(ShortInfo.of(item), binding.albumImage, DownloadType.PREVIEW_MINI);
            setupOnClickListener(binding.getRoot(), item, listener);
        } catch (Throwable e) {
            Log.e(TAG, "AlbumViewHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }


}
