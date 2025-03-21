package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.Collections;
import java.util.List;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Casts;
import info.sajib.moviesapp.R;
/**
 * Created by sajib on 11-03-2016.
 */
public class Casts_adapter extends RecyclerView.Adapter<Casts_adapter.Myviewholder> {

    private Context context;
    private List<Casts> cast= Collections.emptyList();

    public Casts_adapter(Context context, List<Casts> cast)
    {
        this.cast=cast;
        this.context=context;
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Myviewholder(LayoutInflater.from(context).inflate(R.layout.description_recyclerview_item,parent,false));
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {
            Casts movie=cast.get(position);
        String url= Endpoint.IMAGE+"/w185/"+movie.getCharaterimage();
        holder.textView.setText(movie.getCast());
        holder.textView1.setText(movie.getChracter());
        Picasso.get().load(url).error(R.drawable.yoimage).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cast.size();
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView textView1;
        public Myviewholder(View itemView) {
            super(itemView);
            textView1= (TextView) itemView.findViewById(R.id.description_recyclerview_charactername);
            imageView= (ImageView) itemView.findViewById(R.id.description_recyclerview_characterimage);
            textView= (TextView) itemView.findViewById(R.id.description_recyclerview_castname);
        }
    }
}
