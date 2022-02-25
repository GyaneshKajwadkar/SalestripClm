package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.common_classes.GeneralClass
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIClient.getClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.progress_view.*
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class LoginActivity : BaseActivity() {
    var apiInterface: APIInterface? = null
    var sharePreferance: PreferenceClass?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()   //Hiding Toolbar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dexterPermission(this) }

    // initilizeVariables and click
    fun initView()
    {
        sharePreferance = PreferenceClass(this)
        apiInterface= getClient(1, "").create(APIInterface::class.java)

        mpin_tv!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var setText: String = mpin_tv!!.getText().toString()
                if (setText.equals("Login with MPIN")) {
                    mpin_tv!!.setText("Login with password")
                    password_et!!.setHint("MPIN *")
                } else {
                    mpin_tv!!.setText("Login with MPIN")
                    password_et!!.setHint("Password *")
                } } })

        //if user name exist then auto fill user name
        if(!sharePreferance?.getPref("userName_login")?.isEmpty()!!)
        { userName_et?.setText(sharePreferance?.getPref("userName_login")) }

        //userName_et?.setText("shubham")
         userName_et?.setText("NILESH")
       // password_et?.setText("jack@321")
         password_et?.setText("NILESH")
        companyCode_et.setText("UAT2")
        //forgot click
        forgotPass_tv!!.setOnClickListener {
            val intent = Intent(this, ForgotActivity::class.java)
            startActivity(intent)
        }

        //SignIn Button Check
        signIn_btn!!.setOnClickListener{

            if(userName_et!!.getText().toString().isEmpty())
            {
                userName_et!!.setError("Required")
                userName_et!!.requestFocus()
                return@setOnClickListener
            }
            if(password_et!!.getText().toString().isEmpty())
            {
                password_et!!.setError("Required")
                password_et!!.requestFocus()
                return@setOnClickListener
            }
            if(generalClass.isInternetAvailable())
            { login_api() }
            else
            { alertClass.networkAlert() }
        }

        //Verify Button Check
        verifyCompany_btn!!.setOnClickListener{

            if(companyCode_et!!.getText().toString().isEmpty())
            {
                companyCode_et!!.setError("Required")
                companyCode_et!!.requestFocus()
                return@setOnClickListener
            }
            generalClass.hideKeyboard(this,it)

            if(generalClass.isInternetAvailable())
            { checkCC_api() }
            else
            { alertClass.networkAlert() }
        }

        //enable company code is not null
        if(sharePreferance?.getPref("companyCode")?.isEmpty() == false)
        {
            companyVerfy_ll?.visibility = View.GONE
            login_ll?.visibility = View.VISIBLE
            checkVersioin_api(sharePreferance?.getPref("companyCode")!!)
        }
    }


    //Login APi
    private fun login_api()
    {
        alertClass.showAlert("Verify User")
        apiInterface= getClient(2, sharePreferance?.getPref("secondaryUrl")).create(APIInterface::class.java)

        var call: Call<LoginModel> = apiInterface?.loginAPI(
            "password", userName_et?.getText().toString() + "," +
                    sharePreferance?.getPref("companyCode").toString(), password_et?.getText().toString()
        ) as Call<LoginModel>
        call.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>?, response: Response<LoginModel?>) {
                Log.e("login_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    var loginModel = response.body()

                    sharePreferance?.setPref("userName_login", userName_et?.getText().toString())
                    sharePreferance?.setPrefBool("isLogin", true)
                    sharePreferance?.setPref(
                        "userNameLogin", userName_et?.getText().toString() + "," +
                                sharePreferance?.getPref("companyCode")
                    )
                    sharePreferance?.setPref("password", password_et?.getText().toString())
                    val gson = Gson()
                    sharePreferance?.setPref("profileData", gson.toJson(loginModel))
                    callHomePage()
                }
                else {
                    alertClass.commonAlert("","Incorrect username or password")
                }
                alertClass.hideAlert()
            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
            }
        })
    }

    fun callHomePage()
    {
        val intent= Intent(this, HomePage::class.java)
        startActivity(intent)
        finish()
    }

    //CheckCompanyCode Api
    fun  checkCC_api()
    {
        alertClass.showAlert("Verify Company code")

        var call: Call<String> = apiInterface?.checkCompanyCode(
            companyCode_et?.getText().toString().uppercase()
        ) as Call<String>
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.e("checkCC_api", response.code().toString() + "")
                if (response.body().toString().isEmpty()) {
                    companyCode_et!!.setError("Invalid Company code")
                    companyCode_et!!.requestFocus()
                } else {
                    //check and save company code in sp
                    sharePreferance?.setPref("secondaryUrl", response.body().toString())
                    sharePreferance?.setPref(
                        "companyCode",
                        companyCode_et?.getText().toString().uppercase()
                    )
                    checkVersioin_api(companyCode_et?.getText().toString().uppercase())
                }
                alertClass.hideAlert()
            }

            override fun onFailure(call: Call<String?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
            }
        })
    }

    //check Version API
    fun  checkVersioin_api(companyCode: String)
    {
        alertClass.showAlert("")

        var call: Call<String> = apiInterface?.checkVersion() as Call<String>
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.e("checkVersioin_api", response.code().toString() + "")
                if (response.body().toString().isEmpty()) {
                }
                else {
                    sharePreferance?.setPref("version", response.body().toString())
                    var onlineVersion: String = response.body().toString()
                    val namesList: List<String> = onlineVersion.split(",")
                    companyVerfy_ll?.visibility = View.GONE
                    login_ll?.visibility = View.VISIBLE

                    try {
                        val pInfo: PackageInfo =
                            getPackageManager().getPackageInfo(getPackageName(), 0)
                        var version = pInfo.versionName.replace(".","").toInt()
                        //  version = "2.1.30"

                        //   var updateLower: Boolean = checkForUpdateLower(version, namesList.get(0))
                        //   var updateHigher: Boolean = checkForUpdateHigher(version, namesList.get(1))

                        val verionLower = namesList.get(0).replace(".","").toInt()
                        val versionHigher = namesList.get(1).replace(".","").toInt()

                        if(version<verionLower||version>versionHigher)
                        { needUpdateAlert() }
                    /*  if (updateLower) {
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
                          }*/
                    } catch (e: PackageManager.NameNotFoundException) {
                        e.printStackTrace()
                    }
                }
                alertClass.hideAlert()
            }
            override fun onFailure(call: Call<String?>, t: Throwable?) {
                generalClass.checkInternet()
                call.cancel()
                alertClass.hideAlert()
            }
        })
    }

    fun needUpdateAlert()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.update_app_dialog, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.setCancelable(false)
        alertDialog.setCanceledOnTouchOutside(false);

        val okBtn_rl =
            dialogView.findViewById<View>(R.id.update_btn) as Button

        okBtn_rl.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
            } catch (e: ActivityNotFoundException) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        initView()
        createConnectivity(this)
    }

    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
    }

    //CreateDialog
    fun openLocationAsk_Dialog()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.location_layout, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val okBtn_rl =
            dialogView.findViewById<View>(R.id.okBtn_rl) as RelativeLayout

        okBtn_rl.setOnClickListener{
            alertDialog.dismiss()
            val intent= Intent(this, HomePage::class.java)
            startActivity(intent)
        }
        alertDialog.show()
    }
}