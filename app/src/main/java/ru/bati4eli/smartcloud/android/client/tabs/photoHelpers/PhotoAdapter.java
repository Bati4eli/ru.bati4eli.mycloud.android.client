package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

public class PhotoAdapter extends AbstractItemAdapter<ShortMediaInfoDto> {

    private int itemSize = 120;

    public PhotoAdapter setItemSize(int size) {
        this.itemSize = size;
        notifyDataSetChanged();
        return this;
    }

    public PhotoAdapter(OnItemClickListener clickListener) {
        super(clickListener);
    }

    @NotNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int extViewType) {
        return new PhotoViewHolder(parent, itemSize);
    }

    public void finishAndShow() {
        notifyDataSetChanged();
    }

}
