package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemListLayoutBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewListHolder extends AbstractViewHolder<GrpcFile> {
    private final FileItemListLayoutBinding binding;

    public FileViewListHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.file_item_list_layout);
        binding = FileItemListLayoutBinding.bind(super.itemView);
    }

    /**
     * Биндинг для каждого файла
     */
    @Override
    public void bind(GrpcFile item, OnItemClickListener listener) {
        try {
            binding.fileName.setText(item.getName());
            binding.fileDate.setText(MyUtils.formatDate(item.getLastModify()));
            binding.fileSize.setText(item.getShortSize());
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition(), null);
                }
            });
            setupIcon(ShortInfo.of(item), binding.fileIcon, DownloadType.PREVIEW_MINI);
        } catch (Throwable e) {
            Log.e(TAG, "FileViewListHolder: " + e.getLocalizedMessage());
        }
    }

}
