package `in`.processmaster.salestripclm.presentation_and_zoom

import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.activity.BaseActivity
import `in`.processmaster.salestripclm.fragments.DisplayVisualFragment
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.other.MeetingCommonCallback
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.remotecontrol.MeetingRemoteControlHelper
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.share.MeetingShareHelper
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.share.MeetingShareHelper.MeetingShareUICallBack
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.user.MeetingUserCallback
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.view.adapter.AttenderVideoAdapter
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.view.adapter.AttenderVideoAdapter.ItemClickListener
import `in`.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.view.share.CustomShareView
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import us.zoom.sdk.*

class OnlinePresentationActivity : BaseActivity(), View.OnClickListener, LifecycleObserver,
  MeetingUserCallback.UserEvent, MeetingCommonCallback.CommonEvent {

    private var TAG: String = OnlinePresentationActivity::class.java.getSimpleName()

    private var from = 0

    private var currentLayoutType = -1
    private var LAYOUT_TYPE_PREVIEW = 0
    private var LAYOUT_TYPE_WAITHOST = 1
    private var LAYOUT_TYPE_IN_WAIT_ROOM = 2
    private var LAYOUT_TYPE_ONLY_MYSELF = 3
    private var LAYOUT_TYPE_ONETOONE = 4
    private var LAYOUT_TYPE_LIST_VIDEO = 5
    private var LAYOUT_TYPE_VIEW_SHARE = 6
    private var LAYOUT_TYPE_SHARING_VIEW = 7
    private var LAYOUT_TYPE_WEBINAR_ATTENDEE = 8

    private var videoListLayout: LinearLayout? = null

    private var layout_lans: View? = null

    private var mMeetingFailed = false


    private var mDefaultVideoView: MobileRTCVideoView? = null
    private var mDefaultVideoViewMgr: MobileRTCVideoViewManager? = null

    private var meetingShareHelper: MeetingShareHelper? = null

    private var remoteControlHelper: MeetingRemoteControlHelper? = null

    private var mMeetingService: MeetingService? = null

    private var mInMeetingService: InMeetingService? = null


    private var customShareView: CustomShareView? = null

    private var mVideoListView: RecyclerView? = null

    private var mAdapter: AttenderVideoAdapter? = null



    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_online_presentation)

        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        mMeetingService = ZoomSDK.getInstance().meetingService
        mInMeetingService = ZoomSDK.getInstance().inMeetingService

        meetingShareHelper = MeetingShareHelper(this, shareCallBack)

        if (mMeetingService == null || mInMeetingService == null) {
            finish()
            return
        }

     //   if (null != intent.extras) {
     //       from = intent.extras!!.getInt("from")
     //   }

    //    var inflater = layoutInflater
//
    //   var mNormalSenceView = inflater.inflate(R.layout.layout_meeting_content_normal, null)
    //    mDefaultVideoView = mNormalSenceView.findViewById<View>(R.id.videoView) as MobileRTCVideoView
//
    //    customShareView = mNormalSenceView.findViewById<View>(R.id.custom_share_view) as CustomShareView
    //    remoteControlHelper = MeetingRemoteControlHelper(customShareView)
//
//
    //    mVideoListView = findViewById<View>(R.id.videoList) as RecyclerView
    //    mVideoListView!!.bringToFront()
//
    //    videoListLayout = findViewById(R.id.videoListLayoutt)
//
    //    layout_lans = findViewById(R.id.layout_lans)


        registerListener()

    //   mVideoListView!!.layoutManager = LinearLayoutManager(
    //       this,
    //       LinearLayoutManager.HORIZONTAL,
    //       false
    //   )
    //   mAdapter = AttenderVideoAdapter(
    //       this,
    //       windowManager.defaultDisplay.width,
    //       pinVideoListener
    //   )
    //   mVideoListView!!.adapter = mAdapter

      //  MeetingWindowHelper.getInstance().hiddenMeetingWindow(false)
      //  checkShowVideoLayout()
      //  meetingVideoHelper!!.checkVideoRotation(this)
      //  mDefaultVideoView!!.onResume()

        val fragment = DisplayVisualFragment()
        openFragment(fragment)

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);

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
        val bundle = Bundle()
        bundle.putString("type", "present")
        fragment.setArguments(bundle);

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

    private fun unRegisterListener()
    {
        try
        {
            MeetingUserCallback.getInstance().removeListener(this)
            MeetingCommonCallback.getInstance().removeListener(this)
        }
        catch (e: Exception)
        {

        }
    }

    var pinVideoListener =
        ItemClickListener { view, position, userId ->
            if (currentLayoutType == LAYOUT_TYPE_VIEW_SHARE || currentLayoutType == LAYOUT_TYPE_SHARING_VIEW) {
                return@ItemClickListener
            }
            mDefaultVideoViewMgr!!.removeAllVideoUnits()
            var renderInfo =
                MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
            mDefaultVideoViewMgr!!.addAttendeeVideoUnit(userId, renderInfo)
        }

    override fun onClick(v: View) {
        var id = v.id
        when (id) {

        }
    }

/*    private fun checkShowVideoLayout() {

        mDefaultVideoViewMgr = mDefaultVideoView!!.videoViewManager
        if (mDefaultVideoViewMgr != null) {
            var newLayoutType = getNewVideoMeetingLayout()
            if (currentLayoutType != newLayoutType || newLayoutType == LAYOUT_TYPE_WEBINAR_ATTENDEE) {
                removeOldLayout(currentLayoutType)
                currentLayoutType = newLayoutType
                addNewLayout(newLayoutType)
            }
        }
    }

    private fun getNewVideoMeetingLayout(): Int {
        var newLayoutType = -1
        if (mMeetingService!!.meetingStatus == MeetingStatus.MEETING_STATUS_WAITINGFORHOST) {
            newLayoutType = LAYOUT_TYPE_WAITHOST
            return newLayoutType
        }
        if (mMeetingService!!.meetingStatus == MeetingStatus.MEETING_STATUS_IN_WAITING_ROOM) {
            newLayoutType = LAYOUT_TYPE_IN_WAIT_ROOM
            return newLayoutType
        }

      else {
            var userlist =
                mInMeetingService!!.inMeetingUserList
            var userCount = 0
            if (userlist != null) {
                userCount = userlist.size
            }
            if (userCount > 1) {
                var preCount = userCount
                for (i in 0 until preCount) {
                    var userInfo =
                        mInMeetingService!!.getUserInfoById(userlist!![i])
                    if (mInMeetingService!!.isWebinarMeeting) {
                        if (userInfo != null && userInfo.inMeetingUserRole == InMeetingUserInfo.InMeetingUserRole.USERROLE_ATTENDEE) {
                            userCount--
                        }
                    }
                }
            }
            newLayoutType = if (userCount == 0) {
                LAYOUT_TYPE_PREVIEW
            } else if (userCount == 1) {
                LAYOUT_TYPE_ONLY_MYSELF
            } else {
                LAYOUT_TYPE_LIST_VIDEO
            }
        }
        return newLayoutType
    }

    private fun removeOldLayout(type: Int) {
        if (type == LAYOUT_TYPE_WAITHOST) {
        } else if (type == LAYOUT_TYPE_IN_WAIT_ROOM) {
        } else if (type == LAYOUT_TYPE_PREVIEW || type == LAYOUT_TYPE_ONLY_MYSELF || type == LAYOUT_TYPE_ONETOONE) {
            mDefaultVideoViewMgr!!.removeAllVideoUnits()
        } else if (type == LAYOUT_TYPE_LIST_VIDEO || type == LAYOUT_TYPE_VIEW_SHARE) {
            mDefaultVideoViewMgr!!.removeAllVideoUnits()
            mDefaultVideoView!!.setGestureDetectorEnabled(false)
        } else if (type == LAYOUT_TYPE_SHARING_VIEW) {
        }
        if (type != LAYOUT_TYPE_SHARING_VIEW) {
            if (null != customShareView) {
                customShareView!!.visibility = View.INVISIBLE
            }
        }
    }*/

/*    private fun addNewLayout(type: Int) {
        if (type == LAYOUT_TYPE_WAITHOST) {
        } else if (type == LAYOUT_TYPE_IN_WAIT_ROOM) {
       //     videoListLayout!!.visibility = View.GONE
        } else if (type == LAYOUT_TYPE_PREVIEW) {
            showPreviewLayout()
        } else if (type == LAYOUT_TYPE_ONLY_MYSELF || type == LAYOUT_TYPE_WEBINAR_ATTENDEE) {
            showOnlyMeLayout()
        } else if (type == LAYOUT_TYPE_ONETOONE) {
            showOne2OneLayout()
        } else if (type == LAYOUT_TYPE_LIST_VIDEO) {
            showVideoListLayout()
        } else if (type == LAYOUT_TYPE_VIEW_SHARE) {

        } else if (type == LAYOUT_TYPE_SHARING_VIEW)
        {
        }
    }

    private fun showPreviewLayout() {
        var renderInfo1 = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        mDefaultVideoView!!.visibility = View.VISIBLE
        mDefaultVideoViewMgr!!.addPreviewVideoUnit(renderInfo1)
      //  videoListLayout!!.visibility = View.GONE
    }*/

   /* private fun showOnlyMeLayout() {
        mDefaultVideoView!!.visibility = View.VISIBLE
     //   videoListLayout!!.visibility = View.GONE
        var renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        var myUserInfo = mInMeetingService!!.myUserInfo
        if (myUserInfo != null) {
            mDefaultVideoViewMgr!!.removeAllVideoUnits()

            mDefaultVideoViewMgr!!.addAttendeeVideoUnit(myUserInfo.userId, renderInfo)
        }
    }
*/
   /* private fun showOne2OneLayout() {
        mDefaultVideoView!!.visibility = View.VISIBLE
        videoListLayout!!.visibility = View.VISIBLE
        var renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        //options.aspect_mode = MobileRTCVideoUnitAspectMode.VIDEO_ASPECT_PAN_AND_SCAN;
        mDefaultVideoViewMgr!!.addActiveVideoUnit(renderInfo)
        mAdapter!!.setUserList(mInMeetingService!!.inMeetingUserList)
        mAdapter!!.notifyDataSetChanged()
    }*/

  /*  fun showVideoListLayout()
    {
        var renderInfo = MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
        //options.aspect_mode = MobileRTCVideoUnitAspectMode.VIDEO_ASPECT_PAN_AND_SCAN;
        mDefaultVideoViewMgr!!.addActiveVideoUnit(renderInfo)
      //  videoListLayout?.visibility = View.VISIBLE
        updateAttendeeVideos(mInMeetingService!!.inMeetingUserList, 0)
    }*/

    fun updateAttendeeVideos(
        userlist: List<Long>,
        action: Int)
     {
        if (action == 0) {
            MeetingWindowHelper.getInstance().showMeetingWindow(this)

            // mAdapter!!.setUserList(userlist)
           // mAdapter!!.notifyDataSetChanged()
        } else if (action == 1)
        {
            MeetingWindowHelper.getInstance().showMeetingWindow(this)

            //  mAdapter!!.addUserList(userlist)
          //  mAdapter!!.notifyDataSetChanged()
        }
        else {
            MeetingWindowHelper.getInstance().hiddenMeetingWindow(true)

            //  var userId = mAdapter!!.selectedUserId
          //  if (userlist.contains(userId)) {
          //      var inmeetingUserList =
          //          mInMeetingService!!.inMeetingUserList
          //      if (inmeetingUserList.size > 0) {
          //          mDefaultVideoViewMgr!!.removeAllVideoUnits()
          //          var renderInfo =
          //              MobileRTCVideoUnitRenderInfo(0, 0, 100, 100)
          //          mDefaultVideoViewMgr!!.addAttendeeVideoUnit(inmeetingUserList[0], renderInfo)
          //      }
          //  }
          //  mAdapter!!.removeUserList(userlist)
        }
    }


    override fun onPause() {
        super.onPause()
        if (mMeetingService == null || mInMeetingService == null) {
            return
        }
        stopConnectivity(this)

       // mDefaultVideoView!!.onPause()
    }

    override fun onStop() {
        super.onStop()
        if (mMeetingService == null || mInMeetingService == null) {
            return
        }
    }

    override fun onDestroy() {
        super.onDestroy()
      /*  if (null != remoteControlHelper) {
            remoteControlHelper!!.onDestroy()
        }*/
            // unRegisterListener()


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

       if(meetingShareHelper?.isSharingOut!!)
       {
           stopPresentationAlert()
       }
        else{
           super.onBackPressed()
       }

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



}

