// MonthFastScrollerAdapter.java
package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.MonthBucket;
import ru.bati4eli.smartcloud.android.client.utils.MyUtils;

import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class MonthFastScrollerAdapter extends RecyclerView.Adapter<MonthFastScrollerAdapter.VH> {

    public interface OnMonthClick {
        void onClick(YearMonth ym);
    }

    private final List<MonthBucket> buckets;
    private final OnMonthClick onClick;

    public MonthFastScrollerAdapter(List<MonthBucket> buckets, OnMonthClick onClick) {
        this.buckets = buckets;
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fast_month, parent, false);
        return new VH(v);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MonthBucket b = buckets.get(position);
        YearMonth ym = b.getYearMonth();
        holder.title.setText(MyUtils.getLabelYearAndMonth(ym));
        holder.itemView.setOnClickListener(v -> onClick.onClick(ym));
    }

    @Override
    public int getItemCount() {
        return buckets.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView title;

        VH(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.fastMonthTitle);
        }
    }
}
