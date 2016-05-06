package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;

/**
 * Created by sajib on 13-03-2016.
 */
public class KnownforAdapter extends RecyclerView.Adapter<KnownforAdapter.MyViewholder> {
    Context context;
    List<Movie> listitem= Collections.emptyList();
    public KnownforAdapter(Context context, List<Movie> listitem) {
        this.context=context;
        this.listitem=listitem;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewholder(LayoutInflater.from(context).inflate(R.layout.cast_details_knownfor_recycler_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {
        Movie movie=listitem.get(position);
        String url= Endpoint.IMAGE+"/w185/"+movie.getPosterPath();
        Picasso.with(context).load(url).error(R.drawable.yoimage).into(holder.imageview);
    }



    @Override
    public int getItemCount() {
        return listitem.size();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        ImageView imageview;
        public MyViewholder(View itemView) {
            super(itemView);
            imageview= (ImageView) itemView.findViewById(R.id.cast_detail_knownfor_recycler_item_image);
        }
    }


}
