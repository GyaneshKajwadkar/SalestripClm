package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.models.LoginModel
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
import com.google.android.material.snackbar.Snackbar
import com.zipow.videobox.ptapp.PhoneProtos
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class GeneralClass(val activity: Activity) {

    var thread: Thread? = null

    fun hideKeyboard(context: Activity,view: View)
    {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.getWindowToken(), 0)
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
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAction("Close", View.OnClickListener { })
            .setActionTextColor(Color.WHITE)
            .setBackgroundTint(Color.RED)
            .show()
    }

    fun HideKeyboard(view: View) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun getCurrentDate():String{
        val c: Date = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return df.format(c)
    }

    fun getCurrentDateTimeApiForamt():String
    {
        val df: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
        return df.format(Calendar.getInstance().time)
    }

    fun convertApiDateTime_toDate(inputDate : String?) :String
    {  var fetchDate=""
        try {
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val startDate = format.parse(inputDate)
            format = SimpleDateFormat("dd/MM/yyyy")
            fetchDate = format.format(startDate)
        }
        catch (e: Exception){ Log.e("exception_FromConvertApiDate",e.message.toString()) }
        return  fetchDate
    }

    fun currentDateMMDDYY():String{
        val c: Date = Calendar.getInstance().getTime()
        val df = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        return df.format(c)
    }

    fun checkStringNullEmpty(string: String):Boolean{
        if(string.isEmpty()||string.equals("")||string==null)return true
        else return false
    }

    fun checkDateValidation(dateString:String) :Boolean
    {
        val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'")
        try {
            val date = format.parse(dateString)
            val c = Calendar.getInstance()
            c.setTime(date)
            if(c.get(Calendar.YEAR)<2000) return false
            else return true
        } catch (e: ParseException) {
            e.printStackTrace()
            return false
        }
    }

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }

    fun checkCurrentDateIsValid(loginModelHomePage:LoginModel=LoginModel(), from :String= "" ):Boolean {
        try
        {
            var format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")

            var apiDateString=Date()
            if(from.equals("splash")) apiDateString = format.parse(loginModelHomePage.currentDate)
                else apiDateString = format.parse(HomePage.loginModelHomePage.currentDate)

            format = SimpleDateFormat("dd/MM/yyyy")
            val apiDate= format.format(apiDateString)
            val deviceDate = format.format(Calendar.getInstance().time)
            Log.e("getAPIDate",apiDate.toString())
            Log.e("getcurrentDate", deviceDate.toString())

            if(apiDate.equals(deviceDate)) return true
            else return false
        }
        catch (e: Exception) {
            return true
        }
    }


}