package info.sajib.moviesapp.volleysingleton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import info.sajib.moviesapp.MyApplication;
import info.sajib.moviesapp.lrucache.BitmapLruCache;
/**
 * Created by sajib on 19-02-2016.
 */
public class VolleySingleton {
    private static VolleySingleton sInstance = null;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    public static final String TAG = VolleySingleton.class.getSimpleName();

    private VolleySingleton() {
        requestQueue = Volley.newRequestQueue(MyApplication.getAppContext());
        imageLoader = new ImageLoader(requestQueue,BitmapLruCache.getInstance());
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static VolleySingleton getInstance() {
        if (sInstance == null) {
            sInstance = new VolleySingleton();
        }
        return sInstance;

    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

}
