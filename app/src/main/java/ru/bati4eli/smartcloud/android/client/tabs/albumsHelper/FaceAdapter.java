package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.PhotoAdapter;

public class FaceAdapter extends AbstractItemAdapter<FaceCardModel> {

    private int pixelSize;

    public FaceAdapter(OnItemClickListener listener) {
        super(listener);
    }

    public FaceAdapter setPixelSize(int size) {
        this.pixelSize = size;
        notifyDataSetChanged();
        return this;
    }

    @Override
    public void finishAndShow() {
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AbstractViewHolder<FaceCardModel> onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        return new FaceViewHolder(viewGroup,pixelSize);
    }
}
