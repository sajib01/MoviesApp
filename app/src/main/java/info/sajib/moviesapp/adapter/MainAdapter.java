package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;

/**
 * Created by sajib on 06-03-2016.
 */
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.Myviewholder> {

    private LayoutInflater inflater;
    private Context context;
    private String url;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private List<Movie> data = Collections.emptyList();
    private int item;
    private int startposition;

    public MainAdapter(Context context) {
        this.context = context;

        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new Mainviewholder(inflater.from(context).inflate(R.layout.fragment_movie_layout_item, parent, false));

    }

    public void setmovielist(List<Movie> data, int startposition) {
        this.data = data;
        notifyItemRangeInserted(startposition, data.size());
        this.startposition = startposition;
    }

    @Override
    public void onBindViewHolder(final Myviewholder holder, int position) {

        final Mainviewholder mainviewholder = (Mainviewholder) holder;
        Movie currentmovie = data.get(position);
        String textrating = String.format("%.2f", currentmovie.getVoteAverage());
        mainviewholder.ratingBar.setRating(currentmovie.getVoteAverage() / 2f);
        mainviewholder.Rating.setText(textrating);
        url = Endpoint.IMAGE + "/w185/" + currentmovie.getPosterPath();
        mainviewholder.imageView.setImageUrl(url, imageLoader);
        mainviewholder.imageView.setErrorImageResId(R.drawable.yoimage);
        mainviewholder.textView.setText(currentmovie.getOriginalTitle());
        mainviewholder.textView1.setText(currentmovie.getReleaseDate());

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class Mainviewholder extends Myviewholder {
        FadeInNetworkImageView imageView;
        TextView textView;
        TextView textView1;
        RatingBar ratingBar;
        TextView Rating;
        public Mainviewholder(View itemView) {
            super(itemView);
            imageView = (FadeInNetworkImageView) itemView.findViewById(R.id.fragment_movie_layout_item_imageview);
            textView = (TextView) itemView.findViewById(R.id.fragment_movie_layout_item_textview);
            textView1 = (TextView) itemView.findViewById(R.id.fragment_movie_layout_item_textview1);
            ratingBar= (RatingBar) itemView.findViewById(R.id.fragment_movie_layout_item_stars);
            Rating= (TextView) itemView.findViewById(R.id.fragment_movie_layout_item_rating);
        }
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        public Myviewholder(View itemView) {
            super(itemView);
        }
    }

}
