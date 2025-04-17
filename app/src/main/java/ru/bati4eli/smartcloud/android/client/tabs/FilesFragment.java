package ru.bati4eli.smartcloud.android.client.tabs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.appbar.MaterialToolbar;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.MainActivity;
import ru.bati4eli.smartcloud.android.client.R;
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
    private TabFilesBinding binding;
    private MainActivity activity;
    private MaterialToolbar toolbar;
    private GrpcFile currentFolder;
    private FileAdapter fileAdapter;
    private Stack<GrpcFile> folderStack = new Stack<>();
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabFilesBinding.inflate(inflater, container, false);
        activity = (MainActivity) getActivity();
        toolbar = binding.toolbar;
        toolbar.setOnMenuItemClickListener(this::onToolbarItemClicked);

        fileAdapter = new FileAdapter(this);
        binding.recyclerViewFiles.setAdapter(fileAdapter);
        binding.recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        // Вызов метода обновления данных
        binding.swipeRefreshLayout.setOnRefreshListener(this::updateSubFiles);
        moveToFolder(grpcService.getRootFileInfo());
        return binding.getRoot();
    }

    /**
     * Обработка по клику на кнопках меню в тулбаре.
     */
    public boolean onToolbarItemClicked(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_select) {// Действия для добавления
            return true;
        } else if (itemId == R.id.action_search) {// Действия для поиска
            return true;
        } else if (itemId == R.id.action_option) {
            BottomSheetSortingSettings bottomSheetFragment = new BottomSheetSortingSettings();
            bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
            return true;
        }
        return false;
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
        updateToolbarNavigationState();
        updateSubFiles();
    }

    private void updateToolbarNavigationState() {
        if (isAtRootDirectory()) {
            toolbar.setNavigationIcon(null);
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_back);
        }
        toolbar.setTitle(isAtRootDirectory() ? "Files:" : currentFolder.getName());
        toolbar.setNavigationOnClickListener(isAtRootDirectory() ? null : v -> activity.onBackPressed());
    }

    private boolean isAtRootDirectory() {
        return currentFolder.getMediaType() == TypeOfFile.ROOT;
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

    /**
     * Метод onAttach(Context context) является частью жизненного цикла фрагмента в Android и вызывается,
     * когда фрагмент присоединяется к активности. Этот метод предоставляет возможность получить доступ к активности,
     * к которой данный фрагмент прикреплен, и выполнить необходимые действия на ранних этапах жизненного цикла фрагмента.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}
