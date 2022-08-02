package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.os.*
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.*
import java.io.File
import java.lang.Runnable


class SplashActivity : BaseActivity()
{
    companion object {
        @kotlin.jvm.JvmField
        var connectivityChangeReceiver= ConnectivityChangeReceiver()
        var alertDialogNetwork: AlertDialog? = null
        var staticSyncData: SyncModel.Data? =SyncModel.Data()
        var showNetworkAlert: Boolean =true
    }
    var sharePreferance: PreferenceClass?= null
    var progressBar: ProgressBar?=null
    var apiInterface: APIInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        getSupportActionBar()?.hide()   //Hiding Toolbar

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharePreferance = PreferenceClass(this)
        progressBar=findViewById(R.id.progressBar) as ProgressBar

       /* FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token: String = task.getResult().toString()
                Log.e("TOKEN",token)
            })*/

        val getDeviceMemory= getAvailableInternalMemorySize()
        Log.e("deviceMemory",getDeviceMemory.toString())
        if(getDeviceMemory<1)
        {
            val r: Runnable = object : Runnable {
                override fun run() {
                    if(AlertClass.retunDialog) finish()
                }
            }
            alertClass.commonAlertWithRunnable("OOps...","Your device is running low on memory. Please delete some file" +
                    " and try again later",r)
            return
        }


        dbBase= DatabaseHandler.getInstance(applicationContext)



        if(sharePreferance?.getPrefBool("isLogin") == true)
        {
//         //if no internet connection and database have sync data then open directly home page
            if(!generalClass.isInternetAvailable() && dbBase?.getDatasCount()!! > 0)
            {
                generalClass.enableSimpleProgress(progressBar!!)
                val coroutineScope= CoroutineScope(Dispatchers.IO).launch {

                    val syncSmallData= async {
                        Handler(Looper.getMainLooper()).postDelayed({
                            runOnUiThread {
                                dataSync_tv.setText("Please wait.... Data sync in progress")
                                dataSync_tv.visibility= View.VISIBLE
                            }
                        },5000)

                        staticSyncData= dbBase?.getSYNCApiData(0)
                    }
                    syncSmallData.await()
                }
                coroutineScope.invokeOnCompletion {
                    this.runOnUiThread(java.lang.Runnable { generalClass.disableSimpleProgress(progressBar!!) })
                    val intent = Intent(this@SplashActivity, HomePage::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else
            {
                if(!generalClass.isInternetAvailable()){
                    alertClass.networkAlert()
                    return
                }

                generalClass.enableSimpleProgress(progressBar!!)
                val scope= lifecycleScope.launch( generalClass.coroutineExceptionHandler) {
                    try{
                        callingSyncAPI()
                    }
                    catch (e:Exception)
                    {
                        try{
                            var profileData = sharePreferance?.getPref("profileData")
                            val loginModelHomePage = Gson().fromJson(profileData, LoginModel::class.java)

                            if(generalClass.checkCurrentDateIsValid(loginModelHomePage,"splash") == false)
                            {
                                alertClass?.commonAlert("Date error","Device date is not correct. Please set it to current date")
                            }
                            else  alertClass.lowNetworkAlert()

                        }
                        catch (e: Exception){}
                    }
                    }
                scope.invokeOnCompletion {
                    this.runOnUiThread(java.lang.Runnable { generalClass.disableSimpleProgress(progressBar!!) })
                }
            }}

        else
        {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }

    }
    suspend fun callingSyncAPI()
    {
        var profileData =sharePreferance?.getPref("profileData")
        val loginModel= Gson().fromJson(profileData, LoginModel::class.java)

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).syncApiCoo("bearer " + loginModel?.accessToken)

        Handler(Looper.getMainLooper()).postDelayed({
            runOnUiThread {
                dataSync_tv.setText("Please wait.... Data sync in progress")
                dataSync_tv.visibility= View.VISIBLE
            }
        },5000)

        withContext(Dispatchers.IO) {
            var intent=Intent()
                if (response?.code() == 200 && !response.body().toString().isEmpty())
                {
                    staticSyncData = response.body()?.data
                    val apiModel=response.body()?.data
                    insertSyncData(apiModel)

                  /*  val gson=Gson()

                    runBlocking {
                        withContext(Dispatchers.IO) {
                            launch {  dbBase?.deleteAll_SYNCAPI() }
                        }
                    }
                    runBlocking {
                        withContext(Dispatchers.IO){
                            launch { dbBase?.addSYNCAPIData(gson.toJson(apiModel?.settingDCR),
                                gson.toJson(apiModel?.workTypeList),"",
                                "",gson.toJson(apiModel?.workingWithList),gson.toJson(apiModel?.fieldStaffTeamList),""
                                ,apiModel?.configurationSetting, gson.toJson(apiModel?.schemeList),
                                "",0, gson.toJson(apiModel?.doctorList)) }
                            launch {   dbBase?.addRoutes(apiModel?.routeList) }
                            launch {  dbBase?.addRetailer(apiModel?.retailerList) }
                            launch {  dbBase?.addProduct(apiModel?.productList)  }

                        }
                    }*/

                    intent = Intent(this@SplashActivity, HomePage::class.java)
                }
                else
                {
                    sharePreferance?.setPrefBool("isLogin", false)
                    intent= Intent(this@SplashActivity, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
        }
    }

    override fun onDestroy() {
        generalClass.disableSimpleProgress(progressBar!!)
        super.onDestroy()
    }

    fun getAvailableInternalMemorySize(): Long {
        val path: File = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize: Long
        val availableBlocks: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong()
            availableBlocks = stat.getAvailableBlocksLong()
        } else {
            blockSize = stat.getBlockSize().toLong()
            availableBlocks = stat.getAvailableBlocks().toLong()
        }
        val convertByteIntoMb=availableBlocks * blockSize
        val getInKb= convertByteIntoMb/1000
        val getInMb= getInKb/1000
        val getInGb= getInMb/1000
        return getInGb
    }

}