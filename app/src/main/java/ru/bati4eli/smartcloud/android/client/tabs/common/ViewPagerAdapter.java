package ru.bati4eli.smartcloud.android.client.tabs.common;

import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.tabs.AlbumsFragment;
import ru.bati4eli.smartcloud.android.client.tabs.FilesFragment;
import ru.bati4eli.smartcloud.android.client.tabs.MapFragment;
import ru.bati4eli.smartcloud.android.client.tabs.PhotosFragment;
import ru.bati4eli.smartcloud.android.client.tabs.SettingsFragment;

import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> fragments = List.of(
            new FilesFragment(),
            new PhotosFragment(),
            new AlbumsFragment(),
            new MapFragment(),
            new SettingsFragment()
    );

    private final ViewPager2 viewPager;

    public ViewPagerAdapter(@NonNull AppCompatActivity activity, ViewPager2 viewPager) {
        super(activity);
        viewPager.setAdapter(this);
        this.viewPager = viewPager;
        viewPager.setUserInputEnabled(false);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public Fragment getCurrentFragment() {
        return fragments.get(viewPager.getCurrentItem());
    }

    public void setupPosition(MenuItem item) {
        Integer itemId = item.getItemId();
        Log.i("MainActivity", "### bottomNavigationView.setOnItemSelectedListener Position: " + itemId);
        // Проверяем, какая вкладка выбрана, и устанавливаем соответствующую страницу ViewPager
        int position;
        if (itemId == R.id.tab_files) position = 0;
        else if (itemId == R.id.tab_photos) position = 1;
        else if (itemId == R.id.tab_albums) position = 2;
        else if (itemId == R.id.tab_map) position = 3;
        else if (itemId == R.id.tab_settings) position = 4;
        else position = 0;

//        int position = fragments.stream()
//                .filter(fragment -> fragment.getId() == itemId)
//                .findFirst()
//                .map(Fragment::getId)
//                .orElse(0);
        viewPager.setCurrentItem(position, true);
    }

    /**
     * Устанавливаем слушатель для перелистывания страниц.
     * @param bottomNavigationView
     */
    public void registerOnPageChangeCallback(BottomNavigationView bottomNavigationView) {
        viewPager.registerOnPageChangeCallback(getOnPageChangeCallback(bottomNavigationView));
    }

    /**
     * Подсвечивает кнопку выбранной вкладки.
     * @param bottomNavigationView
     */
    private ViewPager2.OnPageChangeCallback getOnPageChangeCallback(final BottomNavigationView bottomNavigationView) {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Синхронизация BottomNavigationView с ViewPager
                bottomNavigationView
                        .getMenu()
                        .getItem(position)
                        .setChecked(true);
            }
        };
    }
}
