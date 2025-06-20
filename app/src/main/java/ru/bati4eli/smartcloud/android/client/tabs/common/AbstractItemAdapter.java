package ru.bati4eli.smartcloud.android.client.tabs.common;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractItemAdapter<TYPE> extends RecyclerView.Adapter<AbstractViewHolder<TYPE>> {

    protected final OnItemClickListener listener;

    @Getter
    protected final List<TYPE> items = new ArrayList<>();

    public abstract void finishAndShow();

    public void add(TYPE item) {
        if (item == null) {
            return;
        }
        items.add(item);
        notifyItemInserted(items.size() - 1);
        //notifyItemChanged(items.size() - 1);
    }

    public AbstractItemAdapter<TYPE> addAll(List<TYPE> list) {
        items.addAll(list);
        notifyDataSetChanged();
        return this;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public final void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    public final TYPE get(int position) {
        return items.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AbstractViewHolder<TYPE> holder, int position) {
        holder.bind(get(position), listener);
    }

}
