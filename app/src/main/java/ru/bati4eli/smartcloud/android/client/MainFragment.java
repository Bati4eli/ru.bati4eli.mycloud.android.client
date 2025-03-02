package ru.bati4eli.smartcloud.android.client;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.bati4eli.smartcloud.android.client.tabs.ViewPagerAdapter;

public class MainFragment extends Fragment {

    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Инициализация ViewPager2 и BottomNavigationView
        viewPager = view.findViewById(R.id.viewPager);
        bottomNavigationView = view.findViewById(R.id.bottomNavigationView);

        // Устанавливаем Adapter для ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Устанавливаем слушатель для перелистывания страниц
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                Log.i("SERG", "### registerOnPageChangeCallback Position: " + position);
                // Синхронизация BottomNavigationView с ViewPager
                bottomNavigationView
                        .getMenu()
                        .getItem(position)
                        .setChecked(true);
            }
        });

        // Переключение вкладки при нажатии на кнопки меню внизу:
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Log.i("SERG", "### bottomNavigationView.setOnItemSelectedListener Position: " + item.getItemId());
            // Проверяем, какая вкладка выбрана, и устанавливаем соответствующую страницу ViewPager
            if (item.getItemId() == R.id.tab_files) {
                viewPager.setCurrentItem(0, true);
            } else if (item.getItemId() == R.id.tab_photos) {
                viewPager.setCurrentItem(1, true);
            } else if (item.getItemId() == R.id.tab_albums) {
                viewPager.setCurrentItem(2, true);
            } else if (item.getItemId() == R.id.tab_map) {
                viewPager.setCurrentItem(3, true);
            } else if (item.getItemId() == R.id.tab_settings) {
                viewPager.setCurrentItem(4, true);
            } else {
                // Обработка случая по умолчанию
                viewPager.setCurrentItem(0, true);
            }
            return true;
        });

        return view;
    }
}
