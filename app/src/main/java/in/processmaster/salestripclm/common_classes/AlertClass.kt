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
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class AlertClass(val context : Context)
{
    companion object {
         var retunDialog=false
        var alertDialog: AlertDialog? =null
    }

   fun twoButtonAlert(
       cancelButtonText: String = "cancel", mainButtonText: String, subTitleInput: Int,
       subtitleMessage: String, mainHeading: String, r: Runnable
   )
    {
        retunDialog=false
        var subtitleFull=""
        when(subTitleInput)
        {
            1-> { subtitleFull="Are you sure you want to $subtitleMessage" }
        }

        val activity = context as Activity
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val message_tv = dialogView.findViewById<View>(R.id.message_tv) as TextView
        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton
        cancel_btn.setText(cancelButtonText)
        exit_btn.setText(mainButtonText)
        message_tv.setText(subtitleFull)
        mainHeading_tv.setText(mainHeading)

        exit_btn.setOnClickListener{
            retunDialog=true
            r.run()
            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener{
            retunDialog=false
            alertDialog.dismiss()
        }
        alertDialog.show()

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
        dialogBuilder.setCancelable(false)
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
    //network alert
    fun networkAlert() {
        val activity = context as Activity
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.networkalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val okBtn_rl = dialogView.findViewById<View>(R.id.okBtn_rl) as RelativeLayout

        okBtn_rl.setOnClickListener {

            if(context?.javaClass?.simpleName.toString().equals("SplashActivity"))
            {
                activity.finish()
            }
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    fun commonAlert(headerString: String, message: String) {
        context as Activity
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
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

        okBtn_rl.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }



}