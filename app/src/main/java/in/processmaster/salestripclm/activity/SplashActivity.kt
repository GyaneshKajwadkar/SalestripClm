package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
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

                // Get new FCM registration token
                val token: String = task.getResult().toString()
                Log.e("TOKEN",token)

            })

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
                //call sync api for token check
                sync_api()
            }
//            //login_api()

            // sync_api()
        }
        //call login screen
        else
        {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 2000)
        }

    }

    private fun sync_api()
    {

        var profileData =sharePreferance?.getPref("profileData")
        val loginModel= Gson().fromJson(profileData, LoginModel::class.java)

        generalClass.enableSimpleProgress(progressBar!!)
        apiInterface= APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(APIInterface::class.java)

        var call: Call<SyncModel> = apiInterface?.syncApi("bearer " + loginModel?.accessToken) as Call<SyncModel>
        call.enqueue(object : Callback<SyncModel?> {
            override fun onResponse(call: Call<SyncModel?>?, response: Response<SyncModel?>) {
                Log.e("sync_api", response.code().toString() + "")

                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    staticSyncData = response.body()
                    val intent = Intent(this@SplashActivity, HomePage::class.java)
                    startActivity(intent)
                    finish()
                }
                else if (response.code() == 401) {
                    sharePreferance?.setPrefBool("isLogin", false)
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<SyncModel?>, t: Throwable?) {
                Log.e("syncApiError", t?.message.toString())
                generalClass.checkInternet()
                call.cancel()
                generalClass.disableSimpleProgress(progressBar!!)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        generalClass.disableSimpleProgress(progressBar!!)
    }

}