package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import ru.bati4eli.smartcloud.android.client.databinding.AlbumCardBinding;

public class AlbumCard extends RelativeLayout {
    private AlbumCardBinding binding;

    public AlbumCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = AlbumCardBinding.inflate(inflater, this, true);
    }

    public void setImgSrc(int resId) {
        binding.albumImage.setImageResource(resId);
    }

    public void setLabel(String label) {
        binding.labelText.setText(label);
    }

    public void setAmount(int amount) {
        binding.amountText.setText(String.valueOf(amount));
    }

    public void setAwesomeIcon(int resId) {
        binding.awesomeIcon.setImageResource(resId);
    }
}
