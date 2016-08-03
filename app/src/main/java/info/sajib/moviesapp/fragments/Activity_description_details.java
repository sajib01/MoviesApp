package info.sajib.moviesapp.fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Genreitem;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_description_details extends Fragment {

    private long id;
    private RequestQueue requestQueue;
    private List<Movie> listitem = new ArrayList<>();
    private List<Genreitem> genreitem = new ArrayList<>();
    private List<String> crewmember = new ArrayList<>();
    private ProgressDialog progressDialog;
    private ImageView poster;
    private TextView title;
    private TextView date;
    private TextView rating;
    private TextView budget;
    private TextView director;
    private TextView overview;
    private TextView genre;
    private Bitmap bbitmap;
    private ImageLoader imageLoader;
    private RelativeLayout relativeLayout;
    public static Activity_description_details newInstance(long Id) {
        Activity_description_details myFragment = new Activity_description_details();

        Bundle args = new Bundle();
        args.putLong("ID", Id);
        myFragment.setArguments(args);
        return myFragment;
    }

    public Activity_description_details() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getLong("ID", 0);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        String Id = String.valueOf(id);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.show();

        imageLoader=VolleySingleton.getInstance().getImageLoader();
        String url = Endpoint.MOVIE + Id + "?api_key=" + MyApplication.TMDB_API_KEY+ "&append_to_response=casts,images,trailers&language=en&include_image_language=en,null";

        SendJsonRequest(url);
    }

    private void SendJsonRequest(String url) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
                parsejsonrequest(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View details = inflater.inflate(R.layout.activity_description_details, container, false);

        title= (TextView) details.findViewById(R.id.Movie_title);
        date= (TextView) details.findViewById(R.id.Movie_date);
        rating= (TextView) details.findViewById(R.id.Movie_vote);
        genre= (TextView) details.findViewById(R.id.Movie_genre);
        budget= (TextView) details.findViewById(R.id.Movie_budget);
        director= (TextView) details.findViewById(R.id.Movie_Director);
        overview= (TextView) details.findViewById(R.id.Movie_description);
        poster= (ImageView) details.findViewById(R.id.Movie_poster);
        relativeLayout= (RelativeLayout) details.findViewById(R.id.Movie_details);

        return details;
    }

    private void parsejsonrequest(JSONObject response) {

        if (response != null || response.length() > 0) {
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

                }
                JSONObject casts = response.getJSONObject("casts");
                JSONArray cast = casts.getJSONArray("cast");
                JSONArray crew = casts.getJSONArray("crew");
                String directorname = "NA";
                for (int l = 0; l < crew.length(); l++) {
                    JSONObject crewJson = crew.getJSONObject(l);
                    String job = crewJson.getString("job");
                    if (job.equals("Director")) {
                        directorname = crewJson.getString("name");
                    }
                    crewmember.add(directorname);
                }
                String overview = response.getString("overview");
                String title = response.getString("original_title");
                String posterpath = response.getString("poster_path");
                long budget =response.getInt("budget");
                String date = response.getString("release_date");
                float rating = Float.parseFloat(response.getString("vote_average"));

                Movie movie = new Movie();
                movie.setOriginalTitle(title);
                movie.setVoteAverage(rating);
                movie.setReleaseDate(date);
                movie.setOverview(overview);
                movie.setBudget(budget);
                movie.setPosterPath(posterpath);
                listitem.add(movie);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            finally {
                settext();
            }
        }
    }

    private void settext() {
        StringBuffer buffer = new StringBuffer();
        int counter = 0;
        String direc=null;
        for (int i = 0; i < genreitem.size(); i++) {
            counter++;
            Genreitem gener = genreitem.get(i);
            buffer.append(gener.getName());
            if (counter != genreitem.size()) {
                buffer.append(",");
            }
        }

        for (int j = 0; j < crewmember.size(); j++) {
            if (!crewmember.get(j).equals("NA")) {
                direc = crewmember.get(j);
            }
        }
        if(listitem.size()>0) {
            Movie movie = listitem.get(0);
            Picasso.with(getActivity()).load(Endpoint.IMAGE + "/w185/" + movie.getPosterPath()).into(poster);
            title.setText(movie.getOriginalTitle());
            date.setText(movie.getReleaseDate());
            rating.setText(String.format("%.2f", movie.getVoteAverage()));
            String budget1="$"+movie.getBudget();
            budget.setText(budget1);
            director.setText(direc);
            overview.setText(movie.getOverview());
            genre.setText(buffer);
        }

    }


}
