package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import info.sajib.moviesapp.activity.DescriptionActivity;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.pojo.PersonDetails;
import info.sajib.moviesapp.pojo.Upcoming;

/**
 * Created by sajib on 11-03-2016.
 */
public class Cast_details_adapter extends RecyclerView.Adapter<Cast_details_adapter.Myviewholder> {
    private static final int TYPE1=1;
    private static final int TYPE2=2;
    private static final int TYPE3=3;
    private ImageLoader imageLoader;
    Context context;
    List<PersonDetails> data= Collections.emptyList();
    List<Movie> listitem=Collections.emptyList();
    List<Upcoming> list=Collections.emptyList();
    public Cast_details_adapter(Context context, List<PersonDetails> data, List<Movie> listitem, List<Upcoming> list)
    {
        this.context=context;
        this.data=data;
        this.listitem=listitem;
        this.list=list;
        imageLoader= VolleySingleton.getInstance().getImageLoader();
    }

    @Override
    public Myviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType)
        {
            case TYPE1:
                return new Firstviewholder(LayoutInflater.from(context).inflate(R.layout.cast_details_background_viewpager,parent,false));
            case TYPE2:
                return new Secondviewholder(LayoutInflater.from(context).inflate(R.layout.cast_details_knownfor_reyclerview,parent,false));
            case TYPE3:
                return new Thirdviewholder(LayoutInflater.from(context).inflate(R.layout.cast_details_upcoming_movies_recycler,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(Myviewholder holder, int position) {

        switch (holder.getItemViewType())
        {
            case TYPE1:
                Log.d("recyclerhero", String.valueOf(listitem.size()));
                final Firstviewholder fholder= (Firstviewholder) holder;
                PersonDetails personDetails=data.get(0);
                fholder.actorbirthday.setText(personDetails.getBirthdate());
                fholder.actorplaceofbirth.setText(personDetails.getPlaceofbirth());
                fholder.actordescription.setText(personDetails.getDescription());
                final String url= Endpoint.IMAGE+"/w185/"+personDetails.getProfilepic();
                if(personDetails.getProfilepic()!=null)
                {
                    imageLoader.get(url, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                            fholder.actorimage.setImageBitmap(response.getBitmap());
                        }

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            fholder.actorimage.setImageResource(R.drawable.yoimage);
                        }
                    });
                }

                break;
            case TYPE2:
                LinearLayoutManager linearLayoutManager=new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                Secondviewholder sholder= (Secondviewholder) holder;
                KnownforAdapter knownforAdapter=new KnownforAdapter(context,listitem);
                sholder.recyclerView.setLayoutManager(linearLayoutManager);
                sholder.recyclerView.setAdapter(knownforAdapter);
                knownforAdapter.notifyDataSetChanged();

                sholder.recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(context, sholder.recyclerView, new ClickListner() {
                    @Override
                    public void OnClick(View v, int Position) {
                        Movie movie = listitem.get(Position);
                        long id = movie.getId();
                        Log.d("idnum", String.valueOf(id));
                        String title = movie.getOriginalTitle();
                        String date = movie.getReleaseDate();
                        float vote = movie.getVoteAverage();
                        String description = movie.getOverview();
                        Intent intent = new Intent(context, DescriptionActivity.class);
                        intent.putExtra("movieid", id);
                        intent.putExtra("title", title);
                        intent.putExtra("moviedate", date);
                        intent.putExtra("movievote", vote);
                        intent.putExtra("description", description);
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
            case TYPE3:
                LinearLayoutManager linearLayoutManager1=new LinearLayoutManager(context);
                linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
                Thirdviewholder tholder= (Thirdviewholder) holder;
                Comingadapter comingadapter = new Comingadapter(context,list);
                tholder.ThirdRecyclerview.setLayoutManager(linearLayoutManager1);
                tholder.ThirdRecyclerview.setAdapter(comingadapter);
                comingadapter.notifyDataSetChanged();
                tholder.ThirdRecyclerview.addOnItemTouchListener(new RecyclerViewTouchListner(context, tholder.ThirdRecyclerview, new ClickListner() {
                    @Override
                    public void OnClick(View v, int Position) {
                        Upcoming movie = list.get(Position);
                        long id = movie.getId();
                        Log.d("idnum", String.valueOf(id));
                        String title = movie.getOriginalTitle();
                        String date = movie.getReleaseDate();
                        float vote = movie.getVoteAverage();
                        String description = movie.getOverview();
                        Intent intent = new Intent(context, DescriptionActivity.class);
                        intent.putExtra("movieid", id);
                        intent.putExtra("title", title);
                        intent.putExtra("moviedate", date);
                        intent.putExtra("movievote", vote);
                        intent.putExtra("description", description);
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
    public int getItemCount() {
        return 3;
    }


    public class Firstviewholder extends Myviewholder
    {
        ImageView actorimage;
        TextView actorbirthday;
        TextView actordescription;
        TextView actorplaceofbirth;
        public Firstviewholder(View itemView) {
            super(itemView);
            actorimage= (ImageView) itemView.findViewById(R.id.cast_details_imageview);
            actorbirthday= (TextView) itemView.findViewById(R.id.cast_details_birthday);
            actordescription= (TextView) itemView.findViewById(R.id.cast_details_actordetails);
            actorplaceofbirth= (TextView) itemView.findViewById(R.id.cast_details_placeofbirth);

        }
    }

    public class Secondviewholder extends Myviewholder
    {
        RecyclerView recyclerView;
        public Secondviewholder(View itemView) {

            super(itemView);
            recyclerView= (RecyclerView) itemView.findViewById(R.id.cast_details_knownfor_recycler);
        }
    }

    public class Thirdviewholder extends Myviewholder
    {
        RecyclerView ThirdRecyclerview;
        public Thirdviewholder(View itemView) {
            super(itemView);
            ThirdRecyclerview= (RecyclerView) itemView.findViewById(R.id.cast_detail_upcoming_movies_recyclerview);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0)
        {
            return TYPE1;
        }
        if(position==1)
        {
            return TYPE2;
        }
        if(position==2)
        {
            return TYPE3;
        }
        return TYPE3;
    }

    public class Myviewholder extends RecyclerView.ViewHolder {
        public Myviewholder(View itemView) {
            super(itemView);

        }
    }

}
