package info.sajib.moviesapp.custom;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by sajib on 21-03-2016.
 */
public class Customviewpager extends AutoScrollViewPager {
    private boolean enabled=false;
    public Customviewpager(Context context) {
        super(context);
    }

    public Customviewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }

        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
