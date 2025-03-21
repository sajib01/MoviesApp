package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tvcast;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 13-04-2016.
 */
public class Tv_description_castadapter extends RecyclerView.Adapter<Tv_description_castadapter.Viewholder>{
    private Context context;
    private List<Tvcast> tvcastdata= Collections.emptyList();
    private ImageLoader imageLoader;

    Tv_description_castadapter(Context context,List<Tvcast> tvcastdata)
    {
        this.context=context;
        this.tvcastdata=tvcastdata;
        imageLoader= VolleySingleton.getInstance().getImageLoader();

    }
    @Override
    public Tv_description_castadapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.tv_description_cast_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(final Tv_description_castadapter.Viewholder holder, int position) {
        Tvcast tvcast=tvcastdata.get(position);
        holder.Originalname.setText(tvcast.getName());
        String url= Endpoint.IMAGE+"/w185/"+tvcast.getProfile_path();
        Picasso.get().load(url).error(R.drawable.yoimage).into(holder.imageView);
        holder.charactername.setText("("+tvcast.getCharacter()+")");

    }

    @Override
    public int getItemCount() {
        return tvcastdata.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView charactername;
        TextView Originalname;
        public Viewholder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.tv_description_cast_layout_profileimage);
            charactername= (TextView) itemView.findViewById(R.id.tv_description_cast_layout_charactername);
            Originalname= (TextView) itemView.findViewById(R.id.tv_description_cast_layout_actorname);
        }
    }
}
