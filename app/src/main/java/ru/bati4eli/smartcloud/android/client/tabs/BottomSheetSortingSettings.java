package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import ru.bati4eli.smartcloud.android.client.MainActivity;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.BottomSheetSortingSettingsBinding;
import ru.bati4eli.smartcloud.android.client.enums.GroupNameEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortByEnum;
import ru.bati4eli.smartcloud.android.client.enums.SortOrderEnum;
import ru.bati4eli.smartcloud.android.client.enums.ViewTypeEnum;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

import java.util.Map;

public class BottomSheetSortingSettings extends BottomSheetDialogFragment {
    public static final Map<Integer, Integer> MAPPING_IDS = Map.of(
            //viewType
            R.id.rb_view_list, ViewTypeEnum.VIEW_LIST.getParameterId(),
            R.id.rb_view_grid, ViewTypeEnum.VIEW_GRID.getParameterId(),
            //sortBy
            R.id.rb_sort_by_changed, SortByEnum.SORT_BY_CHANGE_DATE.getParameterId(),
            R.id.rb_sort_by_created, SortByEnum.SORT_BY_CREATE_DATE.getParameterId(),
            R.id.rb_sort_by_name, SortByEnum.SORT_BY_NAME.getParameterId(),
            R.id.rb_sort_by_type, SortByEnum.SORT_BY_TYPE.getParameterId(),
            R.id.rb_sort_by_size, SortByEnum.SORT_BY_SIZE.getParameterId(),
            //sortOrder
            R.id.rb_sort_ascending, SortOrderEnum.SORT_ASCENDING.getParameterId(),
            R.id.rb_sort_descending, SortOrderEnum.SORT_DESCENDING.getParameterId()
    );

    private BottomSheetSortingSettingsBinding binding;
    private MainActivity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Использование View Binding для создания представления
        binding = BottomSheetSortingSettingsBinding.inflate(inflater, container, false);
        activity = (MainActivity) getActivity();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Инициализация RadioGroup для выбора вида представления
        initializeRadioGroup(binding.viewType, GroupNameEnum.VIEW_TYPE);
        // Инициализация RadioGroup для сортировки по
        initializeRadioGroup(binding.sortBy, GroupNameEnum.SORT_BY);
        // Инициализация RadioGroup для порядка сортировки
        initializeRadioGroup(binding.sortOrder, GroupNameEnum.SORT_ORDER);
    }

    /**
     * Инициализация радио-групп и их радио кнопок
     *
     * @param radioGroup
     * @param groupName
     */
    private void initializeRadioGroup(RadioGroup radioGroup, GroupNameEnum groupName) {
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            // Получаем каждый LinearLayout в RadioGroup
            LinearLayout layout = (LinearLayout) radioGroup.getChildAt(i);
            int innerCount = layout.getChildCount();
            for (int j = 0; j < innerCount; j++) {
                View view = layout.getChildAt(j);
                // Проверяем, является ли этот View RadioButton
                if (view instanceof RadioButton) {
                    // Установка параметров для RadioButton
                    RadioButton radioButton = (RadioButton) view;
                    radioButton.setChecked(isChecked(groupName, radioButton));
                    radioButton.setOnClickListener(v -> {
                        // Снимаем выбор у всех RadioButton в текущей группе
                        int id = radioButton.getId();
                        Integer value = MAPPING_IDS.get(id);
                        ParametersUtil.setSortParam(groupName, value);
                        activity.onParametersChanged(groupName, value);
                        clearRadioGroupSelection(radioGroup, id);
                    });
                }
            }
        }
    }

    /**
     * получение флага "Выбран"
     */
    private boolean isChecked(GroupNameEnum groupName, RadioButton radioButton) {
        // Id параметра, который соответствует radioButton Id
        Integer parameterId = MAPPING_IDS.get(radioButton.getId());
        // Сохраненный в параметрах Id
        int sortParam = ParametersUtil.getSortParam(groupName);
        return parameterId == sortParam;
    }

    /**
     * Снятие с других радио-кнопок флага
     */
    private void clearRadioGroupSelection(RadioGroup radioGroup, int selectedId) {
        int count = radioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            LinearLayout layout = (LinearLayout) radioGroup.getChildAt(i);
            int innerCount = layout.getChildCount();
            for (int j = 0; j < innerCount; j++) {
                View view = layout.getChildAt(j);
                if (view instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) view;
                    if (radioButton.getId() != selectedId) {
                        radioButton.setChecked(false); // Снимаем выбор с других RadioButton
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Освобождаем ссылки View Binding
        binding = null;
    }
}
