package ru.bati4eli.smartcloud.android.client.tabs.helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
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

    public ViewPagerAdapter(@NonNull AppCompatActivity fragment, ViewPager2 viewPager) {
        super(fragment);
        viewPager.setAdapter(this);
        this.viewPager = viewPager;
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
}
