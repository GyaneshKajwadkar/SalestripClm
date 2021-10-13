package in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.video;

import android.util.Log;

import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.BaseCallback;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.BaseEvent;
import in.processmaster.salestripclm.sdksampleapp.inmeetingfunction.customizedmeetingui.SimpleInMeetingListener;
import us.zoom.sdk.ZoomSDK;


public class MeetingVideoCallback extends BaseCallback<MeetingVideoCallback.VideoEvent> {

    private static final String TAG = MeetingVideoCallback.class.getSimpleName();

    public interface VideoEvent extends BaseEvent {
        void onUserVideoStatusChanged(long userId);
    }

    private static MeetingVideoCallback instance;

    public static MeetingVideoCallback getInstance() {
        if (null == instance) {
            synchronized (MeetingVideoCallback.class) {
                if (null == instance) {
                    instance = new MeetingVideoCallback();
                }
            }
        }
        return instance;
    }

    private MeetingVideoCallback() {
        init();
    }

    protected void init() {
        ZoomSDK.getInstance().getInMeetingService().addListener(videoListener);
    }


    SimpleInMeetingListener videoListener = new SimpleInMeetingListener() {

        @Override
        public void onUserVideoStatusChanged(long userId) {
            for (VideoEvent event : callbacks) {
                event.onUserVideoStatusChanged(userId);
            }
        }

        @Override
        public void onSpotlightVideoChanged(boolean on) {
            Log.d(TAG, "onSpotlightVideoChanged:" + on);

        }

        @Override
        public void onMeetingActiveVideo(long userId) {
            Log.d(TAG, "onMeetingActiveVideo:" + userId);
        }
    };

}
