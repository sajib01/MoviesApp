package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 13-04-2016.
 */
public class Tv_description_viewpageraddapter extends PagerAdapter {
    Context context;
    List<String> backdrops= Collections.emptyList();
    ImageLoader imageLoader;
    public Tv_description_viewpageraddapter(Context context, List<String> backdrops) {
        this.context=context;
        this.backdrops=backdrops;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return backdrops.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(LinearLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.tv_description_layout_viewpager_layout,container,false);
        FadeInNetworkImageView imageView= (FadeInNetworkImageView) itemview.findViewById(R.id.tv_description_layout_viewpager_layout_imageview);
        String url= Endpoint.IMAGE+"/w500/"+backdrops.get(position);
        imageView.setImageUrl(url,imageLoader);
        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }
}
