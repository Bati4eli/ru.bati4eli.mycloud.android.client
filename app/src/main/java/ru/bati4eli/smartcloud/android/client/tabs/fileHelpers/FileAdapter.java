package ru.bati4eli.smartcloud.android.client.tabs.fileHelpers;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.GrpcFileComparator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@RequiredArgsConstructor
public class FileAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final OnItemClickListener listener;

    @Setter
    private ViewTypeEnum viewType;

    private TreeSet<GrpcFile> sortedFiles = new TreeSet<>(GrpcFileComparator.getFileComparator());

    @Getter
    private final List<GrpcFile> files = new ArrayList<>();

    public FileAdapter(OnItemClickListener clickListener, ViewTypeEnum viewType) {
        this.listener = clickListener;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int extViewType) {
        return viewType == ViewTypeEnum.VIEW_LIST ? new FileViewListHolder(parent) : new FileViewGridHolder(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(files.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void finishAndShow() {
        this.files.clear();
        this.files.addAll(sortedFiles);
        notifyDataSetChanged();
    }

    public void reSort() {
        files.sort(GrpcFileComparator.getFileComparator());
        notifyDataSetChanged();
    }

    public void clear() {
        this.files.clear();
        notifyDataSetChanged();
    }

    public GrpcFile get(int position) {
        return files.get(position);
    }

    public void resetNewTree(Comparator<GrpcFile> fileComparator) {
        sortedFiles = new TreeSet<>(fileComparator);
    }

    public void add(GrpcFile grpcFile) {
        sortedFiles.add(grpcFile);
    }
}
