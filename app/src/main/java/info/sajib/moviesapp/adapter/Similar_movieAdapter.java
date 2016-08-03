package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 13-03-2016.
 */
public class Similar_movieAdapter extends RecyclerView.Adapter<Similar_movieAdapter.Myviewholder> {
    private Context context;
    private List<Movie> listitem = Collections.emptyList();
    private ImageLoader imageLoader;

    public Similar_movieAdapter(Context context) {
        this.context = context;
        imageLoader = VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(context).inflate(R.layout.activity_description_similarmovies_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final Myviewholder holder, int position) {
        Movie movie = listitem.get(position);
        String url = Endpoint.IMAGE + "/w185/" + movie.getPosterPath();
        holder.imageView.setImageUrl(url,imageLoader);
        holder.textview.setText(movie.getOriginalTitle());
        holder.textView1.setText(movie.getReleaseDate());
    }

    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public void setdata(int startposition, List<Movie> listitem) {
        this.listitem=listitem;
        notifyItemRangeInserted(startposition,listitem.size());
    }


    public class Myviewholder extends RecyclerView.ViewHolder {
        FadeInNetworkImageView imageView;
        TextView textview;
        TextView textView1;

        public Myviewholder(View itemView) {
            super(itemView);
            imageView = (FadeInNetworkImageView) itemView.findViewById(R.id.activity_description_layout_item_imageview);
            textview = (TextView) itemView.findViewById(R.id.activity_description_layout_item_textview);
            textView1= (TextView) itemView.findViewById(R.id.activity_description_layout_item_textview1);
        }
    }
}
