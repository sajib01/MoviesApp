package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 14-04-2016.
 */
public class Tv_description_seasons extends RecyclerView.Adapter<Tv_description_seasons.Myviewholder> {
    private List<Tv> tvdata= Collections.emptyList();
    private Context context;
    private ImageLoader imageLoader;

    public Tv_description_seasons(Context context, List<Tv> tvdata) {
        this.context=context;
        this.tvdata=tvdata;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }


    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(context).inflate(R.layout.tv_description_layout_seasons_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {
        Tv tv = tvdata.get(position);
        String url1 = Endpoint.IMAGE + "/w185/" + tv.getSeasons_posterpath();
        Log.d("elementsize", url1);
        holder.posterpath.setImageUrl(url1, imageLoader);
        holder.season.setText(tv.getSeasons_no());
        holder.episodeno.setText(tv.getSeasons_episodecount());
        holder.description.setText("Air date: " + tv.getSeasons_airdate());

    }


    @Override
    public int getItemCount() {
        return tvdata.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        FadeInNetworkImageView posterpath;
        TextView season;
        TextView episodeno;
        TextView description;
        public Myviewholder(View itemView) {
            super(itemView);
            posterpath = (FadeInNetworkImageView) itemView.findViewById(R.id.tv_description_layout_season_layout_posterpath);
            season = (TextView) itemView.findViewById(R.id.tv_description_layout_season_layout_seasonname);
            episodeno = (TextView) itemView.findViewById(R.id.tv_description_layout_season_layout_episodeno);
            description = (TextView) itemView.findViewById(R.id.tv_description_layout_season_layout_overview);
        }
    }
}
