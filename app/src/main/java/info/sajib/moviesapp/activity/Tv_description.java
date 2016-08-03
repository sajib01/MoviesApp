package info.sajib.moviesapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;

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
import info.sajib.moviesapp.adapter.Tv_description_adapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.pojo.Tvcast;
import info.sajib.moviesapp.pojo.Tvsimilar;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 12-04-2016.
 */
public class Tv_description extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RequestQueue requestQueue;
    private List<String> genre = new ArrayList<>();
    private List<Tv> tvdata = new ArrayList<>();
    private List<Tvcast> tvcastdata = new ArrayList<>();
    private List<String> backdrops = new ArrayList<>();
    private List<String> creaditedby = new ArrayList<>();
    private List<Tvsimilar> tvsimilars = new ArrayList<>();
    private List<String> generalinformation = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Tv_description_adapter descriptionAdapter;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tv_description);

        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        id = getIntent().getLongExtra("tvid", 0);

        recyclerView = (RecyclerView) findViewById(R.id.tv_description_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        descriptionAdapter = new Tv_description_adapter(Tv_description.this);

        senjsonrequest();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.show();

    }

    private void senjsonrequest() {
        String mEndpoin = Endpoint.TV + id + "?api_key=" + MyApplication.TMDB_API_KEY + "&append_to_response=credits,images,similar";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndpoin, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                parsejson(response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
        requestQueue.getCache().clear();
    }

    private void parsejson(JSONObject response) {

        if (response != null || response.length() > 0) {
            try {


                JSONArray array = response.getJSONArray("seasons");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String seasonno = object.getString("season_number");
                    String airdate = object.getString("air_date");
                    String episodeno = object.getString("episode_count");
                    String posterpath = object.getString("poster_path");
                    int seasonid = object.getInt("id");
                    Tv tv = new Tv();
                    tv.setSeasons_id(seasonid);
                    tv.setSeasons_airdate(airdate);
                    tv.setSeasons_episodecount(episodeno);
                    tv.setSeasons_posterpath(posterpath);
                    tv.setSeasons_no(seasonno);
                    tvdata.add(tv);

                }

                JSONArray genrearray = response.getJSONArray("genres");
                for (int i = 0; i < genrearray.length(); i++) {
                    String genres = "NA";
                    JSONObject jsonObject = genrearray.getJSONObject(i);
                    genres = jsonObject.getString("name");
                    genre.add(genres);

                }

                JSONObject imageobject = response.getJSONObject("images");
                JSONArray backdroparray = imageobject.getJSONArray("backdrops");
                for (int i = 0; i < backdroparray.length(); i++) {
                    JSONObject jsonObject = backdroparray.getJSONObject(i);
                    String backdrop = jsonObject.getString("file_path");
                    backdrops.add(backdrop);

                }

                JSONObject creditsobject = response.getJSONObject("credits");
                JSONArray castarray = creditsobject.getJSONArray("cast");
                for (int i = 0; i < castarray.length(); i++) {
                    JSONObject object = castarray.getJSONObject(i);
                    String charactername = object.getString("character");
                    int id = object.getInt("id");
                    String name = object.getString("name");
                    String profilepath = object.getString("profile_path");
                    Tvcast tvcast = new Tvcast();
                    tvcast.setCharacter(charactername);
                    tvcast.setId(id);
                    tvcast.setName(name);
                    tvcast.setProfile_path(profilepath);
                    tvcastdata.add(tvcast);

                }
                JSONArray creadit = response.getJSONArray("created_by");
                for (int i = 0; i < creadit.length(); i++) {
                    JSONObject jsonObject = creadit.getJSONObject(i);
                    String name = jsonObject.getString("name");

                    creaditedby.add(name);

                }
                JSONObject similar = response.getJSONObject("similar");
                JSONArray similararray = similar.getJSONArray("results");
                for (int i = 0; i < similararray.length(); i++) {
                    JSONObject object = similararray.getJSONObject(i);
                    int id = object.getInt("id");
                    String name = object.getString("original_name");
                    String posterpath = object.getString("poster_path");
                    Tvsimilar tvsimilar = new Tvsimilar();
                    tvsimilar.setId(id);
                    tvsimilar.setOriginalname(name);
                    tvsimilar.setPosterpath(posterpath);
                    tvsimilars.add(tvsimilar);
                }
                String firstairdate = response.getString("first_air_date");
                String lastAirDate = response.getString("last_air_date");
                String posterPath = response.getString("poster_path");
                String overview = response.getString("overview");
                String originalname = response.getString("original_name");
                String voteAverage = response.getString("vote_average");
                generalinformation.add(firstairdate);
                generalinformation.add(lastAirDate);
                generalinformation.add(posterPath);
                generalinformation.add(overview);
                generalinformation.add(originalname);
                generalinformation.add(voteAverage);

                descriptionAdapter.setdata(tvdata, generalinformation, tvcastdata, backdrops, creaditedby, tvsimilars, genre);
                recyclerView.setAdapter(descriptionAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}
