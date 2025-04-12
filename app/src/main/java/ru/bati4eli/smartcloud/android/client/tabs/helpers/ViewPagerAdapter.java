package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.AlbumsFragment;
import ru.bati4eli.smartcloud.android.client.tabs.FilesFragment;
import ru.bati4eli.smartcloud.android.client.tabs.MapFragment;
import ru.bati4eli.smartcloud.android.client.tabs.PhotosFragment;
import ru.bati4eli.smartcloud.android.client.tabs.SettingsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FilesFragment(); // Первая вкладка
            case 1:
                return new PhotosFragment(); // Вторая вкладка
            case 2:
                return new AlbumsFragment(); // Третья вкладка
            case 3:
                return new MapFragment(); // Четвертая вкладка
            case 4:
                return new SettingsFragment(); // Пятая вкладка
            default:
                return new FilesFragment(); // По умолчанию первая вкладка
        }
    }

    @Override
    public int getItemCount() {
        return 5; // Всего пять вкладок
    }
}
