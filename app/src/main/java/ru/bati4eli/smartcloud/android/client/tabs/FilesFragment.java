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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.material.appbar.MaterialToolbar;
import ru.bati4eli.mycloud.repo.GrpcFile;
import ru.bati4eli.mycloud.repo.TypeOfFile;
import ru.bati4eli.smartcloud.android.client.MainActivity;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.TabFilesBinding;
import ru.bati4eli.smartcloud.android.client.enums.GroupNameEnum;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.service.observers.AdapterItemsObserver;
import ru.bati4eli.smartcloud.android.client.tabs.fileHelpers.BottomSheetSortingSettings;
import ru.bati4eli.smartcloud.android.client.tabs.fileHelpers.FileAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnChangedSortOrView;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.utils.GrpcFileComparator;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.util.Stack;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;
import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.calculateSpanCount;

public class FilesFragment extends Fragment implements OnItemClickListener, OnBackPressedListener, OnChangedSortOrView {
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

        fileAdapter = new FileAdapter(this, ParametersUtil.getViewType());
        changeViewType();
        binding.recyclerViewFiles.setHasFixedSize(true);
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
        if (itemId == R.id.action_select) {// Действия для выбора
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
        fileAdapter.resetNewTree(GrpcFileComparator.getFileComparator());
        grpcService.getSubFilesSync(currentFolder, new AdapterItemsObserver<>(fileAdapter, binding.swipeRefreshLayout));
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
            GrpcFile grpcFile = fileAdapter.get(position);
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
     * Получение уведомления о смене сортировки или настроек вида.
     */
    @Override
    public void onParametersChanged(GroupNameEnum groupName, Integer value) {
        if (groupName == GroupNameEnum.VIEW_TYPE) {
            changeViewType();
            fileAdapter.notifyDataSetChanged();
        } else {
            fileAdapter.reSort();
        }
    }

    private void changeViewType() {
        final ViewTypeEnum viewType = ParametersUtil.getViewType();

        fileAdapter.setViewType(viewType);
        binding.recyclerViewFiles.setAdapter(fileAdapter);
        if (viewType == ViewTypeEnum.VIEW_LIST) {
            binding.recyclerViewFiles.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            int spanCount = calculateSpanCount(this, 100); // Метод для определения количества колонок
            binding.recyclerViewFiles.setLayoutManager(new GridLayoutManager(getContext(), spanCount));
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
