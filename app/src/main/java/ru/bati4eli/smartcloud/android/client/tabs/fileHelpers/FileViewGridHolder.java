package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemGridLayoutBinding;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewGridHolder extends AbstractViewHolder {
    private final FileItemGridLayoutBinding binding;

    public FileViewGridHolder(@NonNull ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item_grid_layout, parent, false));
        binding = FileItemGridLayoutBinding.bind(super.itemView);
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
