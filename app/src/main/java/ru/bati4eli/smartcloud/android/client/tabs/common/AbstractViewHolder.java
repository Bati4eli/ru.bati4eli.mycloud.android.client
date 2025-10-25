package ru.bati4eli.smartcloud.android.client.tabs.common;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;

import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.setupPreviewAsync;

public abstract class AbstractViewHolder<TYPE> extends RecyclerView.ViewHolder {

    public AbstractViewHolder(ViewGroup parent, int viewId) {
        super(LayoutInflater.from(parent.getContext()).inflate(viewId, parent, false));
    }

    public abstract void bind(TYPE item, OnItemClickListener<TYPE> listener);

    protected void setupOnClickListener(ViewGroup root, TYPE item, OnItemClickListener<TYPE> listener) {
        root.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(getAdapterPosition(), item);
            }
        });
    }

    /**
     * Установка размера для каждого item-а если требуется
     *
     * @param width
     * @param height
     */
    protected void setupLayoutSize(int width, int height) {
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(width, height);
        } else {
            params.width = width;
            params.height = height;
        }
        itemView.setLayoutParams(params);
    }

    protected static void setupIcon(ShortInfo info, ImageView fileIcon, DownloadType downloadType) {
        TypeOfFile type = info.getType();
        if (type == TypeOfFile.FOLDER) {
            fileIcon.setImageResource(R.drawable.ic_folder);
        } else if (type == TypeOfFile.IMAGE || type == TypeOfFile.VIDEO) {
            setupPreviewAsync(info, fileIcon, downloadType);
        } else {
            // Если загрузка не удалась, возможно, использовать иконку файла по умолчанию
            fileIcon.setImageResource(R.drawable.ic_file);
        }
    }

}

