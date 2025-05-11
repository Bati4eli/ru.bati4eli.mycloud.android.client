package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemGridLayoutBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewGridHolder extends AbstractViewHolder<GrpcFile> {
    private final FileItemGridLayoutBinding binding;

    public FileViewGridHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.file_item_grid_layout);
        binding = FileItemGridLayoutBinding.bind(super.itemView);
    }

    /**
     * Биндинг для каждого файла
     */
    @Override
    public void bind(GrpcFile item, OnItemClickListener listener) {
        try {
            binding.fileName.setText(item.getName());
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), null);
                }
            });
            setupIcon(ShortInfo.of(item), binding.fileIcon, DownloadType.PREVIEW_MINI);
        } catch (Throwable e) {
            Log.e(TAG, "FileViewTileHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

}
