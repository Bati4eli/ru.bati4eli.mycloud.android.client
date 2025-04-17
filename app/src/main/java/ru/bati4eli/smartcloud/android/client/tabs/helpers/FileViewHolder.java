package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.FileItemLayoutBinding;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FileViewHolder extends RecyclerView.ViewHolder {
    private FileItemLayoutBinding binding;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        binding = FileItemLayoutBinding.bind(itemView);
    }

    /**
     * Биндинг для каждого файла
     */
    public void bind(GrpcFile file, OnItemClickListener listener) {
        try {

            binding.fileName.setText(file.getName());
            binding.fileDate.setText(MyUtils.formatDate(file.getLastModify()));
            binding.fileSize.setText(file.getShortSize());

            if (file.getMediaType() == TypeOfFile.FOLDER) {
                binding.fileIcon.setImageResource(R.drawable.ic_folder);
            } else {
                setMediaIcon(file);
            }
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        } catch (Throwable e) {
            Log.e(TAG,"FileViewHolder: " + e.getLocalizedMessage());
            // binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

    private void setMediaIcon(GrpcFile file) {
        if (file.getMediaType() == TypeOfFile.IMAGE || file.getMediaType() == TypeOfFile.VIDEO) {
            String filePath = MyUtils.getFilePath(file, DownloadType.PREVIEW_MINI);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap != null) {
                binding.fileIcon.setImageBitmap(bitmap);
            } else {
                // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
                binding.fileIcon.setImageResource(R.drawable.ic_file);
            }
        } else {
            // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
            binding.fileIcon.setImageResource(R.drawable.ic_file);
        }
    }
}
