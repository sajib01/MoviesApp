package info.sajib.moviesapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.adapter.Descriptionactivity_Pageradapter;
import info.sajib.moviesapp.fragments.Activity_description_cast;
import info.sajib.moviesapp.fragments.Activity_description_details;
import info.sajib.moviesapp.fragments.Activity_description_review;
import info.sajib.moviesapp.fragments.Activity_description_similarmovies;
import info.sajib.moviesapp.pagetransformer.DepthPageTransformer;
import info.sajib.moviesapp.slidingtab.SlidingTabLayout;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Casts;
import info.sajib.moviesapp.pojo.Genreitem;
import info.sajib.moviesapp.pojo.Movie;

public class DescriptionActivity extends AppCompatActivity {
    private List<Genreitem> genreitem = new ArrayList<>();
    private RequestQueue requestQueue;
    private List<Movie> data = new ArrayList<>();
    private String mEndpoint;
    private String Id;
    ViewPager viewPager;
    ViewPager viewpager1;
    SlidingTabLayout designSlidingTabLayout;
    long id;
    Descriptionactivity_Pageradapter Dpageradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();

        viewPager = (ViewPager) findViewById(R.id.activity_description_viewpager1);
        Dpageradapter =new Descriptionactivity_Pageradapter(DescriptionActivity.this,data);
        viewPager.setAdapter(Dpageradapter);
        viewPager.setPageTransformer(false,new DepthPageTransformer());
        viewpager1 = (ViewPager) findViewById(R.id.activity_description_viewpager2);

        designSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.activity_description_slidingtab);
        viewpager1.setAdapter(new Myadapter(getSupportFragmentManager()));
        designSlidingTabLayout.setDistributeEvenly(true);
        designSlidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {

            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        designSlidingTabLayout.setViewPager(viewpager1);

        id = getIntent().getLongExtra("movieid", 0);

        Id = String.valueOf(id);
        Log.d("recce", String.valueOf(id));

        mEndpoint = Endpoint.MOVIE + Id + "?api_key=" + MyApplication.TMDB_API_KEY + "&append_to_response=casts,images,trailers&language=en&include_image_language=en,null";
        SendRequestjson(mEndpoint);

    }

    class Myadapter extends FragmentStatePagerAdapter {

        String tabs[];

        public Myadapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.DescriptionTab   );

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            if (position == 0) {
                fragment = Activity_description_details.newInstance(id);
            }
            if (position == 1) {
                fragment =Activity_description_cast.newInstance(id);
            }
            if (position == 2) {
                fragment= Activity_description_similarmovies.newInstance(id);
            }
            if(position == 3)
            {
                fragment =Activity_description_review.newInstance(id);
            }

            return fragment;
        }
    }


    private void SendRequestjson(String mEndpoint) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndpoint, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Parsejson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }

    private void Parsejson(JSONObject response) {

        if (response != null && response.length() > 0) {
            genreitem.clear();

            try {
                String genrename = "NA";
                JSONArray result = response.getJSONArray("genres");
                for (int i = 0; i < result.length(); i++) {
                    JSONObject path = result.getJSONObject(i);
                    if (path.has("name") && !path.isNull("name")) {
                        genrename = path.getString("name");
                    }
                    Genreitem generitem = new Genreitem();
                    generitem.setName(genrename);
                    genreitem.add(generitem);
                    Log.d("reccee", genrename);
                }
                JSONObject trailer=response.getJSONObject("trailers");
                JSONArray youtube=trailer.getJSONArray("youtube");
                for(int i=0;i<youtube.length();i++)
                {
                    JSONObject youtube1=youtube.getJSONObject(i);
                    String name=youtube1.getString("name");
                    String size=youtube1.getString("size");
                    String source=youtube1.getString("source");
                    String type=youtube1.getString("type");
                    Movie movie=new Movie();
                    movie.setName(name);
                    movie.setSize(size);
                    movie.setSource(source);
                    movie.setType(type);
                    data.add(movie);
                }

                JSONObject images = response.getJSONObject("images");
                JSONArray backdrop = images.getJSONArray("backdrops");
                for (int j = 0; j < backdrop.length(); j++) {
                    JSONObject backdrops = backdrop.getJSONObject(j);
                    String filepath = backdrops.getString("file_path");
                    Movie movie = new Movie();
                    movie.setBackdropPath(filepath);
                    movie.setId(id);
                    data.add(movie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                Dpageradapter.notifyDataSetChanged();
            }
        }
    }


    }




