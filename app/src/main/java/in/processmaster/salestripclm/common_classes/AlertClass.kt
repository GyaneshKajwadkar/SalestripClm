package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar

class AlertClass(val context : Context)
{
    companion object {
        public var retunDialog=false
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

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Close", View.OnClickListener { })
            .setActionTextColor(Color.YELLOW)
            .show()
    }

}