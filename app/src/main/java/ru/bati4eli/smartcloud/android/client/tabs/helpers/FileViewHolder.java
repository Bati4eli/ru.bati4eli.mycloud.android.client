package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemLayoutBinding;

public class FileViewHolder extends RecyclerView.ViewHolder {
    private FileItemLayoutBinding binding;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = FileItemLayoutBinding.bind(itemView);
    }

    public void bind(GrpcFile file) {
        binding.fileName.setText(file.getName());
        binding.fileDate.setText(file.getLastModify());
        binding.fileSize.setText(file.getShortSize());
    }
}
