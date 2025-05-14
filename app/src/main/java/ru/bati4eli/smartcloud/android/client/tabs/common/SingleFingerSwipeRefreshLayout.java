package ru.bati4eli.smartcloud.android.client.tabs.common;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SingleFingerSwipeRefreshLayout extends androidx.swiperefreshlayout.widget.SwipeRefreshLayout {
    private boolean isMultiTouch = false;

    public SingleFingerSwipeRefreshLayout(Context context) {
        super(context);
    }

    public SingleFingerSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // Проверяем любые новые пальцы
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isMultiTouch = false;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                isMultiTouch = true;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // Если опять стал одиночным дотрагиванием
                if (ev.getPointerCount() - 1 == 1) {
                    isMultiTouch = false;
                }
                break;
        }

        // Если на данный момент multitouch — вообще ничего не перехватывать
        if (isMultiTouch) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        // Аналогично — предотвращаем refresh при любом multitouch
        if (isMultiTouch || ev.getPointerCount() > 1) {
            return false;
        }
        return super.onTouchEvent(ev);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        // Отключаем прослушивание жеста, если мультитач
//        if (ev.getPointerCount() > 1) {
//            return false;
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
