package ru.bati4eli.smartcloud.android.client;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.navigation.NavigationBarView;
import lombok.Getter;
import ru.bati4eli.smartcloud.android.client.databinding.ActivityMainBinding;
import ru.bati4eli.smartcloud.android.client.enums.GroupNameEnum;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnChangedSortOrView;
import ru.bati4eli.smartcloud.android.client.tabs.common.ViewPagerAdapter;
import ru.bati4eli.smartcloud.android.client.utils.ParametersUtil;

public class MainActivity extends AppCompatActivity implements OnChangedSortOrView {

    @Getter
    private ActivityMainBinding binding;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParametersUtil.setContext(getApplicationContext());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Устанавливаем Adapter для ViewPager2
        adapter = new ViewPagerAdapter(this, binding.viewPager);
        adapter.registerOnPageChangeCallback(binding.bottomNavigationView);
        // Переключение вкладки при нажатии на кнопки меню внизу
        binding.bottomNavigationView.setOnItemSelectedListener(getOnItemSelectedListener());
        //binding.viewPager.setUserInputEnabled(false);
    }

    /**
     * Перелистывает страницы, при клике кнопок внизу.
     */
    private NavigationBarView.OnItemSelectedListener getOnItemSelectedListener() {
        return item -> {
            adapter.setupPosition(item);
            return true;
        };
    }

    /**
     * При нажатии НАЗАД передается вызов текущей странице
     */
    @Override
    public void onBackPressed() {
        Fragment currentFragment = adapter.getCurrentFragment();
        if (currentFragment instanceof OnBackPressedListener) {
            // Передача эвента фрагменту
            ((OnBackPressedListener) currentFragment)
                    .onBackPressed(super::onBackPressed);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Получение уведомления о смене сортировки или настроек вида.
     */
    @Override
    public void onParametersChanged(GroupNameEnum groupName, Integer value) {
        Fragment currentFragment = adapter.getCurrentFragment();
        if (currentFragment instanceof OnChangedSortOrView) {
            ((OnChangedSortOrView) currentFragment).onParametersChanged(groupName, value);
        }
    }
}
