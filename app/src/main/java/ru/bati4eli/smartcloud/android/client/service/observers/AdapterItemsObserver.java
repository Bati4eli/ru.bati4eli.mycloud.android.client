package ru.bati4eli.smartcloud.android.client.service.observers;

import android.util.Log;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import ru.bati4eli.smartcloud.android.client.tabs.common.AbstractItemAdapter;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;


public class AdapterItemsObserver<TYPE, ADAPTER extends AbstractItemAdapter<TYPE>> extends BaseStreamObserver<TYPE> {
    private final ADAPTER adapter;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public AdapterItemsObserver(ADAPTER adapter) {
        this.adapter = adapter;
        this.swipeRefreshLayout = adapter.getSwipeRefreshLayout();
    }

    /* GRPC OBSERVER METHOD */
    @Override
    public void onNext(TYPE item) {
        adapter.add(item);
    }

    /* GRPC OBSERVER METHOD */
    @Override
    public void onError(Throwable throwable) {
        onFinally();
        Log.d(TAG, "AdapterItemsObserver: " + throwable.getLocalizedMessage());
    }

    /* GRPC OBSERVER METHOD */
    @Override
    public void onCompleted() {
        onFinally();
    }

    private void onFinally() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        adapter.finishAndShow();
        working.set(false);
    }

}
