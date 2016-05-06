package info.sajib.moviesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import info.sajib.moviesapp.adapter.Picactivity_adapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Backdrop;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 24-04-2016.
 */
public class Picactivity extends AppCompatActivity {
    long posterpath;
    RecyclerView recyclerview;
    Picactivity_adapter picadapter;
    List<Backdrop> item=new ArrayList<>();
    RequestQueue requestqueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picactivity);
        requestqueue= VolleySingleton.getInstance().getRequestQueue();
        posterpath=getIntent().getLongExtra("movieid",0);
        sendjsonrequest();
        recyclerview= (RecyclerView) findViewById(R.id.picactivity_recyclerview);
        GridLayoutManager layoutmanager=new GridLayoutManager(this,4);
        layoutmanager.setSpanCount(2);
        picadapter=new Picactivity_adapter(this);
        recyclerview.setLayoutManager(layoutmanager);
        recyclerview.setAdapter(picadapter);
        recyclerview.addOnItemTouchListener(new RecyclerViewTouchListner(this, recyclerview, new ClickListner() {
            @Override
            public void OnClick(View v, int Position) {
                Intent intent=new Intent(Picactivity.this,Piczoom.class);
                Backdrop backdrop=item.get(Position);
                intent.putExtra("poster",backdrop.getBackdrop());
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View v, int Position) {

            }

            @Override
            public void OnScroll() {

            }
        }));

    }

    private void sendjsonrequest() {
        String url= Endpoint.MOVIE+posterpath+"?api_key=" + MyApplication.TMDB_API_KEY + "&append_to_response=casts,images,trailers&language=en&include_image_language=en,null";
        final JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, url, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(response!=null||response.length()>0)
                {

                    try {
                        JSONObject images = response.getJSONObject("images");
                        JSONArray backdrop = images.getJSONArray("backdrops");
                        for (int j = 0; j < backdrop.length(); j++) {
                            JSONObject backdrops = backdrop.getJSONObject(j);
                            String filepath = backdrops.getString("file_path");
                            Backdrop movie = new Backdrop();
                            movie.setBackdrop(filepath);
                            item.add(movie);
                        }

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    picadapter.setposter(item);
                    }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestqueue.add(request);
        requestqueue.getCache().clear();
    }

}
