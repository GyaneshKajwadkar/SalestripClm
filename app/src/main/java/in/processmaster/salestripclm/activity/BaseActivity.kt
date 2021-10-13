package `in`.processmaster.salestripclm.activity

import DocManagerModel
import `in`.processmaster.salestripclm.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.alertDialogNetwork
import `in`.processmaster.salestripclm.activity.SplashActivity.Companion.connectivityChangeReceiver
import `in`.processmaster.salestripclm.models.LoginModel
import `in`.processmaster.salestripclm.models.TeamsModel
import `in`.processmaster.salestripclm.networkUtils.APIClient
import `in`.processmaster.salestripclm.networkUtils.APIInterface
import `in`.processmaster.salestripclm.utils.DatabaseHandler
import `in`.processmaster.salestripclm.utils.PreferenceClass
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.progress_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.ref.WeakReference


open class BaseActivity : AppCompatActivity() {

    var alertDialog: AlertDialog? = null
    var sharePreferanceBase: PreferenceClass?= null
    var loginModelBase= LoginModel()
    var dbBase= DatabaseHandler(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        connectivityChangeReceiver= ConnectivityChangeReceiver()

        try{
            sharePreferanceBase = PreferenceClass(this)
            var profileData =sharePreferanceBase?.getPref("profileData")
            loginModelBase = Gson().fromJson(profileData, LoginModel::class.java)
        }
        catch(e:Exception)
        {

        }
    }

    //network alert
    fun networkAlert(context: Activity) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.networkalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val okBtn_rl =
            dialogView.findViewById<View>(R.id.okBtn_rl) as RelativeLayout

        okBtn_rl.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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


    //exit app alert
    fun exitAppAlert(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val exit_btn =
            dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        exit_btn.setOnClickListener {
            finish();
            System.exit(0);
            alertDialog.dismiss()
        }

        cancel_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }


    //common alert with message and ok button
    @SuppressLint("WrongViewCast")
    public fun commonAlert(context: Context, headerString: String, message: String) {
        val dialogBuilder = AlertDialog.Builder(context)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.common_alert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));

        val okBtn_rl =
            dialogView.findViewById<View>(R.id.ok_btn) as AppCompatButton

        var headerTv =
            dialogView.findViewById<View>(R.id.header_tv) as TextView
        var messageTv =
            dialogView.findViewById<View>(R.id.message_tv) as TextView

        headerTv.setText(headerString)
        messageTv.setText(message)

        okBtn_rl.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
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
        alertDialog!!.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        alertDialog!!.setCanceledOnTouchOutside(false);

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


    //check internet availability
    fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //check versin of mobile device
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }

        return result
    }

    //enable progress with screen not touchable
    fun enableProgress(progressBar: RelativeLayout) {

        progressBar?.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    //disable progress bar
    fun disableProgress(progressBar: RelativeLayout) {
        progressBar?.visibility = View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

    }


    //enable progress with screen not touchable
    fun enableProgress(progressBar: ProgressBar) {
        progressBar?.visibility = View.VISIBLE
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        );
    }

    //disable progress bar
    fun disableProgress(progressBar: ProgressBar) {
        progressBar?.visibility = View.GONE
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    fun HideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Close", View.OnClickListener { })
            .setActionTextColor(Color.YELLOW)
            .show()
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

    fun getSecondaryApiInterface(): APIInterface {
        return APIClient.getClient(2, sharePreferanceBase?.getPref("secondaryUrl")).create(
            APIInterface::class.java)
    }


    //Get teams api
    fun getTeamsApi( context:Context, progressmessage:String) : ArrayList<DocManagerModel>
    {
        progressMessage_tv?.setText(progressmessage)
        enableProgress(progressView_parentRv!!)
        var getResponseList=ArrayList<DocManagerModel>()

        var call: Call<TeamsModel> = getSecondaryApiInterface().getTeamsMember(
            "bearer " + loginModelBase?.accessToken,
            loginModelBase.empId.toString()
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

                        getResponseList.add(selectorModel)
                    }
                }
                else
                {
                    Toast.makeText(this@BaseActivity, "Server error ", Toast.LENGTH_SHORT).show()
                    return
                }
                disableProgress(progressView_parentRv!!)

            }

            override fun onFailure(call: Call<TeamsModel?>, t: Throwable?) {
                call.cancel()
                disableProgress(progressView_parentRv!!)

            }
        })
        return getResponseList
    }

}