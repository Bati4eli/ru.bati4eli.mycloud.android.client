package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

import android.util.Log;
import android.view.ViewGroup;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.jetbrains.annotations.NotNull;
import ru.bati4eli.mycloud.repo.ShortMediaInfoDto;
import ru.bati4eli.smartcloud.android.client.service.GrpcService;
import ru.bati4eli.smartcloud.android.client.service.MiserableDI;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder;
import ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.HeaderItem;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.Item;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.MonthBucket;
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.PhotoItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.ItemTypes.VT_HEADER;
import static ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.ItemTypes.VT_PHOTO;
import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

@Accessors(chain = true)
public class PhotoAdapter extends RecyclerView.Adapter<AbstractViewHolder<Item>> {
    private List<Item> items = new ArrayList<>();
    private int pixelSize = 120;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    public YearMonth getMonthForPosition(int position) {
        if (position < 0 || position >= items.size()) return null;
        // Если это фото — вернуть его месяц, если заголовок — его же
        return items
                .get(position)
                .getYearMonth();
    }

    /**
     * Заполняем items пустыми ячейками, и подгоняем по размерам его до указанных в счетчиках
     */
    public void initListFromCounters(List<MonthBucket> monthBuckets) {
        monthBuckets.forEach(bucket -> {
            int headerPosition = addHeader(bucket);    // Добавляем заголовок
            bucket.setStartIndexPhoto(headerPosition); // Начало будет указывать на заголовок бакета
            for (long i = 0; i < bucket.getAmount(); i++) {
                items.add(new PhotoItem(bucket));
            }
            bucket.setEndIndexPhoto(items.size() - 1); // Запоминаем индекс последнего фото для этой даты
        });
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VT_HEADER, VT_PHOTO})
    public @interface ViewType {
    }

    private int addHeader(MonthBucket bucket) {
        items.add(new HeaderItem(bucket));
        int position = items.size() - 1;
        notifyItemInserted(position);
        return position;
    }

    public void addPhoto(MonthBucket bucket, ShortMediaInfoDto mediaInfoDto) {
        int index = bucket.getIndex().incrementAndGet(); // инкрементируем индекс в бакете для вставки
        Log.d(TAG, "PhotoAdapter addPhoto: getFileId=" + mediaInfoDto.getFileId() + ", index=" + index);
        if (index > bucket.getEndIndexPhoto()) {
            Log.w(TAG, "Index out of bound! bucket = " + bucket.getYearMonth() + ", index=" + index + ", endIndexPhoto=" + bucket.getEndIndexPhoto());
            return;
        }
        PhotoItem item = (PhotoItem) items.get(index);
        item.setPhoto(mediaInfoDto);
        item.setLoaded(true);
        bucket.setLoaded(true);
        notifyItemChanged(index);
    }

    @Override
    public int getItemViewType(int position) {
        Item it = items.get(position);
        if (it instanceof HeaderItem) return VT_HEADER;
        return VT_PHOTO;
    }

    @Setter
    @Getter
    private OnItemClickListener<PhotoItem> listener;

    @Setter
    @Getter
    private SwipeRefreshLayout swipeRefreshLayout;

    public PhotoAdapter setPixelSize(int size) {
        this.pixelSize = size;
        notifyDataSetChanged();
        return this;
    }

    @NonNull
    @Override
    public AbstractViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //LayoutInflater inf = LayoutInflater.from(parent.getContext());
        if (viewType == VT_HEADER) {
            //View v = inf.inflate(R.layout.item_month_header, parent, false);
            return new HeaderViewHolder(parent);
        } else {
            //View v = inf.inflate(R.layout.item_photo_layout, parent, false);
            return new PhotoViewHolder(parent, pixelSize);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AbstractViewHolder holder, int position) {
        Item it = items.get(position);
        holder.bind(it, listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public MonthBucket getBucketByIndex(int index) {
        return items.get(index).getBucket();
    }

}
