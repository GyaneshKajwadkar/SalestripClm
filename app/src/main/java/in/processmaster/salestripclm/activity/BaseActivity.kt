package `in`.processmaster.salestripclm.activity

import DocManagerModel
import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.alertDialogNetwork
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.connectivityChangeReceiver
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import `in`.processmaster.salestripclm.utils.ZoomInitilizeClass
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.progress_view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import us.zoom.sdk.*
import java.lang.Exception
import java.lang.ref.WeakReference

open class BaseActivity : AppCompatActivity()/*, UserLoginCallback.ZoomDemoAuthenticationListener ,
    MeetingServiceListener, InitAuthSDKCallback*/ {

    var alertDialog: AlertDialog? = null
    var sharePreferanceBase: PreferenceClass?= null
    var dbBase= DatabaseHandler(this)
    var zoomSDKBase: ZoomSDK? = null
    val generalClass=GeneralClass(this)
    val alertClass=AlertClass(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        connectivityChangeReceiver=
            ConnectivityChangeReceiver()

        zoomSDKBase = ZoomSDK.getInstance()
        sharePreferanceBase = PreferenceClass(this)

    }


    fun createConnectivity(context: Context)
    {
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        context.registerReceiver(connectivityChangeReceiver, intentFilter)
    }

    //network alert
    fun networkAlertNotification(context: Activity) {

        val loginActivityWeakRef: WeakReference<Activity> = WeakReference<Activity>(context)

        if (loginActivityWeakRef.get()?.isFinishing() == false) {
            val dialogBuilder = AlertDialog.Builder(context)
            val inflater = context.layoutInflater
            val dialogView: View = inflater.inflate(R.layout.networkalert, null)
            dialogBuilder.setView(dialogView)

            alertDialogNetwork = dialogBuilder.create()
            alertDialogNetwork?.getWindow()
                ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val okBtn_rl =
                dialogView.findViewById<View>(R.id.okBtn_rl) as RelativeLayout

            okBtn_rl.setOnClickListener {
                alertDialogNetwork?.dismiss()
            }

            alertDialogNetwork?.show()
        }


    }

    fun disableNetworkAlert() {
        if (alertDialogNetwork != null) {

            alertDialogNetwork?.dismiss()
        }

    }

    //check permission
    fun dexterPermission(context: Context) {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {

                    } else {
                        permissionDenied_Dialog(context, false)
                    }


                    if (report.isAnyPermissionPermanentlyDenied()) {
                        // permission is denied permenantly, navigate user to app settings
                        permissionDenied_Dialog(context, true)
                    }


                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()
    }

    //permission denied dialog
    fun permissionDenied_Dialog(context: Context, setButtonText: Boolean) {
        if (alertDialog != null) {
            if (alertDialog?.isShowing!!) {
                alertDialog!!.dismiss()
            }
        }
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.permissiondenied, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.setCanceledOnTouchOutside(false)

        val ok_btn =
            dialogView.findViewById<View>(R.id.ok_btn) as AppCompatButton
        val retry_btn =
            dialogView.findViewById<View>(R.id.retry_btn) as AppCompatButton

        if (setButtonText) {
            retry_btn.setText("Setting")
        }

        retry_btn.setOnClickListener {

            if (setButtonText) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } else {
                dexterPermission(context)
            }

            alertDialog!!.dismiss()
        }
        ok_btn.setOnClickListener {
            alertDialog!!.dismiss()
        }

        alertDialog!!.show()
    }

    //check version
    open fun checkForUpdateLower(existingVersion: String, lowerVersion: String): Boolean {
        var existingVersion = existingVersion
        var lowerVersion = lowerVersion
        if (existingVersion.isEmpty() || lowerVersion.isEmpty()) {
            return false
        }
        existingVersion = existingVersion.replace("\\.".toRegex(), "")
        lowerVersion = lowerVersion.replace("\\.".toRegex(), "")
        val existingVersionLength = existingVersion.length
        val newVersionLength = lowerVersion.length
        val versionBuilder = StringBuilder()
        if (newVersionLength > existingVersionLength) {
            versionBuilder.append(existingVersion)
            for (i in existingVersionLength until newVersionLength) {
                versionBuilder.append("0")
            }
            existingVersion = versionBuilder.toString()
        } else if (existingVersionLength > newVersionLength) {
            versionBuilder.append(lowerVersion)
            for (i in newVersionLength until existingVersionLength) {
                versionBuilder.append("0")
            }
            lowerVersion = versionBuilder.toString()
        }


        //if lowerVersion is greater give true
        return lowerVersion.toInt() >= existingVersion.toInt()
    }

    open fun checkForUpdateHigher(existingVersion: String, higherVersion: String): Boolean {
        var existingVersion = existingVersion
        var higherVersion = higherVersion
        if (existingVersion.isEmpty() || higherVersion.isEmpty()) {
            return false
        }
        existingVersion = existingVersion.replace("\\.".toRegex(), "")
        higherVersion = higherVersion.replace("\\.".toRegex(), "")
        val existingVersionLength = existingVersion.length
        val newVersionLength = higherVersion.length
        val versionBuilder = StringBuilder()
        if (newVersionLength > existingVersionLength) {
            versionBuilder.append(existingVersion)
            for (i in existingVersionLength until newVersionLength) {
                versionBuilder.append("0")
            }
            existingVersion = versionBuilder.toString()
        } else if (existingVersionLength > newVersionLength) {
            versionBuilder.append(higherVersion)
            for (i in newVersionLength until existingVersionLength) {
                versionBuilder.append("0")
            }
            higherVersion = versionBuilder.toString()
        }

        return higherVersion.toInt() <= existingVersion.toInt()
    }

    fun stopConnectivity(activity: Activity)
    {
        try {
            activity.unregisterReceiver(connectivityChangeReceiver);
        } catch (e: Exception)
        { // already unregistered
        }
    }

    //=========================================API Calling Section===========================================


    //Get teams api
    fun getTeamsApi( context:Activity, progressmessage:String) : ArrayList<DocManagerModel>
    {
        progressMessage_tv?.setText(progressmessage)
        GeneralClass(this).enableProgress(progressView_parentRv!!)
        var getResponseList=ArrayList<DocManagerModel>()

        var call: Call<TeamsModel> = apiInterface?.getTeamsMember(
            "bearer " + loginModelHomePage.accessToken,
            loginModelHomePage.empId.toString()
        ) as Call<TeamsModel>

        call.enqueue(object : Callback<TeamsModel?> {
            override fun onResponse(
                call: Call<TeamsModel?>?,
                response: Response<TeamsModel?>
            ) {
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var getTeamslist=response.body()
                    for(singleItem in getTeamslist?.data?.employeeList!!)
                    {
                        var selectorModel =DocManagerModel()
                        selectorModel.setName(singleItem.firstName+" "+singleItem.lastName)
                        selectorModel.setRoute(singleItem.headQuaterName)
                        selectorModel.setSpeciality(singleItem.divisionName)
                        selectorModel.setId(singleItem.empId)
                        selectorModel.setMailId(singleItem.emailId)


                        getResponseList.add(selectorModel)
                    }
                }
                else
                {
                    Toast.makeText(this@BaseActivity, "Server error ", Toast.LENGTH_SHORT).show()
                    return
                }
                GeneralClass(context).disableProgress(progressView_parentRv!!)

            }

            override fun onFailure(call: Call<TeamsModel?>, t: Throwable?) {
                call.cancel()
                GeneralClass(context).disableProgress(progressView_parentRv!!)

            }
        })
        return getResponseList
    }

    suspend fun getSheduleMeetingAPI()
    {
        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).getScheduledMeetingCoo("bearer " + loginModelHomePage.accessToken,loginModelHomePage.empId.toString())
        withContext(Dispatchers.Main) {
            Log.e("getScheduleAPI",response.toString())
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val gson = Gson()
                    var model = response.body()
                    dbBase?.addAPIData(gson.toJson(model),2,)
                    Log.e("theScheduledApiModel",model?.getData()?.meetingList?.size.toString())

                }
                else
                {
                    Log.e("elseGetScheduled", response.code().toString())
                }
            }
            else
            {   Log.e("scheduleERROR", response.errorBody().toString())
            }
        }

    }




//     fun getsheduledMeeting_api(): String? {
//
//        progressMessage_tv?.setText("Please wait")
//
//
//        var call: Call<GetScheduleModel> = getSecondaryApiInterface().getScheduledMeeting("bearer " + loginModelBase?.accessToken,loginModelBase.empId.toString()) as Call<GetScheduleModel>
//        call.enqueue(object : Callback<GetScheduleModel?> {
//            override fun onResponse(call: Call<GetScheduleModel?>?, response: Response<GetScheduleModel?>) {
//                Log.e("getscheduled_api", response.code().toString() + "")
//                if (response.code() == 200 && !response.body().toString().isEmpty()) {
//                    val gson = Gson()
//                    var model = response.body()
//                    dbBase?.insertOrUpdateAPI("1",gson.toJson(model))
//                }
//                else
//                {
//                    Log.e("elseGetScheduled", response.code().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<GetScheduleModel?>, t: Throwable?) {
//                call.cancel()
//            }
//        })
//
//        return dbBase.getApiDetail(1)
//    }

    suspend fun getCredientailAPI(context: Activity)
    {
        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).getZoomCredientailCoo("bearer " + loginModelHomePage.accessToken,loginModelHomePage.empId.toString())
        withContext(Dispatchers.Main) {
            Log.e("getScheduleAPIII",response.toString())
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage=="")
                {
                    var model = response.body()
                    ZoomInitilizeClass().initilizeZoom(context as Activity,model)
                }
                else
                {
                    Log.e("elseGetCrediential", response.code().toString())
                }
            }
            else
            {
                Log.e("scheduleERROR", response.errorBody().toString())
            }
        }
    }


    fun getCredientail_api(context: Activity) {

        var call: Call<ZoomCredientialModel> = apiInterface?.getZoomCredientail("bearer " + loginModelHomePage.accessToken,loginModelHomePage.empId.toString()) as Call<ZoomCredientialModel>
        call.enqueue(object : Callback<ZoomCredientialModel?> {
            override fun onResponse(call: Call<ZoomCredientialModel?>?, response: Response<ZoomCredientialModel?>) {
                Log.e("getcrediential_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    var model = response.body()
                    ZoomInitilizeClass().initilizeZoom(context as Activity,model)
                }
                else
                {
                    Log.e("elseGetCrediential", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ZoomCredientialModel?>, t: Throwable?) {
                Log.e("failGetCrediential",t?.message.toString())
                call.cancel()
            }
        })

    }

    suspend fun submitDCRCo()
    {
        val stringArray=dbBase.getAllSaveSend("feeback")
        if(stringArray.size<=0 )return

        val saveModel=Gson().fromJson(stringArray.get(0),Send_EDetailingModel::class.java)

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).submitEdetailingApiCoo("bearer " + loginModelHomePage.accessToken,saveModel)
        withContext(Dispatchers.Main) {
            Log.e("submitDCRCoAPI",response.toString())
            if (response!!.isSuccessful)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val jsonObjError: JsonObject = response.body()?.get("errorObj") as JsonObject
                    if(!jsonObjError.get("errorMessage").asString.isEmpty())
                    {
                        Log.e("errorOnSubmitEDetailing",jsonObjError.get("errorMessage").asString)
                        //alertClass?.commonAlert("",jsonObjError.get("errorMessage").asString)
                    }
                    else {
                        dbBase.deleteSaveSend(saveModel.dcrId!!)
                        submitDCRCo();
                    } }
                else Log.e("elsesubmitDCRCoAPI", response.code().toString())
            }
            else Log.e("submitDCRCoAPIERROR", response.errorBody().toString())

        }

    }

}