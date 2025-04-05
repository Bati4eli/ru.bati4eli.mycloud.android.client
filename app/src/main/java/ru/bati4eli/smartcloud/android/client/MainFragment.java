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
import com.google.android.material.navigation.NavigationBarView;
import ru.bati4eli.smartcloud.android.client.databinding.FragmentMainBinding;
import ru.bati4eli.smartcloud.android.client.tabs.ViewPagerAdapter;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(inflater, container, false);
        // Устанавливаем Adapter для ViewPager2
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        binding.viewPager.setAdapter(adapter);
        // Устанавливаем слушатель для перелистывания страниц
        binding.viewPager.registerOnPageChangeCallback(getOnPageChangeCallback());
        // Переключение вкладки при нажатии на кнопки меню внизу:
        binding.bottomNavigationView.setOnItemSelectedListener(getOnItemSelectedListener());
        return binding.getRoot();
    }

    private ViewPager2.OnPageChangeCallback getOnPageChangeCallback() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.i("SERG", "### registerOnPageChangeCallback Position: " + position);
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
            Log.i("SERG", "### bottomNavigationView.setOnItemSelectedListener Position: " + item.getItemId());
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
