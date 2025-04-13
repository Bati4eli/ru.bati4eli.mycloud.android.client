package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.smartcloud.android.client.R;

public class FileViewHolder extends RecyclerView.ViewHolder {
    private TextView fileNameView;

    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        fileNameView = itemView.findViewById(R.id.fileName);
    }

    public void bind(GrpcFile file) {
        fileNameView.setText(file.getName());
    }
}
