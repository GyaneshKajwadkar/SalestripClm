package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.SyncModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
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
         var  connectivityChangeReceiver= ConnectivityChangeReceiver()
         var alertDialogNetwork: AlertDialog? = null
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
           /* //if no internet connection and database have sync data then open directly home page
            if(isInternetAvailable(this)==false && db.datasCount > 0)
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
            //login_api()*/

            sync_api()
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

    //sync api
    private fun sync_api()
    {

        var profileData =sharePreferance?.getPref("profileData")
        val loginModel= Gson().fromJson(profileData, LoginModel::class.java)

        enableProgress(progressBar!!)
        apiInterface= APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(APIInterface::class.java)

        var call: Call<SyncModel> = apiInterface?.syncApi("bearer " + loginModel?.accessToken) as Call<SyncModel>
        call.enqueue(object : Callback<SyncModel?> {
            override fun onResponse(call: Call<SyncModel?>?, response: Response<SyncModel?>) {
                Log.e("sync_api", response.code().toString() + "")

                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    val intent = Intent(this@SplashActivity, HomePage::class.java)
                    startActivity(intent)
                    finish()
                } else if (response.code() == 401) {
                    sharePreferance?.setPrefBool("isLogin", false)
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<SyncModel?>, t: Throwable?) {
                Log.e("syncApiError", t?.message.toString());
                checkInternet()
                call.cancel()
                disableProgress(progressBar!!)
            }
        })
    }


    fun checkInternet()
    {
        if(isInternetAvailable(this)==true)
        {
            commonAlert(this, "Error", "Something went wrong please try again later")
        }
        else
        {
            networkAlert(this)
        }
    }


    //========================================== For now this is not in used.
    fun  checkVersioin_api(companyCode: String)
    {
        apiInterface= APIClient.getClient(1, "").create(APIInterface::class.java)

        enableProgress(progressBar!!)

        var call: Call<String> = apiInterface?.checkVersion(
            /*    companyCode*/
        ) as Call<String>
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.e("checkVersioin_api", response.body().toString() + "")
                if (response.body().toString().isEmpty()) {

                } else {

                    sharePreferance?.setPref("version", response.body().toString())
                    var onlineVersion: String = response.body().toString()
                    val namesList: List<String> = onlineVersion.split(",")

                    try {
                        val pInfo: PackageInfo =
                                getPackageManager().getPackageInfo(getPackageName(), 0)
                        var version = pInfo.versionName
                        //   version="2.1.50"
                        var updateLower: Boolean = checkForUpdateLower(version, namesList.get(0))
                        var updateHigher: Boolean = checkForUpdateHigher(version, namesList.get(1))


                        if (!updateHigher) {
                            try {
                                startActivity(
                                        Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("market://details?id=$packageName")
                                        )
                                )
                            } catch (e: ActivityNotFoundException) {
                                startActivity(
                                        Intent(
                                                Intent.ACTION_VIEW,
                                                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                        )
                                )
                            }
                        }

                        val intent = Intent(this@SplashActivity, HomePage::class.java)
                        startActivity(intent)
                        finish()

                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
                disableProgress(progressBar!!)
            }

            override fun onFailure(call: Call<String?>, t: Throwable?) {
                checkInternet()
                call.cancel()
                disableProgress(progressBar!!)
            }
        })
    }

    //checkInternet connection


    //Login APi
    private fun login_api()
    {
        enableProgress(progressBar!!)
        apiInterface= APIClient.getClient(2, sharePreferance?.getPref("secondaryUrl")).create(APIInterface::class.java)


        var call: Call<LoginModel> = apiInterface?.loginAPI(
                "password", sharePreferance?.getPref("userNameLogin")!!, sharePreferance?.getPref("password")!!
        ) as Call<LoginModel>
        call.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>?, response: Response<LoginModel?>) {
                Log.e("login_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty()) {
                    checkVersioin_api(sharePreferance?.getPref("companyCode")!!)
                } else {
                    sharePreferance?.setPrefBool("isLogin", false)

                    Handler(Looper.getMainLooper()).postDelayed({
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, 2000)

                }
                disableProgress(progressBar!!)
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable?) {
                checkInternet()
                call.cancel()
                disableProgress(progressBar!!)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        disableProgress(progressBar!!)
    }


    //Suresh.g
    //DEMO

}