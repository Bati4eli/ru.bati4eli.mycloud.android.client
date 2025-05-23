package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import lombok.Setter;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
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
    public void finishAndShow() {
        super.items.clear();
        super.items.addAll(treeFiles);
        notifyDataSetChanged();
    }

    public void reSort() {
        super.items.sort(GrpcFileComparator.getFileComparator());
        notifyDataSetChanged();
    }

    public void resetNewTree(Comparator<GrpcFile> fileComparator) {
        treeFiles = new TreeSet<>(fileComparator);
    }

    @Override
    public void add(GrpcFile item) {
        treeFiles.add(item);
    }

}
