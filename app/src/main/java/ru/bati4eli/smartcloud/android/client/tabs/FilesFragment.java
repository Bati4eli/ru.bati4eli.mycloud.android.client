package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.databinding.TabFilesBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcFileStreamObserver;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.OnItemClickListener;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FilesFragment extends Fragment implements OnItemClickListener {
    private boolean currIsRoot = true;
    private GrpcFile previousFolder;
    private GrpcFile currentFolder;
    private TabFilesBinding binding;
    private FileAdapter fileAdapter;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabFilesBinding.inflate(inflater, container, false);

        fileAdapter = new FileAdapter(this);
        binding.recyclerViewFiles.setAdapter(fileAdapter);
        binding.recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getContext()));

        // Вызов метода обновления данных
        binding.swipeRefreshLayout.setOnRefreshListener(this::loadFiles);
        loadFiles();

        return binding.getRoot();
    }

    private void loadFiles() {
        fileAdapter.clear();

        if (currIsRoot) {
            grpcService.getRootFilesSync(new GrpcFileStreamObserver(fileAdapter, binding.swipeRefreshLayout));
        } else {
            grpcService.getSubFilesSync(currentFolder, new GrpcFileStreamObserver(fileAdapter, binding.swipeRefreshLayout));
        }
    }

    @Override
    public void onItemClick(int position) {
        try {
            GrpcFile grpcFile = fileAdapter.getFiles().get(position);
            if (grpcFile.getMediaType() == TypeOfFile.FOLDER) {
                currIsRoot = false;
                currentFolder = grpcFile;
                loadFiles();
            }
            //Toast.makeText(getActivity(), "Ты кликнул: " + grpcFile.getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

}
