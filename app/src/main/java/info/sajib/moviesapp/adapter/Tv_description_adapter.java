package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.w3c.dom.Text;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import info.sajib.moviesapp.activity.Tv_description;
import info.sajib.moviesapp.custom.FadeInNetworkImageView;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.pojo.Tvcast;
import info.sajib.moviesapp.pojo.Tvsimilar;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 12-04-2016.
 */
public class Tv_description_adapter extends RecyclerView.Adapter<Tv_description_adapter.Tvholder>{
    List<String> genre = Collections.emptyList();
    List<Tv> tvdata = Collections.emptyList();
    List<Tvcast> tvcastdata = Collections.emptyList();
    List<String> backdrops = Collections.emptyList();
    List<String> creaditedby = Collections.emptyList();
    List<Tvsimilar> tvsimilars = Collections.emptyList();
    List<String> generalinformation = Collections.emptyList();
    Context context;

    private static final int TYPE0 = 0;
    private static final int TYPE1 = 1;
    private static final int TYPE2 = 2;
    private static final int TYPE3 = 3;
    private static final int TYPE4 = 4;
    ImageLoader imageLoader;

    public Tv_description_adapter(Context context) {

        this.context = context;
        imageLoader = VolleySingleton.getInstance().getImageLoader();

    }

    public void setdata(List<Tv> tvdata, List<String> generalinformation, List<Tvcast> tvcastdata, List<String> backdrops, List<String> creaditedby, List<Tvsimilar> tvsimilars, List<String> genre) {
        this.tvdata = tvdata;
        this.generalinformation = generalinformation;
        this.tvcastdata = tvcastdata;
        this.tvsimilars = tvsimilars;
        this.backdrops = backdrops;
        this.genre = genre;
        this.creaditedby = creaditedby;
        notifyDataSetChanged();

    }
    @Override
    public Tvholder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (getItemViewType(viewType)) {
            case TYPE0:
                return new Firstvholder(LayoutInflater.from(context).inflate(R.layout.tv_description_layout,parent, false));
            case TYPE1:
                return new Secondvholder(LayoutInflater.from(context).inflate(R.layout.tv_description_cast,parent, false));
            case TYPE2:
                return new Thirdvholder(LayoutInflater.from(context).inflate(R.layout.tv_description_layout_seasons,parent, false));
            case TYPE3:
                return new Fourthvholder(LayoutInflater.from(context).inflate(R.layout.tv_description_layout_similarmovies,parent, false));

        }
        return null;
    }

    @Override
    public void onBindViewHolder(Tvholder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE0:
                Firstvholder firstholder = (Firstvholder) holder;

                firstholder.firstairdate.setText(generalinformation.get(0));
                firstholder.lastairdate.setText(generalinformation.get(1));
                String url = Endpoint.IMAGE + "/w185/" + generalinformation.get(2);
                firstholder.imageView.setImageUrl(url, imageLoader);
                firstholder.overview.setText(generalinformation.get(3));
                firstholder.tvshowname.setText(generalinformation.get(4));
                firstholder.vote.setText(generalinformation.get(5));

                StringBuffer buffer = new StringBuffer();
                for (int i = 0; i < genre.size(); i++) {
                    buffer.append(genre.get(i));
                    if(i!=genre.size()-1) {
                        buffer.append(",");
                    }

                }
                firstholder.genre.setText(buffer);
                StringBuffer buffer1 = new StringBuffer();
                for (int i = 0; i < creaditedby.size(); i++) {
                    buffer1.append(creaditedby.get(i));
                    if(i!=creaditedby.size()-1) {
                        buffer.append(",");
                    }
                }
                firstholder.createdby.setText(buffer1);
                Tv_description_viewpageraddapter tv_description_viewpageraddapter = new Tv_description_viewpageraddapter(context, backdrops);
                firstholder.viewPager.setAdapter(tv_description_viewpageraddapter);
                firstholder.viewPager.startAutoScroll(2000);
                firstholder.viewPager.setInterval(2000);
                break;
            case TYPE1:
                Secondvholder sholder = (Secondvholder) holder;
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                Tv_description_castadapter tvDescriptionCastadapter = new Tv_description_castadapter(context, tvcastdata);
                sholder.mrecyclerView.setLayoutManager(linearLayoutManager);
                sholder.mrecyclerView.setAdapter(tvDescriptionCastadapter);
                tvDescriptionCastadapter.notifyDataSetChanged();
                break;
            case TYPE2:
                Thirdvholder tholder = (Thirdvholder) holder;
                Log.d("positiond", String.valueOf(position));
                LinearLayoutManager lly = new LinearLayoutManager(context);
                lly.setOrientation(LinearLayoutManager.VERTICAL);
                Tv_description_seasons tvDescriptionSeasons = new Tv_description_seasons(context, tvdata);
                tholder.mRecyclerview.setLayoutManager(lly);
                tholder.mRecyclerview.setAdapter(tvDescriptionSeasons);
                tvDescriptionSeasons.notifyDataSetChanged();
                break;
            case TYPE3:
                LinearLayoutManager ll = new LinearLayoutManager(context);
                ll.setOrientation(LinearLayoutManager.HORIZONTAL);
                Fourthvholder fholder = (Fourthvholder) holder;
                Tv_description_similartvadapter similartvadapter = new Tv_description_similartvadapter(context);
                fholder.recyclerView.setLayoutManager(ll);
                fholder.recyclerView.setAdapter(similartvadapter);
                similartvadapter.setdata(tvsimilars);
                fholder.recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(context, fholder.recyclerView, new ClickListner() {
                    @Override
                    public void OnClick(View v, int Position) {
                        Tvsimilar tvsimilar=tvsimilars.get(Position);
                        Intent intent=new Intent(context, Tv_description.class);
                        intent.putExtra("tvid",tvsimilar.getId());
                        context.startActivity(intent);
                    }

                    @Override
                    public void OnLongClick(View v, int Position) {

                    }

                    @Override
                    public void OnScroll() {

                    }
                }));

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE0;
        }
        if (position == 1) {
            return TYPE1;
        }
        if (position == 2){
            return TYPE2;
        }
        else
        return TYPE3;

    }

    @Override
    public int getItemCount() {
        return 4;

    }


    public class Fourthvholder extends Tvholder {
        RecyclerView recyclerView;

        public Fourthvholder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView.findViewById(R.id.tv_description_layout_similarmovies_recyclerview);

        }
    }

    public class Thirdvholder extends Tvholder {

        RecyclerView mRecyclerview;
        public Thirdvholder(View itemView) {
            super(itemView);
            mRecyclerview= (RecyclerView) itemView.findViewById(R.id.tv_description_layout_season_recyclerview);

        }
    }

    public class Secondvholder extends Tvholder {
        RecyclerView mrecyclerView;

        public Secondvholder(View itemView) {
            super(itemView);
            mrecyclerView = (RecyclerView) itemView.findViewById(R.id.tv_description_cast_recyclerview);
        }
    }

    public class Firstvholder extends Tvholder {
        TextView tvshowname;
        TextView firstairdate;
        TextView lastairdate;
        TextView createdby;
        TextView overview;
        TextView vote;
        TextView genre;
        FadeInNetworkImageView imageView;
        AutoScrollViewPager viewPager;

        public Firstvholder(View itemView) {
            super(itemView);
            viewPager = (AutoScrollViewPager) itemView.findViewById(R.id.tv_description_layout_viewpager);
            tvshowname = (TextView) itemView.findViewById(R.id.tv_description_layout_tvshowname);
            firstairdate = (TextView) itemView.findViewById(R.id.tv_description_layout_firstairdate);
            lastairdate = (TextView) itemView.findViewById(R.id.tv_description_layout_lastairdate);
            createdby = (TextView) itemView.findViewById(R.id.tv_description_layout_createdby);
            overview = (TextView) itemView.findViewById(R.id.tv_description_layout_overview);
            vote = (TextView) itemView.findViewById(R.id.tv_description_layout_vote_average);
            genre = (TextView) itemView.findViewById(R.id.tv_description_layout_genre);
            imageView = (FadeInNetworkImageView) itemView.findViewById(R.id.tv_description_layout_profile);
        }
    }

    public class Tvholder extends RecyclerView.ViewHolder {
        public Tvholder(View itemView) {
            super(itemView);
        }
    }
}