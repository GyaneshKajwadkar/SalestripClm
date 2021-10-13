package in.processmaster.salestripclm.networkUtils;

import java.util.ArrayList;

import in.processmaster.salestripclm.models.DevisionModel;
import in.processmaster.salestripclm.models.DownloadEdetail_model;
import in.processmaster.salestripclm.models.GenerateOTPModel;
import in.processmaster.salestripclm.models.LoginModel;
import in.processmaster.salestripclm.models.ProfileModel;
import in.processmaster.salestripclm.models.SyncModel;

import in.processmaster.salestripclm.models.TeamsModel;
import in.processmaster.salestripclm.models.VisualAdsModel_Send;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterfacee {

    //in.processmaster.salestripclm

    @GET("common/baseurl/{COMPANY_ID}")   // check company code api
    Call<String> checkCompanyCode(@Path("COMPANY_ID") String Company_id);

   // @GET("common/latestVersion/V3/android/{COMPANY_ID}")  // check version api
    @GET("common/latestVersionClm")  // check version api
   // Call<String> checkVersion(@Path("COMPANY_ID") String Company_id);
    Call<String> checkVersion(/*@Path("COMPANY_ID") String Company_id*/);

    @FormUrlEncoded
    @POST("token") // login api
    Call<LoginModel> loginAPI(@Field("grant_type") String grant_type, @Field("username") String username, @Field("password") String password);

    @GET("api/common/syncData") //sync api
    Call<SyncModel> syncApi(@Header("Authorization") String authorization);

    @FormUrlEncoded
    @POST("users/generateOTP") // generate OTP api
    Call<GenerateOTPModel> generateOtpApi(@Field("companyCode") String companyCode, @Field("userName") String userName, @Field("userType") String userType);

    @FormUrlEncoded
    @POST("users/verifyOTP")  //verify OTP api
    Call<GenerateOTPModel> verifyOtpAPI(@Field("companyCode") String companyCode, @Field("empId") String empId, @Field("otp") String otp);

    @FormUrlEncoded
    @POST("users/changePasswordByOTP")  //change password api
    Call<GenerateOTPModel> changePassAPI(@Field("companyCode") String companyCode, @Field("empId") String empId, @Field("newPassword") String newPassword);

    
    @GET("api/e-detailing/publish/{divisionId}")     //edetailing api
    Call<DevisionModel> detailingApi(@Header("Authorization") String authorization, @Path("divisionId") String divisionId);

    @GET("api/e-detailing/documents/{eDetailId}")   //download file api
    Call<DownloadEdetail_model> downloadUrl(@Header("Authorization") String authorization, @Path("eDetailId") String eDetailId);

  /*  @FormUrlEncoded
    @POST("api/e-detailing/transaction")    //submit e-detailing api
    Call<SyncModel> submitVisualAds(@Header("Authorization") String authorization,@Field("startDate") String startDate, @Field("endDate") String endDate, @Field("empId") String empId, @Field("doctorId") String doctorId
            , @Field("brandId") String brandId);*/

    @POST("api/e-detailing/transaction")    //submit e-detailing api
    Call<SyncModel> submitVisualAds(@Header("Authorization") String authorization, @Body ArrayList<VisualAdsModel_Send> arrayVisual);

    @GET("api/users/allLevelHierarchyEmp/{empId}") //get teams members
    Call<TeamsModel>getTeamsMember(@Header("Authorization") String authorization, @Path("empId") String empId);

    @GET("api/users/{empId}")
   Call<ProfileModel>getProfileData(@Header("Authorization")String authorization, @Path("empId")String empId);
}
