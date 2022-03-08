package `in`.processmaster.salestripclm.utils
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.MeetingActivity
import `in`.processmaster.salestripclm.models.ZoomCredientialModel
import `in`.processmaster.salestripclm.zoom_sdk_code.initsdk.InitAuthSDKCallback
import `in`.processmaster.salestripclm.zoom_sdk_code.initsdk.InitAuthSDKHelper
import `in`.processmaster.salestripclm.zoom_sdk_code.startjoinmeeting.UserLoginCallback
import `in`.processmaster.salestripclm.zoom_sdk_code.startjoinmeeting.emailloginuser.EmailUserLoginHelper
import android.app.Activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import us.zoom.sdk.*

class ZoomInitilizeClass() : Activity(), UserLoginCallback.ZoomDemoAuthenticationListener ,
    MeetingServiceListener, InitAuthSDKCallback
{
    private var zoomSDKBase = ZoomSDK.getInstance()
    var zoomCredientialModel: ZoomCredientialModel?=null
    var progressDialog: androidx.appcompat.app.AlertDialog? = null

    public fun initilizeZoom(context: Activity, model: ZoomCredientialModel?)
    {
         progressAlert(context)
        this.zoomCredientialModel=model
        InitAuthSDKHelper.getInstance().initSDK(context, this)
    }

    override fun onZoomSDKLoginResult(result: Long)
    {
        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS.toLong())
        {
            Log.e("logResult", "loginSuccessoverride")
            UserLoginCallback.getInstance().removeListener(this)
            progressDialog?.dismiss()
        }
        else
        { Log.e("logResult", "loginErroroverride")
            progressDialog?.dismiss()
        }

    }

    override fun onZoomSDKLogoutResult(result: Long)
    {

        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS.toLong())
        { }

        else
        { }

        progressDialog?.dismiss()
    }

    override fun onZoomIdentityExpired()
    {
        progressDialog?.dismiss()
    }

    override fun onZoomAuthIdentityExpired()
    {
        progressDialog?.dismiss()
    }

    override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int)
    {
        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS)
        {
            Log.e("zoomErrorLog","error- $errorCode internalErrorcode- $internalErrorCode")
        }
        else
        {
            if (zoomSDKBase?.tryAutoLoginZoom() == ZoomApiError.ZOOM_API_ERROR_SUCCESS)
            {
                UserLoginCallback.getInstance().addListener(this)
            }
            else if(!zoomSDKBase?.isLoggedIn!!)
            {
                loginFirstbase(zoomCredientialModel?.getData()?.credentialData)
                UserLoginCallback.getInstance().addListener(this)
            }
        }
        progressDialog?.dismiss()

    }

    override fun onMeetingStatusChanged(p0: MeetingStatus?, p1: Int, p2: Int)
    {
        progressDialog?.dismiss()
    }

    private fun loginFirstbase(credentialData: ZoomCredientialModel.CredentialData?): Unit
    {

        if(credentialData==null)
        {
            if(progressDialog!=null)progressDialog?.dismiss()
            return
        }

        val ret: Int = EmailUserLoginHelper.getInstance().login(
            credentialData?.userName,
            credentialData?.password
        )
        if (ret != ZoomApiError.ZOOM_API_ERROR_SUCCESS)
        {
            if (ret == ZoomApiError.ZOOM_API_ERROR_EMAIL_LOGIN_IS_DISABLED)
            {
                Log.e("logResult", "loginErrorFirst")
            }

            else if(ret == ZoomApiError.ZOOM_API_ERROR_SUCCESS)
            {
                Log.e("logResult", "loginSuccessFirst")
                MeetingActivity().initilizeZoom()
            }

            else
            {
                Log.e("logResult", "login and initilized")
                MeetingActivity().initilizeZoom()
            }
        }
        else
        {

        }
        if(progressDialog!=null)progressDialog?.dismiss()
    }

    fun progressAlert(context: Activity)
    {
        if(progressDialog?.isShowing == true)
        {
            return
        }

        val dialogBuilder = androidx.appcompat.app.AlertDialog.Builder(context)
        val inflater = context.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.progress_view, null)
        dialogBuilder.setView(dialogView)

        progressDialog= dialogBuilder.create()
        progressDialog?.setCanceledOnTouchOutside(false)
        progressDialog?.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val progressbarRelative = dialogView.findViewById<View>(R.id.progressView_parentRv) as RelativeLayout
        val mainTitle = dialogView.findViewById<View>(R.id.progressMessage_tv) as TextView
        progressbarRelative.visibility=View.VISIBLE
        mainTitle.setText("Initilize Zoom")
        progressbarRelative.setBackgroundColor(Color.TRANSPARENT)
        progressDialog?.show()
    }

}