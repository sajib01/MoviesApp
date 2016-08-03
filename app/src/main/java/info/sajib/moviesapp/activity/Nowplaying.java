package info.sajib.moviesapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;

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
import info.sajib.moviesapp.adapter.MainAdapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.expandedrecycler.FilterRecyclerViewScrollListener;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.pojo.Push;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 09-04-2016.
 */
public class Nowplaying extends AppCompatActivity {
    private ProgressDialog mProgressDialog;
    private List<Movie> listitem = new ArrayList<>();
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private LinearLayoutManager mLayoutManager;
    private MainAdapter mAdapter;
    private boolean mLoading = false;
    private int mCurrentPage, mTotalPageSize;
    private String mEndpoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_movie);

        recyclerView = (RecyclerView)findViewById(R.id.movie_recyclerview);
        mCurrentPage = 1;
        mTotalPageSize = 1;

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        sendJsonRequest(mCurrentPage);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_data));
        mProgressDialog.getWindow().setGravity(Gravity.CENTER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MainAdapter(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.getItemAnimator().setChangeDuration(0);

        recyclerView.addOnScrollListener(new FilterRecyclerViewScrollListener(mLayoutManager) {

            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!mLoading) {
                    mLoading = true;

                    mProgressDialog.setMessage(getString(R.string.loading_more));
                    mProgressDialog.getWindow().setGravity(Gravity.BOTTOM);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.show();
                    mTotalPageSize = totalItemsCount;

                    sendJsonRequest(++mCurrentPage);
                }
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(this, recyclerView, new ClickListner() {
            @Override
            public void OnClick(View v, int Position) {
                Movie movie = listitem.get(Position);
                long id = movie.getId();
                Intent intent = new Intent(Nowplaying.this, DescriptionActivity.class);
                intent.putExtra("movieid", id);
                startActivity(intent);

            }


            @Override
            public void OnLongClick(View v, int position) {


            }

            @Override
            public void OnScroll() {

            }

        }));

    }


    private void sendJsonRequest(int mCurrentPage) {
        mEndpoint = Endpoint.MOVIE + "now_playing" + "?page=" + mCurrentPage + "&api_key=" + MyApplication.TMDB_API_KEY + "&language=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndpoint, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                final int startposition = listitem.size();
                listitem = parseJsonRequest(response);
                mAdapter.setmovielist(listitem, startposition);
                recyclerView.stopScroll();
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
                mLoading = false;

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
        requestQueue.getCache().clear();

    }


    private List<Movie> parseJsonRequest(JSONObject response) {


        if (response != null || response.length() > 0) {


            try {

                JSONArray jsonArray = response.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject Object = jsonArray.getJSONObject(i);
                    String overview = Object.getString("overview");
                    String title = Object.getString("original_title");
                    String posterpath = Object.getString("poster_path");
                    String date = Object.getString("release_date");
                    long id = Object.getInt("id");
                    String backdrop = Object.getString("backdrop_path");
                    float rating = Float.parseFloat(Object.getString("vote_average"));

                    Movie movie = new Movie();
                    movie.setOriginalTitle(title);
                    movie.setVoteAverage(rating);
                    movie.setReleaseDate(date);
                    movie.setBackdropPath(backdrop);
                    movie.setOverview(overview);
                    movie.setPosterPath(posterpath);
                    movie.setId(id);
                    listitem.add(movie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return listitem;
    }

    }
