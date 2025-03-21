package info.sajib.moviesapp.pagetransformer;

import android.content.Context;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;

import androidx.viewpager2.widget.ViewPager2;

import java.util.Random;

import info.sajib.moviesapp.R;

/**
 * Created by sajib on 11-03-2016.
 */
public class Zoompagetransformer implements ViewPager2.PageTransformer {
    Context context;

    public Zoompagetransformer(Context context) {
        this.context = context;
    }

    private static final float MIN_SCALE = 0.75f;

    public void transformPage(final View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
                Random random=new Random();
                final float pivotx=random.nextFloat();
                final float pivoty=random.nextFloat();
            float fromScale = Scalee();
            float toScale = Scalee();
            view.setScaleX(fromScale);
            view.setScaleY(fromScale);
            view.setTranslationX(Translation(view.getWidth(), fromScale));
            view.setTranslationY(Translation(view.getHeight(), fromScale));
            ViewPropertyAnimator propertyAnimator = view.animate().
                    translationX(Translation(view.getHeight(),toScale)).
                    translationY(Translation(view.getHeight(),toScale)).
                    scaleX(toScale).
                    scaleY(toScale).
                    setDuration(6000);
            propertyAnimator.start();
            // Use the default slide transition when moving to the left page
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) { // (0,1]
            // Fade the page out.

            view.setAlpha(1 - position);

            // Counteract the default slide transition
            view.setTranslationX(pageWidth * -position);

            // Scale the page down (between MIN_SCALE and 1)
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);


        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            view.setAlpha(0);
        }
    }

    private float Scalee() {
        Random random=new Random();
        float ab=random.nextFloat();
        if (ab>0.5){
            return (float)1.0 + ab * (float).5;
        }
        else {
            return (float) 1.0 + ab;
        }
    }

    private float Translation(int value, float ratio) {
        Random random=new Random();
        float ab=random.nextFloat();
        return value * (ratio - 1.0f) * (ab - 0.5f);
    }

}
