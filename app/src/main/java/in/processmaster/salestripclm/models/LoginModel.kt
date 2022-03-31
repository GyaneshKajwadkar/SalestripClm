package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginModel {

    @SerializedName("access_token")
    @Expose
     var accessToken: String? = null

   /* @SerializedName("token_type")
    @Expose
     var tokenType: String? = null

    @SerializedName("expires_in")
    @Expose
     var expiresIn: Int? = null

    @SerializedName("refresh_token")
    @Expose
     var refreshToken: String? = null*/

    @SerializedName("empId")
    @Expose
     var empId: Int? = null

   /* @SerializedName("isFirstLogin")
    @Expose
     var isFirstLogin: String? = null

    @SerializedName("menuList")
    @Expose
     var menuList: String? = null
*/
    @SerializedName("employeeObj")
    @Expose
    private var employeeObj: String? = null

 /*   @SerializedName("configurationSetting")
    @Expose
     var configurationSetting: String? = null

    @SerializedName("expiredAt")
    @Expose
     var expiredAt: String? = null

    @SerializedName("roleType")
    @Expose
     var roleType: String? = null*/

    @SerializedName("imageName")
    @Expose
     var imageName: String? = null

    /*@SerializedName("compName")
    @Expose
     var compName: String? = null

    @SerializedName("industryType")
    @Expose
     var industryType: String? = null

    @SerializedName("empName")
    @Expose
     var empName: String? = null

    @SerializedName("compAddress")
    @Expose
     var compAddress: String? = null*/

/*    @SerializedName("emailId")
    @Expose
     var emailId: String? = null*/

  /*  @SerializedName("hierarchyId")
    @Expose
     var hierarchyId: Int? = null

    @SerializedName("companyLogo")
    @Expose
     var companyLogo: String? = null

    @SerializedName("isCheckIn")
    @Expose
     var isCheckIn: Boolean? = null

    @SerializedName("currentDate")
    @Expose
     var currentDate: String? = null

    @SerializedName("isMpin")
    @Expose
     var isMpin: Boolean? = null

    @SerializedName("as:client_id")
    @Expose
     var asClientId: String? = null*/

    @SerializedName("userName")
    @Expose
    var userName: String? = null

   /* @SerializedName("compCode")
    @Expose
     var compCode: String? = null

    @SerializedName("runsIn")
    @Expose
     var runsIn: String? = null

    @SerializedName(".issued")
    @Expose
     var issued: String? = null

    @SerializedName(".expires")
    @Expose
     var expires: String? = null*/

    fun getEmployeeObj(): String? {
        return employeeObj
    }





}