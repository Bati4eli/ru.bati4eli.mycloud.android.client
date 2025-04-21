package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemGridLayoutBinding;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewGridHolder extends AbstractViewHolder {
    private FileItemGridLayoutBinding binding;

    public FileViewGridHolder(@NonNull View itemView) {
        super(itemView);
        binding = FileItemGridLayoutBinding.bind(itemView);
    }

    /**
     * Биндинг для каждого файла
     */
    @Override
    public void bind(GrpcFile file, OnItemClickListener listener) {
        try {
            binding.fileName.setText(file.getName());
            setupIcon(file, binding.fileIcon);
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        } catch (Throwable e) {
            Log.e(TAG, "FileViewTileHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

}
