package info.sajib.moviesapp;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by sajib on 11-02-2016.
 */
public class MyApplication extends Application {
    public static final String TMDB_API_KEY = BuildConfig.TMDB_API_KEY;
    public static final String YOUTUBE_API_KEY = BuildConfig.YOUTUBE_API_KEY;
    private static MyApplication sInstance;
    private static boolean activityvisible;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        PrinthashKey();
    }

    public void PrinthashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "info.sajib.moviesapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public static MyApplication getInstance() {
        return sInstance;
    }

    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static boolean isActivityvisible()
    {
        return activityvisible;
    }
    public static void ActivityResumed()
    {
        activityvisible=true;
    }
    public static void ActivityPaused()
    {
        activityvisible=false;
    }
}
