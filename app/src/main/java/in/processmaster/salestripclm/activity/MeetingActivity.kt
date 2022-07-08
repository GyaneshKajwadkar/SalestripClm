package `in`.processmaster.salestripclm.activity
import `in`.processmaster.salestripclm.R
import `in`.processmaster.salestripclm.adapter.ScheduleMeetingAdapter
import `in`.processmaster.salestripclm.common_classes.AlertClass
import `in`.processmaster.salestripclm.fragments.JoinMeetingFragment
import `in`.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper
import `in`.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper
import `in`.processmaster.salestripclm.zoom_sdk_code.startjoinmeeting.UserLoginCallback
import android.app.AlertDialog
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import us.zoom.sdk.*
import java.util.ArrayList
import `in`.processmaster.salestripclm.models.GetScheduleModel
import kotlinx.android.synthetic.main.join_activity_view.*


class MeetingActivity : BaseActivity(), MeetingServiceListener, View.OnClickListener,
    UserLoginCallback.ZoomDemoAuthenticationListener, PreMeetingServiceListener, ZoomSDKAuthenticationListener,
    ZoomSDKInitializeListener {

    private val TAG = "MeetingActivity"
    var adapterRecycler: ScheduleMeetingAdapter? = null
    var meetingListMain = ArrayList<MeetingItem>()
    var connectivityChangeReceiver = ConnectivityChangeReceiver()
    private var mbPendingStartMeeting = false
    lateinit  var zoomSDK: ZoomSDK
    private var mInMeetingService: InMeetingService? = null
    private var isResumed = false
    private var mMeetingService: MeetingService? = null
    var meetingItemGlobal: MeetingItem? = null
    @JvmField  val REQUEST_SHARE_SCREEN_PERMISSION = 1001


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.join_activity_view)


        if(sharePreferanceBase?.getPrefBool("zoomCrediential") == true)
        {
            val r: Runnable = object : Runnable {
                override fun run() {
                    if(AlertClass.retunDialog) {
                        finish()
                    }}}
            alertClass.commonAlertWithRunnable("Zoom Alert","Zoom account is not created. Contact to your administration for more info",r)
            return
        }

        meetingsDrawer.openDrawer(Gravity.RIGHT)

        menu_img.setOnClickListener(View.OnClickListener {
            if (!meetingsDrawer.isDrawerOpen(GravityCompat.START)) {
                meetingsDrawer.openDrawer(GravityCompat.END)
            } else {
                meetingsDrawer.closeDrawer(GravityCompat.START)
            }
        })

        zoomSDK = ZoomSDK.getInstance()
        if (!zoomSDK.isLoggedIn()) {
            getCredientail_api(this)
        } else {
            initilizeZoom()
        }

        if(generalClass.isInternetAvailable())
        { scheduledProgess.setVisibility(View.VISIBLE) }


        scheduledBack_iv?.setOnClickListener(View.OnClickListener { onBackPressed() })

        setScheduleAdapter()
    }

   public fun initilizeZoom() {
        registerListener()
        if (zoomSDK?.isInitialized) {
            zoomSDK?.meetingService.addListener(this)
            zoomSDK?.meetingSettingsHelper.enable720p(true)
        }
        mInMeetingService = zoomSDK?.inMeetingService
        if (zoomSDK?.isInitialized) {
            val preMeetingService = zoomSDK?.preMeetingService
            if (preMeetingService != null) {
                preMeetingService.listMeeting()
                preMeetingService.addListener(this)
            } else {
                Toast.makeText(this, "User not login.", Toast.LENGTH_LONG).show()
            }
        }
        mMeetingService = zoomSDK?.meetingService
        zoomSDK?.meetingSettingsHelper.isCustomizedMeetingUIEnabled = true
    }


    fun setScheduleAdapter() {
        try {
            if(!zoomSDK.isInitialized)return
        }
        catch (e:Exception){return}

        val responseData: String = dbBase!!?.getApiDetail(2)
        if (responseData != "") {
            val getScheduleModel: GetScheduleModel = Gson().fromJson<GetScheduleModel>(
                responseData,
                GetScheduleModel::class.java
            )
            val meetinglist: ArrayList<GetScheduleModel.Data.Meeting> = ArrayList<GetScheduleModel.Data.Meeting>()
            if (getScheduleModel.getData()?.meetingList == null) {
                scheduledProgess?.visibility = View.GONE
                return
            }
            for (i in getScheduleModel.getData()?.meetingList?.indices!!) {
                if (getScheduleModel.getData()?.meetingList?.get(i)?.meetingType == "O") {
                    getScheduleModel.getData()?.meetingList?.get(i)?.let { meetinglist.add(it) }
                }
            }
            if(meetinglist.size!=0){noData_tv.visibility=View.GONE}

            adapterRecycler = ScheduleMeetingAdapter(this, 1, meetinglist, zoomSDK)
            sheduled_rv?.layoutManager = LinearLayoutManager(this)
            sheduled_rv?.adapter = adapterRecycler
            adapterRecycler?.notifyDataSetChanged()
            scheduledProgess?.visibility = View.GONE
        }
    }


    override fun onBackPressed() {
        if (mMeetingService != null) {
            if (mMeetingService?.getMeetingStatus() == MeetingStatus.MEETING_STATUS_INMEETING) {
                showLeaveMeetingDialog()
                return
            }
        }
        super.onBackPressed()
    }


    private fun registerListener() {
        val meetingService = zoomSDK?.meetingService
        meetingService?.addListener(this)
    }


    override fun onPause() {
        super.onPause()
        try {
            unregisterReceiver(connectivityChangeReceiver)
        } catch (e: Exception) { // already unregistered
        }
    }

    override fun onResume() {
        super.onResume()
        isResumed = true
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        this.registerReceiver(connectivityChangeReceiver, intentFilter)
        setScheduleAdapter()
        alertClass = AlertClass(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_SHARE_SCREEN_PERMISSION -> if (resultCode != RESULT_OK) {
                Log.d(
                    TAG,
                    "onActivityResult REQUEST_SHARE_SCREEN_PERMISSION no ok "
                )

            }
        }
        MeetingWindowHelper.getInstance().onActivityResult(requestCode, this)
        for (fragment in supportFragmentManager.fragments) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onDestroy() {
        onClickLeave()
        val zoomSDK = ZoomSDK.getInstance()
        if (zoomSDK.isInitialized) {
            val meetingService = zoomSDK.meetingService
            meetingService.removeListener(this) //unregister meetingServiceListener
        }
        MeetingWindowHelper.getInstance().removeOverlayListener()
        super.onDestroy()
    }

    fun onClickLeave() {
        val zoomSDK = ZoomSDK.getInstance()
        val meetingService = zoomSDK.meetingService
        meetingService?.leaveCurrentMeeting(false)
        finish()
    }


    override fun onClick(v: View) {
        if (v.id == R.id.btnStartMeeting) {
            onClickBtnStartMeeting()
        }
    }


    fun onClickBtnStartMeeting() {
     /*   val ret: Int =
            EmailUserLoginHelper.getInstance().login("anilpratapjain@gmail.com", "*Keepchange2202")
        if (ret != ZoomApiError.ZOOM_API_ERROR_SUCCESS) {
            if (ret == ZoomApiError.ZOOM_API_ERROR_EMAIL_LOGIN_IS_DISABLED) {
                Toast.makeText(this, "Account had disable email login ", Toast.LENGTH_LONG).show()
            }
            scheduledProgess!!.visibility = View.GONE
        }*/
    }


    override fun onMeetingStatusChanged(
        meetingStatus: MeetingStatus, errorCode: Int,
        internalErrorCode: Int
    ) {
        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show()
            parentToolbar?.visibility = View.VISIBLE
            meetingsDrawer?.closeDrawer(Gravity.RIGHT)
            zoomImageView?.visibility = View.VISIBLE
            frameZoom?.visibility = View.GONE
        }
        if (meetingStatus == MeetingStatus.MEETING_STATUS_IDLE) {
            meetingsDrawer?.openDrawer(GravityCompat.END)
            parentToolbar?.visibility = View.VISIBLE
            zoomImageView?.visibility = View.VISIBLE
            frameZoom?.visibility = View.GONE
            if (meetingItemGlobal != null) {
                onClickBtnStart(meetingItemGlobal!!)
            }
        }
        if (mbPendingStartMeeting && meetingStatus == MeetingStatus.MEETING_STATUS_IDLE) {
            mbPendingStartMeeting = false
        }
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            if (ZoomMeetingUISettingHelper.useExternalVideoSource) {
                ZoomMeetingUISettingHelper.changeVideoSource(true)
            }
            showMeetingUi()
            zoomImageView?.visibility = View.GONE
            meetingsDrawer?.closeDrawer(Gravity.RIGHT)
            frameZoom?.visibility = View.VISIBLE
        }
    }


    private fun showMeetingUi() {
        if (ZoomSDK.getInstance().meetingSettingsHelper.isCustomizedMeetingUIEnabled) {
            val sharedPreferences = getSharedPreferences("UI_Setting", MODE_PRIVATE)
            val enable = sharedPreferences.getBoolean("enable_rawdata", false)
            if (!enable) {
                val bundle = Bundle()
                bundle.putInt("from", JoinMeetingFragment.JOIN_FROM_LOGIN)
                val fragment: Fragment = JoinMeetingFragment(zoomSDK)
                fragment.arguments = bundle
                val manager = supportFragmentManager
                val transaction = manager.beginTransaction()
                transaction.add(R.id.frameZoom, fragment, null)
                transaction.commit()
            } else {
            }
        }
    }

    override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(
                this,
                "Failed to initialize Zoom SDK. Error: $errorCode, internalErrorCode=$internalErrorCode",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ZoomSDK.getInstance().zoomUIService.enableMinimizeMeeting(true)
            ZoomSDK.getInstance().zoomUIService.setMiniMeetingViewSize(
                CustomizedMiniMeetingViewSize(
                    0,
                    0,
                    360,
                    540
                )
            )
            ZoomSDK.getInstance().meetingSettingsHelper.enable720p(false)
            ZoomSDK.getInstance().meetingSettingsHelper.enableShowMyMeetingElapseTime(true)
            ZoomSDK.getInstance().meetingService.addListener(this)
            Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show()
            //  callApis();
        }
    }

    override fun onZoomSDKLoginResult(result: Long) {
        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS.toLong()) {
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show()
            scheduledProgess?.visibility = View.GONE
        } else {
            Toast.makeText(this, "Login failed result code = $result", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onZoomSDKLogoutResult(result: Long) {
        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS.toLong()) {
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Logout failed result code = $result", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onZoomIdentityExpired() {}

    override fun onZoomAuthIdentityExpired() {}

    override fun onListMeeting(result: Int, meetingList: List<Long>?) {
        Log.i(TAG, "onListMeeting, result =" + meetingList?.size)
        meetingListMain.clear()
        val zoomSDK = ZoomSDK.getInstance()
        val preMeetingService = zoomSDK.preMeetingService
        if (preMeetingService != null) {
            if (meetingList != null) {
                for (meetingUniqueId in meetingList) {
                    val item = preMeetingService.getMeetingItemByUniqueId(meetingUniqueId)
                    if (item != null) {
                        meetingListMain.add(item)
                    }
                    Log.d(
                        TAG,
                        item?.meetingTopic + ":" + item.asyncGetInviteEmailContent()
                    )
                }
            }
        }
        if (meetingListMain.size != 0) {
            meetingListMain.removeAt(0)
        }
        if (meetingListMain.size == 0) {
        }
        scheduledProgess?.visibility = View.GONE
    }

    override fun onScheduleMeeting(i: Int, l: Long) {}

    override fun onUpdateMeeting(i: Int, l: Long) {}

    override fun onDeleteMeeting(i: Int) {}

    override fun onGetInviteEmailContent(i: Int, l: Long, s: String?) {}

    private fun showLeaveMeetingDialog() {
        val builder = AlertDialog.Builder(this)
        if (mInMeetingService?.isMeetingConnected == true) {
            if (mInMeetingService?.isMeetingHost == true) {
                builder.setTitle("End or leave meeting")
                    .setPositiveButton(
                        "End"
                    ) { dialog, which -> leave(true) }.setNeutralButton(
                        "Leave"
                    ) { dialog, which -> leave(false) }
            } else {
                builder.setTitle("Leave meeting")
                    .setPositiveButton(
                        "Leave"
                    ) { dialog, which -> leave(false) }
            }
        } else {
            builder.setTitle("Leave meeting")
                .setPositiveButton(
                    "Leave"
                ) { dialog, which -> leave(false) }
        }
        if (mInMeetingService?.inMeetingBOController?.isInBOMeeting == true) {
            builder.setNegativeButton(
                "Leave BO"
            ) { dialog, which -> leaveBo() }
        } else {
            builder.setNegativeButton("Cancel", null)
        }
        builder.create().show()
    }

    private fun leaveBo() {
        val boController = mInMeetingService?.inMeetingBOController
        val iboAssistant = boController?.boAssistantHelper
        if (iboAssistant != null) {
            iboAssistant.leaveBO()
        } else {
            val boAttendee = boController?.boAttendeeHelper
            boAttendee?.leaveBo() ?: leave(false)
        }
    }

    private fun leave(end: Boolean) {
        mInMeetingService?.leaveCurrentMeeting(end)
    }

    fun onClickBtnStart(item: MeetingItem) {
        val zoomSDK = ZoomSDK.getInstance()
        if (zoomSDK.isInitialized) {
            val meetingService = zoomSDK.meetingService
            if (meetingService != null) {
                val params = StartMeetingParams()
                params.meetingNo = item.meetingNumber.toString() + ""
                meetingService.startMeetingWithParams(
                    this,
                    params,
                    ZoomMeetingUISettingHelper.getStartMeetingOptions()
                )
            }
        }
    }


    fun enableFullScreen() {
        val layout = findViewById<LinearLayout>(R.id.parentToolbar)
        if (parentToolbar?.visibility == View.VISIBLE) {
            parentToolbar?.visibility = View.GONE
        } else {
            parentToolbar?.visibility = View.VISIBLE
        }
    }

}

