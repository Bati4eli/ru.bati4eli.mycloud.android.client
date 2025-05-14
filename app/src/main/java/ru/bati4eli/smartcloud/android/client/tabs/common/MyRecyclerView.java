package ru.bati4eli.smartcloud.android.client.tabs.common;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(Context context) { super(context); }
    public MyRecyclerView(Context context, AttributeSet attrs) { super(context, attrs); }
    public MyRecyclerView(Context context, AttributeSet attrs, int defStyle) { super(context, attrs, defStyle); }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Когда multitouch: сразу запрещаем родителю трогать
        if (ev.getPointerCount() > 1) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // То же, на всякий случай
        if (ev.getPointerCount() > 1) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return super.onTouchEvent(ev);
    }
}
