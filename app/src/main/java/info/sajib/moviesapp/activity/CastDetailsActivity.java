package info.sajib.moviesapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import info.sajib.moviesapp.adapter.Cast_details_adapter;
import info.sajib.moviesapp.adapter.Cast_pager_adapter;
import info.sajib.moviesapp.custom.Customviewpager;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pagetransformer.Zoompagetransformer;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.pojo.PersonDetails;
import info.sajib.moviesapp.pojo.Upcoming;

public class CastDetailsActivity extends AppCompatActivity{
    private RequestQueue requestqueue;
    private int id;
    private String mEndpoint;
    private List<PersonDetails> personDetailsList;
    private List<Movie> listitem = new ArrayList<>();
    private List<Upcoming> list = new ArrayList<>();
    private String mEnd;
    private String formattedDate;
    private JsonObjectRequest jsonobjectrequest;
    private RecyclerView mRecyclerview;
    private LinearLayoutManager linearLayoutManager;
    private Cast_details_adapter cast_details_adapter;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private int colr;
    private Cast_pager_adapter cast_pager_adapter;
    private Customviewpager viewPager;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_details);

        requestqueue = VolleySingleton.getInstance().getRequestQueue();

        mRecyclerview = (RecyclerView) findViewById(R.id.cast_details_recyclerview);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage(getString(R.string.LoadingProfile));
        mProgress.getWindow().setGravity(Gravity.CENTER);
        mProgress.show();

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c.getTime());

        id = getIntent().getIntExtra("personid", id);
        mEndpoint = Endpoint.PERSON + id + "?api_key=" + MyApplication.TMDB_API_KEY;
        sendjsonrequest(mEndpoint);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        colr = 0xffffff;

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");

        viewPager = (Customviewpager) findViewById(R.id.cast_details_viewpager);
        cast_pager_adapter = new Cast_pager_adapter(this, listitem);

    }

    private void sendjsonrequest(String mEndpoint) {
        jsonobjectrequest = new JsonObjectRequest(Request.Method.GET, mEndpoint, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                personDetailsList = parsejson(response);
                mEnd = Endpoint.DISCOVER + "?api_key=" + MyApplication.TMDB_API_KEY + "&with_people=" + id + "&sort_by=popularity.desc";
                sendjsonrequest2(mEnd);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestqueue.add(jsonobjectrequest);
        requestqueue.getCache().clear();
    }

    private List<PersonDetails> parsejson(JSONObject response) {
        personDetailsList = new ArrayList<>();
        if (response != null || response.length() > 0) {
            try {
                String description = "Sorry We have no details";
                String name = response.getString("name");
                String birthday = response.getString("birthday");
                String placeofbirth = response.getString("place_of_birth");
                String profilepath = response.getString("profile_path");
                if (response.has("biography") && !response.isNull("biography")) {
                    description = response.getString("biography");
                }
                int id = response.getInt("id");
                PersonDetails personDetails = new PersonDetails();
                personDetails.setName(name);
                personDetails.setId(id);
                personDetails.setBirthdate(birthday);
                personDetails.setDescription(description);
                personDetails.setPlaceofbirth(placeofbirth);
                personDetails.setProfilepic(profilepath);
                personDetailsList.add(personDetails);
                Log.d("sajibperson", birthday + personDetailsList.size() + description + name + id + profilepath);
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

            }

        }

        return personDetailsList;
    }

    private void sendjsonrequest2(String mEnd) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEnd, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                parseJsonRequest2(response);

                String mEndp = Endpoint.DISCOVER + "?api_key=" + MyApplication.TMDB_API_KEY + "&with_people=" + id + "&primary_release_date.gte=" + formattedDate;

                sendjsonrequest3(mEndp);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestqueue.add(request);
        requestqueue.getCache().clear();
    }

    private void parseJsonRequest2(JSONObject response) {

        if (response != null || response.length() > 0) {
            listitem.clear();

            try {
                JSONArray jsonArray = response.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject Object = jsonArray.getJSONObject(i);
                    String overview = Object.getString("overview");
                    String title = Object.getString("original_title");
                    String posterpath = Object.getString("poster_path");
                    if (Object.has("backdrop_path") && !Object.isNull("backdrop_path")) {
                        String backdroppath = Object.getString("backdrop_path");
                        String date = Object.getString("release_date");
                        long id = Object.getInt("id");
                        float rating = Float.parseFloat(Object.getString("vote_average"));
                        Movie movie = new Movie();
                        movie.setOriginalTitle(title);
                        movie.setVoteAverage(rating);
                        movie.setBackdropPath(backdroppath);
                        movie.setReleaseDate(date);
                        movie.setOverview(overview);
                        movie.setPosterPath(posterpath);
                        movie.setId(id);
                        listitem.add(movie);
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void sendjsonrequest3(String mEndp) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndp, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (mProgress.isShowing()) {
                    mProgress.dismiss();
                }

                parseJsonRequest3(response);

                cast_details_adapter = new Cast_details_adapter(CastDetailsActivity.this, personDetailsList, listitem, list);
                PersonDetails personDetails = personDetailsList.get(0);
                collapsingToolbarLayout.setTitle(personDetails.getName());

                viewPager.setAdapter(cast_pager_adapter);
                cast_pager_adapter.notifyDataSetChanged();
                mRecyclerview.setAdapter(cast_details_adapter);
                cast_details_adapter.notifyDataSetChanged();
                done();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestqueue.add(request);
        requestqueue.getCache().clear();
    }

    private void parseJsonRequest3(JSONObject response) {

        if (response != null || response.length() > 0) {
            list.clear();

            try {
                JSONArray jsonArray = response.getJSONArray("results");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject Object = jsonArray.getJSONObject(i);
                    String overview = Object.getString("overview");
                    String title = Object.getString("original_title");
                    String posterpath = Object.getString("poster_path");
                    String backdroppath = Object.getString("backdrop_path");
                    String date = Object.getString("release_date");
                    long id = Object.getInt("id");
                    float rating = Float.parseFloat(Object.getString("vote_average"));
                    Upcoming movie = new Upcoming();
                    movie.setOriginalTitle(title);
                    movie.setVoteAverage(rating);
                    movie.setBackdropPath(backdroppath);
                    movie.setReleaseDate(date);
                    movie.setOverview(overview);
                    movie.setPosterPath(posterpath);
                    movie.setId(id);
                    list.add(movie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
        }
        if (id == R.id.search) {
            Intent intent = new Intent(this, Search_Engine.class);
            intent.putExtra("boolean", true);
            startActivity(intent);
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // startActivity(new Intent(this,MainActivity.class));
    }

    public void done() {
        viewPager.setPagingEnabled(false);
        if(listitem!=null) {
            viewPager.startAutoScroll(5000);

        }
        viewPager.setInterval(5000);
        viewPager.setStopScrollWhenTouch(true);
        viewPager.setPageTransformer(false, new Zoompagetransformer(this));
    }




}
