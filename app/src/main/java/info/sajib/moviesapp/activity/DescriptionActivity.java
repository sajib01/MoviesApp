package info.sajib.moviesapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

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
    private ViewPager2 viewPager;
    private ViewPager2 viewpager1;
    private TabLayout designSlidingTabLayout;
    private long id;
    private Descriptionactivity_Pageradapter Dpageradapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();

         viewPager = findViewById(R.id.activity_description_viewpager1);
        Dpageradapter =new Descriptionactivity_Pageradapter(DescriptionActivity.this,data);
        viewPager.setAdapter(Dpageradapter);
        viewPager.setPageTransformer(new DepthPageTransformer());

        viewpager1 = findViewById(R.id.activity_description_viewpager2);

        designSlidingTabLayout = findViewById(R.id.activity_description_slidingtab);

        viewpager1.setAdapter(new Myadapter(this));

        new TabLayoutMediator(designSlidingTabLayout, viewPager,
                (tab, position) -> tab.setText("OBJECT " + (position + 1))
        ).attach();


        id = getIntent().getLongExtra("movieid", 0);

        Id = String.valueOf(id);

        mEndpoint = Endpoint.MOVIE + Id + "?api_key=" + MyApplication.TMDB_API_KEY + "&append_to_response=casts,images,trailers&language=en&include_image_language=en,null";
        SendRequestjson(mEndpoint);

    }

    class Myadapter extends FragmentStateAdapter {

        String tabs[];

        public Myadapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
            tabs = getResources().getStringArray(R.array.DescriptionTab);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
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

        @Override
        public int getItemCount() {
            return 4;
        }
    }


    private void SendRequestjson(String mEndpoint) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndpoint,  null, new Response.Listener<JSONObject>() {
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
        requestQueue.getCache().clear();

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




