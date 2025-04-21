package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.enums.SortByEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortOrderEnum;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;
import ru.bati4eli.smartcloud.android.client.tabs.FilesFragment;
import ru.bati4eli.smartcloud.android.client.utils.GrpcFileComparator;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class FileAdapter extends RecyclerView.Adapter<AbstractViewHolder> {

    private final OnItemClickListener listener;

    @Setter
    private ViewTypeEnum viewType;

    @Getter
    private List<GrpcFile> files = new ArrayList<>();

    public FileAdapter(OnItemClickListener clickListener, ViewTypeEnum viewType) {
        this.listener = clickListener;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int extViewType) {
        if (viewType == ViewTypeEnum.VIEW_LIST) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item_list_layout, parent, false);
            return new FileViewListHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item_grid_layout, parent, false);
            return new FileViewGridHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull AbstractViewHolder holder, int position) {
        holder.bind(files.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void addAll(Collection<GrpcFile> files) {
        this.files.clear();
        this.files.addAll(files);
        notifyDataSetChanged();
    }

    public void reSort() {
        SortByEnum sortBy = ParametersUtil.getSortBy();
        SortOrderEnum sortOrder = ParametersUtil.getSortOrder();
        Comparator<GrpcFile> fileComparator = GrpcFileComparator.getFileComparator(sortBy, sortOrder);
        files.sort(fileComparator);
        notifyDataSetChanged();
    }

    public void clear() {
        this.files.clear();
        notifyDataSetChanged();
    }

    public GrpcFile get(int position) {
        return files.get(position);
    }
}
