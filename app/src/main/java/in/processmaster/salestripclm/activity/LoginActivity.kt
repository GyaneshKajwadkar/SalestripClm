
package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.networkUtils.APIClient.getClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*


class LoginActivity : BaseActivity() {

    var mpin_tv:TextView? =null
    var progressMessage_tv:TextView? =null
    var forgotPass_tv:TextView? = null
    var password_et:EditText?=null
    var userName_et:EditText?=null
    var companyCode_et:EditText?=null
    var signIn_btn: MaterialButton?=null
    var verifyCompany_btn: MaterialButton?=null
    var companyVerfy_ll: LinearLayout?=null
    var progressView_parentRv: RelativeLayout?=null
    var login_ll: LinearLayout?=null
    private var shareTarget: Target? = null


    var apiInterface: APIInterface? = null
    var sharePreferance: PreferenceClass?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide()   //Hiding Toolbar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dexterPermission(this)


    }




    // initilizeVariables and click
    fun initView()
    {
        mpin_tv=findViewById(R.id.mpin_tv) as TextView
        forgotPass_tv=findViewById(R.id.forgotPass_tv) as TextView
        password_et=findViewById(R.id.password_et) as EditText
        userName_et=findViewById(R.id.userName_et) as EditText
        signIn_btn=findViewById(R.id.signIn_btn) as MaterialButton
        companyCode_et=findViewById(R.id.companyCode_et) as EditText
        verifyCompany_btn=findViewById(R.id.verifyCompany_btn) as MaterialButton
        companyVerfy_ll=findViewById(R.id.companyVerfy_ll) as LinearLayout
        login_ll=findViewById(R.id.login_ll) as LinearLayout
        progressView_parentRv=findViewById(R.id.progressView_parentRv) as RelativeLayout
        progressMessage_tv=findViewById(R.id.progressMessage_tv) as TextView

        sharePreferance = PreferenceClass(this)

        apiInterface= getClient(1, "").create(APIInterface::class.java)

        mpin_tv!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var setText: String = mpin_tv!!.getText().toString()
                if (setText.equals("Login with MPIN")) {
                    mpin_tv!!.setText("Login with password");
                    password_et!!.setHint("MPIN *")
                } else {
                    mpin_tv!!.setText("Login with MPIN")
                    password_et!!.setHint("Password *")
                }
            }
        })

        //if user name exist then auto fill user name
        if(!sharePreferance?.getPref("userName_login")?.isEmpty()!!)
        {
            userName_et?.setText(sharePreferance?.getPref("userName_login"))
        }

       userName_et?.setText("shubham")
       password_et?.setText("jack@321")

        //forgot click
        forgotPass_tv!!.setOnClickListener {
            val intent = Intent(
                    this,
                    ForgotActivity::class.java
            )
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

            if(isInternetAvailable(this)==true)
            {
                login_api()
            }
            else
            {
                networkAlert(this)
            }
        }

        //Verify Button Check
        verifyCompany_btn!!.setOnClickListener{

            if(companyCode_et!!.getText().toString().isEmpty())
            {
                companyCode_et!!.setError("Required")
                companyCode_et!!.requestFocus()
                return@setOnClickListener
            }

            try {
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: java.lang.Exception) {
                // TODO: handle exception
            }

            if(isInternetAvailable(this)==true)
            {
                checkCC_api()
            }
            else
            {
                networkAlert(this)
            }
        }

        //enable company code is not null
        if(sharePreferance?.getPref("companyCode")?.isEmpty() == false)
        {
            companyVerfy_ll?.visibility = View.GONE
            login_ll?.visibility = View.VISIBLE
            Log.e("companyCode", sharePreferance?.getPref("companyCode")!!)

            checkVersioin_api(sharePreferance?.getPref("companyCode")!!)
        }
    }


    //Login APi
    private fun login_api()
    {
        //call api interface with seconday url
        progressMessage_tv?.setText("Verify User")
        enableProgress(progressView_parentRv!!)
        apiInterface= getClient(2, sharePreferance?.getPref("secondaryUrl")).create(APIInterface::class.java)

        var call: Call<LoginModel> = apiInterface?.loginAPI(
                "password", userName_et?.getText().toString() + "," +
                sharePreferance?.getPref("companyCode"), password_et?.getText().toString()
        ) as Call<LoginModel>
        call.enqueue(object : Callback<LoginModel?> {
            override fun onResponse(call: Call<LoginModel?>?, response: Response<LoginModel?>) {
                Log.e("login_api", response.code().toString() + "")
                if (response.code() == 200 && !response.body().toString().isEmpty()) {

                    var loginModel = response.body()
                    saveLoginData(loginModel)

                    sharePreferance?.setPref("userName_login", userName_et?.getText().toString())
                    sharePreferance?.setPrefBool("isLogin", true)
                    sharePreferance?.setPref(
                            "userNameLogin", userName_et?.getText().toString() + "," +
                            sharePreferance?.getPref("companyCode")
                    )
                    sharePreferance?.setPref("password", password_et?.getText().toString())

                    //   disableProgress(progressBar!!)

                } else {
                    commonAlert(this@LoginActivity, "", "Incorrect username or password")
                    disableProgress(progressView_parentRv!!)
                }


            }

            override fun onFailure(call: Call<LoginModel?>, t: Throwable?) {
                checkInternet()
                call.cancel()
                disableProgress(progressView_parentRv!!)
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
        progressMessage_tv?.setText("Verify Company code")
        enableProgress(progressView_parentRv!!)

        var call: Call<String> = apiInterface?.checkCompanyCode(
                companyCode_et?.getText().toString().toUpperCase()
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
                            companyCode_et?.getText().toString().toUpperCase()
                    )
                    checkVersioin_api(companyCode_et?.getText().toString().toUpperCase())
                }

                disableProgress(progressView_parentRv!!)
            }

            override fun onFailure(call: Call<String?>, t: Throwable?) {
                checkInternet()
                call.cancel()
                disableProgress(progressView_parentRv!!)
            }
        })
    }

    //check Version API
    fun  checkVersioin_api(companyCode: String)
    {
        enableProgress(progressView_parentRv!!)

        var call: Call<String> = apiInterface?.checkVersion(
              /*  companyCode*/
        ) as Call<String>
        call.enqueue(object : Callback<String?> {
            override fun onResponse(call: Call<String?>?, response: Response<String?>) {
                Log.e("checkVersioin_api", response.code().toString() + "")
                if (response.body().toString().isEmpty()) {

                } else {
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

                     //   Log.e("versionCodeiS", version)
                     //   Log.e("response", response.body().toString())
                     //   Log.e("updateLower", updateLower.toString())
                     //   Log.e("updateHigher", updateHigher.toString())

                       val verionLower = namesList.get(0).replace(".","").toInt()
                       val versionHigher = namesList.get(1).replace(".","").toInt()

                        Log.e("replacedString",verionLower.toString())
                        Log.e("replacedHString",versionHigher.toString())
                        Log.e("replacedCString",versionHigher.toString())

                        if(version<verionLower||version>versionHigher)
                        {
                            needUpdateAlert()

                        }



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

                disableProgress(progressView_parentRv!!)
            }

            override fun onFailure(call: Call<String?>, t: Throwable?) {
                checkInternet()
                call.cancel()
                disableProgress(progressView_parentRv!!)
            }
        })
    }

    public fun needUpdateAlert()
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

    //checkInternet connection
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

    fun saveLoginData(loginModel: LoginModel?)
    {
        //check is image name is empty or not
        if(!loginModel?.imageName?.isEmpty()!!)
        {
            //save user data to shareprefrance
            val gson = Gson()
            sharePreferance?.setPref("profileData", gson.toJson(loginModel))
            //save profile image to local storage
            saveImageTofolder(loginModel.imageName, "profilePic")
           // loginModel?.imageName?.let { saveImageTofolder(it, "profilePic") }
        }
        else
        {
            val gson = Gson()
            sharePreferance?.setPref("profileData", gson.toJson(loginModel))
            callHomePage()
        }
    }


 /*   //download profile pic from url and save to local storage
    fun saveImageTofolder(uRl: String, picName: String)
    {

        Log.e("uRlyes", uRl)

            Picasso.get().load(uRl).
            into(object : com.squareup.picasso.Target {
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    Log.e("exception", e.toString())
                    disableProgress(progressBar!!)
                }

                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {

                    val file: File =
                            File(getFilesDir()?.getAbsolutePath() + "/" + picName + ".jpg")
                    if (file.exists()) {
                        file.delete()
                    }
                    file.createNewFile()
                    val bitmap12 = bitmap
                    try {
                        val out = FileOutputStream(file)
                        bitmap12?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()

                        Log.e("uuyes", "gsefgwesf")

                        sharePreferance?.setPref(picName, file.absolutePath)
                        callHomePage()
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    } catch (e: java.lang.Exception) {
                        Log.e("exceptionProfileSave", e.message.toString())
                        e.printStackTrace()
                        disableProgress(progressBar!!)
                    }
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                    Log.e("placeHolderDrawable", "placeHolderDrawable")
                }

            })
 }*/

    fun saveImageTofolder(uRl: String, picName: String)
    {
        Glide.with(this)
                .asBitmap()
                .load(uRl)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        val file: File =
                                File(getFilesDir()?.getAbsolutePath() + "/" + picName + ".jpg")
                        if (file.exists()) {
                            file.delete()
                        }
                        file.createNewFile()
                        val bitmap12 = resource
                        try {
                            val out = FileOutputStream(file)
                            bitmap12?.compress(Bitmap.CompressFormat.JPEG, 100, out)
                            out.flush()
                            out.close()

                            sharePreferance?.setPref(picName, file.absolutePath)
                            callHomePage()
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                        } catch (e: java.lang.Exception) {
                            Log.e("exceptionProfileSave", e.message.toString())
                            e.printStackTrace()
                            disableProgress(progressView_parentRv!!)
                        }                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
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

}