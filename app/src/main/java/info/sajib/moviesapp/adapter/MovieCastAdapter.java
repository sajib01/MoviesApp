package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Casts;

/**
 * Created by sajib on 09-04-2016.
 */
public class MovieCastAdapter extends RecyclerView.Adapter<MovieCastAdapter.Viewholder>{
    private Context context;
    private List<Casts> castses= Collections.emptyList();

    public MovieCastAdapter(Context context, List<Casts> castsList)
    {
        this.castses=castsList;
        this.context=context;
    }
    @Override
    public MovieCastAdapter.Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.activity_description_cast_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(MovieCastAdapter.Viewholder holder, int position) {
        Casts casts=castses.get(position);
        Picasso.with(context).load(Endpoint.IMAGE+"/w185/"+casts.getCharaterimage()).error(R.drawable.yoimage).into(holder.actorimage);
        holder.actorname.setText(casts.getCast());
        holder.charactername.setText(casts.getChracter());

    }

    @Override
    public int getItemCount() {
        return castses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView actorimage;
        TextView actorname;
        TextView charactername;
        public Viewholder(View itemView) {
            super(itemView);
            actorimage= (CircleImageView) itemView.findViewById(R.id.activity_description_cast_layout_actorimage);
            actorname= (TextView) itemView.findViewById(R.id.activity_description_cast_layout_actorname);
            charactername= (TextView) itemView.findViewById(R.id.activity_description_cast_layout_charatername);
        }
    }
}
