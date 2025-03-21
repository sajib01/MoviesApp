package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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
public class Cast_pager_adapter extends RecyclerView.Adapter<Cast_pager_adapter.ViewHolder> {
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

    @NonNull
    @Override
    public Cast_pager_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cast_details_background_viewpager_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Cast_pager_adapter.ViewHolder holder, int position) {
        final Movie movie = listitem.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
        public void bind(Movie movie){
            final ImageView imageView = (ImageView) view.findViewById(R.id.cast_detail_background_viewpager_item_slider);
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

        }
    }
}

