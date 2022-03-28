package `in`.processmaster.salestripclm.activity

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.fragments.PresentEDetailingFrag
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.other.MeetingCommonCallback
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.share.MeetingShareHelper
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.share.MeetingShareHelper.MeetingShareUICallBack
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.user.MeetingUserCallback
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.common_toolbar.*
import us.zoom.sdk.*


class OnlinePresentationActivity : BaseActivity(), View.OnClickListener, LifecycleObserver,
    MeetingUserCallback.UserEvent, MeetingCommonCallback.CommonEvent {

    private var TAG: String = OnlinePresentationActivity::class.java.getSimpleName()
    private var mMeetingFailed = false
    private var meetingShareHelper: MeetingShareHelper? = null
    private var mMeetingService: MeetingService? = null
    private var mInMeetingService: InMeetingService? = null
    var sendResumeString=""

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_presentation)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mMeetingService = ZoomSDK.getInstance().meetingService
        mInMeetingService = ZoomSDK.getInstance().inMeetingService

        if (mMeetingService == null || mInMeetingService == null) {
        }
        else
        {
            meetingShareHelper = MeetingShareHelper(this, shareCallBack)
            registerListener()
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this)
        }

        dbBase.deleteApiData(7)
        val fragment = PresentEDetailingFrag()
        openFragment(fragment)

    }
    //Basic coding part


    var shareCallBack: MeetingShareUICallBack = object : MeetingShareUICallBack {
        override fun showShareMenu(popupWindow: PopupWindow) {
        }
        override fun requestStoragePermission(): Boolean {
            return true
        }
        override fun getShareView(): MobileRTCShareView? {
            return null
        }
    }


    private fun openFragment(fragment: Fragment)
    {
        val intent: Intent = intent
        val loadsPosition: Int = intent.getIntExtra("doctorID", -1)

        if(intent.getStringExtra("doctorName")!=null)
        {
            val doctorName: String = intent.getStringExtra("doctorName")!!

            doctorName_tv.setText(doctorName)
            toolbarHeader_rl.visibility=View.VISIBLE
            //back_iv.setOnClickListener({onBackPressed()})
            back_iv.visibility=View.INVISIBLE
        }


        val bundle = Bundle()
        bundle.putString("type", "present")
        bundle.putInt("doctorID", loadsPosition)
        bundle.putString("doctorName", doctorName_tv.text.toString())
        if(!sendResumeString.isEmpty()) bundle.putString("apiDataDcr", sendResumeString)
        Log.e("rertetfefddsfdsf",sendResumeString)

        fragment.setArguments(bundle)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framePresentatrion, fragment)
        transaction.commit()
    }


    //Zoom integration part
    private fun registerListener()
    {
        MeetingUserCallback.getInstance().addListener(this)
        MeetingCommonCallback.getInstance().addListener(this)
    }

    override fun onClick(v: View) {
        var id = v.id
        when (id) {

        }
    }

    fun updateAttendeeVideos(
        userlist: List<Long>,
        action: Int)
    {
        if (action == 0) {
            MeetingWindowHelper.getInstance().showMeetingWindow(this)
        } else if (action == 1)
        {
            MeetingWindowHelper.getInstance().showMeetingWindow(this)
        }
        else {
            MeetingWindowHelper.getInstance().hiddenMeetingWindow(true)
        }
    }


    override fun onPause() {
        super.onPause()
        stopConnectivity(this)
        if (mMeetingService == null || mInMeetingService == null) {
            return
        }
    }

    override fun onStop() {
        super.onStop()
        if (mMeetingService == null || mInMeetingService == null) {
            return
        }
    }

    var builder: Dialog? = null

    private fun updateVideoView(
        userList: List<Long>,
        action: Int
    ) {
        updateAttendeeVideos(userList, action)
    }


    var finished = false

    override fun onSilentModeChanged(inSilentMode: Boolean)
    {
    }


    override fun onMeetingUserJoin(userList: List<Long>) {
        // checkShowVideoLayout()
        updateVideoView(userList, 1)
    }

    override fun onMeetingUserLeave(userList: List<Long>) {
        //  checkShowVideoLayout()
        updateVideoView(userList, 2)
    }

    override fun onWebinarNeedRegister() {}

    override fun onMeetingFail(errorCode: Int, internalErrorCode: Int) {
        mMeetingFailed = true
    }

    override fun onMeetingLeaveComplete(ret: Long) {
        meetingShareHelper!!.stopShare()
        if (!mMeetingFailed) finish()
    }

    override fun onMeetingStatusChanged(
        meetingStatus: MeetingStatus?,
        errorCode: Int,
        internalErrorCode: Int
    )
    {
        //checkShowVideoLayout()
    }

    override fun onMeetingNeedPasswordOrDisplayName(
        needPassword: Boolean,
        needDisplayName: Boolean,
        handler: InMeetingEventHandler
    )
    {

    }

    override fun onMeetingNeedColseOtherMeeting(inMeetingEventHandler: InMeetingEventHandler)
    {
    }

    override fun onJoinWebinarNeedUserNameAndEmail(inMeetingEventHandler: InMeetingEventHandler?) {
//        inMeetingEventHandler.setRegisterWebinarInfo("test", time+"@example.com", false);
    }

    override fun onFreeMeetingReminder(
        isOrignalHost: Boolean,
        canUpgrade: Boolean,
        isFirstGift: Boolean
    ) {
        Log.d(
            TAG,
            "onFreeMeetingReminder:$isOrignalHost $canUpgrade $isFirstGift"
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded()
    {
        Log.e("appIs","background")
        MeetingWindowHelper.getInstance().hideOrshow(false)

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded()
    {
        MeetingWindowHelper.getInstance().hideOrshow(true)
        Log.e("appIs","FOREGROUNDED")
    }

    override fun onBackPressed() {

//        if(intent.getStringExtra("doctorName")!=null)
//        {
//          return
//        }

        if(mMeetingService!=null && mInMeetingService == null)
        {
            if(meetingShareHelper?.isSharingOut!!) stopPresentationAlert()
        }
        else
        {
            val r: Runnable = object : Runnable {
                override fun run() {
                    if(AlertClass.retunDialog)
                    {
                        onBackPressSuper()
                    }
                }
            }
            AlertClass(this).twoButtonAlert("no","Yes",
                2,"You want to cancel E-detailing","Are you sure?",r)

        }
    }

    fun onBackPressSuper()
    {
      super.onBackPressed()
    }

    fun stopPresentationAlert()
    {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.exitalert, null)
        dialogBuilder.setView(dialogView)

        val alertDialog: AlertDialog = dialogBuilder.create()
        alertDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val mainHeading_tv = dialogView.findViewById<View>(R.id.mainHeading_tv) as TextView
        val message_tv = dialogView.findViewById<View>(R.id.message_tv) as TextView
        val exit_btn = dialogView.findViewById<View>(R.id.exit_btn) as MaterialButton
        val cancel_btn =
            dialogView.findViewById<View>(R.id.cancel_btn) as MaterialButton

        exit_btn.setText("Stop")
        message_tv.setText("Are you sure you want to stop presentation?")
        mainHeading_tv.setText("Stop Presentation?")

        exit_btn.setOnClickListener{
            meetingShareHelper!!.stopShare()
            MeetingWindowHelper.getInstance().hideOrshow(false)
            alertDialog.dismiss()
            super.onBackPressed()
        }

        cancel_btn.setOnClickListener{
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onResume() {
        super.onResume()
        createConnectivity(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==2){
            setResult(3)
            finish();
        }
        if(resultCode==4){
            Log.e("rertetfefddsfdsf",data?.getStringExtra("apiDataDcr").toString())
                sendResumeString = data?.getStringExtra("apiDataDcr").toString()

        }
    }
}

