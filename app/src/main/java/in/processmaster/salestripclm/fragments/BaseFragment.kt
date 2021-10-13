package `in`.processmaster.salestripclm.fragments

import `in`.processmaster.salestripclm.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton

open class BaseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun isInternetAvailable(context: Context): Boolean
    {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
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

    //common alert with message and ok button
    @SuppressLint("WrongViewCast")
    public fun commonAlert(context: Context,headerString: String, message: String)
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.common_alert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val okBtn_rl =
            dialogView.findViewById<View>(R.id.ok_btn) as AppCompatButton

        var headerTv =
            dialogView.findViewById<View>(R.id.header_tv) as TextView
        var messageTv =
            dialogView.findViewById<View>(R.id.message_tv) as TextView

        headerTv.setText(headerString)
        messageTv.setText(message)

        okBtn_rl.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    //enable progress with screen not touchable
    fun enableProgress(progressBar: ProgressBar,activity: Activity)
    {
        progressBar?.visibility = View.VISIBLE
        activity?.getWindow()?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //disable progress bar
    fun disableProgress(progressBar: ProgressBar,activity: Activity)
    {
        progressBar?.visibility = View.GONE
        progressBar
        activity?.getWindow()?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //network alert
    fun networkAlert(context: Context)
    {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.networkalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val okBtn_rl =
            dialogView.findViewById<View>(R.id.okBtn_rl) as RelativeLayout

        okBtn_rl.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
    //enable progress with screen not touchable
    fun enableProgress(progressBar: RelativeLayout,activity: Activity)
    {

        progressBar?.visibility = View.VISIBLE
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    //disable progress bar
    fun disableProgress(progressBar: RelativeLayout,activity: Activity)
    {
        progressBar?.visibility = View.GONE
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }


}