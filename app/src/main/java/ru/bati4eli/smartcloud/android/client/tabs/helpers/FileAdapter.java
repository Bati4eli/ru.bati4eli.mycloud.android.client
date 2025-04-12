package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bati4eli.mycloud.repo.FileRepoService;
import ru.bati4eli.smartcloud.android.client.R;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    private List<FileRepoService.GrpcFile> files;

    public FileAdapter(List<FileRepoService.GrpcFile> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.file_item_layout, parent, false);

        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        holder.bind(files.get(position));
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder {
        private TextView fileNameView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameView = itemView.findViewById(R.id.fileName);
        }

        public void bind(FileRepoService.GrpcFile file) {
            fileNameView.setText(file.getName()); // Предположим, что у объекта есть метод getName()
        }
    }
}
