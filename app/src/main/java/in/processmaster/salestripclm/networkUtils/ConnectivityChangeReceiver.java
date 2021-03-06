package in.processmaster.salestripclm.networkUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import in.processmaster.salestripclm.activity.BaseActivity;
import in.processmaster.salestripclm.activity.SplashActivity;
import in.processmaster.salestripclm.common_classes.GeneralClass;

public class ConnectivityChangeReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        try{
            if(context instanceof Activity)
            {
                Activity activity= (Activity) context;
                BaseActivity baseActivity = new BaseActivity();

                if(!isConnected(context))
                {
                    if(SplashActivity.Companion.getShowNetworkAlert())
                    {
                        SplashActivity.Companion.setShowNetworkAlert(false);
                        baseActivity.networkAlertNotification(activity);
                    }
                }
                else {
                    SplashActivity.Companion.setShowNetworkAlert(true);
                    baseActivity.disableNetworkAlert(activity);
                }
            }
        }
        catch (Exception e)
        {
            Log.e("exceptionInOnReceive",e.getMessage());
        }
    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}
