package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Upcoming;
import info.sajib.moviesapp.R;
/**
 * Created by sajib on 13-03-2016.
 */
public class Comingadapter extends RecyclerView.Adapter<Comingadapter.MyviewHolder> {
    Context context;
    List<Upcoming> list= Collections.emptyList();
    public Comingadapter(Context context, List<Upcoming> list) {
        this.context=context;
        this.list=list;
        this.context=context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyviewHolder(LayoutInflater.from(context).inflate(R.layout.cast_details_upcoming_recycler_items,parent,false));
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {
        Upcoming upcoming=list.get(position);
        holder.textView.setText(upcoming.getOriginalTitle());
        String url= Endpoint.IMAGE+"/w185"+upcoming.getPosterPath();
        Picasso.with(context).load(url).error(R.drawable.yoimage).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyviewHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.cast_details_upcoming_recycler_imageview);
            textView= (TextView) itemView.findViewById(R.id.cast_details_upcoming_recycler_textview);
        }
    }
}
