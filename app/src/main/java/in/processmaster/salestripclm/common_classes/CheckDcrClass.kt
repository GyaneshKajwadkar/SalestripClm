package `in`.processmaster.salestripclm.common_classes

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity
import `in`.processmaster.salestripclm.activity.SubmitE_DetailingActivity
import `in`.processmaster.salestripclm.fragments.NewCallFragment
import `in`.processmaster.salestripclm.models.CommonModel
import `in`.processmaster.salestripclm.models.GetDcrToday
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.dcr_entry.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class CheckDcrClass(val activity: Activity?, val callingName : String, val additionalData : String ="") {

    val generalClass= activity?.let { GeneralClass(it) }
    val alertClass= AlertClass(activity)
    val sharePreferanceBase= PreferenceClass(activity)

    fun checkDCR_UsingSP(onMenuItemClickListener: MenuItem?=null)
    :Boolean
    {
        if(generalClass?.isInternetAvailable() == true)
        {
            alertClass.showProgressAlert("")
            checkCurrentDCR_API(onMenuItemClickListener)
            return false
        }

        else if(sharePreferanceBase?.checkKeyExist("empIdSp")==false  ||
            sharePreferanceBase?.checkKeyExist("todayDate")==false  || sharePreferanceBase?.checkKeyExist("dcrId")==false || sharePreferanceBase?.getPref("empIdSp") != loginModelHomePage.empId.toString())
        {
            alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")
            if(callingName.equals("homeActivity"))
            {
                onMenuItemClickListener?.setCheckable(false)
                onMenuItemClickListener?.setChecked(false)
            }
            return  false
        }
        else if( sharePreferanceBase?.getPref("todayDate") != generalClass?.currentDateMMDDYY() || sharePreferanceBase?.getPref("dcrId")=="0") {
            alertClass?.commonAlert("Alert!","DCR not submitted please connect to internet and fill DCR first.")

           if(callingName.equals("homeActivity"))
           {
               onMenuItemClickListener?.setCheckable(false)
               onMenuItemClickListener?.setChecked(false)
           }

            return  false
        }
        else{
            if(callingName.equals("homeActivity"))
            {
                onMenuItemClickListener?.setCheckable(true)
                onMenuItemClickListener?.setChecked(true)
                val context=   activity as? HomePage
                context?.onDcrChecked()
            }
            return true
        }
    }

    fun checkCurrentDCR_API(onMenuItemClickListener: MenuItem?=null) {

        if(callingName.equals("homeActivity"))
        {
            onMenuItemClickListener?.setCheckable(false)
            onMenuItemClickListener?.setChecked(false)
        }

        val  apiInterface= APIClientKot().getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<GetDcrToday>? = apiInterface?.checkDCR__API(
            "bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId,
            generalClass?.currentDateMMDDYY()
        )
        call?.enqueue(object : Callback<GetDcrToday?> {
            override fun onResponse(call: Call<GetDcrToday?>?, response: Response<GetDcrToday?>) {
                Log.e("checkCC_api", response.code().toString() + "")
                alertClass.hideAlert()

                if (response.body().toString().isEmpty() || response.body()?.errorObj?.errorMessage?.isEmpty()==false) {
                    alertClass.commonAlert("","Something went wrong please try again later")
                } else {

                    val dcrData=response.body()?.data?.dcrData

                    if(SplashActivity.staticSyncData?.settingDCR?.isCallPlanMandatoryForDCR==true && response.body()?.data?.isCPExiest == false)
                    {
                        activity?.runOnUiThread(java.lang.Runnable {
                            alertClass?.commonAlert("Alert!","Please submit your day plan first")
                        })
                        return
                    }
                    if (dcrData?.rtpApproveStatus?.lowercase() != "a") {
                        alertClass?.commonAlert("Alert!","Tour plan not approved")
                        return
                    }

                    if (dcrData?.dataSaveType?.lowercase() == "s") {

                        activity?.runOnUiThread(java.lang.Runnable {
                            alertClass?.commonAlert("Alert!","The DCR is submitted it cannot be unlocked please connect with your admin")
                        })

                        return
                    }


                    if (dcrData?.routeId.toString()=="" || dcrData?.routeId==null || dcrData?.routeId=="0") {
                        activity?.runOnUiThread {
                            alertClass?.commonAlert("Alert!", "Please submit tour program first")
                            alertClass?.hideAlert()
                        }
                        return
                    }

                    dcrData?.dataSaveType="D"
                    sharePreferanceBase?.setPref("dcrObj", Gson().toJson(dcrData))

                    if (dcrData?.dcrId == 0) {

                        activity?.runOnUiThread {
                            createDCRAlert(
                                dcrData?.routeId.toString(),
                                dcrData?.routeName.toString()
                            )
                        }
                        sharePreferanceBase?.setPref("dcrId", dcrData?.dcrId.toString())

                    } else {
                        sharePreferanceBase?.setPref("todayDate", generalClass?.currentDateMMDDYY())
                        sharePreferanceBase?.setPref("dcrId", dcrData?.dcrId.toString())
                        sharePreferanceBase?.setPref("empIdSp", HomePage.loginModelHomePage.empId.toString())

                        if(dcrData?.otherDCR!=0)
                        {
                            alertClass?.commonAlert("Alert!","You have not planned field working today. Kindly save it from Salestrip app")
                            sharePreferanceBase?.setPref("otherActivitySelected","1")
                            return
                        }
                        else{
                            if(callingName.equals("homeActivity"))
                            {
                                onMenuItemClickListener?.setCheckable(true)
                                onMenuItemClickListener?.setChecked(true)
                                val context=   activity as? HomePage
                                context?.onDcrChecked()
                            }
                            else if(callingName.equals("callFragment"))
                            {
                                NewCallFragment().getInstance()?.openCloseModel()
                            }

                            else if(callingName.equals("doctorAdapter"))
                            {
                                val intent = Intent(activity, SubmitE_DetailingActivity::class.java)
                                intent.putExtra("apiDataDcr", additionalData)
                                activity?.startActivity(intent)
                            }

                        }

                    }
                }

            }

            override fun onFailure(call: Call<GetDcrToday?>, t: Throwable?) {
                generalClass?.checkInternet()
                alertClass.hideAlert()
                call.cancel()
            }
        })
    }

    fun createDCRAlert(routeId: String, routeNameData: String,onMenuItemClickListener: MenuItem?=null)
    { var activityId=0; var startingStation=0; var endingStation=0; var fieldStaffId=0

        val generalClass= activity?.let { GeneralClass(it) }
        val dialogBuilder = activity?.let { AlertDialog.Builder(it) };
        val inflater = activity?.layoutInflater
        val dialogView: View? = inflater?.inflate(R.layout.dcr_entry, null)
        dialogBuilder?.setView(dialogView); dialogBuilder?.setCancelable(false);
        val alertDialog = dialogBuilder?.create()

        val headerText = dialogView?.findViewById<View>(R.id.doctorName_tv) as TextView
        val cancelImag = dialogView?.findViewById<View>(R.id.back_iv) as ImageView
        val toggleSwitch = dialogView.findViewById<View>(R.id.toggleSwitch) as Switch
        val routeName = dialogView.findViewById<TextView>(R.id.doctorName_tv) as TextView
        val selectActivityHeader = dialogView.findViewById<View>(R.id.selectActivityHeader) as TextView

        headerText.setText("New DCR")
        dialogView.dcr_date_tv.setText(activity?.let { GeneralClass(it).getCurrentDate() })
        cancelImag.setOnClickListener({alertDialog?.dismiss()})

        var fieldWorkingList= arrayOf("Select","HQ ","Ex Station","Out Station")
        routeName.setText("Route detail- "+routeNameData)
        routeName.textSize= 14F


        val firstFilter= CommonListGetClass().getNonRouteListForSpinner().filter { s -> (s.routeId != -7)  }
        val secondFilter= firstFilter.filter { s -> (s.routeId != -11)  }
        val thirdFilter= secondFilter.filter { s -> (s.routeId != -6)  }

        val adapterRoute: ArrayAdapter<SyncModel.Data.Route>? = activity?.let {
            ArrayAdapter<SyncModel.Data.Route>(
                it,
                android.R.layout.simple_spinner_dropdown_item, thirdFilter)
        }
        dialogView.activity_spin.setAdapter(adapterRoute)

        val startEndRoute: ArrayAdapter<SyncModel.Data.Route>? = activity?.let {
            ArrayAdapter<SyncModel.Data.Route>(
                it,
                android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getRouteListForSpinner())
        }
        dialogView.startingStation_spin.setAdapter(startEndRoute)
        dialogView.ending_spin.setAdapter(startEndRoute)

        val workingWithList: ArrayAdapter<SyncModel.Data.WorkingWith>? = activity?.let {
            ArrayAdapter<SyncModel.Data.WorkingWith>(
                it,
                android.R.layout.simple_spinner_dropdown_item, CommonListGetClass().getAccListForSpinner())
        }
        dialogView.accomp_spin.setAdapter(workingWithList)

        val adapterField: ArrayAdapter<String>? = activity?.let {
            ArrayAdapter<String>(
                it,
                android.R.layout.simple_spinner_dropdown_item, fieldWorkingList)
        }
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
            alertDialog?.let { it1 ->
                saveDCR_API(commonSaveDcrModel,
                    it1,toggleSwitch.isChecked, onMenuItemClickListener)
            }
        })
        alertDialog?.show()
    }

    fun saveDCR_API(dcrObject: CommonModel.SaveDcrModel, alertDialog: AlertDialog, checked: Boolean,onMenuItemClickListener: MenuItem?=null) {
        alertClass.showProgressAlert("")

        val  apiInterface= APIClientKot().getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)

        var call: Call<JsonObject>? = apiInterface?.saveDCS("bearer " + HomePage.loginModelHomePage.accessToken,dcrObject) as? Call<JsonObject>
        call?.enqueue(object : Callback<JsonObject?> {
            override fun onResponse(call: Call<JsonObject?>?, response: Response<JsonObject?>) {
                alertClass.hideAlert()
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val jsonObjError: JsonObject = response.body()?.get("errorObj") as JsonObject
                    if(!jsonObjError.get("errorMessage").asString.isEmpty())
                    {
                        alertClass.commonAlert("",jsonObjError.get("errorMessage").asString)
                    }
                    else {
                        val jsonObjData: JsonObject = response.body()?.get("data") as JsonObject

                        if(!checked)
                        {
                            alertClass.commonAlert("",jsonObjData.get("message").asString + "And kindly moved to Salestrip to submit DCR")
                        }
                        else
                        {
                            alertClass.commonAlert("",jsonObjData.get("message").asString)
                            if(callingName.equals("homeActivity"))
                            {
                                onMenuItemClickListener?.setCheckable(true)
                                onMenuItemClickListener?.setChecked(true)
                                val context=   activity as? HomePage
                                context?.onDcrChecked()
                            }
                            else if(callingName.equals("callFragment"))
                            {
                                NewCallFragment().getInstance()?.openCloseModel()
                            }

                            else if(callingName.equals("doctorAdapter"))
                            {
                                val intent = Intent(activity, SubmitE_DetailingActivity::class.java)
                                intent.putExtra("apiDataDcr", additionalData)
                                activity?.startActivity(intent)
                            }
                        }

                        if(!checked) sharePreferanceBase.setPref("otherActivitySelected","1")

                        alertDialog.cancel()
                    }
                }
            }

            override fun onFailure(call: Call<JsonObject?>, t: Throwable?) {
                generalClass?.checkInternet()
                alertClass.hideAlert()
                alertDialog.cancel()// check internet connection
                call.cancel()
            }
        })
    }


}