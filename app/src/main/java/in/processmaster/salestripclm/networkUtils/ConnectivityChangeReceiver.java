package in.processmaster.salestripclm.networkUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.RequiresApi;

import in.processmaster.salestripclm.activity.BaseActivity;
import in.processmaster.salestripclm.common_classes.GeneralClass;

public class ConnectivityChangeReceiver extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {

        Activity activity= (Activity) context;
        BaseActivity baseActivity = new BaseActivity();

        if(!isConnected(context))
        {
            baseActivity.networkAlertNotification(activity);
        }
        else {
            baseActivity.disableNetworkAlert();
        }
    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}