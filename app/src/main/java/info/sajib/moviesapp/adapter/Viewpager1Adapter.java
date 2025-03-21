package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.activity.Tv_description;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
/**
 * Created by sajib on 06-04-2016.
 */
public class Viewpager1Adapter extends RecyclerView.Adapter<Viewpager1Adapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;
    private List<Tv> tvitem= Collections.emptyList();

    public Viewpager1Adapter(Context context) {
        this.context=context;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }


    public void setlistitem(List<Tv> tvitem) {
        this.tvitem=tvitem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Viewpager1Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder( LayoutInflater.from(context).inflate(R.layout.viewpager1_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Viewpager1Adapter.ViewHolder holder, int position) {
        final Tv tv=tvitem.get(position);
        holder.bind(tv);
    }

    @Override
    public int getItemCount() {
        return tvitem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
        }

        public void bind(Tv tv){
            final ImageView imageView= (ImageView) view.findViewById(R.id.viewpager1_image);
            ImageView imageView1= (ImageView) view.findViewById(R.id.viewpager1_poster);
            TextView textView= (TextView) view.findViewById(R.id.viewpager1_title);

            textView.setText(tv.getOriginalname());
            if(tv.getBackdrop()!=null) {
                String url = Endpoint.IMAGE + "/w500/" + tv.getBackdrop();
                imageLoader.get(url, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                        imageView.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageResource(R.drawable.yoimage);
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(context, Tv_description.class);
                        intent.putExtra("tvid",tv.getId());
                        context.startActivity(intent);
                    }
                });

                Picasso.get().load(Endpoint.IMAGE + "/w185/" + tv.getPosterPath()).error(R.drawable.yoimage).into(imageView1);
            }
        }
    }
}
