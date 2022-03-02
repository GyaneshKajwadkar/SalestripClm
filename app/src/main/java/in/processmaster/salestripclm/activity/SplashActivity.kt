package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashActivity : BaseActivity()
{
    companion object {
        var connectivityChangeReceiver= ConnectivityChangeReceiver()
        var alertDialogNetwork: AlertDialog? = null
        var staticSyncData: SyncModel? =null
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

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token: String = task.getResult().toString()
                Log.e("TOKEN",token)
            })

        if(sharePreferance!!.getPrefBool("isLogin"))
        {
//         //if no internet connection and database have sync data then open directly home page
            if(!generalClass.isInternetAvailable() && dbBase.getDatasCount() > 0)
            {
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this@SplashActivity, HomePage::class.java)
                    startActivity(intent)
                    finish()
                }, 2000)
            }
            else
            {
                generalClass.enableSimpleProgress(progressBar!!)
                val scope= GlobalScope.launch { callingSyncAPI() }
                scope.invokeOnCompletion {
                    this.runOnUiThread(java.lang.Runnable { generalClass.disableSimpleProgress(progressBar!!) })
                } }}

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
                    staticSyncData = response.body()
                    intent = Intent(this@SplashActivity, HomePage::class.java)
                }
                else {
                    sharePreferance?.setPrefBool("isLogin", false)
                    intent= Intent(this@SplashActivity, LoginActivity::class.java)
                }
                startActivity(intent)
                finish()
        }
    }

    override fun onDestroy() { super.onDestroy()
        generalClass.disableSimpleProgress(progressBar!!)
    }


}