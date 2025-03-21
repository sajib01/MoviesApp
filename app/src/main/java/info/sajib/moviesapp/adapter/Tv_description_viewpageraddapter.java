package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 13-04-2016.
 */
public class Tv_description_viewpageraddapter extends RecyclerView.Adapter<Tv_description_viewpageraddapter.ViewHolder> {

    private Context context;
    private List<String> backdrops= Collections.emptyList();
    private ImageLoader imageLoader;

    public Tv_description_viewpageraddapter(Context context, List<String> backdrops) {
        this.context=context;
        this.backdrops=backdrops;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @NonNull
    @Override
    public Tv_description_viewpageraddapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.tv_description_layout_viewpager_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Tv_description_viewpageraddapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return backdrops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }
        public void bind(int position){
            FadeInNetworkImageView imageView= (FadeInNetworkImageView) view.findViewById(R.id.tv_description_layout_viewpager_layout_imageview);
            String url= Endpoint.IMAGE+"/w500/"+backdrops.get(position);
            imageView.setImageUrl(url,imageLoader);
        }
    }
}
