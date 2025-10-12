package ru.bati4eli.smartcloud.android.client.tabs.photoHelpers;

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
import ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.PhotoItem;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

import static ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.ItemTypes.VT_HEADER;
import static ru.bati4eli.smartcloud.android.client.tabs.photoHelpers.models.ItemTypes.VT_PHOTO;

@Accessors(chain = true)
public class PhotoAdapter extends RecyclerView.Adapter<AbstractViewHolder<Item>> {
    private List<Item> items = new ArrayList();
    private int pixelSize = 120;
    private int totalItemsSize = 0;
    private GrpcService grpcService = MiserableDI.get(GrpcService.class);

    public YearMonth getMonthForPosition(int position) {
        if (position < 0 || position >= items.size()) return null;
        // Если это фото — вернуть его месяц, если заголовок — его же
        return items
                .get(position)
                .getYearMonth();
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({VT_HEADER, VT_PHOTO})
    public @interface ViewType {
    }

    public int addHeader(YearMonth ym) {
        HeaderItem header = new HeaderItem(ym);
        items.add(header);
        int pos = items.size() - 1;
        notifyItemInserted(pos);
        return pos;
    }

    public void addPhoto(YearMonth ym, ShortMediaInfoDto photo) {
        // Гарантируем, что перед фотками соответствующего месяца уже есть заголовок.
        // Здесь упрощённо: считаем, что заголовок добавлен ранее через addHeader().
        PhotoItem pi = new PhotoItem(ym, photo);
        items.add(pi);
        notifyItemInserted(items.size() - 1);
    }

    @Override
    public int getItemViewType(int position) {
        if(items.isEmpty()) return VT_HEADER; //todo fixme

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
            return new HeaderVH(parent);
        } else {
            //View v = inf.inflate(R.layout.item_photo_layout, parent, false);
            return new PhotoViewHolder(parent, pixelSize);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AbstractViewHolder holder, int position) {
        Item it = items.get(position);
        if (holder instanceof HeaderVH && it instanceof HeaderItem) {
            HeaderItem hi = (HeaderItem) it;
            ((HeaderVH) holder).bind(hi, null);
        } else if (holder instanceof PhotoViewHolder && it instanceof PhotoItem) {
            PhotoItem pi = (PhotoItem) it;
            ((PhotoViewHolder) holder).bind(pi, listener);
        }
    }

    @Override
    public int getItemCount() {
        return items.size() + 1;
    }

//    @SuppressLint("NewApi")
//    public void start() {
//        Iterator<DateCounterResponse> counters = grpcService.getAllPhotosDateCounters();
//        counters.forEachRemaining(counter -> {
//            int year = counter.getYear();
//            int month = counter.getMonth();
//            long amount = counter.getAmount();
//            YearMonth yearMonth = YearMonth.of(year, month);
//            //items.computeIfAbsent(yearMonth, k -> new PhotosContainer().setSize(amount));
//            totalItemsSize += amount + 1; // +1 Заголовок
//            Log.i(TAG, "Loading photos for " + year + "-" + month);
//        });
//    }

}
