package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.databinding.TabFilesBinding;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.service.Observers.GrpcFileStreamObserver;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.OnItemClickListener;

import java.util.Stack;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

public class FilesFragment extends Fragment implements OnItemClickListener, OnBackPressedListener {

    private GrpcFile currentFolder;
    private TabFilesBinding binding;
    private FileAdapter fileAdapter;
    private Stack<GrpcFile> folderStack = new Stack<>(); // Стек для хранения папок
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabFilesBinding.inflate(inflater, container, false);

        fileAdapter = new FileAdapter(this);
        binding.recyclerViewFiles.setAdapter(fileAdapter);
        binding.recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getContext()));

        // Вызов метода обновления данных
        binding.swipeRefreshLayout.setOnRefreshListener(this::updateSubFiles);

        moveToFolder(grpcService.getRootFileInfo());

        return binding.getRoot();
    }


    private void updateSubFiles() {
        fileAdapter.clear();
        grpcService.getSubFilesSync(currentFolder, new GrpcFileStreamObserver(fileAdapter, binding.swipeRefreshLayout));
    }

    private void moveToFolder(GrpcFile grpcFile) {
        if (currentFolder != null) {
            folderStack.push(currentFolder); // добавляем текущую папку в стек перед переходом
        }
        currentFolder = grpcFile; // обновляем текущую папку
        updateSubFiles();
    }

    /**
     * Событие когда кликают по одному из файлов
     */
    @Override
    public void onItemClick(int position) {
        try {
            GrpcFile grpcFile = fileAdapter.getFiles().get(position);
            if (grpcFile.getMediaType() == TypeOfFile.FOLDER) {
                moveToFolder(grpcFile);
            }
            //Toast.makeText(getActivity(), "Ты кликнул: " + grpcFile.getName(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {
        if (!folderStack.isEmpty()) {
            currentFolder = folderStack.pop(); // извлекаем предыдущую папку
            moveToFolder(currentFolder); // загружаем содержимое предыдущей папки
        } else {
            superOnBackPressed.run();
        }
    }
}
