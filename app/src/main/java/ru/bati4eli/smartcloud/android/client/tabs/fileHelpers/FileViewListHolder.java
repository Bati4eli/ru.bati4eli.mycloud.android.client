package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemListLayoutBinding;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewListHolder extends AbstractViewHolder {
    private final FileItemListLayoutBinding binding;

    public FileViewListHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item_list_layout, parent, false));
        binding = FileItemListLayoutBinding.bind(super.itemView);
    }

    /**
     * Биндинг для каждого файла
     */
    @Override
    public void bind(GrpcFile file, OnItemClickListener listener) {
        try {
            binding.fileName.setText(file.getName());
            binding.fileDate.setText(MyUtils.formatDate(file.getLastModify()));
            binding.fileSize.setText(file.getShortSize());

            setupIcon(file, binding.fileIcon);
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        } catch (Throwable e) {
            Log.e(TAG, "FileViewListHolder: " + e.getLocalizedMessage());
        }
    }

}
