package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

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
public class Descriptionactivity_Pageradapter extends RecyclerView.Adapter<Descriptionactivity_Pageradapter.ViewHolder> {
    private List<Movie> data = Collections.emptyList();
    private ImageLoader imageLoader;
    private Context context;

    public Descriptionactivity_Pageradapter(Context context, List<Movie> data) {
        this.context = context;
        this.data = data;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @NonNull
    @Override
    public Descriptionactivity_Pageradapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager2_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Descriptionactivity_Pageradapter.ViewHolder holder, int position) {
        final Movie movie = data.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void bind(Movie movie) {
            final ImageView imageView= view.findViewById(R.id.viewpager2_image);
            final ImageView youtubeicon= view.findViewById(R.id.youtubeicon);
            final ImageView picshade= view.findViewById(R.id.picshade);
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
                imageView.setOnClickListener(v -> {
                    Intent intent=new Intent(context, TrailerActivity.class);
                    intent.putExtra("trailerid",movie.getSource());
                    context.startActivity(intent);
                });
            }
            if(movie.getName()==null)
            {
                imageView.setOnClickListener(v -> {
                    Intent intent=new Intent(context, Picactivity.class);
                    intent.putExtra("movieid",movie.getId());
                    context.startActivity(intent);
                });
            }
        }
    }
}
