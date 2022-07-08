package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.*


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
        dbBase= DatabaseHandler.getInstance(applicationContext)

        if(sharePreferance?.getPrefBool("isLogin") == true)
        {
//         //if no internet connection and database have sync data then open directly home page
            if(!generalClass.isInternetAvailable() && dbBase?.getDatasCount()!! > 0)
            {
                generalClass.enableSimpleProgress(progressBar!!)
                val coroutineScope= CoroutineScope(Dispatchers.IO).launch {
                    val syncSmallData= async {  staticSyncData= dbBase?.getSYNCApiData(0)
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
                        alertClass.lowNetworkAlert()
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
            }, 2000)
        }


    }
    suspend fun callingSyncAPI()
    {
        var profileData =sharePreferance?.getPref("profileData")
        val loginModel= Gson().fromJson(profileData, LoginModel::class.java)

        val response = APIClientKot().getUsersService(2, sharePreferanceBase?.getPref("secondaryUrl")!!
        ).syncApiCoo("bearer " + loginModel?.accessToken)


        withContext(Dispatchers.Main) {
            var intent=Intent()
                if (response?.code() == 200 && !response.body().toString().isEmpty())
                {
                    staticSyncData = response.body()?.data
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


}