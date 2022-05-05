package `in`.processmaster.salestripclm.activity

import DocManagerModel
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.HomePage.Companion.apiInterface
import `in`.processmaster.salestripclm.activity.HomePage.Companion.loginModelHomePage
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.alertDialogNetwork
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.connectivityChangeReceiver
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.fragments.HomeFragment
import `in`.processmaster.salestripclm.models.*
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
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
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import us.zoom.sdk.*
import java.lang.ref.WeakReference


open class BaseActivity : AppCompatActivity(){

    var alertDialog: AlertDialog? = null
    var sharePreferanceBase: PreferenceClass?= null
    var dbBase= DatabaseHandler(this)
    var zoomSDKBase: ZoomSDK? = null
    val generalClass=GeneralClass(this)
    val alertClass=AlertClass(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        connectivityChangeReceiver= ConnectivityChangeReceiver()
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

    fun disableNetworkAlert(activity: Activity) {
        if (alertDialogNetwork != null) { alertDialogNetwork?.dismiss() }

        //check if db have edetailing and send to network when internet enable
        dbBase= DatabaseHandler(activity)
        sharePreferanceBase = PreferenceClass(activity)
        val eDetailingArray=dbBase.getAllSaveSend("feedback")
        val retailerArray=dbBase.getAllSaveSend("retailerFeedback")

        if( eDetailingArray.size!=0)
        {
            val coroutineScope= CoroutineScope(Dispatchers.IO).launch {
                val sendEdetailing= async {
                    submitDCRCo() }
                sendEdetailing.await()
            }
            coroutineScope.invokeOnCompletion {
                AlertClass(activity).commonAlert("","Offline DCR submit successfully")

                if (activity is HomePage) {
                    if(activity.getFragmentRefreshListener()!=null){
                        activity?.getFragmentRefreshListener()?.onRefresh()
                    }
                }

            }
        }

        if(retailerArray.size!=0)
        {
            val coroutineScope= CoroutineScope(Dispatchers.IO).launch {
                val sendEdetailing= async {
                    submitDCRRetailer() }
                sendEdetailing.await()
            }
            coroutineScope.invokeOnCompletion {
                AlertClass(activity).commonAlert("","Offline DCR submit successfully")

                if (activity is HomePage) {
                    if(activity.getFragmentRefreshListener()!=null){
                        activity?.getFragmentRefreshListener()?.onRefresh()
                    }
                }

            }
        }
    }

    //check permission
    fun dexterPermission(context: Context) {
        Dexter.withContext(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) { }
                    else { permissionDenied_Dialog(context, false) }

                   /* if (report.isAnyPermissionPermanentlyDenied()) {
                        // permission is denied permenantly, navigate user to app settings
                        permissionDenied_Dialog(context, true) }*/
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) { token.continuePermissionRequest() }
            })
            .onSameThread()
            .check()
    }

    //permission denied dialog
    fun permissionDenied_Dialog(context: Context, setButtonText: Boolean) {
        if (alertDialog != null) {
            if (alertDialog?.isShowing == true) {
                alertDialog?.dismiss()
            }
        }
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.permissiondenied, null)
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create()
        alertDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog?.setCanceledOnTouchOutside(false)

        val ok_btn = dialogView.findViewById<View>(R.id.ok_btn) as AppCompatButton
        val retry_btn = dialogView.findViewById<View>(R.id.retry_btn) as AppCompatButton

        if (setButtonText) {
            retry_btn.setText("Setting")
        }

        retry_btn.setOnClickListener {
            if (setButtonText) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            } else { dexterPermission(context) }
            alertDialog?.dismiss()
        }
        ok_btn.setOnClickListener {
            this.finish();
            System.exit(0);
            alertDialog?.dismiss() }
        alertDialog?.show()
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
        alertClass.showProgressAlert("")

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
                    for(singleItem in getTeamslist?.getData()?.employeeList!!)
                    {
                        var selectorModel =DocManagerModel()
                        selectorModel.name=singleItem.fullName
                        selectorModel.routeName= singleItem.headQuaterName
                        selectorModel.specialityName= singleItem.divisionName
                        selectorModel.id= singleItem.empId
                        selectorModel.mailId= singleItem.emailId
                        getResponseList.add(selectorModel)
                    }
                }
                else
                {
                    Toast.makeText(this@BaseActivity, "Server error ", Toast.LENGTH_SHORT).show()
                    return
                }
                alertClass.hideAlert()

            }

            override fun onFailure(call: Call<TeamsModel?>, t: Throwable?) {
                call.cancel()
                alertClass.hideAlert()
            }
        })
        return getResponseList
    }

    suspend fun getSheduleMeetingAPI()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).getScheduledMeetingCoo("bearer " + loginModelHomePage.accessToken,loginModelHomePage.empId.toString())
            }
        withContext(Dispatchers.Main) {
            Log.e("getScheduleAPI",response.toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    val gson = Gson()
                    var model = response.body()
                    dbBase?.addAPIData(gson.toJson(model), 2) }
                else
                { Log.e("elseGetScheduled", response.code().toString()) }
            }
            else
            {   Log.e("scheduleERROR", response?.errorBody().toString()) }
        }

    }

    suspend fun getCredientailAPI(context: Activity)
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).getZoomCredientailCoo("bearer " + loginModelHomePage.accessToken,loginModelHomePage.empId.toString())
            }
        withContext(Dispatchers.Main) {
            Log.e("getScheduleAPIII",response.toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage=="")
                {
                    var model = response.body()
                    ZoomInitilizeClass().initilizeZoom(context as Activity,model)
                    sharePreferanceBase?.setPrefBool("zoomCrediential", true)
                }
                else
                { Log.e("elseGetCrediential", response.code().toString())
                    sharePreferanceBase?.setPrefBool("zoomCrediential", false)}
            }
            else
            { Log.e("scheduleERROR", response?.errorBody().toString())
                sharePreferanceBase?.setPrefBool("zoomCrediential", false)}
        } }

    fun getCredientail_api(context: Activity) {

        var call: Call<ZoomCredientialModel> = apiInterface?.getZoomCredientail("bearer " + loginModelHomePage.accessToken, loginModelHomePage.empId
        ) as Call<ZoomCredientialModel>
        call.enqueue(object : Callback<ZoomCredientialModel?> {
            override fun onResponse(call: Call<ZoomCredientialModel?>?, response: Response<ZoomCredientialModel?>) {
                Log.e("getcrediential_api", response.code().toString() + "")
                if (response.code() == 200 && response.body().toString().isEmpty())
                {
                    var model = response.body()
                    sharePreferanceBase?.setPrefBool("zoomCrediential", true)
                    ZoomInitilizeClass().initilizeZoom(context as Activity,model)
                }
                else
                { Log.e("elseGetCrediential", response.code().toString())
                    sharePreferanceBase?.setPrefBool("zoomCrediential", false)
                }
            }

            override fun onFailure(call: Call<ZoomCredientialModel?>, t: Throwable?) {
                Log.e("failGetCrediential",t?.message.toString())
                sharePreferanceBase?.setPrefBool("zoomCrediential", false)

                call.cancel()
            } }) }

    suspend fun submitDCRCo()
    {

        val eDetailingArray=dbBase.getAllSaveSend("feedback")
        if(eDetailingArray.size==0)
        { getDocCallAPI()
            return }


        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).submitEdetailingApiCoo("bearer " + loginModelHomePage.accessToken,eDetailingArray.get(0))
            }
        withContext(Dispatchers.Main) {
            if (response?.isSuccessful == true)
            {
                //   Log.e("dfsdgtrertef", response.body().toString())
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    if(response.body()?.getErrorObj()?.errorMessage==null || !response.body()?.getErrorObj()?.errorMessage.toString().isEmpty())
                    {
                        response.body()?.getErrorObj()?.errorMessage?.let { Log.e("errorOnSubmitEDetailing", it) }
                    }
                    else {
                        Log.e("getSubmitEdetailingData", response.body().toString())
                        eDetailingArray.get(0).doctorId?.let { dbBase.deleteSaveSend(it) }
                        submitDCRCo()
                    }
                }
                else Log.e("elsesubmitDCRCoAPI", response.code().toString())
            }
            else Log.e("submitDCRCoAPIERROR", response?.errorBody().toString())
        }
    }

    suspend fun submitDCRRetailer()
    {

        val dcrRetailerList=dbBase.getAllSaveSendRetailer("retailerFeedback")
        if(dcrRetailerList.size==0)
        {
            getDocCallAPI()
            return
        }
        val sendRetailerDcr: ArrayList<DailyDocVisitModel.Data.DcrDoctor> = ArrayList()
         sendRetailerDcr.add(dcrRetailerList.get(0))

        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).retailerSendApiCoo("bearer " + loginModelHomePage.accessToken,sendRetailerDcr)
            }
        withContext(Dispatchers.Main) {
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    if(response.body()?.getErrorObj()?.errorMessage==null || !response.body()?.getErrorObj()?.errorMessage.toString().isEmpty())
                    {
                        response.body()?.getErrorObj()?.errorMessage?.let { Log.e("errorOnSubmitEDetailing", it) }
                    }
                    else {
                           Log.e("getSubmitEdetailingData", response.body().toString())
                        dcrRetailerList.get(0).retailerId?.let { dbBase.deleteSaveSend(it) }
                        submitDCRRetailer()
                    }
                }
                else Log.e("elsesubmitDCRCoAPI", response.code().toString())
            }
            else Log.e("submitDCRCoAPIERROR", response?.errorBody().toString())
        }
    }

     suspend fun getDocCallAPI()
    {
        val response =
            sharePreferanceBase?.getPref("secondaryUrl")?.let {
                APIClientKot().getUsersService(2, it
                ).dailyDocCallApi("bearer " + loginModelHomePage.accessToken,generalClass.currentDateMMDDYY())
            }
        withContext(Dispatchers.Main) {
            Log.e("getDocCallAPI", response?.body().toString())
            if (response?.isSuccessful == true)
            {
                if (response.code() == 200 && response.body()?.getErrorObj()?.errorMessage?.isEmpty() == true) {
                    var model = response.body()
                    if(model?.getData()?.dcrDoctorlist?.size==0)
                    {
                        dbBase.deleteAllDoctorEdetailing()
                    }

                    dbBase?.addAPIData(Gson().toJson(model?.getData()), 5)
                }
                else Log.e("elsegetDocCallAPI", response.code().toString())
            }
            else Log.e("getDocCallAPIERROR", response?.errorBody().toString())
        }
    }



}