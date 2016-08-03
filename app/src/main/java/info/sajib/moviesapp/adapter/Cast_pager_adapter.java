package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import java.util.Collections;
import java.util.List;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;

/**
 * Created by sajib on 11-03-2016.
 */
public class Cast_pager_adapter extends PagerAdapter {
    private Context context;
    private int id;
    private RequestQueue requestque;
    private ImageLoader imageLoader;
    private List<Movie> listitem = Collections.emptyList();

    public Cast_pager_adapter(Context context, List<Movie> listitem) {
        this.listitem = listitem;
        requestque = VolleySingleton.getInstance().getRequestQueue();
        imageLoader = VolleySingleton.getInstance().getImageLoader();
        this.context = context;

    }

    @Override
    public int getCount() {
        return listitem.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final Movie movie = listitem.get(position);
        View itemview = LayoutInflater.from(context).inflate(R.layout.cast_details_background_viewpager_item, container, false);
        final ImageView imageView = (ImageView) itemview.findViewById(R.id.cast_detail_background_viewpager_item_slider);
        String Url = Endpoint.IMAGE + "/w1000/" + movie.getBackdropPath();
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


        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}

