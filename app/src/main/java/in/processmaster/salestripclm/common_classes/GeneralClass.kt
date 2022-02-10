package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.activity.PhotoSlideShowActivity
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import com.google.android.material.snackbar.Snackbar

class GeneralClass(val activity: Activity) {

    var thread: Thread? = null

    fun hideKeyboard(context: Activity,view: View)
    {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
    }

    fun enableProgress(progressBar: RelativeLayout)
    {
        progressBar?.visibility = View.VISIBLE
        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //disable progress bar
    fun disableProgress(progressBar: RelativeLayout)
    {
        progressBar?.visibility = View.GONE
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    fun enableSimpleProgress(progressBar: ProgressBar)
    {
        progressBar?.visibility = View.VISIBLE
        activity?.getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //disable progress bar
    fun disableSimpleProgress(progressBar: ProgressBar)
    {
        progressBar?.visibility = View.GONE
        progressBar
        activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    fun checkInternet()
    {
        if(isInternetAvailable()==true)
        {
            AlertClass(activity).commonAlert("Error", "Something went wrong please try again later")
        }
        else
        {
            AlertClass(activity).networkAlert()
        }
    }


    fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //check versin of mobile device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Close", View.OnClickListener { })
            .setActionTextColor(Color.YELLOW)
            .show()
    }

    fun HideKeyboard(view: View) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }



}