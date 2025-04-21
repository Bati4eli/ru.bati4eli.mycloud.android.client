package ru.bati4eli.smartcloud.android.client;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationBarView;
import lombok.Getter;
import ru.bati4eli.smartcloud.android.client.databinding.ActivityMainBinding;
import ru.bati4eli.smartcloud.android.client.enums.GroupNameEnum;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.OnBackPressedListener;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.OnChangedSortOrView;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.ViewPagerAdapter;
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
        // Устанавливаем слушатель для перелистывания страниц
        binding.viewPager.registerOnPageChangeCallback(getOnPageChangeCallback());
        // Переключение вкладки при нажатии на кнопки меню внизу
        binding.bottomNavigationView.setOnItemSelectedListener(getOnItemSelectedListener());
    }

    private ViewPager2.OnPageChangeCallback getOnPageChangeCallback() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Синхронизация BottomNavigationView с ViewPager
                binding.bottomNavigationView
                        .getMenu()
                        .getItem(position)
                        .setChecked(true);
            }
        };
    }

    private NavigationBarView.OnItemSelectedListener getOnItemSelectedListener() {
        return item -> {
            Log.i("MainActivity", "### bottomNavigationView.setOnItemSelectedListener Position: " + item.getItemId());
            // Проверяем, какая вкладка выбрана, и устанавливаем соответствующую страницу ViewPager
            int position;

            if (item.getItemId() == R.id.tab_files) position = 0;
            else if (item.getItemId() == R.id.tab_photos) position = 1;
            else if (item.getItemId() == R.id.tab_albums) position = 2;
            else if (item.getItemId() == R.id.tab_map) position = 3;
            else if (item.getItemId() == R.id.tab_settings) position = 4;
            else position = 0;

            binding.viewPager.setCurrentItem(position, true);
            return true;
        };
    }

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
