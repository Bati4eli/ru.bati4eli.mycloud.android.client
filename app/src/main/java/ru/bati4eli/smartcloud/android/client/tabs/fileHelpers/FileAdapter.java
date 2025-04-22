package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import lombok.Setter;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.GrpcFileComparator;

import java.util.Comparator;
import java.util.TreeSet;


public class FileAdapter extends AbstractItemAdapter<GrpcFile> {

    @Setter
    private ViewTypeEnum viewType;

    private TreeSet<GrpcFile> treeFiles = new TreeSet<>(GrpcFileComparator.getFileComparator());

    public FileAdapter(OnItemClickListener clickListener, ViewTypeEnum viewType) {
        super(clickListener);
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public AbstractViewHolder<GrpcFile> onCreateViewHolder(@NonNull ViewGroup parent, int extViewType) {
        return viewType == ViewTypeEnum.VIEW_LIST ? new FileViewListHolder(parent) : new FileViewGridHolder(parent);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void finishAndShow() {
        this.items.clear();
        this.items.addAll(treeFiles);
        notifyDataSetChanged();
    }

    public void reSort() {
        items.sort(GrpcFileComparator.getFileComparator());
        notifyDataSetChanged();
    }

    public void resetNewTree(Comparator<GrpcFile> fileComparator) {
        treeFiles = new TreeSet<>(fileComparator);
    }

    @Override
    public void add(GrpcFile item) {
        treeFiles.add(item);
        downloadPreviewAsync(ShortInfo.of(item), DownloadType.PREVIEW_MINI);
    }

}
