package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIClientKot
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : BaseActivity()
{
    companion object {
        var connectivityChangeReceiver=
            ConnectivityChangeReceiver()
        var alertDialogNetwork: AlertDialog? = null
        var staticSyncData: SyncModel? =null
    }
    var sharePreferance: PreferenceClass?= null
    var progressBar: ProgressBar?=null
    var apiInterface: APIInterface? = null
    //Database
    var db = DatabaseHandler(this)

    override fun onCreate(savedInstanceState: Bundle?) {

        getSupportActionBar()?.hide()   //Hiding Toolbar

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        sharePreferance = PreferenceClass(this)
        progressBar=findViewById(R.id.progressBar) as ProgressBar

        // FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token: String = task.getResult().toString()
                Log.e("TOKEN",token)
            })

       // launchZoomUrl()

        if(sharePreferance!!.getPrefBool("isLogin"))
        {
//         //if no internet connection and database have sync data then open directly home page
            if(!generalClass.isInternetAvailable() && db.datasCount > 0)
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
                val scope= GlobalScope.launch {
                    callingSyncAPI()
                }
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



//    private fun sync_api()
//    {
//
//        var profileData =sharePreferance?.getPref("profileData")
//        val loginModel= Gson().fromJson(profileData, LoginModel::class.java)
//
//        generalClass.enableSimpleProgress(progressBar!!)
//        apiInterface= APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(APIInterface::class.java)
//
//        var call: Call<SyncModel> = apiInterface?.syncApi("bearer " + loginModel?.accessToken) as Call<SyncModel>
//        call.enqueue(object : Callback<SyncModel?> {
//            override fun onResponse(call: Call<SyncModel?>?, response: Response<SyncModel?>) {
//                Log.e("sync_api", response.code().toString() + "")
//
//                if (response.code() == 200 && !response.body().toString().isEmpty())
//                {
//                    staticSyncData = response.body()
//                    val intent = Intent(this@SplashActivity, HomePage::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//                else if (response.code() == 401) {
//                    sharePreferance?.setPrefBool("isLogin", false)
//                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//
//            override fun onFailure(call: Call<SyncModel?>, t: Throwable?) {
//                Log.e("syncApiError", t?.message.toString())
//                generalClass.checkInternet()
//                call.cancel()
//                generalClass.disableSimpleProgress(progressBar!!)
//            }
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()
        generalClass.disableSimpleProgress(progressBar!!)
    }


    private fun launchZoomUrl() {
        val str="zoomus://zoom.us/start?browser=chrome&confno=87442731712&zc=0&stype=100&uid=7JxmLMQRRYyEHrJTsduDYQ&token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJxMG9iRzFqU1JUYTI3R29DSG8tVlp3IiwiZXhwIjoxODAwMDAwMDAwMH0.KHz1LEHUa6EP3i0ClDlN10G2Ew_1r9OknbBnrCaAcMI&uname=kajwadkar13@gmail.com&password=13Zoom@003"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(str))
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        }
        else{
            Toast.makeText(this,"zoom not installed",Toast.LENGTH_SHORT).show()
        }
    }

//    iOS/Android client: zoomus://zoom.us/start?browser=chrome&confno=123456789&zc=0&stype=100&sid=lBGPGzGdT8–2Yf3kjDY5gg&uid=lBGPGzGdT8–2Yf3kjDY5gg&token=xxxxxx&uname=Betty
//    Parameters in zoomus and zoommtg protocol
//    confno: Meeting ID
//    zc: Zoom meeting control options
//    0 — with video and audio
//    1 — screen share only without video and audio
//    browser: browser type (optional)
//    “chrome”
//    “firefox”
//    “msie”
//    “safari”
//    uname: User name
//    stype: User’s login type
//    100 — Work Email user
//    0 — Facebook user
//    1 — Google user
//    101 — SSO user
//    99 — Cust API user
//    uid: Host user ID
//    token: Host user token, does not until user change the password (You can get it from the user GET REST API call)
//    pwd: Meeting Password


}