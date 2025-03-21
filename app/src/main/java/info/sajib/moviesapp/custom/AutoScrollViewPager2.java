package info.sajib.moviesapp.custom;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import info.sajib.moviesapp.adapter.Cast_pager_adapter;
import info.sajib.moviesapp.pagetransformer.Zoompagetransformer;

public class AutoScrollViewPager2 extends FrameLayout {

    private ViewPager2 viewPager;
    private boolean isPagingEnabled = true;
    private boolean stopScrollWhenTouch = true;
    private int interval = 3000; // Default 3 seconds
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;

    public AutoScrollViewPager2(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AutoScrollViewPager2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        viewPager = new ViewPager2(context);
        addView(viewPager); // Add ViewPager2 inside this custom view

        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getAdapter() != null) {
                    int itemCount = viewPager.getAdapter().getItemCount();
                    int currentItem = viewPager.getCurrentItem();
                    if (itemCount > 1) {
                        viewPager.setCurrentItem((currentItem + 1) % itemCount, true);
                        handler.postDelayed(this, interval);
                    }
                }
            }
        };
    }

    // ✅ Start auto-scroll
    public void startAutoScroll(int delay) {
        interval = delay;
        handler.removeCallbacks(autoScrollRunnable);
        handler.postDelayed(autoScrollRunnable, interval);
    }

    // ✅ Stop auto-scroll
    public void stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable);
    }

    // ✅ Set scrolling interval
    public void setInterval(int interval) {
        this.interval = interval;
    }

    // ✅ Enable or disable paging manually
    public void setPagingEnabled(boolean enabled) {
        isPagingEnabled = enabled;
    }

    // ✅ Stop scrolling when touched
    public void setStopScrollWhenTouch(boolean stopScrollWhenTouch) {
        this.stopScrollWhenTouch = stopScrollWhenTouch;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isPagingEnabled) {
            return false;
        }

        if (stopScrollWhenTouch) {
            stopAutoScroll();
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isPagingEnabled) {
            return false;
        }

        if (stopScrollWhenTouch && ev.getAction() == MotionEvent.ACTION_UP) {
            startAutoScroll(interval);
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAutoScroll();
    }

    public ViewPager2 getViewPager() {
        return viewPager;
    }

    public void setPageTransformer(ViewPager2.PageTransformer pageTransformer) {
        this.viewPager.setPageTransformer(pageTransformer);
    }

    public void setAdapter(@Nullable @SuppressWarnings("rawtypes") RecyclerView.Adapter adapter) {
        this.viewPager.setAdapter(adapter);
    }
}
