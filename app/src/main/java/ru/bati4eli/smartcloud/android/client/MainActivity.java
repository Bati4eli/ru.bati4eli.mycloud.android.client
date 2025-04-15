package ru.bati4eli.smartcloud.android.client;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.navigation.NavigationBarView;
import ru.bati4eli.smartcloud.android.client.databinding.ActivityMainBinding;
import ru.bati4eli.smartcloud.android.client.tabs.helpers.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Устанавливаем Adapter для ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        // Устанавливаем слушатель для перелистывания страниц
        binding.viewPager.registerOnPageChangeCallback(getOnPageChangeCallback());
        // Переключение вкладки при нажатии на кнопки меню внизу
        binding.bottomNavigationView.setOnItemSelectedListener(getOnItemSelectedListener());
        setSupportActionBar(binding.toolbar);
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
}
