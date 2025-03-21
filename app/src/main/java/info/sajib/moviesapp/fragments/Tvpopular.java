package info.sajib.moviesapp.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import info.sajib.moviesapp.activity.Tv_description;
import info.sajib.moviesapp.adapter.Tvcommon_adapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.expandedrecycler.FilterRecyclerViewScrollListener;
import info.sajib.moviesapp.pojo.Tv;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Tvpopular#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tvpopular extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView mRecycler;
    private List<Tv> tvdata=new ArrayList<>();
    private int resource;
    private int imageresource;
    private Tvcommon_adapter tvcommonAdapter;
    private RequestQueue request;
    private int startingposition=0;
    private int page=1;
    private String mEndpoint;
    private ProgressDialog mProgressdialog;
    private boolean loading=false;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ProgressBar progressBar;

    public Tvpopular() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tvpopular.
     */
    // TODO: Rename and change types and number of parameters
    public static Tvpopular newInstance(String param1, String param2) {
        Tvpopular fragment = new Tvpopular();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        request= VolleySingleton.getInstance().getRequestQueue();
        Sendjsonrequest(page);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_tvpopular, container, false);

        mRecycler= (RecyclerView) v.findViewById(R.id.tvpopular);
        resource=R.layout.fragment_tvpopular_layout;
        imageresource=R.id.tvpopular_layout_poster;

        mProgressdialog=new ProgressDialog(getActivity());
        progressBar= (ProgressBar) v.findViewById(R.id.progressBar);
        LinearLayoutManager gridLayoutManager=new GridLayoutManager(getActivity(),2);
        mRecycler.setLayoutManager(gridLayoutManager);
        tvcommonAdapter=new Tvcommon_adapter(getActivity(),resource,imageresource);
        mRecycler.setAdapter(tvcommonAdapter);

        mRecycler.addOnScrollListener(new FilterRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if(!loading) {
                    loading=true;
                    mProgressdialog.setCancelable(true);
                    mProgressdialog.getWindow().setGravity(Gravity.BOTTOM);
                    mProgressdialog.setMessage(getString(R.string.loading_data));
                    mProgressdialog.show();
                    Sendjsonrequest(++page);
                }
            }
        });
        mRecycler.addOnItemTouchListener(new RecyclerViewTouchListner(getActivity(), mRecycler, new ClickListner() {
            @Override
            public void OnClick(View v, int Position) {
                Tv tv=tvdata.get(Position);
                Intent intent=new Intent(getActivity(), Tv_description.class);
                intent.putExtra("tvid",tv.getId());
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View v, int Position) {

            }

            @Override
            public void OnScroll() {

            }
        }));
        return v;
    }

    private void Sendjsonrequest(int page) {
        mEndpoint= Endpoint.TV+"popular"+"?page="+page+"&api_key="+ MyApplication.TMDB_API_KEY;
        JsonObjectRequest json=new JsonObjectRequest(Request.Method.GET, mEndpoint,  null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if(mProgressdialog.isShowing()) {
                    mProgressdialog.dismiss();
                }
                progressBar.setVisibility(View.GONE);
                mRecycler.setVisibility(View.VISIBLE);
                loading=false;
                startingposition=tvdata.size();
                tvdata =parseJson(response);
                tvcommonAdapter.setdata(startingposition,tvdata);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.add(json);
    }

    private List<Tv> parseJson(JSONObject response) {

        if(response!=null||response.length()>0){
            try {
                JSONArray result=response.getJSONArray("results");
                for(int i=0;i<result.length();i++)
                {
                    tvdata.clear();
                    JSONObject object=result.getJSONObject(i);

                        String poster=object.getString("poster_path");
                        long id=object.getLong("id");
                        Tv tv=new Tv();
                        tv.setPosterPath(poster);
                        tv.setId(id);
                        tvdata.add(tv);


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return tvdata;
    }


}
