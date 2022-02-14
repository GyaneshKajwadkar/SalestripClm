package `in`.processmaster.salestripclm.networkUtils

import `in`.processmaster.salestripclm.models.*
import com.google.gson.JsonObject
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.ArrayList
import retrofit2.http.POST
import retrofit2.Response

interface APIInterface
{
    //in.processmaster.salestripclm
    @GET("common/baseurl/{COMPANY_ID}")
    fun  // check company code api
            checkCompanyCode(@Path("COMPANY_ID") Company_id: String?): Call<String?>?

    // @GET("common/latestVersion/V3/android/{COMPANY_ID}")  // check version api
    @GET("common/latestVersionClm")
    fun  // check version api
    // Call<String> checkVersion(@Path("COMPANY_ID") String Company_id);
            checkVersion( /*@Path("COMPANY_ID") String Company_id*/): Call<String?>?

    @FormUrlEncoded
    @POST("token")
    fun  // login api
            loginAPI(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Call<LoginModel?>?


    @FormUrlEncoded
    @POST("token")
    suspend  fun  // login api
            loginAPICoo(
        @Field("grant_type") grant_type: String?,
        @Field("username") username: String?,
        @Field("password") password: String?
    ): Response<LoginModel?>?

    @GET("api/common/syncData")
    suspend fun  //sync api
            syncApiCoo(@Header("Authorization") authorization: String?): Response<SyncModel?>?



    @GET("api/common/syncData")
    fun  //sync api
            syncApi(@Header("Authorization") authorization: String?): Call<SyncModel?>?

    @FormUrlEncoded
    @POST("users/generateOTP")
    fun  // generate OTP api
            generateOtpApi(
        @Field("companyCode") companyCode: String?,
        @Field("userName") userName: String?,
        @Field("userType") userType: String?
    ): Call<GenerateOTPModel?>?

    @FormUrlEncoded
    @POST("users/verifyOTP")
    fun  //verify OTP api
            verifyOtpAPI(
        @Field("companyCode") companyCode: String?,
        @Field("empId") empId: String?,
        @Field("otp") otp: String?
    ): Call<GenerateOTPModel?>?

    @FormUrlEncoded
    @POST("users/changePasswordByOTP")
    fun  //change password api
            changePassAPI(
        @Field("companyCode") companyCode: String?,
        @Field("empId") empId: String?,
        @Field("newPassword") newPassword: String?
    ): Call<GenerateOTPModel?>?

    //edetailing api
    @GET("api/e-detailing/publish/{divisionId}")
    fun detailingApi(
        @Header("Authorization") authorization: String?,
        @Path("divisionId") divisionId: String?
    ): Call<DevisionModel?>?

    @GET("api/e-detailing/publish/{divisionId}")
    suspend fun detailingApiCoo(
        @Header("Authorization") authorization: String?,
        @Path("divisionId") divisionId: String?
    ): Response<DevisionModel?>?

    @GET("api/e-detailing/documents/{eDetailId}")
    fun  //download file api
            downloadUrl(
        @Header("Authorization") authorization: String?,
        @Path("eDetailId") eDetailId: String?
    ): Call<DownloadEdetail_model?>?

    /*  @FormUrlEncoded
    @POST("api/e-detailing/transaction")    //submit e-detailing api
    Call<SyncModel> submitVisualAds(@Header("Authorization") String authorization,@Field("startDate") String startDate, @Field("endDate") String endDate, @Field("empId") String empId, @Field("doctorId") String doctorId
            , @Field("brandId") String brandId);*/

    /*  @FormUrlEncoded
    @POST("api/e-detailing/transaction")    //submit e-detailing api
    Call<SyncModel> submitVisualAds(@Header("Authorization") String authorization,@Field("startDate") String startDate, @Field("endDate") String endDate, @Field("empId") String empId, @Field("doctorId") String doctorId
            , @Field("brandId") String brandId);*/

    @POST("api/e-detailing/transaction")
    fun  //submit e-detailing api
            submitVisualAds(
        @Header("Authorization") authorization: String?,
        @Body arrayVisual: ArrayList<VisualAdsModel_Send>
    ): Call<SyncModel?>?

    @GET("api/users/allLevelHierarchyEmp/{empId}")
    fun  //get teams members
            getTeamsMember(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Call<TeamsModel?>?

    @GET("api/users/allLevelHierarchyEmp/{empId}")
    suspend fun  //get teams members
            getTeamsMemberCoo(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Response<TeamsModel?>?


    @GET("api/users/{empId}")
    fun getProfileData(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Call<ProfileModel?>?

    @POST("api/users/changePassword")
    fun changePassword(@Header("Authorization") authorization: String?, @Body params: JSONObject):Call<GenerateOTPModel?>?

    @Multipart
    @POST("api/users/documents")
    fun changeProflePic(@Header("Authorization") authorization: String?,
                        @Part filePart: MultipartBody.Part?, @Part("model") model: RequestBody
    ): Call<GenerateOTPModel?>?

    @Multipart
    @POST("api/mailBox/mailWithAttachment")
    fun sendEmail(@Header("Authorization") authorization: String?,
                  @Part files: Array<MultipartBody.Part?>, @Part("model") model: RequestBody
    ): Call<GenerateOTPModel?>?


    @POST("api/zoomMeeting/schedule")
    suspend fun setScheduleMeetingApi(@Header("Authorization") authorization: String?, @Body params: RequestBody):Response<GenerateOTPModel?>?

    @POST("api/zoomMeeting/schedule")
    fun setScheduleFirstApi(@Header("Authorization") authorization: String?, @Body params: RequestBody):Call<GenerateOTPModel?>?


    @GET("api/zoomMeeting/employeewise/{empId}")
    fun getScheduledMeeting(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Call<GetScheduleModel?>?

    @GET("api/zoomMeeting/employeewise/{empId}")
    suspend fun getScheduledMeetingCoo(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Response<GetScheduleModel?>?

    @GET("api/zoomMeeting/credentials/{empId}")
    fun getZoomCredientail(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Call<ZoomCredientialModel?>?

    @GET("api/zoomMeeting/credentials/{empId}")
    suspend fun getZoomCredientailCoo(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Response<ZoomCredientialModel?>?

    //in.processmaster.salestripclm
    @GET("api/report/lastVisitSummary/{docId}")
    fun priCallAnalysisApi(
        @Header("Authorization") authorization: String?,
        @Path("docId") dotorId: Int?): Call<PreCallModel>

    @GET("api/dcr/byDCRDate/{empId}")
    fun checkDCR_API(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: Int?,
        @Query("date") date:String?
    ): Call<JsonObject>
}