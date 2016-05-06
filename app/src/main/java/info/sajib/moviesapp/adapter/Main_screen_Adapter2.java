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
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 07-04-2016.
 */
public class Main_screen_Adapter2 extends RecyclerView.Adapter<Main_screen_Adapter2.Mainviewholder> {

    Context context;
    List<Movie> listitem= Collections.emptyList();
    ImageLoader imageLoader;
    public Main_screen_Adapter2(Context context, List<Movie> listitem) {
        this.context=context;
        this.listitem=listitem;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public Main_screen_Adapter2.Mainviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Mainviewholder(LayoutInflater.from(context).inflate(R.layout.main_screen_movies_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(final Main_screen_Adapter2.Mainviewholder holder, int position) {
            Movie movie=listitem.get(position);
            String Url = Endpoint.IMAGE + "/w185/" + movie.getPosterPath();
            holder.imageView.setImageDrawable(null);
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
        return listitem.size();
    }

    public class Mainviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public Mainviewholder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.main_screen_movies_layout_poster);
        }
    }


}
