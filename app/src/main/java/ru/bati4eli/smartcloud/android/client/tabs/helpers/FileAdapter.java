package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FileAdapter extends RecyclerView.Adapter<FileViewHolder> {

    private final OnItemClickListener listener;

    @Getter
    private List<GrpcFile> files = new ArrayList<>();

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_item_layout, parent, false);

        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bind(files.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void add(GrpcFile grpcFile) {
        files.add(grpcFile);
    }

    public void clear() {
        files.clear();
        notifyDataSetChanged();
    }
}
