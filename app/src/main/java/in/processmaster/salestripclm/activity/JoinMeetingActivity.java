package in.processmaster.salestripclm.activity;

import android.app.Activity;
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
import in.processmaster.salestripclm.ConnectivityChangeReceiver;
import in.processmaster.salestripclm.R;
import in.processmaster.salestripclm.adapter.ScheduleMeetingAdapter;
import in.processmaster.salestripclm.fragments.JoinMeetingFragment;
import in.processmaster.salestripclm.models.GetScheduleModel;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.RawDataMeetingActivity;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.SimpleZoomUIDelegate;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.view.MeetingOptionBar;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper;
import in.processmaster.salestripclm.sdksampleapp.startjoinmeeting.UserLoginCallback;
import in.processmaster.salestripclm.sdksampleapp.startjoinmeeting.emailloginuser.EmailUserLoginHelper;
import in.processmaster.salestripclm.sdksampleapp.ui.InitAuthSDKActivity;
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
import us.zoom.sdk.MobileRTCShareView;
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

    private View mProgressPanel;
    private MobileRTCShareView mShareView;

    ArrayList<MeetingItem> meetingListMain= new ArrayList<>();
    ConnectivityChangeReceiver  connectivityChangeReceiver= new ConnectivityChangeReceiver();

    private boolean mbPendingStartMeeting = false;

    private ZoomSDK zoomSDK;
    private InMeetingService mInMeetingService;
  // private MeetingShareHelper meetingShareHelper;
    MeetingOptionBar meetingOptionBar;
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

        mProgressPanel = (View) findViewById(R.id.progressPanel);
        mShareView = (MobileRTCShareView) findViewById(R.id.sharingView);

        meetingsDrawer.openDrawer(Gravity.RIGHT);

        menu_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!meetingsDrawer.isDrawerOpen(GravityCompat.START))
                {
                    meetingsDrawer.openDrawer(GravityCompat.END);
                }
                else {
                    meetingsDrawer.closeDrawer(GravityCompat.START);
                }
            }
        });

        zoomSDK = ZoomSDK.getInstance();
        if(!zoomSDK.isLoggedIn())
        {
           getCredientail_api(this);
        }
        else{
            initilizeZoom();
        }



       // UserLoginCallback.getInstance().addListener(this);fo

     /*   int ret=EmailUserLoginHelper.getInstance().login("kajwadkar13@gmail.com", "13Zoom@003");
        if(!(ret== ZoomApiError.ZOOM_API_ERROR_SUCCESS)) {
            if (ret == ZoomApiError.ZOOM_API_ERROR_EMAIL_LOGIN_IS_DISABLED) {
               // Toast.makeText(this, "Account had disable email login ", Toast.LENGTH_LONG).show();
            } else {
              //  Toast.makeText(this, "ZoomSDK has not been initialized successfully or sdk is logging in.", Toast.LENGTH_LONG).show();
            }
        } else {

        }*/

      //  Fragment fragmentS1 = new MyMeetingFrag();
      //  getSupportFragmentManager().beginTransaction().replace(R.id.frameZoom, fragmentS1).commit();




     //   meetingShareHelper = new MeetingShareHelper(this, shareCallBack);




        scheduledProgess.setVisibility(View.VISIBLE);

      // onClickBtnStartMeeting();

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


    private void setScheduleAdapter()
    {

        String responseData= new DatabaseHandler(this).getApiDetail(1);

        if(!responseData.equals(""))
        {
            GetScheduleModel getScheduleModel= new Gson().fromJson(responseData, GetScheduleModel.class);
            ArrayList<GetScheduleModel.Data.Meeting>meetinglist=new ArrayList<>();
            for(int i=0;i<getScheduleModel.getData().getMeetingList().size();i++)
            {
                if(getScheduleModel.getData().getMeetingList().get(i).getMeetingType().equals("O"))
                {
                    meetinglist.add(getScheduleModel.getData().getMeetingList().get(i));
                }
            }
            ScheduleMeetingAdapter adapterRecycler= new ScheduleMeetingAdapter(this,1,meetinglist,zoomSDK);
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




 /*   private void registerListener() {
        MeetingService meetingService = ZoomSDK.getInstance().getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);//register meetingServiceListener
        }
    }*/


    private void registerListener() {
        //  zoomSDK.addAuthenticationListener(this);
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
       // MeetingWindowHelper.getInstance().hiddenMeetingWindow(true);
       // ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);

        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(connectivityChangeReceiver, intentFilter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MeetingWindowHelper.getInstance().onActivityResult(requestCode, this);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }


    private void refreshUI() {
        if(null== ZoomSDK.getInstance().getMeetingService())
        {
            return;
        }
        MeetingStatus meetingStatus = ZoomSDK.getInstance().getMeetingService().getMeetingStatus();
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING || meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING
                || meetingStatus == MeetingStatus.MEETING_STATUS_RECONNECTING) {
         //   mBtnSettings.setVisibility(View.GONE);
         //   mReturnMeeting.setVisibility(View.VISIBLE);
        } else {
         //   mBtnSettings.setVisibility(View.VISIBLE);
         //   mReturnMeeting.setVisibility(View.GONE);
        }
     /*   if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING && isResumed) {
                MeetingWindowHelper.getInstance().showMeetingWindow(this);
            } else {
                MeetingWindowHelper.getInstance().hiddenMeetingWindow(true);
            }
        }*/

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
              //  Log.i(TAG, "log" + "1");
                Toast.makeText(this, "Account had disable email login ", Toast.LENGTH_LONG).show();
            }
            else
            {
                //  Toast.makeText(this, "ZoomSDK has not been initialized successfully or sdk is logging in.", Toast.LENGTH_LONG).show();
                //     int retLogin = LoginUserStartMeetingHelper.getInstance().startInstanceMeeting(this);
            }
            scheduledProgess.setVisibility(View.GONE);
        }

  /*      String meetingNo = mEdtMeetingNo.getText().toString().trim();

        String userId=mEdtUserId.getText().toString().trim();
        String zak=mEdtZak.getText().toString().trim();

        if (TextUtils.isEmpty(userId)) {
            Toast.makeText(this, "You need to enter user id", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(zak)) {
            Toast.makeText(this, "You need to enter zoom access token(zak)", Toast.LENGTH_LONG).show();
            return;
        }


        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        final MeetingService meetingService = zoomSDK.getMeetingService();

        if (meetingService.getMeetingStatus() != MeetingStatus.MEETING_STATUS_IDLE) {
            long lMeetingNo = 0;
            try {
                lMeetingNo = Long.parseLong(meetingNo);
            }
            catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid meeting number: " + meetingNo, Toast.LENGTH_LONG).show();
                return;
            }

            if (meetingService.getCurrentRtcMeetingNumber() == lMeetingNo) {
                meetingService.returnToMeeting(this);
                return;
            }

            new AlertDialog.Builder(this)
                    .setMessage("Do you want to leave current meeting and start another?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mbPendingStartMeeting = true;
                            meetingService.leaveCurrentMeeting(false);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            Log.e("yesThisTelling","yesThisT");
            return;

        }

        int ret = -1;
            ret = ApiUserStartMeetingHelper.getInstance().startMeetingWithNumber(this, meetingNo,userId,zak);
            Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);*/
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
            //  onClickBtnStartMeeting(null);
        }
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            if (ZoomMeetingUISettingHelper.useExternalVideoSource) {
                ZoomMeetingUISettingHelper.changeVideoSource(true);
            }
            showMeetingUi();
          //  parentToolbar.setVisibility(View.GONE);
            zoomImageView.setVisibility(View.GONE);
            meetingsDrawer.closeDrawer(Gravity.RIGHT);
            frameZoom.setVisibility(View.VISIBLE);

        }
        refreshUI();
    }

 /*   private void refreshUI()
    {
        if(!ZoomSDK.getInstance().isInitialized())
        {
            return;
        }
        ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);

        MeetingStatus meetingStatus = ZoomSDK.getInstance().getMeetingService().getMeetingStatus();

        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING ) {
                MeetingWindowHelper.getInstance().showMeetingWindow(this);
            } else {
                MeetingWindowHelper.getInstance().hiddenMeetingWindow(true);
            }
        }
    }*/



/*
    private void refreshUI() {
        MeetingStatus meetingStatus = ZoomSDK.getInstance().getMeetingService().getMeetingStatus();
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING || meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING
                || meetingStatus == MeetingStatus.MEETING_STATUS_RECONNECTING) {

        } else {

        }
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING && isResumed) {
                MeetingWindowHelper.getInstance().showMeetingWindow(this);
            } else {
                MeetingWindowHelper.getInstance().hiddenMeetingWindow(true);
            }
        }
    }
*/

    private void showMeetingUi() {
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            SharedPreferences sharedPreferences = getSharedPreferences("UI_Setting", Context.MODE_PRIVATE);
            boolean enable = sharedPreferences.getBoolean("enable_rawdata", false);
            Intent intent = null;
            if (!enable) {
              //  intent = new Intent(this, MyMeetingActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("from", JoinMeetingFragment.JOIN_FROM_LOGIN);

                Fragment fragment= new JoinMeetingFragment(zoomSDK);
                fragment.setArguments(bundle);


                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.frameZoom,fragment,null);
             //   transaction.addToBackStack(null);
                transaction.commit();


            }
            else {
                intent = new Intent(this, RawDataMeetingActivity.class);
            }
         //   intent.putExtra("from",MyMeetingActivity.JOIN_FROM_LOGIN);
         //   intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
           // this.startActivity(intent);
        }
    }



 /*   InMeetingNotificationHandle handle=new InMeetingNotificationHandle() {

        @Override
        public boolean handleReturnToConfNotify(Context context, Intent intent) {
            // intent = new Intent(context, MyMeetingActivity.class);
            // intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            // if(!(context instanceof Activity)) {
            //     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // }
            // intent.setAction(InMeetingNotificationHandle.ACTION_RETURN_TO_CONF);
            // context.startActivity(intent);
            return true;
        }
    };
*/


    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {

        if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
            Toast.makeText(this, "Failed to initialize Zoom SDK. Error: " + errorCode + ", internalErrorCode=" + internalErrorCode, Toast.LENGTH_LONG).show();
        } else {
            ZoomSDK.getInstance().getZoomUIService().enableMinimizeMeeting(true);
            ZoomSDK.getInstance().getZoomUIService().setMiniMeetingViewSize(new CustomizedMiniMeetingViewSize(0, 0, 360, 540));
            setMiniWindows();
            ZoomSDK.getInstance().getMeetingSettingsHelper().enable720p(false);
            ZoomSDK.getInstance().getMeetingSettingsHelper().enableShowMyMeetingElapseTime(true);
            ZoomSDK.getInstance().getMeetingService().addListener(this);
         //   ZoomSDK.getInstance().getMeetingSettingsHelper().setCustomizedNotificationData(null, handle);
            Toast.makeText(this, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG).show();
          //  callApis();
        }
    }


    private void setMiniWindows() {
            if (null != zoomSDK && zoomSDK.isInitialized() && !zoomSDK.getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
                ZoomSDK.getInstance().getZoomUIService().setZoomUIDelegate(new SimpleZoomUIDelegate() {
                    @Override
                    public void afterMeetingMinimized(Activity activity) {
                        Intent intent = new Intent(activity, InitAuthSDKActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        activity.startActivity(intent);
                    }
                });
            }
        }

    @Override
    public void onZoomSDKLoginResult(long result) {
        if(result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
          //  UserLoginCallback.getInstance().removeListener(this);
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
                    Log.e("theUniqueId",meetingUniqueId+"");
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
           // noData_tv.setVisibility(View.VISIBLE);
        }

      /*  ScheduledMeetingAdapter adapterRecycler= new ScheduledMeetingAdapter(1,meetingListMain,this);
        sheduled_rv.setLayoutManager(new LinearLayoutManager(this));
        sheduled_rv.setAdapter(adapterRecycler);*/
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



    /*
    private void showLeaveMeetingDialogAdapter(MeetingItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (mInMeetingService.isMeetingConnected()) {
            if (mInMeetingService.isMeetingHost()) {
                builder.setTitle("End or leave meeting")
                        .setPositiveButton("End", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leaveAdapter(true,item);
                            }
                        }).setNeutralButton("Leave", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leaveAdapter(false, item);
                    }
                });
            } else {
                builder.setTitle("Leave meeting")
                        .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                leaveAdapter(false, item);
                            }
                        });
            }
        } else {
            builder.setTitle("Leave meeting")
                    .setPositiveButton("Leave", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            leaveAdapter(false, item);
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
*/
    /*
    private void leaveAdapter(boolean end, MeetingItem item) {
      */
    /*  if (meetingShareHelper.isSharingOut()) {
            meetingShareHelper.stopShare();
        }*/
    /*

        mInMeetingService.leaveCurrentMeeting(end);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                onClickBtnStart(item);
            }
        }, 500);
    }
*/

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
      /*  if (meetingShareHelper.isSharingOut()) {
            meetingShareHelper.stopShare();
        }*/
       // finish();
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


   /* private void getCredientail_api() {

        dialog = ProgressDialog.show(this, "",
                "Loading. Please wait...", true);
        PreferenceClass sharedPreferences= new PreferenceClass(this);
        String profileData =sharedPreferences.getPref("profileData");
        LoginModel loginModelBase = new Gson().fromJson(profileData,LoginModel.class);

        APIInterface  apiInterface = APIClient.getClient(2,sharedPreferences.getPref("secondaryUrl")).create(APIInterface.class);
        Call<ZoomCredientialModel> call = apiInterface.getZoomCredientail("bearer " + loginModelBase.getAccessToken(),loginModelBase.getEmpId().toString());
        call.enqueue(new Callback<ZoomCredientialModel>() {
            @Override
            public void onResponse(Call<ZoomCredientialModel> call, Response<ZoomCredientialModel> response) {


                if (response.code() == 200 && !response.body().toString().isEmpty())
                {
                    ZoomCredientialModel model  = response.body();
                    new ZoomInitilizeClass().initilizeZoom(JoinMeetingActivity.this,model);

                }
                else
                {
                    Log.e("elseGetCrediential", response.code()+"");
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<ZoomCredientialModel> call, Throwable t) {
                Log.e("failGetCrediential",t.getMessage().toString());
                dialog.dismiss();

            }

        });
    }*/

    }

