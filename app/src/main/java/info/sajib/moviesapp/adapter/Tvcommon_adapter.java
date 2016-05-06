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
import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 10-04-2016.
 */
public class Tvcommon_adapter extends RecyclerView.Adapter<Tvcommon_adapter.Viewholder> {
    Context context;
    List<Tv> tvdata= Collections.emptyList();
    int resource;
    int imageresource;
    ImageLoader imageLoader;
    int startingposition;
    public Tvcommon_adapter(Context context,int resource,int imageresource)
    {
        this.context=context;
        this.resource=resource;
        this.imageresource=imageresource;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }
    @Override
    public Tvcommon_adapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(resource,parent,false));
    }

    @Override
    public void onBindViewHolder(final Tvcommon_adapter.Viewholder holder, int position) {
        Tv tv=tvdata.get(position);
        String url= Endpoint.IMAGE+"/w185/"+tv.getPosterPath();
        holder.poster.setImageDrawable(null);
        holder.poster.setImageUrl(url,imageLoader);
    }

    @Override
    public int getItemCount() {
        return tvdata.size();
    }

    public void setdata(int startingposition, List<Tv> tvdata) {
        this.tvdata=tvdata;
        notifyItemRangeChanged(startingposition,tvdata.size());
        this.startingposition=startingposition;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        FadeInNetworkImageView poster;
        public Viewholder(View itemView) {
            super(itemView);
            poster= (FadeInNetworkImageView) itemView.findViewById(imageresource);
        }
    }
}
