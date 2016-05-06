package info.sajib.moviesapp.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.activity.CastDetailsActivity;
import info.sajib.moviesapp.adapter.MovieCastAdapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Casts;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_description_cast extends Fragment {
    long id;
    List<Casts> castsList=new ArrayList<>();
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    MovieCastAdapter adapter;
    ProgressDialog progressDialog;
    public static Activity_description_cast newInstance(long Id) {
        Activity_description_cast myFragment = new Activity_description_cast();
        Bundle args = new Bundle();
        args.putLong("ID",Id);
        myFragment.setArguments(args);
        return myFragment;
    }
    public Activity_description_cast() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id=getArguments().getLong("ID",0);
        requestQueue= VolleySingleton.getInstance().getRequestQueue();
        String mEndpoint=Endpoint.MOVIE + id + "?api_key=" + MyApplication.TMDB_API_KEY + "&append_to_response=casts,images,trailers&language=en&include_image_language=en,null";
        SendRequestjson(mEndpoint);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View cast = inflater.inflate(R.layout.activity_description_cast, container, false);
        recyclerView= (RecyclerView) cast.findViewById(R.id.activity_description_cast_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter=new MovieCastAdapter(getActivity(),castsList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.show();
        recyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(getActivity(), recyclerView, new ClickListner() {
            @Override
            public void OnClick(View v, int Position) {
                Casts casts=castsList.get(Position);
                Intent intent=new Intent(getActivity(), CastDetailsActivity.class);
                intent.putExtra("personid",casts.getId());
                startActivity(intent);

            }

            @Override
            public void OnLongClick(View v, int Position) {

            }

            @Override
            public void OnScroll() {

            }
        }));
        recyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .drawable(R.drawable.line)
                        .size(1)
                        .build());
        return cast;
    }

    private void SendRequestjson(String mEndpoint) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndpoint, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Parsejson(response);
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
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
            castsList.clear();

            try {
                JSONObject casts = response.getJSONObject("casts");
                JSONArray cast = casts.getJSONArray("cast");

                for (int p = 0; p < cast.length(); p++) {
                    JSONObject castJSONObject = cast.getJSONObject(p);
                    String charactername = castJSONObject.getString("character");
                    String name = castJSONObject.getString("name");
                    String profilepath = castJSONObject.getString("profile_path");
                    int id = castJSONObject.getInt("id");
                    Casts casts1 = new Casts();
                    casts1.setCharaterimage(profilepath);
                    casts1.setChracter(charactername);
                    casts1.setCast(name);
                    casts1.setId(id);
                    castsList.add(casts1);
                    Log.d("charac", name + charactername + profilepath);

                }



            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                adapter.notifyDataSetChanged();
            }
        }
    }

}
