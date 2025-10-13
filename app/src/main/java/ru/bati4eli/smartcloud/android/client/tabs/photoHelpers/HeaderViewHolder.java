package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.annotation.SuppressLint;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import ru.bati4eli.smartcloud.android.client.R;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.HeaderItem;

import static ru.bati4eli.smartcloud.android.client.utils.MyUtils.getLabelYearAndMonth;


public class HeaderViewHolder extends AbstractViewHolder<HeaderItem> {
    private TextView title;

    public HeaderViewHolder(@NonNull ViewGroup parent) {
        super(parent, R.layout.item_month_header);
        title = itemView.findViewById(R.id.headerTitle);
    }

    @SuppressLint("NewApi")
    @Override
    public void bind(HeaderItem item, OnItemClickListener<HeaderItem> listener) {
        String label = getLabelYearAndMonth(item.getYearMonth());
        title.setText(label);
    }

}
