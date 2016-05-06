package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.activity.Picactivity;
import info.sajib.moviesapp.activity.TrailerActivity;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 07-04-2016.
 */
public class Descriptionactivity_Pageradapter extends PagerAdapter {
    List<Movie> data = Collections.emptyList();
    ImageLoader imageLoader;
    Context context;
    public Descriptionactivity_Pageradapter(Context context, List<Movie> data) {
        this.context = context;
        this.data = data;
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
    public Object instantiateItem(final ViewGroup container, int position) {
        final Movie movie = data.get(position);
        View view = LayoutInflater.from(context).inflate(R.layout.viewpager2_layout, container, false);
        final ImageView imageView= (ImageView) view.findViewById(R.id.viewpager2_image);
        final ImageView youtubeicon= (ImageView) view.findViewById(R.id.youtubeicon);
        final ImageView picshade= (ImageView) view.findViewById(R.id.picshade);
        youtubeicon.setImageResource(R.drawable.youtube);
        String url=null;

        if(movie.getName()!=null)
        {

          url=Endpoint.YOUTUBE_THUMBNAIL+movie.getSource()+"/hqdefault.jpg";
        }
        if(movie.getName()==null)
        {

            url=Endpoint.IMAGE+"/w500/"+movie.getBackdropPath();
        }
        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        if(movie.getName()!=null)
        {
            youtubeicon.setVisibility(View.VISIBLE);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, TrailerActivity.class);
                    intent.putExtra("trailerid",movie.getSource());
                    context.startActivity(intent);
                }
            });
        }
        if(movie.getName()==null)
        {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Picactivity.class);
                    intent.putExtra("movieid",movie.getId());
                    context.startActivity(intent);
                }
            });
        }

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}
