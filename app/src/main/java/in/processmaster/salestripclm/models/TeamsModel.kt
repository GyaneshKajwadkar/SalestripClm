package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.ArrayList

class TeamsModel {

    @SerializedName("responseCode")
    @Expose
    var responseCode:  Int = 0

    @SerializedName("errorObj")
    @Expose
    private var errorObj: ErrorObj? = null

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getResponseCode(): Int? {
        return responseCode
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

    class Data {
        @SerializedName("employeeList")
        @Expose
        var employeeList: ArrayList<Employee>? = ArrayList()

        @SerializedName("pobNo")
        @Expose
        var pobNo:  String = ""

        @SerializedName("sobNo")
        @Expose
        var sobNo:  String = ""

        inner class Employee {
            @SerializedName("empId")
            @Expose
            var empId: Int = 0

           /* @SerializedName("firstName")
            @Expose
            var firstName: String = ""

            @SerializedName("lastName")
            @Expose
            var lastName: String = ""*/

            @SerializedName("userName")
            @Expose
            var userName: String = ""

            @SerializedName("password")
            @Expose
            var password: String = ""

            @SerializedName("emailId")
            @Expose
            var emailId: String = ""

          /*  @SerializedName("mobileNo")
            @Expose
            var mobileNo: String = ""

            @SerializedName("dateOfBirth")
            @Expose
            var dateOfBirth: String = ""

            @SerializedName("doa")
            @Expose
            var doa: String = ""

            @SerializedName("gender")
            @Expose
            var gender: Int = 0

            @SerializedName("address1")
            @Expose
            var address1: String = ""

            @SerializedName("address2")
            @Expose
            var address2: String = ""

            @SerializedName("cityId")
            @Expose
            var cityId: Int = 0
*/
            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

          /*  @SerializedName("countryId")
            @Expose
            var countryId: Int = 0

            @SerializedName("countryName")
            @Expose
            var countryName: String = ""

            @SerializedName("pinCode")
            @Expose
            var pinCode: Int = 0

            @SerializedName("phone")
            @Expose
            var phone: String = ""
*/
            @SerializedName("division")
            @Expose
            var division: String = ""

           /* @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: String = ""*/

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String = ""

          /*  @SerializedName("hierachyId")
            @Expose
            var hierachyId: Int = 0

            @SerializedName("hierachyName")
            @Expose
            var hierachyName: String = ""

            @SerializedName("reportingHierachy")
            @Expose
            var reportingHierachy: Int = 0

            @SerializedName("reportingManager")
            @Expose
            var reportingManager: Int = 0

            @SerializedName("qualificationId")
            @Expose
            var qualificationId: Int = 0

            @SerializedName("marriedStatus")
            @Expose
            var marriedStatus: Int = 0

            @SerializedName("reportingManagerName")
            @Expose
            var reportingManagerName:  String = ""

            @SerializedName("reportingManagerEmail")
            @Expose
            var reportingManagerEmail:  String = ""

            @SerializedName("hierachyLevel")
            @Expose
            var hierachyLevel: Int = 0

            @SerializedName("roleId")
            @Expose
            var roleId: Int = 0

            @SerializedName("active")
            @Expose
            var active: Boolean = false

            @SerializedName("entryBy")
            @Expose
            var entryBy: Int = 0

            @SerializedName("updateBy")
            @Expose
            var updateBy: Int = 0

            @SerializedName("deleteBy")
            @Expose
            var deleteBy: Int = 0
*/
            @SerializedName("mode")
            @Expose
            var mode: Int = 0

        /*    @SerializedName("compKey")
            @Expose
            var compKey: Int = 0

            @SerializedName("isFirstLogin")
            @Expose
            var isFirstLogin: Boolean = false

            @SerializedName("authPassword")
            @Expose
            var authPassword:  String = ""

            @SerializedName("oldAuthPassword")
            @Expose
            var oldAuthPassword:  String = ""

            @SerializedName("dashboardLayout")
            @Expose
            var dashboardLayout:  String = ""

            @SerializedName("companyCode")
            @Expose
            var companyCode:  String = ""

            @SerializedName("oldPassword")
            @Expose
            var oldPassword: String = ""

            @SerializedName("newPassword")
            @Expose
            var newPassword:  String = ""

            @SerializedName("userType")
            @Expose
            var userType:  String = ""

            @SerializedName("imageName")
            @Expose
            var imageName: String = ""

            @SerializedName("imagePath")
            @Expose
            var imagePath:  String = ""

            @SerializedName("imageExt")
            @Expose
            var imageExt:  String = ""

            @SerializedName("isAuthPassword")
            @Expose
            private var isAuthPassword: Boolean = false

            @SerializedName("roleType")
            @Expose
            var roleType:  String = ""

            @SerializedName("roleName")
            @Expose
            var roleName:  String = ""

            @SerializedName("prevRoleName")
            @Expose
            var prevRoleName:  String = ""

            @SerializedName("roleTypeName")
            @Expose
            var roleTypeName:  String = ""

            @SerializedName("linkedStates")
            @Expose
            var linkedStates:  String = ""

            @SerializedName("deactivationDate")
            @Expose
            var deactivationDate:  String = ""

            @SerializedName("oldReportingManagerName")
            @Expose
            var oldReportingManagerName:  String = ""

            @SerializedName("expenseTemplateData")
            @Expose
            var expenseTemplateData:  String = ""

            @SerializedName("isCheckIn")
            @Expose
            var isCheckIn: Boolean = false

            @SerializedName("allowDoctorEdit")
            @Expose
            var allowDoctorEdit: Boolean = false

            @SerializedName("lastDCRDate")
            @Expose
            var lastDCRDate:  String = ""

            @SerializedName("strLastDCRDate")
            @Expose
            var strLastDCRDate:  String = ""

            @SerializedName("mobileAppInstall")
            @Expose
            var mobileAppInstall:  String = ""

            @SerializedName("lastLoginDate")
            @Expose
            var lastLoginDate:  String = ""

            @SerializedName("strLastLoginDate")
            @Expose
            var strLastLoginDate:  String = ""

            @SerializedName("mPin")
            @Expose
            private var mPin:  String = ""

            @SerializedName("fingerprint")
            @Expose
            var fingerprint:  String = ""

            @SerializedName("workingHeadQuarter")
            @Expose
            var workingHeadQuarter:  String = ""

            @SerializedName("cityName")
            @Expose
            var cityName:  String = ""

            @SerializedName("canBeDeleted")
            @Expose
            var canBeDeleted:  Boolean = false
*/
            @SerializedName("absolutePath")
            @Expose
            var absolutePath:  String = ""

         /*   @SerializedName("disableSMSNotification")
            @Expose
            var disableSMSNotification:  Boolean = false

            @SerializedName("prevFirstName")
            @Expose
            var prevFirstName:  String = ""

            @SerializedName("prevLastName")
            @Expose
            var prevLastName:  String = ""

            @SerializedName("prevUserName")
            @Expose
            var prevUserName:  String = ""
*/
            @SerializedName("fullName")
            @Expose
            var fullName:  String = ""

         /*   @SerializedName("headQuaterType")
            @Expose
            var headQuaterType:  Int = 0

            @SerializedName("employeeCode")
            @Expose
            var employeeCode:  String = ""

            @SerializedName("joiningDate")
            @Expose
            var joiningDate:  String = ""

            @SerializedName("onFieldJobDate")
            @Expose
            var onFieldJobDate:  String = ""

            @SerializedName("hierachyCode")
            @Expose
            var hierachyCode:  String = ""

            @SerializedName("hierachyType")
            @Expose
            var hierachyType:  String = ""

            @SerializedName("reportingManagerStatus")
            @Expose
            var reportingManagerStatus:  String = ""

            @SerializedName("lastLoginDeviceId")
            @Expose
            var lastLoginDeviceId:  String = ""

            @SerializedName("pushToken")
            @Expose
            var pushToken:  String = ""

            @SerializedName("hierDesc")
            @Expose
            var hierDesc:  String = ""

            @SerializedName("teamLevel")
            @Expose
            var teamLevel:  Int = 0
*/
            @SerializedName("divisionName")
            @Expose
            var divisionName:  String = ""

        /*    @SerializedName("linkedStateName")
            @Expose
            var linkedStateName:  String = ""

            @SerializedName("qualificationName")
            @Expose
            var qualificationName:  String = ""

            @SerializedName("nameWithCode")
            @Expose
            var nameWithCode:  String = ""

            @SerializedName("receiveEmailUpdate")
            @Expose
            var receiveEmailUpdate:  Boolean = false

            @SerializedName("mainHeadQuarter")
            @Expose
            var mainHeadQuarter:  Int = 0

            @SerializedName("callDetails")
            @Expose
            var callDetails:  String = ""

            @SerializedName("strDateOfBirth")
            @Expose
            var strDateOfBirth:  String = ""

            @SerializedName("strJoiningDate")
            @Expose
            var strJoiningDate:  String = ""

            @SerializedName("strOnFieldJobDate")
            @Expose
            var strOnFieldJobDate:  String = ""

            @SerializedName("reportingHierachyCode")
            @Expose
            var reportingHierachyCode:  String = ""

            @SerializedName("mobileAppVersionInUse")
            @Expose
            var mobileAppVersionInUse:  String = ""

            @SerializedName("previousHierarchyType")
            @Expose
            var previousHierarchyType:  String = ""
*/
            @SerializedName("month")
            @Expose
            var month:  Int = 0

            @SerializedName("year")
            @Expose
            var year:  Int = 0

         /*   @SerializedName("promotionDate")
            @Expose
            var promotionDate:  String = ""

            @SerializedName("previousPost")
            @Expose
            var previousPost:  Int = 0

            @SerializedName("previousReportingManager")
            @Expose
            var previousReportingManager:  Int = 0

            @SerializedName("isExpenseApprovalReq")
            @Expose
            var isExpenseApprovalReq:  Boolean = false

            @SerializedName("expenseApproveBy")
            @Expose
            var expenseApproveBy:  Int = 0

            @SerializedName("isMpin")
            @Expose
            var isMpin:  Boolean = false

            @SerializedName("ledgerCode")
            @Expose
            var ledgerCode:  String = ""

            @SerializedName("isFieldWorkingUser")
            @Expose
            var isFieldWorkingUser:  Boolean = false

            @SerializedName("isAccessBlocked")
            @Expose
            var isAccessBlocked:  Boolean = false

            @SerializedName("otp")
            @Expose
            var otp:  String = ""

            @SerializedName("isGeoFencingApplicable")
            @Expose
            var isGeoFencingApplicable:  Boolean = false

            @SerializedName("allowLocationUpdate")
            @Expose
            var allowLocationUpdate:  Boolean = false

            @SerializedName("enableSelfieAttendance")
            @Expose
            var enableSelfieAttendance:  Boolean = false

            @SerializedName("regionId")
            @Expose
            var regionId:  Int = 0

            @SerializedName("zoneId")
            @Expose
            var zoneId:  Int = 0

            @SerializedName("strDeactivationDate")
            @Expose
            var strDeactivationDate:  String = ""*/

            //this param is added only for get pob number in CreatePobActivity


        }
    }

    class ErrorObj {
        @SerializedName("errorMessage")
        @Expose
        var errorMessage:  String = ""

        @SerializedName("fldErrors")
        @Expose
        var fldErrors:  String = ""
    }

}