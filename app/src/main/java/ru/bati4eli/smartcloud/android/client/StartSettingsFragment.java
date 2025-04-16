package ru.bati4eli.smartcloud.android.client;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import ru.bati4eli.smartcloud.android.client.databinding.FragmentStartSettingsBinding;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.DCIM_PATH;


public class StartSettingsFragment extends Fragment {

    private FragmentStartSettingsBinding binding;

    private static final int FOLDER_PICKER_REQUEST_CODE = 1; // Код для выбора папки

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentStartSettingsBinding.inflate(inflater, container, false);
//        Log.i(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath());
//        Log.i(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
//        Log.i(TAG, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath());

        // Установка значений по умолчанию
        binding.selectedPathEntry.setText(ParametersUtil.getMainFolder());
        binding.selectedPathEntry.setText(DCIM_PATH);
        binding.switchNeedScreenshots.setChecked(ParametersUtil.getNeedScreenshots());
        binding.switchNeedYears.setChecked(ParametersUtil.getNeedSplitsByYears());
        // Установка действий кнопок
        binding.prevButton.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.return_to_login));
        binding.nextButton.setOnClickListener(v -> onNextButtonClicked());
        binding.selectFolderButton.setOnClickListener(v -> openFolderPicker());
        return binding.getRoot();
    }

    private void openFolderPicker() {
        // Открытие папки (необходима реализация выбора директории)
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, FOLDER_PICKER_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FOLDER_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Получаем Uri папки и обновляем текстовое поле
                binding.selectedPathEntry.setText(data.getData().getPath()); // заменить на правильное получение пути
            } else {
                Toast.makeText(getActivity(), "Папка не выбрана.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onNextButtonClicked() {
        String syncFolder = binding.selectedPathEntry.getText().toString();
        boolean isNeedScreens = binding.switchNeedScreenshots.isChecked();
        boolean isNeedYears = binding.switchNeedYears.isChecked();

        if (syncFolder.isEmpty()) {
            Toast.makeText(getActivity(), "Папка не может быть пустой", Toast.LENGTH_SHORT).show();
            return;
        }

        ParametersUtil.setMainFolder(syncFolder);
        ParametersUtil.setNeedSetupPage(false);
        ParametersUtil.setNeedSplitsByYears(isNeedYears);
        ParametersUtil.setNeedScreenshots(isNeedScreens);
        // Navigation to MainActivity
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
