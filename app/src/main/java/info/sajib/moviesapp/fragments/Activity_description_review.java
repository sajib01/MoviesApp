package info.sajib.moviesapp.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import info.sajib.moviesapp.adapter.Reviewadapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Review;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 */
public class Activity_description_review extends Fragment {

    private long id;
    private RecyclerView recyclerView;
    private List<Review> reviews=new ArrayList<>();
    private Reviewadapter adapter;
    private RequestQueue requestQueue;
    private ProgressDialog progressDialog;

    public static Activity_description_review newInstance(long Id) {
        Activity_description_review myFragment = new Activity_description_review();

        Bundle args = new Bundle();
        args.putLong("ID", Id);
        myFragment.setArguments(args);
        return myFragment;
    }

    public Activity_description_review() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestQueue= VolleySingleton.getInstance().getRequestQueue();
        id = getArguments().getLong("ID", 0);

        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.loading_data));
        progressDialog.getWindow().setGravity(Gravity.BOTTOM);
        progressDialog.show();

        String mEndpoint=Endpoint.MOVIE+id+"/reviews?api_key="+ MyApplication.TMDB_API_KEY;
        SendRequestjson(mEndpoint);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_description_review, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.activity_description_review_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new Reviewadapter(getActivity(), reviews);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    private void SendRequestjson(String mEndpoint) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, mEndpoint, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(progressDialog.isShowing())
                {
                    progressDialog.dismiss();
                }
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
            reviews.clear();

            try {
                JSONArray data = response.getJSONArray("results");

                for (int p = 0; p < data.length(); p++) {
                    JSONObject object = data.getJSONObject(p);
                    String author = object.getString("author");
                    String content= object.getString("content");
                    Review review = new Review();
                    review.setAuthor(author);
                    review.setContent(content);
                    reviews.add(review);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {

                adapter.notifyDataSetChanged();
            }
        }
    }
}
