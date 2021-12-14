package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog


class ProgressClass(val context:Context) {

    companion object {
        var alertDialog: AlertDialog? =null
    }

    fun showAlert(message: String)
    {
        if(alertDialog!=null&& alertDialog!!.isShowing)
        {
            alertDialog?.dismiss()
        }
        val activity = context as Activity
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.progress_view, null)
        dialogBuilder.setView(dialogView)

        val progressMessage = dialogView.findViewById<View>(R.id.progressMessage_tv) as TextView
        val parentRelative = dialogView.findViewById<View>(R.id.progressView_parentRv) as RelativeLayout
        parentRelative.visibility=View.VISIBLE

        alertDialog= dialogBuilder.create()
        alertDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        progressMessage.setText(message)

        activity.getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        alertDialog?.show()

    }

    fun hideAlert()
    {

        if(alertDialog!=null&& alertDialog!!.isShowing)
            alertDialog?.dismiss()

        val activity = context as Activity
        activity.runOnUiThread(Runnable {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        })


    }



}