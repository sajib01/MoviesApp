package info.sajib.moviesapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import info.sajib.moviesapp.activity.MainActivity;

import static info.sajib.moviesapp.activity.MainActivity.ConnectionAvailable;

/**
 * Created by sajib on 18-04-2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {
    static boolean HADSCONNECTION;
    @Override
    public void onReceive(final Context context, final Intent intent) {

        String status = NetworkUtil.getConnectivityStatusString(context);
        HADSCONNECTION=haveNetworkConnection(context);

        if(HADSCONNECTION && MyApplication.isActivityvisible())
        {
            ConnectionAvailable();
        }

        Toast.makeText(context, status, Toast.LENGTH_LONG).show();

    }

    private boolean haveNetworkConnection(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager)   context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}