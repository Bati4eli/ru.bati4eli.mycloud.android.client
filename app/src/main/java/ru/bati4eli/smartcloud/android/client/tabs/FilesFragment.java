package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.bati4eli.smartcloud.android.client.databinding.TabFilesBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;

public class FilesFragment extends Fragment {
    private TabFilesBinding binding;
    private FileAdapter fileAdapter;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabFilesBinding.inflate(inflater, container, false);

        fileAdapter = new FileAdapter();
        binding.recyclerViewFiles.setAdapter(fileAdapter);
        binding.recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        fileAdapter.notifyDataSetChanged();
        loadFiles();

        return binding.getRoot();
    }

    private void loadFiles() {
        fileAdapter.clear();
        grpcService.getRootFiles(fileAdapter);
    }
}
