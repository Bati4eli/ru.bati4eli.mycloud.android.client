package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.bati4eli.mycloud.repo.FileRepoService;
import ru.bati4eli.smartcloud.android.client.databinding.TabFilesBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FilesFragment extends Fragment {
    private TabFilesBinding binding;
    private FileAdapter fileAdapter;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);
    private List<FileRepoService.GrpcFile> fileList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabFilesBinding.inflate(inflater, container, false);

        fileAdapter = new FileAdapter(fileList);
        binding.recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewFiles.setAdapter(fileAdapter);
        loadFiles();

        return binding.getRoot();
    }

    private void loadFiles() {
        new Thread(() -> {
            Iterator<FileRepoService.GrpcFile> fileIterator = grpcService.getRootFiles();
            while (true) {
                fileList.add(fileIterator.next());
                fileAdapter.notifyDataSetChanged();
                if (!fileIterator.hasNext()) break;
            }
        }).start();
    }
}
