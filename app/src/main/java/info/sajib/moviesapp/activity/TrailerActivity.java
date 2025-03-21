package info.sajib.moviesapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.R;

/**
 * Created by sajib on 14-03-2016.
 */
public class TrailerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {
    private String trailerid;
    private YouTubePlayerView youTubeView;
    private static final int RECOVERY_REQUEST = 1;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.trailer);

        trailerid=getIntent().getStringExtra("trailerid");

        youTubeView = (YouTubePlayerView) findViewById(R.id.youtubeplayer);
        youTubeView.initialize(MyApplication.YOUTUBE_API_KEY, this);
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.cueVideo(trailerid); // Plays https://www.youtube.com/watch?v=fhWaJi1Hsfo
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), errorReason);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(MyApplication.YOUTUBE_API_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }



}
