package ru.bati4eli.smartcloud.android.client.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.databinding.TabAlbumsBinding;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnBackPressedListener;

public class AlbumsFragment extends Fragment implements OnBackPressedListener {
    private TabAlbumsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = TabAlbumsBinding.inflate(inflater, container, false);

        binding.myCard.setAwesomeIcon(R.drawable.ic_folder);
        binding.myCard.setLabel("Photos");
        binding.myCard.setAmount(20);
        binding.myCard.setImgSrc(R.drawable.ic_file);
        return binding.getRoot();
    }

    @Override
    public void onBackPressed(Runnable superOnBackPressed) {

    }
}
