package info.sajib.moviesapp.activity;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import java.util.Locale;

import info.sajib.moviesapp.CustomRetryPolicy;
import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;
import info.sajib.moviesapp.adapter.SearchAdapter;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.pojo.Movie;
import info.sajib.moviesapp.recyclertouchlistner.ClickListner;
import info.sajib.moviesapp.recyclertouchlistner.RecyclerViewTouchListner;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;

/**
 * Created by sajib on 05-03-2016.
 */
public class Search_Engine extends AppCompatActivity {
    String query;
    private boolean issearched = false;
    private EditText editText;
    private ImageButton clear;
    private ImageButton home;
    private String mEndpoint;
    private RequestQueue requestQueue;
    public List<Movie> listitem;
    private RecyclerView mrecyclerView;
    public SearchAdapter mAdapter;
    private String posterpath;
    private LinearLayoutManager mLayoutmanager;
    private ImageButton btnSpeak;
    private boolean animationstarted = true;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static final String TAG = Search_Engine.class
            .getSimpleName();
    private static final int REQUEST_TIMEOUT = 5 * 1000;
    private static final int MAX_RETRIES = 3;
    private static final int BACKOFF_MULTIPLIER = 1;
    private CardView card;
    private LinearLayout linearLayout;
    private StringBuffer year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_engine);
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        listitem = new ArrayList<>();
        editText = (EditText) findViewById(R.id.editsearch);
        card = (CardView) findViewById(R.id.card);
        mrecyclerView = (RecyclerView) findViewById(R.id.search_recyclerview);
        mLayoutmanager = new LinearLayoutManager(this);
        mrecyclerView.setLayoutManager(mLayoutmanager);
        mAdapter = new SearchAdapter(this, listitem);
        mrecyclerView.setAdapter(mAdapter);
        mrecyclerView.addOnItemTouchListener(new RecyclerViewTouchListner(this, mrecyclerView, new ClickListner() {
            @Override
            public void OnClick(View v, int Position) {
                InputMethodManager imm = (InputMethodManager) Search_Engine.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                Movie movie = listitem.get(Position);
                long id = movie.getId();
                String title = movie.getOriginalTitle();
                String date = movie.getReleaseDate();
                float vote = movie.getVoteAverage();
                String description = movie.getOverview();
                String collectioname = movie.getCollectionname();
                Intent intent = new Intent(Search_Engine.this, DescriptionActivity.class);
                intent.putExtra("collectionname", collectioname);
                intent.putExtra("movieid", id);
                intent.putExtra("title", title);
                intent.putExtra("moviedate", date);
                intent.putExtra("movievote", vote);
                intent.putExtra("description", description);
                startActivity(intent);
            }

            @Override
            public void OnLongClick(View v, int Position) {

            }

            @Override
            public void OnScroll() {
                InputMethodManager imm = (InputMethodManager) Search_Engine.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            }
        }));

        clear = (ImageButton) findViewById(R.id.clear);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                listitem.clear();
                mAdapter.notifyDataSetChanged();
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) Search_Engine.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        btnSpeak = (ImageButton) findViewById(R.id.mic);
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
                editText.setText("");
            }
        });
        home = (ImageButton) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Search_Engine.this, MainActivity.class));
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationstarted && Build.VERSION.SDK_INT >= 21) {
            card.post(new Runnable() {
                @Override
                public void run() {
                    animationstart(card);
                    animationstarted = false;
                }
            });
        }
        if (Build.VERSION.SDK_INT < 21) {
            handlemenusearch();
            card.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void animationstart(final View card) {

        int cx = card.getMeasuredWidth();
        int cy = card.getMeasuredHeight() / 2;
        final int radius = Math.max(card.getWidth(), card.getHeight());
        Animator animator = ViewAnimationUtils.createCircularReveal(card, cx, cy, 0, radius);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                card.setVisibility(View.VISIBLE);
                handlemenusearch();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        card.setVisibility(View.VISIBLE);
        animator.setDuration(500);
        animator.start();
    }

    public void handlemenusearch() {

        if (issearched) {

            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

            issearched = false;
        } else {

            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        Toast.makeText(Search_Engine.this, "hello i am here", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            });

            TextWatcher mTextEditorWatcher = new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    query = String.valueOf(s);
                    String newquery = query.replaceAll(" ", "%20");
                    int length = s.length();
                    mEndpoint = Endpoint.SEARCH + "/multi" + "?api_key=" + MyApplication.TMDB_API_KEY + "&query=" + newquery;
                    final int startposition = listitem.size();
                    searchmovietitle(mEndpoint);
                    mAdapter.notifyItemRangeChanged(0, listitem.size());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    query = String.valueOf(s).trim();
                    if (s.length() == 0) {
                        listitem.clear();
                        mAdapter.notifyDataSetChanged();

                    }
                }

            };
            editText.addTextChangedListener(mTextEditorWatcher);
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        return true;
                    }
                    return false;
                }
            });
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            issearched = true;
        }
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    editText.setText(result.get(0));
                    editText.requestFocus();
                    String Query = result.get(0);
                    String NewQuery = Query.replaceAll(" ", "%20");
                    String mpoint = Endpoint.SEARCH + "?api_key=" + MyApplication.TMDB_API_KEY + "&query=" + NewQuery;
                    searchmovietitle(mpoint);
                    editText.setSelection(query.length());
                    mAdapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) Search_Engine.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                }
                break;
            }

        }
    }

    private void searchmovietitle(String mEndpoint) {
        String Endpointt = mEndpoint;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Endpointt, (String) null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null || response.length() > 0) {

                    listitem.clear();
                    try {

                        JSONArray jsonArray = response.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject Object = jsonArray.getJSONObject(i);
                            if (Object.getString("media_type").equalsIgnoreCase("person")) {
                                JSONArray array = Object.getJSONArray("known_for");
                                String profilepath = Object.getString("profile_path");
                                for (int j = 0; j < array.length(); j++) {
                                    JSONObject jj = array.getJSONObject(j);
                                    String overview = jj.getString("overview");
                                    String title = jj.getString("original_title");
                                    posterpath = jj.getString("poster_path");
                                    String date = jj.getString("release_date");
                                    String collectionname = Object.getString("media_type");
                                    long id = jj.getInt("id");
                                    float rating = Float.parseFloat(jj.getString("vote_average"));
                                    Movie movie = new Movie();
                                    movie.setOriginalTitle(title);
                                    movie.setVoteAverage(rating);
                                    movie.setReleaseDate(date);
                                    movie.setYear(String.valueOf(year));
                                    movie.setOverview(overview);
                                    movie.setCollectionname(collectionname);
                                    movie.setBackdropPath(profilepath);
                                    movie.setPosterPath(posterpath);
                                    movie.setId(id);
                                    listitem.add(movie);

                                }
                            }
                            if ((Object.getString("media_type")).equals("movie")) {

                                String overview = Object.getString("overview");
                                String title = Object.getString("original_title");
                                String posterpath = Object.getString("poster_path");
                                String collectionname = Object.getString("media_type");
                                String date = Object.getString("release_date");
                                long id = Object.getInt("id");
                                float rating = Float.parseFloat(Object.getString("vote_average"));
                                Movie movie = new Movie();
                                movie.setOriginalTitle(title);
                                movie.setVoteAverage(rating);
                                movie.setReleaseDate(date);
                                movie.setOverview(overview);
                                movie.setCollectionname(collectionname);
                                movie.setPosterPath(posterpath);
                                movie.setId(id);
                                listitem.add(movie);

                            }

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    mAdapter.notifyDataSetChanged();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }

}