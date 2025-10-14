package ru.bati4eli.smartcloud.android.client.tabs.albumsHelper;

import android.view.ViewGroup;
import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;

public class FaceAdapter extends AbstractItemAdapter<FaceCardModel> {

    public FaceAdapter(OnItemClickListener listener) {
        super(listener);
    }

    @Override
    public void finishAndShow() {
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public AbstractViewHolder<FaceCardModel> onCreateViewHolder(@NonNull @NotNull ViewGroup viewGroup, int i) {
        return new FaceViewHolder(viewGroup);
    }
}
