package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.activity.DescriptionActivity;
import info.sajib.moviesapp.activity.Nowplaying;
import info.sajib.moviesapp.activity.Tvshow;
import info.sajib.moviesapp.activity.Upcomingmovie;
import info.sajib.moviesapp.custom.AutoScrollViewPager2;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.pojo.Upcoming;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;

/**
 * Created by sajib on 06-04-2016.
 */

public class Main_screen_Adapter extends RecyclerView.Adapter<Main_screen_Adapter.MainViewHolder> {

    private List<Tv> tvitem= Collections.emptyList();
    private List<Movie> listitem= Collections.emptyList();
    private List<Upcoming> upitem= Collections.emptyList();
    private Context context;
    private static final int TYPE1=0;
    private static final int TYPE2=1;
    private static final int TYPE3=2;

    public Main_screen_Adapter(Context context)
    {
        this.context=context;
    }

    public void setdata(List<Tv> tvitem, List<Movie> listitem, List<Upcoming> upitem)
    {
        this.tvitem=tvitem;
        this.listitem=listitem;
        this.upitem=upitem;
        notifyDataSetChanged();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (getItemViewType(viewType))
        {
            case TYPE1:
                return new Firstviewholder(LayoutInflater.from(context).inflate(R.layout.main_screen_tv,parent,false));
            case TYPE2:
                return new Secondviewholder(LayoutInflater.from(context).inflate(R.layout.main_screen_movies,parent,false));
            case TYPE3:
                return new Thirdviewholder(LayoutInflater.from(context).inflate(R.layout.main_screen_upcoming,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        switch (holder.getItemViewType())
        {

            case TYPE1:
                Firstviewholder firstviewholder= (Firstviewholder) holder;
                Viewpager1Adapter viewpageradapter=new Viewpager1Adapter(context);
                firstviewholder.viewpager.setAdapter(viewpageradapter);
                viewpageradapter.setlistitem(tvitem);
               // firstviewholder.circleIndicator.setViewPager(firstviewholder.viewpager);
                firstviewholder.viewpager.setInterval(6000);
                firstviewholder.viewpager.startAutoScroll(6000);
                firstviewholder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context,Tvshow.class));
                    }
                });
                break;
            case TYPE2:
                final Secondviewholder secondviewholder= (Secondviewholder) holder;
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                Main_screen_Adapter2 main_screen_adapter2=new Main_screen_Adapter2(context,listitem);
                secondviewholder.recyclerView.setLayoutManager(linearLayoutManager);
                secondviewholder.recyclerView.setAdapter(main_screen_adapter2);
                main_screen_adapter2.notifyDataSetChanged();
                secondviewholder.recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(context, secondviewholder.recyclerView, new ClickListner() {
                    @Override
                    public void OnClick(View v, int Position) {
                        Movie movie=listitem.get(Position);
                        Intent intent=new Intent(context, DescriptionActivity.class);
                        intent.putExtra("movieid",movie.getId());
                        context.startActivity(intent);
                    }

                    @Override
                    public void OnLongClick(View v, int Position) {

                    }

                    @Override
                    public void OnScroll() {

                    }
                }));

                secondviewholder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, Nowplaying.class));
                    }
                });

                break;

            case TYPE3:
                Thirdviewholder thirdviewholder= (Thirdviewholder) holder;
                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(context);
                linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                Main_screen_Adapter3 main_screen_adapter3=new Main_screen_Adapter3(context,upitem);
                thirdviewholder.recyclerView.setLayoutManager(linearLayoutManager1);
                thirdviewholder.recyclerView.setAdapter(main_screen_adapter3);
                main_screen_adapter3.notifyDataSetChanged();
                thirdviewholder.recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(context, thirdviewholder.recyclerView, new ClickListner() {
                    @Override
                    public void OnClick(View v, int Position) {
                        Upcoming movie=upitem.get(Position);
                        Intent intent=new Intent(context, DescriptionActivity.class);
                        intent.putExtra("movieid",movie.getId());
                        context.startActivity(intent);
                    }

                    @Override
                    public void OnLongClick(View v, int Position) {

                    }

                    @Override
                    public void OnScroll() {

                    }
                }));

                thirdviewholder.viewall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, Upcomingmovie.class));
                    }
                });

                break;
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
        {
            return TYPE1;
        }
        if(position==1) {
            return TYPE2;
        }
        else {
            return TYPE3;
        }
    }

    public class Firstviewholder extends MainViewHolder
    {
        AutoScrollViewPager2 viewpager;
        //CirclePageIndicator circleIndicator;
        Button textView;
        public Firstviewholder(View itemView) {
            super(itemView);
            viewpager=  itemView.findViewById(R.id.tv_viewpager);
           // circleIndicator= (CirclePageIndicator) itemView.findViewById(R.id.tv_viewpager_indicator);
            textView= (Button) itemView.findViewById(R.id.tvshows);
        }
    }

    public class Secondviewholder extends MainViewHolder
    {
        RecyclerView recyclerView;
        TextView textView;
        public Secondviewholder(View itemView) {
            super(itemView);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.main_screen_movies_recycler);
            textView= (TextView) itemView.findViewById(R.id.main_screen_movies_viewall);
        }
       }

    public class Thirdviewholder extends MainViewHolder
    {
        RecyclerView recyclerView;
        TextView viewall;
        public Thirdviewholder(View itemView) {
            super(itemView);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.main_screen_upcoming_recycler);
            viewall= (TextView) itemView.findViewById(R.id.main_screen_upcoming_Viewall);
        }
    }

    public class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(View itemView) {
            super(itemView);
        }
    }


}
