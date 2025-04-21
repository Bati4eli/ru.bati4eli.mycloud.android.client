package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    public AbstractViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
    }

    public abstract void bind(GrpcFile file, OnItemClickListener listener) ;

    protected void setupIcon(GrpcFile file, ImageView fileIcon) {
        if (file.getMediaType() == TypeOfFile.FOLDER) {
            fileIcon.setImageResource(R.drawable.ic_folder);
        } else {
            setMediaIcon(file, fileIcon);
        }
    }

    protected void setMediaIcon(GrpcFile file, ImageView fileIcon) {
        if (file.getMediaType() == TypeOfFile.IMAGE || file.getMediaType() == TypeOfFile.VIDEO) {
            String filePath = MyUtils.getFilePath(file, DownloadType.PREVIEW_MINI);
            Bitmap bitmap = BitmapFactory.decodeFile(filePath);
            if (bitmap != null) {
                fileIcon.setImageBitmap(bitmap);
            } else {
                // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
                fileIcon.setImageResource(R.drawable.ic_file);
            }
        } else {
            // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
            fileIcon.setImageResource(R.drawable.ic_file);
        }
    }
}
