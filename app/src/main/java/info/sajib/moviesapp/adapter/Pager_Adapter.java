package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.R;
/**
 * Created by sajib on 10-03-2016.
 */
public class Pager_Adapter extends PagerAdapter {
    Context context;
    String Url;
    List<Movie> data = Collections.emptyList();
    LayoutInflater mLayoutInflater;
    ImageLoader imageLoader;
    public Pager_Adapter(Context context, List<Movie> data) {
        this.context = context;
        this.data = data;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (FrameLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Movie movie=data.get(position);
        View itemview=mLayoutInflater.inflate(R.layout.viewpager_item_image_layout,container,false);
        Url= Endpoint.IMAGE+"/w500/"+movie.getBackdropPath();
        final ImageView imageView= (ImageView) itemview.findViewById(R.id.Slider);
        if(Url!=null)
        {
            imageLoader.get(Url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.drawable.yoimage);
                }
            });
        }
        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout)object);
    }
}
