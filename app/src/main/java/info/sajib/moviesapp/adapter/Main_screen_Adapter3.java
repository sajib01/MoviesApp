package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.pojo.Upcoming;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 07-04-2016.
 */
public class Main_screen_Adapter3 extends RecyclerView.Adapter<Main_screen_Adapter3.Mainviewholder>{
    Context context;
    List<Upcoming> upitem= Collections.emptyList();
    ImageLoader imageLoader;
    public Main_screen_Adapter3(Context context, List<Upcoming> upitem) {
        this.context=context;
        this.upitem=upitem;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public Mainviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Mainviewholder(LayoutInflater.from(context).inflate(R.layout.main_screen_upcoming_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(final Mainviewholder holder, int position) {
        Upcoming upcoming=upitem.get(position);
            holder.imageView.setImageDrawable(null);
            String Url = Endpoint.IMAGE + "/w185/" + upcoming.getPosterPath();
            imageLoader.get(Url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.imageView.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    holder.imageView.setImageResource(R.drawable.yoimage);
                }
            });

    }

    @Override
    public int getItemCount() {
        return upitem.size();
    }

    public class Mainviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Mainviewholder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.main_screen_upcoming_layout_poster);
        }
    }
}
