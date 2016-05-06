package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.activity.Tv_description;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
/**
 * Created by sajib on 06-04-2016.
 */
public class viewpager1_adapter extends PagerAdapter {

    ImageLoader imageLoader;
    Context context;
    List<Tv> tvitem= Collections.emptyList();
    public viewpager1_adapter(Context context) {
        this.context=context;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return tvitem.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==(RelativeLayout)object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.viewpager1_layout,container,false);
        final ImageView imageView= (ImageView) itemview.findViewById(R.id.viewpager1_image);
        ImageView imageView1= (ImageView) itemview.findViewById(R.id.viewpager1_poster);
        TextView textView= (TextView) itemview.findViewById(R.id.viewpager1_title);
        final Tv tv=tvitem.get(position);
        textView.setText(tv.getOriginalname());
        if(tv.getBackdrop()!=null) {
            String url = Endpoint.IMAGE + "/w300/" + tv.getBackdrop();
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    imageView.setImageResource(R.drawable.yoimage);
                }
            });
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, Tv_description.class);
                    intent.putExtra("tvid",tv.getId());
                    context.startActivity(intent);
                }
            });

            Picasso.with(context).load(Endpoint.IMAGE + "/w185/" + tv.getPosterPath()).error(R.drawable.yoimage).into(imageView1);
        }

        container.addView(itemview);
        return itemview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       container.removeView((RelativeLayout) object);
    }

    public void setlistitem(List<Tv> tvitem) {
        this.tvitem=tvitem;
        notifyDataSetChanged();
    }
}
