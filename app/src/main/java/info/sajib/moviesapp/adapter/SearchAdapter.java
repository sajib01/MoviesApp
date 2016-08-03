package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 08-03-2016.
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.Myviewholder> {
    private Context context;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private String Url;
    private List<Movie> data = Collections.emptyList();

    public SearchAdapter(Context context, List<Movie> data) {
        this.data = data;
        this.context = context;
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(context).inflate(R.layout.search_layout_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final Myviewholder holder, int position) {
        Movie current = data.get(position);
        String textrating = String.format("%.2f", current.getVoteAverage());
        holder.ratingBar.setRating(current.getVoteAverage() / 2f);
        holder.Rating.setText(textrating);
        holder.textView.setText(current.getOriginalTitle());
        Url = Endpoint.IMAGE + "/w92/" + current.getPosterPath();

        int color = context.getResources().getColor(R.color.white);
        Picasso.with(context).load(Url).noFade().error(R.drawable.yoimage).into(holder.imageView);
        Picasso.with(context).load(Endpoint.IMAGE + "/w92" + current.getBackdropPath()).error(color).resize(92, 100).centerInside().into(holder.imageView2);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView Rating;
        RatingBar ratingBar;
        CircleImageView imageView2;

        public Myviewholder(View itemView) {
            super(itemView);
            Rating = (TextView) itemView.findViewById(R.id.rating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingbar);
            textView = (TextView) itemView.findViewById(R.id.search_layout_reycler_item_textview);
            imageView = (ImageView) itemView.findViewById(R.id.search_layout_recycler_item_imageview);
            imageView2 = (CircleImageView) itemView.findViewById(R.id.search_layout_castpicture);
        }
    }
}
