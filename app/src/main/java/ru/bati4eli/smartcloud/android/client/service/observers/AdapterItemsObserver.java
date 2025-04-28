package ru.bati4eli.smartcloud.android.client.service.observers;

import android.util.Log;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import lombok.Data;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


@Data
public class AdapterItemsObserver<TYPE, ADAPTER extends AbstractItemAdapter<TYPE>> extends BaseStreamObserver<TYPE> {
    private final ADAPTER adapter;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public AdapterItemsObserver(ADAPTER fileAdapter, SwipeRefreshLayout swipeRefreshLayout) {
        this.adapter = fileAdapter;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    public void onNext(TYPE item) {
        adapter.add(item);
    }

    @Override
    public void onError(Throwable throwable) {
        onFinally();
        Log.d(TAG, "AdapterItemsObserver: " + throwable.getLocalizedMessage());
    }

    @Override
    public void onCompleted() {
        onFinally();
    }

    private void onFinally() {
        swipeRefreshLayout.setRefreshing(false);
        adapter.finishAndShow();
        working.set(false);
    }

}
