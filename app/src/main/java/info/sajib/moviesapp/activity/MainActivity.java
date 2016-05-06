package info.sajib.moviesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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
import info.sajib.moviesapp.adapter.Main_screen_Adapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.pojo.Upcoming;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;


public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private NavigationView mNavigationview;
    static RequestQueue requestQueue;
    static List<Movie> listitem = new ArrayList<>();
    static List<Tv> tvitem = new ArrayList<>();
    static List<Upcoming> upitem = new ArrayList<>();
    static Main_screen_Adapter main_screen_adapter;
    static RecyclerView recyclerView;
    static ProgressBar progressBar;
    public static boolean HASCONNECTION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

        ConnectionAvailable();

        recyclerView = (RecyclerView) findViewById(R.id.main_reccycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        main_screen_adapter = new Main_screen_Adapter(MainActivity.this);
        recyclerView.setAdapter(main_screen_adapter);
        recyclerView.setHasFixedSize(true);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationview = (NavigationView) findViewById(R.id.nav_view);



        setupnav();

        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mActionBarDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mActionBarDrawerToggle.syncState();
            }
        });


        mNavigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                if (item.isChecked()) {
                    item.setChecked(false);
                } else {
                    item.setChecked(true);
                }

                mDrawerLayout.closeDrawers();

                switch (item.getItemId()) {
                    case R.id.movie: {

                        return true;
                    }
                    case R.id.tv: {
                        startActivity(new Intent(MainActivity.this, Tvshow.class));
                        return true;
                    }
                    default: {
                        Toast.makeText(getApplicationContext(), "You Have Selected Nothing", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                }

            }
        });

    }

    public static void ConnectionAvailable() {
        if(!HASCONNECTION) {

            sendJsonRequest();
        }
    }


    private static void sendJsonRequest() {
        String url = Endpoint.MOVIE + "now_playing" + "?api_key=" + MyApplication.TMDB_API_KEY + "&language=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                sendJsonRequest2();
                if (response != null || response.length() > 0) {


                    try {

                        JSONArray jsonArray = response.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Object = jsonArray.getJSONObject(i);
                            if (Object.has("poster_path") && !Object.isNull("poster_path")) {
                                String posterpath = Object.getString("poster_path");
                                int id = Object.getInt("id");
                                String orginalname = Object.getString("original_title");
                                Movie movie = new Movie();
                                movie.setPosterPath(posterpath);
                                movie.setId(id);
                                movie.setOriginalTitle(orginalname);
                                listitem.add(movie);
                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }

    private static void sendJsonRequest2() {

        String url = Endpoint.TV + "on_the_air" + "?api_key=" + MyApplication.TMDB_API_KEY;
        JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                sendJsonRequest3();
                if (response != null || response.length() > 0) {
                    try {
                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Object = jsonArray.getJSONObject(i);
                            if (Object.has("backdrop_path") && !Object.isNull("backdrop_path") && Object.has("poster_path") && !Object.isNull("poster_path")) {
                                long id = Object.getInt("id");
                                String backdropath = Object.getString("backdrop_path");
                                String posterpath = Object.getString("poster_path");
                                String originalName = Object.getString("original_name");
                                Tv tv = new Tv();
                                tv.setId(id);
                                tv.setPosterPath(posterpath);
                                tv.setBackdrop(backdropath);
                                tv.setOriginalname(originalName);
                                tvitem.add(tv);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request1);

    }

    private static void sendJsonRequest3() {
        String url = Endpoint.MOVIE + "upcoming" + "?api_key=" + MyApplication.TMDB_API_KEY + "&language=en";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                if (response != null || response.length() > 0) {


                    try {

                        JSONArray jsonArray = response.getJSONArray("results");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Object = jsonArray.getJSONObject(i);
                            if (Object.has("poster_path") && !Object.isNull("poster_path")) {
                                String posterpath = Object.getString("poster_path");
                                String orginalname = Object.getString("original_title");
                                long id = Object.getInt("id");
                                Upcoming upcoming = new Upcoming();
                                upcoming.setPosterPath(posterpath);
                                upcoming.setId(id);
                                upcoming.setOriginalTitle(orginalname);
                                upitem.add(upcoming);
                            }

                        }
                        HASCONNECTION=true;
                        main_screen_adapter.setdata(tvitem, listitem, upitem);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }


    //setting up the navigation click istner and toolbar home enabling
    private void setupnav() {
        if (mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
            });
        }
    }

    //optionmenu Creating


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.ActivityResumed();
        if(HASCONNECTION) {
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MyApplication.ActivityPaused();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        moveTaskToBack(true);
        finish();

    }
}
