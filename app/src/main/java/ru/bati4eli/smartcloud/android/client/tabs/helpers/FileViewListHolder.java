package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemListLayoutBinding;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewListHolder extends AbstractViewHolder {
    private FileItemListLayoutBinding binding;

    public FileViewListHolder(@NonNull View itemView) {
        super(itemView);
        binding = FileItemListLayoutBinding.bind(itemView);
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
