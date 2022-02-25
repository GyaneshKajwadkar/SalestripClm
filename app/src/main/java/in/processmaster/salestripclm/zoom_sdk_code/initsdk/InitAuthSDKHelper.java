package in.processmaster.salestripclm.zoom_sdk_code.initsdk;

import android.content.Context;
import android.util.Log;

import in.processmaster.salestripclm.models.ZoomCredientialModel;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKInitParams;
import us.zoom.sdk.ZoomSDKInitializeListener;
import us.zoom.sdk.ZoomSDKRawDataMemoryMode;

/**
 * Init and auth zoom sdk first before using SDK interfaces
 */
public class InitAuthSDKHelper implements  ZoomSDKInitializeListener {

    private final static String TAG = "InitAuthSDKHelper";

    private static InitAuthSDKHelper mInitAuthSDKHelper;

    private ZoomSDK mZoomSDK;

    private InitAuthSDKCallback mInitAuthSDKCallback;

    private InitAuthSDKHelper() {
        mZoomSDK = ZoomSDK.getInstance();
    }

    public synchronized static InitAuthSDKHelper getInstance() {
        mInitAuthSDKHelper = new InitAuthSDKHelper();
        return mInitAuthSDKHelper;
    }

    /**
     * init sdk method
     */
    public void initSDK(Context context, InitAuthSDKCallback callback) {
        if (!mZoomSDK.isInitialized()) {
            mInitAuthSDKCallback = callback;

            ZoomSDKInitParams initParams = new ZoomSDKInitParams();
        //initParams.jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOm51bGwsImlzcyI6InEwb2JHMWpTUlRhMjdHb0NIby1WWnciLCJleHAiOjE2MzQ5MDA2NTMsImlhdCI6MTYzNDg5NTI1NX0.MCsLJuWLIdb2yat37jzRwfKaWIlwKYSkQ04q4NMo5qY";
            initParams.appKey =    "QwVV3KBshDXPuRQvZ1f09zSnKDKoV5k1JE8r";
           // initParams.appKey =    "kAVNTHpxiWm17pRAdtBYosArXjjHg93eHv8n";    //gyanesh
            initParams.appSecret = "6R5etDpduZgbnjxGSfyPciKPsFcYLp0NdmiV";
           // initParams.appSecret = "du3cFHs1SsoeBHOpisoOMcH3IEATbJEHCgyI";       //gyanesh
            initParams.enableLog = true;
            initParams.enableGenerateDump =true;
            initParams.logSize = 5;
            initParams.domain="zoom.us";
            initParams.videoRawDataMemoryMode = ZoomSDKRawDataMemoryMode.ZoomSDKRawDataMemoryModeStack;
            mZoomSDK.initialize(context, this, initParams);

        }
    }

    @Override
    public void onZoomSDKInitializeResult(int errorCode, int internalErrorCode) {
        Log.i(TAG, "onZoomSDKInitializeResult, errorCode=" + errorCode + ", internalErrorCode=" + internalErrorCode);

        if (mInitAuthSDKCallback != null)
        {
            mInitAuthSDKCallback.onZoomSDKInitializeResult(errorCode, internalErrorCode);
        }
    }

    @Override
    public void onZoomAuthIdentityExpired() {
        Log.e(TAG,"onZoomAuthIdentityExpired in init");
    }

    public void reset(){
        mInitAuthSDKCallback = null;
    }
}
