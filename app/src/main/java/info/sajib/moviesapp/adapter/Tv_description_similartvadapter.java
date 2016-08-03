package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.pojo.Tvsimilar;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 13-04-2016.
 */
public class Tv_description_similartvadapter extends RecyclerView.Adapter<Tv_description_similartvadapter.Viewholder> {

    private List<Tvsimilar> tvdata= Collections.emptyList();
    private Context context;
    private ImageLoader imageLoader;

    public void setdata(List<Tvsimilar> tvdata)
    {
        this.tvdata=tvdata;
        notifyDataSetChanged();
    }

    public Tv_description_similartvadapter(Context context) {

        this.context=context;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.tv_description_layout_similarmovies_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(final Tv_description_similartvadapter.Viewholder holder, int position) {
        Tvsimilar tv=tvdata.get(position);
        String url= Endpoint.IMAGE+"/w185/"+tv.getPosterpath();
        Picasso.with(context).load(url).error(R.drawable.yoimage).into(holder.poster);
        holder.name.setText(tv.getOriginalname());
    }

    @Override
    public int getItemCount() {
        return tvdata.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView name;
        public Viewholder(View itemView) {
            super(itemView);
            poster= (ImageView) itemView.findViewById(R.id.tv_description_layout_similarmovies_layout_poster);
            name= (TextView) itemView.findViewById(R.id.tv_description_layout_similarmovies_layout_name);
        }
    }
}
