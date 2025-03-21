package info.sajib.moviesapp.activity;

import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.endpoints.Endpoint;
import info.sajib.moviesapp.volleysingleton.VolleySingleton;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by sajib on 25-04-2016.
 */
public class Piczoom extends AppCompatActivity {

    ImageLoader imageLoader;
    String poster;
    ImageViewTouch imageView;
    Bitmap wallpaper;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.piczoom);

        imageLoader = VolleySingleton.getInstance().getImageLoader();
        poster = getIntent().getStringExtra("poster");
        String url = Endpoint.IMAGE + "/w1280/" + poster;

        imageView = (ImageViewTouch) findViewById(R.id.piczoom_image);
        final Button buttonSetWallpaper = (Button)findViewById(R.id.wallapaper);
        progressBar= (ProgressBar) findViewById(R.id.loading);

        imageLoader.get(url, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                imageView.setImageBitmap(response.getBitmap());
                wallpaper=response.getBitmap();
                imageView.setVisibility(View.VISIBLE);
                buttonSetWallpaper.setVisibility(View.VISIBLE);
                if(wallpaper!=null) {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        buttonSetWallpaper.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                WallpaperManager myWallpaperManager
                        = WallpaperManager.getInstance(getApplicationContext());
                try {
                    if(wallpaper!=null) {
                        myWallpaperManager.setBitmap(wallpaper);
                        Toast.makeText(Piczoom.this, "Wallpaper set", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }});



    }



}
