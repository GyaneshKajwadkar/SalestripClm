package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dcr_entry.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

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
            2 -> { subtitleFull=subtitleMessage}
        }

        val activity = context as Activity
        val dialogBuilder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

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


    fun showProgressAlert(message: String)
    {
        if(alertDialog!=null&& alertDialog!!.isShowing)
        {
            alertDialog?.dismiss()
        }
        val activity = context as Activity
        activity.runOnUiThread {
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
    }

    fun hideAlert()
    {
        if(alertDialog!=null&& alertDialog!!.isShowing)
            alertDialog?.dismiss()
            alertDialog?.cancel()

        val activity = context as Activity
        activity.runOnUiThread(Runnable {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        })
    }
    //network alert
    fun networkAlert() {
        val activity = context as Activity
        activity.runOnUiThread{
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

    }

    fun commonAlert(headerString: String, message: String) {
        context as Activity

        context.runOnUiThread{
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


    fun commonAlertWithRunnable(headerString: String, message: String,r: Runnable) {
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
            retunDialog=true
            r.run()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    fun createDCRAlert(routeId: String, routeNameData: String)
    { var activityId=0; var startingStation=0; var endingStation=0; var fieldStaffId=0
        context as Activity

        val generalClass=GeneralClass(context)
        val dialogBuilder = AlertDialog.Builder(context);
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dcr_entry, null)
        dialogBuilder.setView(dialogView); dialogBuilder.setCancelable(false); val alertDialog = dialogBuilder.create()

        val headerText = dialogView.findViewById<View>(R.id.doctorName_tv) as TextView
        val cancelImag = dialogView.findViewById<View>(R.id.back_iv) as ImageView
        val toggleSwitch = dialogView.findViewById<View>(R.id.toggleSwitch) as Switch
        val routeName = dialogView.findViewById<TextView>(R.id.doctorName_tv) as TextView
        val selectActivityHeader = dialogView.findViewById<View>(R.id.selectActivityHeader) as TextView

        headerText.setText("New DCR")
        dialogView.dcr_date_tv.setText(GeneralClass(context).getCurrentDate())
        cancelImag.setOnClickListener({alertDialog.dismiss()})

        var fieldWorkingList= arrayOf("Select","HQ ","Ex Station","Out Station")
        routeName.setText("Route detail- "+routeNameData)
        routeName.textSize= 14F


        val firstFilter= CommonListGetClass().getNonRouteListForSpinner().filter { s -> (s.routeId != -7)  }
        val secondFilter= firstFilter.filter { s -> (s.routeId != -11)  }
        val thirdFilter= secondFilter.filter { s -> (s.routeId != -6)  }

        val adapterRoute: ArrayAdapter<SyncModel.Data.Route> = ArrayAdapter<SyncModel.Data.Route>(context,
            android.R.layout.simple_spinner_dropdown_item, thirdFilter)
        dialogView.activity_spin.setAdapter(adapterRoute)

        val startEndRoute: ArrayAdapter<SyncModel.Data.Route> = ArrayAdapter<SyncModel.Data.Route>(context,
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getRouteListForSpinner())
        dialogView.startingStation_spin.setAdapter(startEndRoute)
        dialogView.ending_spin.setAdapter(startEndRoute)

        val workingWithList: ArrayAdapter<SyncModel.Data.WorkingWith> = ArrayAdapter<SyncModel.Data.WorkingWith>(context,
            android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getAccListForSpinner())
        dialogView.accomp_spin.setAdapter(workingWithList)

        val adapterField: ArrayAdapter<String> = ArrayAdapter<String>(context,
            android.R.layout.simple_spinner_dropdown_item, fieldWorkingList)
        dialogView.workingArea_spin.setAdapter(adapterField)

        if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
        {
            dialogView.managerParent_ll.visibility=View.VISIBLE
        }



        dialogView.workingArea_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position==3)
                    dialogView.startEndParent.visibility=View.VISIBLE
                else
                    if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN" && toggleSwitch.isChecked)
                    {
                        dialogView.startEndParent.visibility=View.VISIBLE

                    } else dialogView.startEndParent.visibility=View.GONE
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.startingStation_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                startingStation = CommonListGetClass().getRouteListForSpinner()[position].routeId!!
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.ending_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0)
                    endingStation = CommonListGetClass().getRouteListForSpinner()[position].routeId!!

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.accomp_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0)
                    fieldStaffId = CommonListGetClass().getAccListForSpinner()[position].empId!!

            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })

        dialogView.activity_spin.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if(position!=0) {
                    activityId =
                        CommonListGetClass().getNonRouteListForSpinner()[position].routeId!!
                    if(toggleSwitch.isChecked) dialogView.additionalParent.visibility=View.VISIBLE

                }
                else{
                    if(toggleSwitch.isChecked) dialogView.additionalParent.visibility=View.GONE
                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        })


        toggleSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) { selectActivityHeader.setText("Additional activity")
                if(!dialogView.activity_spin.getSelectedItem().toString().equals("Select")){dialogView.additionalParent.visibility=View.VISIBLE}
                if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
                {
                    dialogView.managerParent_ll.visibility=View.VISIBLE
                    dialogView.startEndParent.visibility=View.VISIBLE
                }
            }
            else{
                dialogView.startEndParent.visibility=View.GONE
                dialogView.managerParent_ll.visibility=View.GONE
                selectActivityHeader.setText("Select activity")
                dialogView.additionalParent.visibility=View.GONE
            }
            if( dialogView.workingArea_spin.getSelectedItem().toString().lowercase().equals("out station")) dialogView.startEndParent.visibility=View.VISIBLE
            else dialogView.startEndParent.visibility=View.GONE

        }

        dialogView.save_btn.setOnClickListener({
            val activitySeletd: String = dialogView.activity_spin.getSelectedItem().toString()
            val endingSeletd: String = dialogView.ending_spin.getSelectedItem().toString()
            val workAreaSeletd: String = dialogView.workingArea_spin.getSelectedItem().toString()
            val startingSeletd: String = dialogView.startingStation_spin.getSelectedItem().toString()
            val accompaniedstr: String = dialogView.accomp_spin.getSelectedItem().toString()

            if(workAreaSeletd.equals("Select")) {generalClass?.showSnackbar(it,"Working area not selected"); return@setOnClickListener}
            if(activitySeletd.equals("Select") && !toggleSwitch.isChecked) {generalClass?.showSnackbar(it,"Activity not selected"); return@setOnClickListener}
            if(startingSeletd.equals("Select") && dialogView.startEndParent.visibility==View.VISIBLE) {generalClass?.showSnackbar(it,"Start station not selected"); return@setOnClickListener}
            if(endingSeletd.equals("Select") && dialogView.startEndParent.visibility==View.VISIBLE) {generalClass?.showSnackbar(it,"End station not selected"); return@setOnClickListener}

            if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
            {
                if(dialogView.objDayEt.text.isEmpty() && toggleSwitch.isChecked) {generalClass?.showSnackbar(it,"Objective of day is empty"); return@setOnClickListener}
              //  if(dialogView.fieldStaffEt.text.isEmpty() && toggleSwitch.isChecked) {generalClass?.showSnackbar(it,"Field staff is empty"); return@setOnClickListener}
            }

            if(dialogView.remarkEt.text.isEmpty() && !toggleSwitch.isChecked) {generalClass?.showSnackbar(it,"Remark is empty"); return@setOnClickListener}
            if(dialogView.additionalEt.text.isEmpty() && toggleSwitch.isChecked && !activitySeletd.equals("Select")) {generalClass?.showSnackbar(it,"Additional activity remark is empty"); return@setOnClickListener}
            val i: Int = workAreaSeletd.indexOf(' ')

            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]

            val commonSaveDcrModel= CommonModel.SaveDcrModel()
            commonSaveDcrModel.dcrDate= generalClass?.currentDateMMDDYY().toString()
            commonSaveDcrModel.empId= HomePage.loginModelHomePage.empId?:0
            commonSaveDcrModel.employeeId= HomePage.loginModelHomePage.empId?:0
            commonSaveDcrModel.workingType=workAreaSeletd.substring(0, i).toString().uppercase()
            commonSaveDcrModel.remark=dialogView.remarkEt.text.toString()
            commonSaveDcrModel.routeId=routeId
            commonSaveDcrModel.routeName= routeNameData
            commonSaveDcrModel.monthNo=month+1
            commonSaveDcrModel.year=year
            commonSaveDcrModel.dayCount="0"

            if(toggleSwitch.isChecked)
            {
                commonSaveDcrModel.additionalActivityId=activityId
                commonSaveDcrModel.additionalActivityName=activitySeletd
            }
            else
            {
                commonSaveDcrModel.OtherDCR=activityId
            }
            commonSaveDcrModel.dayCount="0"
            commonSaveDcrModel.additionalActivityRemark=dialogView.additionalEt.text.toString()
            commonSaveDcrModel.dcrType=if(toggleSwitch.isChecked) 0  else 1

            if(SplashActivity.staticSyncData?.settingDCR?.roleType=="MAN")
            {
                commonSaveDcrModel.accompaniedWith=fieldStaffId
                commonSaveDcrModel.objectiveOfDay=dialogView.objDayEt.text.toString()
                commonSaveDcrModel.feedBack=dialogView.fieldStaffEt.text.toString()
            }

            if(dialogView.startEndParent.visibility==View.VISIBLE)
            {
                commonSaveDcrModel.startingStation=startingStation
                commonSaveDcrModel.endingStation=endingStation
            }
            saveDCR_API(commonSaveDcrModel,alertDialog,toggleSwitch.isChecked)
        })
        alertDialog.show()
    }

    fun saveDCR_API(dcrObject: CommonModel.SaveDcrModel, alertDialog: AlertDialog, checked: Boolean) {
        context as Activity
        showProgressAlert("")
        var call: Call<JsonObject> = HomePage.apiInterface?.saveDCS("bearer " + HomePage.loginModelHomePage.accessToken,dcrObject) as Call<JsonObject>
        call.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                hideAlert()
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val jsonObjError: JsonObject = response.body()?.get("errorObj") as JsonObject
                    if(!jsonObjError.get("errorMessage").asString.isEmpty())
                    {
                        commonAlert("",jsonObjError.get("errorMessage").asString)
                    }
                    else {
                        val jsonObjData: JsonObject = response.body()?.get("data") as JsonObject

                        if(!checked)
                        {
                            commonAlert("",jsonObjData.get("message").asString + "And kindly moved to Salestrip to submit DCR")
                        }
                        else
                        {
                            commonAlert("",jsonObjData.get("message").asString)
                        }

                        if(!checked) PreferenceClass(context).setPref("otherActivitySelected","1")

                        alertDialog.cancel()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                GeneralClass(context).checkInternet()
                hideAlert()
                alertDialog.cancel()// check internet connection
                call.cancel()
            }
        })
    }

}