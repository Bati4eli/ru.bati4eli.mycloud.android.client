package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import lombok.Getter;
import ru.bati4eli.mycloud.repo.DownloadType;
import ru.bati4eli.smartcloud.android.client.databinding.AlbumCardBinding;
import ru.bati4eli.smartcloud.android.client.model.ShortInfo;

import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.setupPreviewAsync;

public class AlbumCardLayout extends RelativeLayout {

    @Getter
    private AlbumCardBinding binding;

    public AlbumCardLayout(Context context, AttributeSet attrs, AlbumCardModel model) {
        super(context, attrs);
        LayoutInflater inflater = LayoutInflater.from(context);
        binding = AlbumCardBinding.inflate(inflater, this, true);
        setAll(model);
    }

    public void setAll(AlbumCardModel model) {
        setLabel(model.getLabel());
        setAmount(model.getAmount());
        setAwesomeIcon(model.getFontAwesomeIcon());
        setupPreviewAsync(ShortInfo.of(model.getAlbumInfo()), binding.albumImage, DownloadType.PREVIEW_MINI);
    }

    public void setLabel(String label) {
        binding.labelText.setText(label);
    }

    public void setAmount(int amount) {
        binding.amountText.setText(String.valueOf(amount));
    }

    public void setAwesomeIcon(String fontAwesomeIcon) {
//        FontAwesomeIcons fontAwesomeIcon
//        IconDrawable iconDrawable = new IconDrawable(getContext(), fontAwesomeIcon);
        binding.awesomeIcon.setText(fontAwesomeIcon);
    }
}
