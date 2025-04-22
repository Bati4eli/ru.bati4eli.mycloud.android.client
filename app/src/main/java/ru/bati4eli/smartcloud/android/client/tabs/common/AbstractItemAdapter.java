package ru.bati4eli.smartcloud.android.client.tabs.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.fileHelpers.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractItemAdapter<TYPE> extends RecyclerView.Adapter<AbstractViewHolder<TYPE>> {

    protected final OnItemClickListener listener;

    @Getter
    protected final List<TYPE> items = new ArrayList<>();
    private final GrpcService grpcService = MiserableDI.get(GrpcService.class);

    public abstract void finishAndShow();

    public abstract void add(TYPE item);

    public final void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public final TYPE get(int position) {
        return items.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AbstractViewHolder<TYPE> holder, int position) {
        holder.bind(items.get(position), listener);
    }

    protected void downloadPreviewAsync(ShortInfo info, DownloadType downloadType) {
        // Если это Видео/Фото, то загружаем превью для него!
        if (info.getType() == TypeOfFile.IMAGE || info.getType() == TypeOfFile.VIDEO) {
//            Executors.newSingleThreadExecutor().execute(() -> {
            // Повторно не надо скачивать превью
            if (!MyUtils.previewExists(info, downloadType))
                // Синхронный метод!
                grpcService.downloadFile(info, downloadType);
//            });
        }
    }
}
