package info.sajib.moviesapp.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.activity.DescriptionActivity;
import info.sajib.moviesapp.adapter.Similar_movieAdapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.expandedrecycler.FilterRecyclerViewScrollListener;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

public class Activity_description_similarmovies extends Fragment {

    private RecyclerView recyclerView;
    private List<Movie> listitem=new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private String mEndpoint;
    private Similar_movieAdapter similar_movieAdapter;
    private RequestQueue requestQueue;
    private long id;
    private ProgressDialog progressDialog;
    private int mCurrentPage;
    private boolean mLoading = false;

    public Activity_description_similarmovies() {
        // Required empty public constructor
    }

    public static Activity_description_similarmovies newInstance(long Id) {
        Activity_description_similarmovies myFragment = new Activity_description_similarmovies();
        Bundle args = new Bundle();
        args.putLong("ID",Id);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=getArguments().getLong("ID",0);
        requestQueue= VolleySingleton.getInstance().getRequestQueue();
        mCurrentPage=1;
        sendJsonRequest(mCurrentPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.activity_description_similarmovies, container, false);

        recyclerView=  v.findViewById(R.id.activity_description_similarmovies_recyclerview);
        final LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        similar_movieAdapter=new Similar_movieAdapter(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(similar_movieAdapter);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.show();

        recyclerView.addOnScrollListener(new FilterRecyclerViewScrollListener(linearLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!mLoading) {
                    mLoading = true;
                    progressDialog.show();
                    sendJsonRequest(++page);
                }
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(getActivity(), recyclerView, new ClickListner() {
            @Override
            public void OnClick(View v, int Position) {
                Movie movie=listitem.get(Position);
                Intent intent=new Intent(getActivity(), DescriptionActivity.class);
                intent.putExtra("movieid",movie.getId());
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View v, int Position) {

            }

            @Override
            public void OnScroll() {

            }
        }));
        return  v;
    }

    // TODO: Rename method, update argument and hook method into UI event

    private void sendJsonRequest(int mCurrentPage) {

        mEndpoint= Endpoint.MOVIE+id+"/similar"+"?page="+mCurrentPage+"&api_key="+ MyApplication.TMDB_API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                mEndpoint,
                null,
                new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(progressDialog.isShowing()){
                    progressDialog.dismiss();
                }
                final int startposition=listitem.size();
                parseJsonRequest(response);
                similar_movieAdapter.setdata(startposition,listitem);
                mLoading = false;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }



    private void parseJsonRequest(JSONObject response) {

        if (response != null || response.length() > 0) {


            try {
                JSONArray jsonArray = response.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject Object = jsonArray.getJSONObject(i);
                    String overview=Object.getString("overview");
                    String title = Object.getString("original_title");
                    String posterpath = Object.getString("poster_path");
                    String date = Object.getString("release_date");
                    long id = Object.getInt("id");
                    float rating = Float.parseFloat(Object.getString("vote_average"));
                    Movie movie = new Movie();
                    movie.setOriginalTitle(title);
                    movie.setVoteAverage(rating);
                    movie.setReleaseDate(date);
                    movie.setOverview(overview);
                    movie.setPosterPath(posterpath);
                    movie.setId(id);
                    listitem.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }


}
