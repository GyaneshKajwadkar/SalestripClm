package in.processmaster.salestripclm.activity;

import android.app.AlertDialog;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import in.processmaster.salestripclm.networkUtils.ConnectivityChangeReceiver;
import in.processmaster.salestripclm.R;
import in.processmaster.salestripclm.adapter.ScheduleMeetingAdapter;
import in.processmaster.salestripclm.fragments.JoinMeetingFragment;
import in.processmaster.salestripclm.models.GetScheduleModel;
import in.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper;
import in.processmaster.salestripclm.zoom_sdk_code.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper;
import in.processmaster.salestripclm.zoom_sdk_code.startjoinmeeting.UserLoginCallback;
import in.processmaster.salestripclm.zoom_sdk_code.startjoinmeeting.emailloginuser.EmailUserLoginHelper;
import in.processmaster.salestripclm.utils.DatabaseHandler;
import us.zoom.sdk.CustomizedMiniMeetingViewSize;
import us.zoom.sdk.IBOAssistant;
import us.zoom.sdk.IBOAttendee;
import us.zoom.sdk.InMeetingBOController;
import us.zoom.sdk.InMeetingService;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingItem;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.PreMeetingService;
import us.zoom.sdk.PreMeetingServiceListener;
import us.zoom.sdk.StartMeetingParams;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdk.ZoomSDKInitializeListener;


public class JoinMeetingActivity extends BaseActivity implements MeetingServiceListener, View.OnClickListener, UserLoginCallback.ZoomDemoAuthenticationListener, PreMeetingServiceListener, ZoomSDKAuthenticationListener, ZoomSDKInitializeListener {

    private final static String TAG = "ZoomSDKExample";

      ScheduleMeetingAdapter adapterRecycler;
    ArrayList<MeetingItem> meetingListMain= new ArrayList<>();
    ConnectivityChangeReceiver  connectivityChangeReceiver= new ConnectivityChangeReceiver();

    private boolean mbPendingStartMeeting = false;

    private ZoomSDK zoomSDK;
    private InMeetingService mInMeetingService;
    private RecyclerView sheduled_rv;
    private ProgressBar scheduledProgess;
    ImageView scheduledBack_iv;
    private boolean isResumed = false;
    FrameLayout frameZoom;
    private MeetingService mMeetingService;
    MeetingItem meetingItemGlobal;
    DrawerLayout meetingsDrawer;
    ImageView menu_img;
    ImageView zoomImageView;
    LinearLayout parentToolbar;
    TextView noData_tv;
    ProgressDialog dialog ;
    public final static int REQUEST_SHARE_SCREEN_PERMISSION = 1001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.join_activity_view);
        sheduled_rv = findViewById(R.id.sheduled_rv);
        scheduledProgess = findViewById(R.id.scheduledProgess);
        scheduledBack_iv = findViewById(R.id.scheduledBack_iv);
        frameZoom = findViewById(R.id.frameZoom);
        meetingsDrawer = findViewById(R.id.meetingsDrawer);
        noData_tv = findViewById(R.id.noData_tv);
        menu_img = findViewById(R.id.menu_img);
        parentToolbar = findViewById(R.id.parentToolbar);
        zoomImageView = findViewById(R.id.zoomImageView);

        meetingsDrawer.openDrawer(Gravity.RIGHT);

        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!meetingsDrawer.isDrawerOpen(GravityCompat.START))
                {
                    meetingsDrawer.openDrawer(GravityCompat.END);
                }
                else
                {
                    meetingsDrawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        zoomSDK = ZoomSDK.getInstance();
        if(!zoomSDK.isLoggedIn())
        {
            getCredientail_api(this);
        }
        else
        {
            initilizeZoom();
        }


        scheduledProgess.setVisibility(View.VISIBLE);

        scheduledBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });

        setScheduleAdapter();
    }

    public void initilizeZoom()
   {
       registerListener();
       if (zoomSDK.isInitialized())
            {
                zoomSDK.getMeetingService().addListener(this);
                zoomSDK.getMeetingSettingsHelper().enable720p(true);
            }

            mInMeetingService =zoomSDK.getInMeetingService();

            if(zoomSDK.isInitialized()) {
                PreMeetingService preMeetingService = zoomSDK.getPreMeetingService();
                if(preMeetingService != null)
                {
                    preMeetingService.listMeeting();
                    preMeetingService.addListener(this);
                }
                else
                {
                    Toast.makeText(this, "User not login.", Toast.LENGTH_LONG).show();
                }
            }

        mMeetingService = zoomSDK.getMeetingService();
       zoomSDK.getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);


    }


    public void setScheduleAdapter( )
    {

        String responseData= new DatabaseHandler(this).getApiDetail(2);

        if(!responseData.equals(""))
        {
            GetScheduleModel getScheduleModel= new Gson().fromJson(responseData, GetScheduleModel.class);
            ArrayList<GetScheduleModel.Data.Meeting>meetinglist=new ArrayList<>();
            if(getScheduleModel.getData().getMeetingList()==null)
            {
                scheduledProgess.setVisibility(View.GONE);
                return;
            }

            for(int i=0;i<getScheduleModel.getData().getMeetingList().size();i++)
            {
                if(getScheduleModel.getData().getMeetingList().get(i).getMeetingType().equals("O"))
                {
                    meetinglist.add(getScheduleModel.getData().getMeetingList().get(i));
                }
            }
             adapterRecycler= new ScheduleMeetingAdapter(this,1,meetinglist,zoomSDK);
            sheduled_rv.setLayoutManager(new LinearLayoutManager(this));
            sheduled_rv.setAdapter(adapterRecycler);
            adapterRecycler.notifyDataSetChanged();
            scheduledProgess.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed() {

        if(mMeetingService!=null)
        {
            if (mMeetingService.getMeetingStatus() == MeetingStatus.MEETING_STATUS_INMEETING)
            {
                showLeaveMeetingDialog();
                return;
            }
        }

        super.onBackPressed();
    }



    private void registerListener() {
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            this.unregisterReceiver(connectivityChangeReceiver);
        } catch ( Exception e)
        { // already unregistered
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(connectivityChangeReceiver, intentFilter);
        setScheduleAdapter( );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SHARE_SCREEN_PERMISSION:
                if (resultCode != RESULT_OK) {
                    Log.d(TAG, "onActivityResult REQUEST_SHARE_SCREEN_PERMISSION no ok ");
                    break;
                }
                break;
        }

        MeetingWindowHelper.getInstance().onActivityResult(requestCode, this);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDestroy() {

        onClickLeave();
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (zoomSDK.isInitialized())
        {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);//unregister meetingServiceListener
        }

        MeetingWindowHelper.getInstance().removeOverlayListener();

        super.onDestroy();
    }

    public void onClickLeave() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if(meetingService != null) {
            meetingService.leaveCurrentMeeting(false);
        }
        finish();
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnStartMeeting) {
            onClickBtnStartMeeting();
        }
    }


    public void onClickBtnStartMeeting() {

        int ret= EmailUserLoginHelper.getInstance().login("anilpratapjain@gmail.com", "*Keepchange2202");
        if(!(ret== ZoomApiError.ZOOM_API_ERROR_SUCCESS)) {
            if (ret == ZoomApiError.ZOOM_API_ERROR_EMAIL_LOGIN_IS_DISABLED) {
                Toast.makeText(this, "Account had disable email login ", Toast.LENGTH_LONG).show();
            }

            scheduledProgess.setVisibility(View.GONE);
        }

    }


    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {

        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
            parentToolbar.setVisibility(View.VISIBLE);
            meetingsDrawer.closeDrawer(Gravity.RIGHT);
            zoomImageView.setVisibility(View.VISIBLE);
            frameZoom.setVisibility(View.GONE);
        }

        if(meetingStatus == MeetingStatus.MEETING_STATUS_IDLE)
        {
            meetingsDrawer.openDrawer(GravityCompat.END);
            parentToolbar.setVisibility(View.VISIBLE);
            zoomImageView.setVisibility(View.VISIBLE);
            frameZoom.setVisibility(View.GONE);
            if(meetingItemGlobal!=null)
            {
                onClickBtnStart(meetingItemGlobal);
            }
        }


        if (mbPendingStartMeeting && meetingStatus == MeetingStatus.MEETING_STATUS_IDLE) {
            mbPendingStartMeeting = false;
        }
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            if (ZoomMeetingUISettingHelper.useExternalVideoSource) {
                ZoomMeetingUISettingHelper.changeVideoSource(true);
            }
            showMeetingUi();
            zoomImageView.setVisibility(View.GONE);
            meetingsDrawer.closeDrawer(Gravity.RIGHT);
            frameZoom.setVisibility(View.VISIBLE);

        }
    }


    private void showMeetingUi() {
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            SharedPreferences sharedPreferences = getSharedPreferences("UI_Setting", Context.MODE_PRIVATE);
            boolean enable = sharedPreferences.getBoolean("enable_rawdata", false);
            if (!enable) {

                Bundle bundle = new Bundle();
                bundle.putInt("from", JoinMeetingFragment.JOIN_FROM_LOGIN);

                Fragment fragment= new JoinMeetingFragment(zoomSDK);
                fragment.setArguments(bundle);

                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.frameZoom,fragment,null);
                transaction.commit();
            }
            else {
            }

        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            ZoomSDK.getInstance().getZoomUIService().enableMinimizeMeeting(true);
            ZoomSDK.getInstance().getZoomUIService().setMiniMeetingViewSize(new CustomizedMiniMeetingViewSize(0, 0, 360, 540));
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(false);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enableShowMyMeetingElapseTime(true);
            ZoomSDK.getInstance().getMeetingService().addListener(this);
            Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
          //  callApis();
        }
    }

    @Override
    public void onZoomSDKLoginResult(long result) {
        if(result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
            scheduledProgess.setVisibility(View.GONE);
        }
        else {
            Toast.makeText(this, "Login failed result code = " + result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onZoomSDKLogoutResult(long result) {
        if(result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Logout failed result code = " + result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onZoomIdentityExpired() {

    }

    @Override
    public void onZoomAuthIdentityExpired() {

    }

    @Override
    public void onListMeeting(int result, List<Long> meetingList) {
        Log.i(TAG, "onListMeeting, result =" + meetingList.size());
        meetingListMain.clear();

        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        PreMeetingService preMeetingService = zoomSDK.getPreMeetingService();
        if(preMeetingService != null) {
            if (meetingList != null) {
                for (long meetingUniqueId : meetingList) {
                    MeetingItem item = preMeetingService.getMeetingItemByUniqueId(meetingUniqueId);
                    if(item != null) {
                        meetingListMain.add(item);
                    }
                    Log.d(TAG,item.getMeetingTopic()+":"+item.asyncGetInviteEmailContent());
                }
            }
        }

        if(meetingListMain.size()!=0)
        {
            meetingListMain.remove(0);
        }
        if(meetingListMain.size()==0)
        {
        }
        scheduledProgess.setVisibility(View.GONE);

    }

    @Override
    public void onScheduleMeeting(int i, long l) {

    }

    @Override
    public void onUpdateMeeting(int i, long l) {

    }

    @Override
    public void onDeleteMeeting(int i) {

    }

    @Override
    public void onGetInviteEmailContent(int i, long l, String s) {

    }

    private void showLeaveMeetingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (mInMeetingService.isMeetingConnected()) {
            if (mInMeetingService.isMeetingHost()) {
                builder.setTitle("End or leave meeting")
                        .setPositiveButton("End", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leave(true);
                            }
                        }).setNeutralButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leave(false);
                    }
                });
            } else {
                builder.setTitle("Leave meeting")
                        .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leave(false);
                            }
                        });
            }
        } else {
            builder.setTitle("Leave meeting")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            leave(false);
                        }
                    });
        }
        if (mInMeetingService.getInMeetingBOController().isInBOMeeting()) {
            builder.setNegativeButton("Leave BO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    leaveBo();
                }
            });
        } else {
            builder.setNegativeButton("Cancel", null);
        }
        builder.create().show();
    }

    private void leaveBo(){
        InMeetingBOController boController = mInMeetingService.getInMeetingBOController();
        IBOAssistant iboAssistant = boController.getBOAssistantHelper();
        if (iboAssistant != null) {
            iboAssistant.leaveBO();
        } else {
            IBOAttendee boAttendee = boController.getBOAttendeeHelper();
            if (boAttendee != null)
            {
                boAttendee.leaveBo();
            }else {
                leave(false);
            }
        }
    }

    private void leave(boolean end) {
        mInMeetingService.leaveCurrentMeeting(end);
    }

    void onClickBtnStart(MeetingItem item) {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if(zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            if(meetingService != null) {
                StartMeetingParams params = new StartMeetingParams();
                params.meetingNo = item.getMeetingNumber() + "";
                meetingService.startMeetingWithParams(this, params, ZoomMeetingUISettingHelper.getStartMeetingOptions());
            }
        }
    }


    public void enableFullScreen()
    {
        LinearLayout layout=findViewById(R.id.parentToolbar);
        if(parentToolbar.getVisibility()==View.VISIBLE)
        {
            parentToolbar.setVisibility(View.GONE);
        }
        else
        {
            parentToolbar.setVisibility(View.VISIBLE);
        }
    }

    }

