package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Backdrop;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 24-04-2016.
 */
public class Picactivity_adapter extends RecyclerView.Adapter<Picactivity_adapter.Myviewholder> {
    Context context;
    List<Backdrop> item = Collections.emptyList();
    ImageLoader imageLoader;
    public Picactivity_adapter(Context context) {
        this.context = context;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public Picactivity_adapter.Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(context).inflate(R.layout.picactivity_item, parent, false));
    }

    @Override
    public void onBindViewHolder(Picactivity_adapter.Myviewholder holder, int position) {
        Backdrop backdrop = item.get(position);
        String url = Endpoint.IMAGE + "/w185/" + backdrop.getBackdrop();
        holder.poster.setImageUrl(url,imageLoader);
    }

    @Override
    public int getItemCount() {
        return item.size();
    }

    public void setposter(List<Backdrop> item) {
        this.item = item;
        notifyDataSetChanged();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        FadeInNetworkImageView poster;
        public Myviewholder(View itemView) {
            super(itemView);
            poster = (FadeInNetworkImageView) itemView.findViewById(R.id.picactivity_item_poster);
        }
    }
}
