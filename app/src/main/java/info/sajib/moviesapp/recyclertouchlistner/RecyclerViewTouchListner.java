package info.sajib.moviesapp.recyclertouchlistner;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
/**
 * Created by sajib on 03-04-2016.
 */
public class RecyclerViewTouchListner implements RecyclerView.OnItemTouchListener {
    private GestureDetector gestureDetector;
    ClickListner click;

    public RecyclerViewTouchListner(Context context, final RecyclerView recyclerView, final ClickListner click) {
        this.click = click;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                click.OnScroll();
                return super.onScroll(e1, e2, distanceX, distanceY);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && click != null ) {
                    click.OnLongClick(child, recyclerView.getChildAdapterPosition(child));
                }
            }
        });


    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && click != null && gestureDetector.onTouchEvent(e)) {
            click.OnClick(child, rv.getChildAdapterPosition(child));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

