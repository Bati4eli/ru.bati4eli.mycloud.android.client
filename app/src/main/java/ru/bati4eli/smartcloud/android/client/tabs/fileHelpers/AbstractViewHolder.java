package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

public abstract class AbstractViewHolder<TYPE> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
    }

    public abstract void bind(TYPE item, OnItemClickListener listener);

    protected void setupIcon(ShortInfo info, ImageView fileIcon, DownloadType downloadType) {
        if (info.getType() == TypeOfFile.FOLDER) {
            fileIcon.setImageResource(R.drawable.ic_folder);
        } else if (info.getType() == TypeOfFile.IMAGE || info.getType() == TypeOfFile.VIDEO) {
            downloadPreviewAsync(info, fileIcon, downloadType);
        } else {
            // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
            fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

    private static void downloadPreviewAsync(ShortInfo info, ImageView fileIcon, DownloadType downloadType) {
        // Повторно не надо скачивать превью
        if (MyUtils.previewExists(info, downloadType)) {
            setupPreview(info, fileIcon, downloadType);
            return;
        }
        MiserableDI.get(GrpcService.class)
                .downloadFileAsync(info, downloadType, () -> setupPreview(info, fileIcon, downloadType));
    }

    private static void setupPreview(ShortInfo info, ImageView fileIcon, DownloadType downloadType) {
        String filePath = MyUtils.getFilePath(info, downloadType);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        if (bitmap != null) {
            fileIcon.setImageBitmap(bitmap);
        } else {
            // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
            fileIcon.setImageResource(R.drawable.ic_file);
        }
    }
}

