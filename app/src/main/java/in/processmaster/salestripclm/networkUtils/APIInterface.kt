package `in`.processmaster.salestripclm.networkUtils

import `in`.processmaster.salestripclm.models.*
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
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


    @GET("api/common/syncData") //sync api
    suspend fun
            syncApiCoo(@Header("Authorization") authorization: String?): Response<SyncModel?>?

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

    @GET("api/users/allLevelHierarchyEmp/{empId}") //get teams members
    fun getTeamsMember(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Call<TeamsModel?>?

  /*  @GET("api/users/{empId}")
    fun getProfileData(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Call<ProfileModel?>?*/



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
        @Path("empId") empId: Int?
    ): Call<ZoomCredientialModel?>?

    @GET("api/zoomMeeting/credentials/{empId}")
    suspend fun getZoomCredientailCoo(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Response<ZoomCredientialModel?>?

    //in.processmaster.salestripclm
    @GET("api/report/lastVisitSummary/{docId}")
    fun priCallAnalysisApi(
        @Header("Authorization") authorization: String,
        @Path("docId") dotorId: Int): Call<PreCallModel>

    @GET("api/dcr/byDCRDate/{empId}")
    suspend fun checkDCR_API(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: Int?,
        @Query("date") date:String?
    ): Response<GetDcrToday>

    @POST("api/dcr")
    fun saveDCS(
        @Header("Authorization") authorization: String?,
        @Body arrayVisual: CommonModel.SaveDcrModel
    ): Call<JsonObject?>?

    @POST("api/dcr/doctor/v2")
    fun submitEdetailingApi(
        @Header("Authorization") authorization: String?,
        @Body arrayVisual: DailyDocVisitModel.Data.DcrDoctor
    ): Call<JsonObject?>?

    @POST("api/dcr/clm/doctor")
    suspend fun submitEdetailingApiCoo(@Header("Authorization") authorization: String, @Body arrayVisual: DailyDocVisitModel.Data.DcrDoctor): Response<DailyDocVisitModel>


    @GET("api/gs-receive/employee-sample-balance")
    suspend fun getQuantiyApiCoo(@Header("Authorization") authorization: String): Response<CommonModel.QuantityModel>

    @GET("api/dashboard/visited-doctor-retailer")
    suspend fun
            visitDoctorGraphApi(@Header("Authorization") authorization: String?,
                                @Query("fromDate") fromDate:String,
                                @Query("toDate") toDate:String): Response<DoctorGraphModel>

    @GET("api/dcr/doctors")
    suspend fun
            dailyDocCallApi(@Header("Authorization") authorization: String?,
                                @Query("dcrDate") fromDate:String): Response<DailyDocVisitModel>

    @GET("api/users/{empId}") // profile api
    suspend fun getProfileData(
        @Header("Authorization") authorization: String?,
        @Path("empId") empId: String?
    ): Response<ProfileModel?>?


    @POST("api/dcr/clm/doctor")
    fun submitEdetailingApiAndGet(
        @Header("Authorization") authorization: String?,
        @Body arrayVisual: DailyDocVisitModel.Data.DcrDoctor
    ): Call<DailyDocVisitModel?>?

    @POST("api/dcr/retailer-stockist")
    fun submitRetailer(
        @Header("Authorization") authorization: String?,
        @Body arrayVisual: ArrayList<RetailerPobModel>
    ): Call<DailyDocVisitModel?>?

    @POST("api/dcr/retailer-stockist")
    suspend fun
            retailerSendApiCoo(@Header("Authorization") authorization: String?,
                               @Body arrayVisual: ArrayList<RetailerPobModel>): Response<DailyDocVisitModel>

    @Streaming
    @GET
    suspend fun downloadFile(@Url fileUrl:String): Response<ResponseBody>

}