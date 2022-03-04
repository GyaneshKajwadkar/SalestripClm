package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

public class ProfileModel
{
    @SerializedName("responseCode")
    @Expose
    private var responseCode: Int? = null

    @SerializedName("errorObj")
    @Expose
    private var errorObj: ErrorObj? = null

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
        this.responseCode = responseCode
    }

    fun getErrorObj(): ErrorObj? {
        return errorObj
    }

    fun setErrorObj(errorObj: ErrorObj?) {
        this.errorObj = errorObj
    }

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class ErrorObj {
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String? = null

        @SerializedName("fldErrors")
        @Expose
        var fldErrors: Any? = null
    }


    class Data {
      /*  @SerializedName("hqList")
        @Expose
        var hqList: List<Any>? = null

        @SerializedName("stateList")
        @Expose
        var stateList: List<Any>? = null*/

        @SerializedName("users")
        @Expose
        var users: List<User> = ArrayList()

        class User {
            @SerializedName("empId")
            @Expose
            var empId: Int? = null

            @SerializedName("firstName")
            @Expose
            var firstName: String? = null

            @SerializedName("lastName")
            @Expose
            var lastName: String? = null

            @SerializedName("userName")
            @Expose
            var userName: String? = null

            @SerializedName("password")
            @Expose
            var password: String? = null

            @SerializedName("emailId")
            @Expose
            var emailId: String? = null

            @SerializedName("mobileNo")
            @Expose
            var mobileNo: String? = null

            @SerializedName("dateOfBirth")
            @Expose
            var dateOfBirth: String? = null

            @SerializedName("doa")
            @Expose
            var doa: String? = null

            @SerializedName("gender")
            @Expose
            var gender: Int? = null

            @SerializedName("address1")
            @Expose
            var address1: String? = null

            @SerializedName("address2")
            @Expose
            var address2: String? = null

            @SerializedName("cityId")
            @Expose
            var cityId: Int? = null

            @SerializedName("stateId")
            @Expose
            var stateId: Int? = null

            @SerializedName("stateName")
            @Expose
            var stateName: String? = null

            @SerializedName("countryId")
            @Expose
            var countryId: Int? = null

            @SerializedName("countryName")
            @Expose
            var countryName: String? = null

            @SerializedName("pinCode")
            @Expose
            var pinCode: String? = null

            @SerializedName("phone")
            @Expose
            var phone: String? = null

            @SerializedName("division")
            @Expose
            var division: String? = null

            @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: String? = null

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String? = null

            @SerializedName("hierachyId")
            @Expose
            var hierachyId: Int? = null

            @SerializedName("hierachyName")
            @Expose
            var hierachyName: Any? = null

            @SerializedName("reportingHierachy")
            @Expose
            var reportingHierachy: Int? = null

            @SerializedName("reportingManager")
            @Expose
            var reportingManager: Int? = null

            @SerializedName("qualificationId")
            @Expose
            var qualificationId: Int? = null

            @SerializedName("marriedStatus")
            @Expose
            var marriedStatus: Int? = null

            @SerializedName("reportingManagerName")
            @Expose
            var reportingManagerName: String? = null

            @SerializedName("reportingManagerEmail")
            @Expose
            var reportingManagerEmail: String? = null

            @SerializedName("hierachyLevel")
            @Expose
            var hierachyLevel: Int? = null

            @SerializedName("roleId")
            @Expose
            var roleId: Int? = null

            @SerializedName("active")
            @Expose
            var active: Boolean? = null

            @SerializedName("entryBy")
            @Expose
            var entryBy: Int? = null

            @SerializedName("updateBy")
            @Expose
            var updateBy: Int? = null

            @SerializedName("deleteBy")
            @Expose
            var deleteBy: Int? = null

            @SerializedName("mode")
            @Expose
            var mode: Int? = null

          /*  @SerializedName("compKey")
            @Expose
            var compKey: Int? = null

            @SerializedName("isFirstLogin")
            @Expose
            var isFirstLogin: Boolean? = null

            @SerializedName("authPassword")
            @Expose
            var authPassword: String? = null

            @SerializedName("oldAuthPassword")
            @Expose
            var oldAuthPassword: String? = null

            @SerializedName("dashboardLayout")
            @Expose
            var dashboardLayout: String? = null

            @SerializedName("companyCode")
            @Expose
            var companyCode: String? = null

            @SerializedName("oldPassword")
            @Expose
            var oldPassword: String? = null

            @SerializedName("newPassword")
            @Expose
            var newPassword: String? = null

            @SerializedName("userType")
            @Expose
            var userType: String? = null

            @SerializedName("imageName")
            @Expose
            var imageName: String? = null

            @SerializedName("imagePath")
            @Expose
            var imagePath: String? = null

            @SerializedName("imageExt")
            @Expose
            var imageExt: String? = null
*/
            @SerializedName("isAuthPassword")
            @Expose
            private var isAuthPassword: Boolean? = null

         /*   @SerializedName("roleType")
            @Expose
            var roleType: String? = null

            @SerializedName("roleName")
            @Expose
            var roleName: String? = null

            @SerializedName("prevRoleName")
            @Expose
            var prevRoleName: String? = null

            @SerializedName("roleTypeName")
            @Expose
            var roleTypeName: String? = null

            @SerializedName("linkedStates")
            @Expose
            var linkedStates: String? = null

            @SerializedName("deactivationDate")
            @Expose
            var deactivationDate: String? = null

            @SerializedName("oldReportingManagerName")
            @Expose
            var oldReportingManagerName: Any? = null

            @SerializedName("expenseTemplateData")
            @Expose
            var expenseTemplateData: Any? = null

            @SerializedName("isCheckIn")
            @Expose
            var isCheckIn: Boolean? = null

            @SerializedName("allowDoctorEdit")
            @Expose
            var allowDoctorEdit: Boolean? = null

            @SerializedName("lastDCRDate")
            @Expose
            var lastDCRDate: String? = null

            @SerializedName("strLastDCRDate")
            @Expose
            var strLastDCRDate: Any? = null

            @SerializedName("mobileAppInstall")
            @Expose
            var mobileAppInstall: Any? = null

            @SerializedName("lastLoginDate")
            @Expose
            var lastLoginDate: String? = null

            @SerializedName("strLastLoginDate")
            @Expose
            var strLastLoginDate: Any? = null
*/
            @SerializedName("mPin")
            @Expose
            private var mPin: Any? = null

            @SerializedName("fingerprint")
            @Expose
            var fingerprint: Any? = null

            @SerializedName("workingHeadQuarter")
            @Expose
            var workingHeadQuarter: String? = null

            @SerializedName("cityName")
            @Expose
            var cityName: String? = null

            @SerializedName("canBeDeleted")
            @Expose
            var canBeDeleted: Boolean? = null

            @SerializedName("absolutePath")
            @Expose
            var absolutePath: String? = null

       /*     @SerializedName("disableSMSNotification")
            @Expose
            var disableSMSNotification: Boolean? = null

            @SerializedName("prevFirstName")
            @Expose
            var prevFirstName: String? = null

            @SerializedName("prevLastName")
            @Expose
            var prevLastName: String? = null

            @SerializedName("prevUserName")
            @Expose
            var prevUserName: String? = null*/

            @SerializedName("fullName")
            @Expose
            var fullName: String? = null

            @SerializedName("headQuaterType")
            @Expose
            var headQuaterType: Int? = null

            @SerializedName("employeeCode")
            @Expose
            var employeeCode: String? = null

            @SerializedName("joiningDate")
            @Expose
            var joiningDate: String? = null

         /*   @SerializedName("onFieldJobDate")
            @Expose
            var onFieldJobDate: String? = null

            @SerializedName("hierachyCode")
            @Expose
            var hierachyCode: String? = null

            @SerializedName("hierachyType")
            @Expose
            var hierachyType: String? = null

            @SerializedName("reportingManagerStatus")
            @Expose
            var reportingManagerStatus: String? = null

            @SerializedName("lastLoginDeviceId")
            @Expose
            var lastLoginDeviceId: String? = null

            @SerializedName("pushToken")
            @Expose
            var pushToken: String? = null
*/
            @SerializedName("hierDesc")
            @Expose
            var hierDesc: String? = null

            @SerializedName("teamLevel")
            @Expose
            var teamLevel: Int? = null

            @SerializedName("divisionName")
            @Expose
            var divisionName: String? = null

          /*  @SerializedName("linkedStateName")
            @Expose
            var linkedStateName: String? = null

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String? = null

            @SerializedName("nameWithCode")
            @Expose
            var nameWithCode: String? = null

            @SerializedName("receiveEmailUpdate")
            @Expose
            var receiveEmailUpdate: Boolean? = null

            @SerializedName("mainHeadQuarter")
            @Expose
            var mainHeadQuarter: Int? = null
*/
            @SerializedName("callDetails")
            @Expose
            var callDetails: Any? = null

        /*    @SerializedName("strDateOfBirth")
            @Expose
            var strDateOfBirth: String? = null

            @SerializedName("strJoiningDate")
            @Expose
            var strJoiningDate: String? = null

            @SerializedName("strOnFieldJobDate")
            @Expose
            var strOnFieldJobDate: String? = null

            @SerializedName("reportingHierachyCode")
            @Expose
            var reportingHierachyCode: String? = null

            @SerializedName("mobileAppVersionInUse")
            @Expose
            var mobileAppVersionInUse: String? = null
*/
            @SerializedName("previousHierarchyType")
            @Expose
            var previousHierarchyType: Any? = null

            @SerializedName("month")
            @Expose
            var month: Int? = null

            @SerializedName("year")
            @Expose
            var year: Int? = null

            @SerializedName("promotionDate")
            @Expose
            var promotionDate: String? = null

            @SerializedName("previousPost")
            @Expose
            var previousPost: Int? = null

            @SerializedName("previousReportingManager")
            @Expose
            var previousReportingManager: Int? = null

            @SerializedName("isExpenseApprovalReq")
            @Expose
            var isExpenseApprovalReq: Boolean? = null

            @SerializedName("expenseApproveBy")
            @Expose
            var expenseApproveBy: Int? = null

            @SerializedName("isMpin")
            @Expose
            var isMpin: Boolean? = null

            @SerializedName("ledgerCode")
            @Expose
            var ledgerCode: String? = null

            @SerializedName("isFieldWorkingUser")
            @Expose
            var isFieldWorkingUser: Boolean? = null

            @SerializedName("isAccessBlocked")
            @Expose
            var isAccessBlocked: Boolean? = null

            @SerializedName("otp")
            @Expose
            var otp: Any? = null

            @SerializedName("isGeoFencingApplicable")
            @Expose
            var isGeoFencingApplicable: Boolean? = null

            @SerializedName("allowLocationUpdate")
            @Expose
            var allowLocationUpdate: Boolean? = null

            @SerializedName("enableSelfieAttendance")
            @Expose
            var enableSelfieAttendance: Boolean? = null

            @SerializedName("regionId")
            @Expose
            var regionId: Int? = null

            @SerializedName("zoneId")
            @Expose
            var zoneId: Int? = null

            @SerializedName("strDeactivationDate")
            @Expose
            var strDeactivationDate: Any? = null

            fun getIsAuthPassword(): Boolean? {
                return isAuthPassword
            }

            fun setIsAuthPassword(isAuthPassword: Boolean?) {
                this.isAuthPassword = isAuthPassword
            }

            fun getmPin(): Any? {
                return mPin
            }

            fun setmPin(mPin: Any?) {
                this.mPin = mPin
            }
        }
    }

}