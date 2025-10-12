package ru.bati4eli.smartcloud.android.client.tabs.common;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.grpc.stub.StreamObserver;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static ru.bati4eli.smartcloud.android.client.utils.Constants.TAG;

/**
 * Abstract RecyclerView.Adapter that also acts as a StreamObserver for a gRPC stream of items.
 * Accumulates incoming elements, notifies item insertions, and finalizes UI state (including
 * SwipeRefreshLayout) on completion or error. Exposes isWorking()/waiting() to track stream
 * lifecycle, and delegates click handling via the provided OnItemClickListener.
 *
 * Notes:
 * - Call clear() to reset the dataset; get(int) returns an item by position.
 * - onNext/onError/onCompleted should be invoked on the main thread when UI updates are required.
 *
 * @param <TYPE> the model type displayed by this adapter and emitted by the observed stream
 * @see androidx.recyclerview.widget.RecyclerView.Adapter
 * @see io.grpc.stub.StreamObserver
 * @see androidx.swiperefreshlayout.widget.SwipeRefreshLayout
 * @see ru.bati4eli.smartcloud.android.client.tabs.common.AbstractViewHolder
 * @see ru.bati4eli.smartcloud.android.client.tabs.common.OnItemClickListener
 */
@RequiredArgsConstructor
public abstract class AbstractObservableAdapter<TYPE>
        extends RecyclerView.Adapter<AbstractViewHolder<TYPE>>
        implements StreamObserver<TYPE>
{
    protected final OnItemClickListener listener;
    // protected TYPE response;
    protected Throwable error;
    @Getter
    @Setter
    private SwipeRefreshLayout swipeRefreshLayout;
    @Getter
    protected final List<TYPE> items = new ArrayList<>();
    protected AtomicBoolean working = new AtomicBoolean(true);


    /* GRPC OBSERVER METHOD */
    public final boolean isWorking() {
        return working.get();
    }

    /* GRPC OBSERVER METHOD */
    public final void waiting() {
        while (isWorking()) {
        }
    }

    /* GRPC OBSERVER METHOD */
    @Override
    public void onNext(TYPE item) {
        if (item == null) {
            return;
        }
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    /* GRPC OBSERVER METHOD */
    @Override
    public void onError(Throwable throwable) {
        onFinally();
        error = throwable;
        Log.d(TAG, "AdapterItemsObserver: " + throwable.getLocalizedMessage());
    }

    /* GRPC OBSERVER METHOD */
    @Override
    public void onCompleted() {
        onFinally();
    }

    /* GRPC OBSERVER METHOD */
    private void onFinally() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        notifyDataSetChanged();
        working.set(false);
    }

    /* ADAPTER METHOD */
    @Override
    public int getItemCount() {
        return items.size();
    }

    /* ADAPTER METHOD */
    public final void clear() {
        this.items.clear();
        notifyDataSetChanged();
    }

    /* ADAPTER METHOD */
    public final TYPE get(int position) {
        return items.get(position);
    }

    /* ADAPTER METHOD */
    @Override
    public void onBindViewHolder(@NonNull @NotNull AbstractViewHolder<TYPE> holder, int position) {
        holder.bind(get(position), listener);
    }

}
