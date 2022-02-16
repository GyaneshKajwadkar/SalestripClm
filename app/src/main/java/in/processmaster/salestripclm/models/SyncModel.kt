package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SyncModel {
    @SerializedName("responseCode")
    @Expose
     var responseCode: Int = 0

    @SerializedName("errorObj")
    @Expose
     var errorObj: ErrorObj? = null

    @SerializedName("data")
    @Expose
     var data: Data? = null


    class ErrorObj {
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String = ""

        @SerializedName("fldErrors")
        @Expose
        var fldErrors: String = ""
    }
    class Data {
        @SerializedName("settingDCR")
        @Expose
        var settingDCR: SettingDCR? = null

        @SerializedName("routeList")
        @Expose
        var routeList= ArrayList<Route>()

        @SerializedName("specialistList")
        @Expose
        var specialistList= ArrayList<Specialist>()

        @SerializedName("qualificationList")
        @Expose
        var qualificationList= ArrayList<Qualification>()

        @SerializedName("cityList")
        @Expose
        var cityList= ArrayList<City>()

        @SerializedName("transportList")
        @Expose
        var transportList= ArrayList<Transport>()

        @SerializedName("workTypeList")
        @Expose
        var workTypeList= ArrayList<WorkType>()

        @SerializedName("retailerList")
        @Expose
        var retailerList= ArrayList<Retailer>()

        @SerializedName("doctorList")
        @Expose
        var doctorList= ArrayList<Doctor>()

        @SerializedName("productList")
        @Expose
        var productList= ArrayList<Product>()

        @SerializedName("brandList")
        @Expose
        var brandList= ArrayList<Brand>()

        @SerializedName("workingWithList")
        @Expose
        var workingWithList: ArrayList<WorkingWith>? = null

        @SerializedName("expenseListTypeWiseList")
        @Expose
        var expenseListTypeWiseList: ArrayList<ExpenseListTypeWise>? = null

        @SerializedName("fieldStaffTeamList")
        @Expose
        var fieldStaffTeamList: ArrayList<FieldStaffTeam>? = null

        @SerializedName("doctorFieldConfigDict")
        @Expose
        var doctorFieldConfigDict: String = ""

        @SerializedName("retailerFieldConfigDict")
        @Expose
        var retailerFieldConfigDict: String = ""

        @SerializedName("configurationSetting")
        @Expose
        var configurationSetting: String = ""

        @SerializedName("holidayList")
        @Expose
        var holidayList: ArrayList<Holiday>? = null

        @SerializedName("doctorCategoryList")
        @Expose
        var doctorCategoryList: ArrayList<DoctorCategory>? = null

        @SerializedName("commonMastersList")
        @Expose
        var commonMastersList: ArrayList<CommonMasters>? = null

        @SerializedName("hospitalList")
        @Expose
        var hospitalList: ArrayList<Hospital>? = null

        @SerializedName("schemeList")
        @Expose
        var schemeList: ArrayList<Scheme>? = null

        class Brand {
            @SerializedName("brandId")
            @Expose
            var brandId: Int = 0

            @SerializedName("brandName")
            @Expose
            var brandName: String = ""

            @SerializedName("divisionId")
            @Expose
            var divisionId: Int = 0

            @SerializedName("userId")
            @Expose
            var userId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("preBrandName")
            @Expose
            var preBrandName: String = ""

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("doctorId")
            @Expose
            var doctorId: Int = 0

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("eDetailId")
            @Expose
            private var eDetailId: Int = 0

            @SerializedName("categoryId")
            @Expose
            var categoryId: Int = 0

        }
        class City {
            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("cityId")
            @Expose
            var cityId: Int = 0

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("stdCode")
            @Expose
            var stdCode: String = ""

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("countryName")
            @Expose
            var countryName: String = ""

            @SerializedName("countryId")
            @Expose
            var countryId: Int = 0

            @SerializedName("preCityName")
            @Expose
            var preCityName: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""
        }
        class CommonMasters {
            @SerializedName("fieldCode")
            @Expose
            var fieldCode: String = ""

            @SerializedName("id")
            @Expose
            var id: Int = 0

            @SerializedName("name")
            @Expose
            var name: String = ""

            @SerializedName("active")
            @Expose
            var active: Boolean = false

            @SerializedName("mode")
            @Expose
            var mode: Int = 0
        }
        class Doctor {
            @SerializedName("doctorId")
            @Expose
            var doctorId: Int = 0

            @SerializedName("routeId")
            @Expose
            var routeId: Int = 0

            @SerializedName("entryBy")
            @Expose
            var entryBy: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("doctorName")
            @Expose
            var doctorName: String = ""

            @SerializedName("hqId")
            @Expose
            var hqId: Int = 0

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("hqName")
            @Expose
            var hqName: String = ""

            @SerializedName("routeName")
            @Expose
            var routeName: String = ""

            @SerializedName("visitFrequency")
            @Expose
            var visitFrequency: Int = 0

            @SerializedName("mobileNo")
            @Expose
            var mobileNo: String = ""

            @SerializedName("emailId")
            @Expose
            var emailId: String = ""

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("specialityName")
            @Expose
            var specialityName: String = ""

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String = ""

            @SerializedName("grade")
            @Expose
            var grade: String = ""

            @SerializedName("class")
            @Expose
            var class_: String = ""

            @SerializedName("territory")
            @Expose
            var territory: String = ""

            @SerializedName("firstName")
            @Expose
            var firstName: String = ""

            @SerializedName("latitude")
            @Expose
            var latitude: Double = 0.0

            @SerializedName("longitude")
            @Expose
            var longitude: Double = 0.0

            @SerializedName("linkedBrandList")
            @Expose
            var linkedBrandList: String = ""

            @SerializedName("totalMonthlyBusiness")
            @Expose
            var totalMonthlyBusiness: Double = 0.0

            @SerializedName("noOfPatients")
            @Expose
            var noOfPatients: Int = 0

            @SerializedName("incorporationDay")
            @Expose
            var incorporationDay: String = ""

            @SerializedName("amCore")
            @Expose
            var amCore: Boolean = false

            @SerializedName("rmCore")
            @Expose
            var rmCore: Boolean = false

            @SerializedName("mrCore")
            @Expose
            var mrCore: Boolean = false

            @SerializedName("routeType")
            @Expose
            var routeType: String = ""

            @SerializedName("product1Id")
            @Expose
            var product1Id: Int = 0

            @SerializedName("product2Id")
            @Expose
            var product2Id: Int = 0

            @SerializedName("product3Id")
            @Expose
            var product3Id: Int = 0

            @SerializedName("product4Id")
            @Expose
            var product4Id: Int = 0

            @SerializedName("product5Id")
            @Expose
            var product5Id: Int = 0

            @SerializedName("product1Name")
            @Expose
            var product1Name: String = ""

            @SerializedName("product2Name")
            @Expose
            var product2Name: String = ""

            @SerializedName("product3Name")
            @Expose
            var product3Name: String = ""

            @SerializedName("product4Name")
            @Expose
            var product4Name: String = ""

            @SerializedName("product5Name")
            @Expose
            var product5Name: String = ""

            @SerializedName("fieldStaffId")
            @Expose
            var fieldStaffId: Int = 0

            @SerializedName("speciality")
            @Expose
            var speciality: Int = 0
        }
        class DoctorCategory {
            @SerializedName("categoryId")
            @Expose
            var categoryId: Int = 0

            @SerializedName("categoryCode")
            @Expose
            var categoryCode: String = ""

            @SerializedName("categoryName")
            @Expose
            var categoryName: String = ""

            @SerializedName("visitFreq")
            @Expose
            var visitFreq: Int = 0

            @SerializedName("active")
            @Expose
            var active: Boolean = false
        }
        class ExpenseListTypeWise {
            @SerializedName("expenseHeadId")
            @Expose
            var expenseHeadId: Int = 0

            @SerializedName("expenseHead")
            @Expose
            var expenseHead: String = ""

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("preExpenseHead")
            @Expose
            var preExpenseHead: String = ""

            @SerializedName("expenseType")
            @Expose
            var expenseType: String = ""

            @SerializedName("expenseTypeName")
            @Expose
            var expenseTypeName: String = ""

            @SerializedName("type")
            @Expose
            var type: String = ""
        }
        class FieldStaffTeam {
            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("firstName")
            @Expose
            var firstName: String = ""

            @SerializedName("lastName")
            @Expose
            var lastName: String = ""

            @SerializedName("userName")
            @Expose
            var userName: String = ""

            @SerializedName("password")
            @Expose
            var password: String = ""

            @SerializedName("emailId")
            @Expose
            var emailId: String = ""

            @SerializedName("mobileNo")
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

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("countryId")
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

            @SerializedName("division")
            @Expose
            var division: String = ""

            @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: String = ""

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String = ""

            @SerializedName("hierachyId")
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
            var reportingManagerName: String = ""

            @SerializedName("reportingManagerEmail")
            @Expose
            var reportingManagerEmail: String = ""

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

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("compKey")
            @Expose
            var compKey: Int = 0

            @SerializedName("isFirstLogin")
            @Expose
            var isFirstLogin: Boolean = false

            @SerializedName("authPassword")
            @Expose
            var authPassword: String = ""

            @SerializedName("oldAuthPassword")
            @Expose
            var oldAuthPassword: String = ""

            @SerializedName("dashboardLayout")
            @Expose
            var dashboardLayout: String = ""

            @SerializedName("companyCode")
            @Expose
            var companyCode: String = ""

            @SerializedName("oldPassword")
            @Expose
            var oldPassword: String = ""

            @SerializedName("newPassword")
            @Expose
            var newPassword: String = ""

            @SerializedName("userType")
            @Expose
            var userType: String = ""

            @SerializedName("imageName")
            @Expose
            var imageName: String = ""

            @SerializedName("imagePath")
            @Expose
            var imagePath: String = ""

            @SerializedName("imageExt")
            @Expose
            var imageExt: String = ""

            @SerializedName("isAuthPassword")
            @Expose
            private var isAuthPassword: Boolean = false

            @SerializedName("roleType")
            @Expose
            var roleType: String = ""

            @SerializedName("roleName")
            @Expose
            var roleName: String = ""

            @SerializedName("prevRoleName")
            @Expose
            var prevRoleName: String = ""

            @SerializedName("roleTypeName")
            @Expose
            var roleTypeName: String = ""

            @SerializedName("linkedStates")
            @Expose
            var linkedStates: String = ""

            @SerializedName("oldReportingManagerName")
            @Expose
            var oldReportingManagerName: String = ""

            @SerializedName("expenseTemplateData")
            @Expose
            var expenseTemplateData: String = ""

            @SerializedName("isCheckIn")
            @Expose
            var isCheckIn: Boolean = false

            @SerializedName("lastDCRDate")
            @Expose
            var lastDCRDate: String = ""

            @SerializedName("strLastDCRDate")
            @Expose
            var strLastDCRDate: String = ""

            @SerializedName("mobileAppInstall")
            @Expose
            var mobileAppInstall: String = ""

            @SerializedName("passwordMd5Hash")
            @Expose
            var passwordMd5Hash: String = ""

            @SerializedName("haveToChangePassword")
            @Expose
            var haveToChangePassword: Boolean = false

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("canBeDeleted")
            @Expose
            var canBeDeleted: Boolean = false

            @SerializedName("absolutePath")
            @Expose
            var absolutePath: String = ""

            @SerializedName("disableSMSNotification")
            @Expose
            var disableSMSNotification: Boolean = false

            @SerializedName("prevFirstName")
            @Expose
            var prevFirstName: String = ""

            @SerializedName("prevLastName")
            @Expose
            var prevLastName: String = ""

            @SerializedName("prevUserName")
            @Expose
            var prevUserName: String = ""

            @SerializedName("fullName")
            @Expose
            var fullName: String = ""

            @SerializedName("headQuaterType")
            @Expose
            var headQuaterType: Int = 0

            @SerializedName("employeeCode")
            @Expose
            var employeeCode: String = ""

            @SerializedName("joiningDate")
            @Expose
            var joiningDate: String = ""

            @SerializedName("onFieldJobDate")
            @Expose
            var onFieldJobDate: String = ""

            @SerializedName("hierachyCode")
            @Expose
            var hierachyCode: String = ""

            @SerializedName("hierachyType")
            @Expose
            var hierachyType: String = ""

            @SerializedName("reportingManagerStatus")
            @Expose
            var reportingManagerStatus: String = ""

            @SerializedName("lastLoginDeviceId")
            @Expose
            var lastLoginDeviceId: String = ""

            @SerializedName("pushToken")
            @Expose
            var pushToken: String = ""

            @SerializedName("hierDesc")
            @Expose
            var hierDesc: String = ""

            @SerializedName("teamLevel")
            @Expose
            var teamLevel: Int = 0

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("linkedStateName")
            @Expose
            var linkedStateName: String = ""

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String = ""

            @SerializedName("nameWithCode")
            @Expose
            var nameWithCode: String = ""

            @SerializedName("receiveEmailUpdate")
            @Expose
            var receiveEmailUpdate: Boolean = false

            @SerializedName("mainHeadQuarter")
            @Expose
            var mainHeadQuarter: Int = 0

            @SerializedName("callDetails")
            @Expose
            var callDetails: String = ""

            @SerializedName("strDateOfBirth")
            @Expose
            var strDateOfBirth: String = ""

            @SerializedName("strJoiningDate")
            @Expose
            var strJoiningDate: String = ""

            @SerializedName("strOnFieldJobDate")
            @Expose
            var strOnFieldJobDate: String = ""

            @SerializedName("reportingHierachyCode")
            @Expose
            var reportingHierachyCode: String = ""

            @SerializedName("mobileAppVersionInUse")
            @Expose
            var mobileAppVersionInUse: String = ""

            @SerializedName("hierFixedType")
            @Expose
            var hierFixedType: String = ""

            @SerializedName("isFieldWorkingUser")
            @Expose
            var isFieldWorkingUser: Boolean = false

            @SerializedName("isAccessBlocked")
            @Expose
            var isAccessBlocked: Boolean = false

            @SerializedName("mPin")
            @Expose
            private var mPin: String = ""

            @SerializedName("isMpin")
            @Expose
            var isMpin: Boolean = false

            @SerializedName("fingerprint")
            @Expose
            var fingerprint: String = ""

            @SerializedName("fieldStaffId")
            @Expose
            var fieldStaffId: Int = 0

            @SerializedName("baseHier")
            @Expose
            var baseHier: Int = 0

            @SerializedName("fsCode")
            @Expose
            var fsCode: String = ""

            @SerializedName("isEmpLink")
            @Expose
            var isEmpLink: Boolean = false

            @SerializedName("zoneId")
            @Expose
            var zoneId: Int = 0

            @SerializedName("zoneName")
            @Expose
            var zoneName: String = ""

            @SerializedName("regionId")
            @Expose
            var regionId: Int = 0

            @SerializedName("regionName")
            @Expose
            var regionName: String = ""

            @SerializedName("deactivateDate")
            @Expose
            var deactivateDate: String = ""

            @SerializedName("level")
            @Expose
            var level: Int = 0

            @SerializedName("reportingFSId")
            @Expose
            var reportingFSId: Int = 0

            @SerializedName("lastLoginDate")
            @Expose
            var lastLoginDate: String = ""

            @SerializedName("strLastLoginDate")
            @Expose
            var strLastLoginDate: String = ""

            @SerializedName("fsCodeWithEmpName")
            @Expose
            var fsCodeWithEmpName: String = ""

            @SerializedName("isTPDraftExist")
            @Expose
            var isTPDraftExist: Boolean = false

            @SerializedName("rtpId")
            @Expose
            var rtpId: Int = 0

            @SerializedName("strDeactivateDate")
            @Expose
            var strDeactivateDate: String = ""

            @SerializedName("isPromoted")
            @Expose
            var isPromoted: Boolean = false

            @SerializedName("preHierachyId")
            @Expose
            var preHierachyId: String = ""

            @SerializedName("transactionDate")
            @Expose
            var transactionDate: String = ""

            @SerializedName("isActive")
            @Expose
            private var isActive: Boolean = false

            @SerializedName("isDocDCRV2")
            @Expose
            var isDocDCRV2: Boolean = false

            @SerializedName("otp")
            @Expose
            var otp: String = ""

            @SerializedName("isGeoFencingApplicable")
            @Expose
            var isGeoFencingApplicable: Boolean = false

            @SerializedName("allowLocationUpdate")
            @Expose
            var allowLocationUpdate: Boolean = false

            @SerializedName("allowDoctorEdit")
            @Expose
            var allowDoctorEdit: Boolean = false

            @SerializedName("lastWorkingDate")
            @Expose
            var lastWorkingDate: String = ""

            @SerializedName("uanNo")
            @Expose
            var uanNo: String = ""

            @SerializedName("panNo")
            @Expose
            var panNo: String = ""

            @SerializedName("aadharNo")
            @Expose
            var aadharNo: String = ""

            @SerializedName("bankName")
            @Expose
            var bankName: String = ""

            @SerializedName("bankAccountNo")
            @Expose
            var bankAccountNo: String = ""

            @SerializedName("lastPasswordChangedDate")
            @Expose
            var lastPasswordChangedDate: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("weekOff")
            @Expose
            var weekOff: String = ""

        }
        class Holiday {
            @SerializedName("holidayId")
            @Expose
            var holidayId: Int = 0

            @SerializedName("holidayName")
            @Expose
            var holidayName: String = ""

            @SerializedName("holidayFrom")
            @Expose
            var holidayFrom: String = ""

            @SerializedName("holidayTo")
            @Expose
            var holidayTo: String = ""

            @SerializedName("preHolidayName")
            @Expose
            var preHolidayName: String = ""

            @SerializedName("strHolidayFrom")
            @Expose
            var strHolidayFrom: String = ""

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("holidayType")
            @Expose
            var holidayType: String = ""

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("state")
            @Expose
            var state: String = ""

            @SerializedName("holidayDate")
            @Expose
            var holidayDate: String = ""

            @SerializedName("holidayTypeValue")
            @Expose
            var holidayTypeValue: String = ""

            @SerializedName("strHolidayTo")
            @Expose
            var strHolidayTo: String = ""

            @SerializedName("canBeEdit")
            @Expose
            var canBeEdit: Boolean = false

            @SerializedName("monthName")
            @Expose
            var monthName: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("year")
            @Expose
            var year: Int = 0

            @SerializedName("month")
            @Expose
            var month: Int = 0

            @SerializedName("day")
            @Expose
            var day: Int = 0

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("divisionId")
            @Expose
            var divisionId: Int = 0

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("division")
            @Expose
            var division: String = ""

            @SerializedName("creationDate")
            @Expose
            var creationDate: String = ""
        }
        class Hospital {
            @SerializedName("address")
            @Expose
            var address: String = ""

            @SerializedName("busiSegmentId")
            @Expose
            var busiSegmentId: Int = 0

            @SerializedName("categoryId")
            @Expose
            var categoryId: Int = 0

            @SerializedName("cityId")
            @Expose
            var cityId: Int = 0

            @SerializedName("competitorName")
            @Expose
            var competitorName: String = ""

            @SerializedName("dataSaveType")
            @Expose
            var dataSaveType: String = ""

            @SerializedName("emailId")
            @Expose
            var emailId: String = ""

            @SerializedName("gradeId")
            @Expose
            var gradeId: Int = 0

            @SerializedName("hospitalId")
            @Expose
            var hospitalId: Int = 0

            @SerializedName("hospitalName")
            @Expose
            var hospitalName: String = ""

            @SerializedName("imageName")
            @Expose
            var imageName: String = ""

            @SerializedName("imagePath")
            @Expose
            var imagePath: String = ""

            @SerializedName("isAssoWithInsurComp")
            @Expose
            var isAssoWithInsurComp: Boolean = false

            @SerializedName("isMISurgery")
            @Expose
            var isMISurgery: Boolean = false

            @SerializedName("isNABHCertified")
            @Expose
            var isNABHCertified: Boolean = false

            @SerializedName("isSurgWithPackAvl")
            @Expose
            var isSurgWithPackAvl: Boolean = false

            @SerializedName("isUsingERP")
            @Expose
            var isUsingERP: Boolean = false

            @SerializedName("latitude")
            @Expose
            var latitude: Double = 0.0

            @SerializedName("longitude")
            @Expose
            var longitude: Double = 0.0

            @SerializedName("mobileNo")
            @Expose
            var mobileNo: String = ""

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("noOfBeds")
            @Expose
            var noOfBeds: Int = 0

            @SerializedName("noOfOT")
            @Expose
            var noOfOT: Int = 0

            @SerializedName("noOfSurgeriesPM")
            @Expose
            var noOfSurgeriesPM: Int = 0

            @SerializedName("phoneNo")
            @Expose
            var phoneNo: String = ""

            @SerializedName("pinCode")
            @Expose
            var pinCode: String = ""

            @SerializedName("prodId")
            @Expose
            var prodId: String = ""

            @SerializedName("specialityId")
            @Expose
            var specialityId: String = ""

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("stockistName")
            @Expose
            var stockistName: String = ""

            @SerializedName("surgeriesId")
            @Expose
            var surgeriesId: String = ""

            @SerializedName("website")
            @Expose
            var website: String = ""

            @SerializedName("hospitalDoctorLinkingList")
            @Expose
            var hospitalDoctorLinkingList: List<HospitalDoctorLinking>? = null

            @SerializedName("active")
            @Expose
            var active: Boolean = false

            @SerializedName("busiSegmentName")
            @Expose
            var busiSegmentName: String = ""

            @SerializedName("categoryName")
            @Expose
            var categoryName: String = ""

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("gradeName")
            @Expose
            var gradeName: String = ""

            @SerializedName("prodName")
            @Expose
            var prodName: String = ""

            @SerializedName("specialityName")
            @Expose
            var specialityName: String = ""

            @SerializedName("surgeriesName")
            @Expose
            var surgeriesName: String = ""

            @SerializedName("fieldStaffId")
            @Expose
            var fieldStaffId: Int = 0

            @SerializedName("fsCode")
            @Expose
            var fsCode: String = ""

            @SerializedName("empName")
            @Expose
            var empName: String = ""

            @SerializedName("routeId")
            @Expose
            var routeId: Int = 0

            @SerializedName("routeName")
            @Expose
            var routeName: String = ""

            @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: Int = 0

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("hospCode")
            @Expose
            var hospCode: String = ""

            @SerializedName("entryDate")
            @Expose
            var entryDate: String = ""

            @SerializedName("entryBy")
            @Expose
            var entryBy: String = ""

            @SerializedName("potential")
            @Expose
            var potential: Int = 0

            @SerializedName("competitorId")
            @Expose
            var competitorId: String = ""

            class HospitalDoctorLinking {
                @SerializedName("authId")
                @Expose
                var authId: Int = 0

                @SerializedName("strDOB")
                @Expose
                var strDOB: String = ""

                @SerializedName("doctorId")
                @Expose
                var doctorId: Int = 0

                @SerializedName("doctorName")
                @Expose
                var doctorName: String = ""

                @SerializedName("emailId")
                @Expose
                var emailId: String = ""

                @SerializedName("gender")
                @Expose
                var gender: String = ""

                @SerializedName("phoneNo")
                @Expose
                var phoneNo: String = ""

                @SerializedName("prefDay")
                @Expose
                var prefDay: String = ""

                @SerializedName("prefTime")
                @Expose
                var prefTime: String = ""

                @SerializedName("specialityName")
                @Expose
                var specialityName: String = ""

                @SerializedName("registrationNo")
                @Expose
                var registrationNo: String = ""

                @SerializedName("hospitalId")
                @Expose
                var hospitalId: Int = 0

                @SerializedName("authName")
                @Expose
                var authName: String = ""

                @SerializedName("grade")
                @Expose
                var grade: String = ""

                @SerializedName("desgId")
                @Expose
                var desgId: Int = 0

                @SerializedName("isLaproSurg")
                @Expose
                var isLaproSurg: String = ""

                @SerializedName("desgName")
                @Expose
                var desgName: String = ""

                @SerializedName("mobileNo")
                @Expose
                var mobileNo: String = ""
            }
        }
        class Product {
            @SerializedName("productId")
            @Expose
            var productId: Int = 0

            @SerializedName("productName")
            @Expose
            var productName: String = ""

            @SerializedName("prodCode")
            @Expose
            var prodCode: String = ""

            @SerializedName("shortName")
            @Expose
            var shortName: String = ""

            @SerializedName("divisionId")
            @Expose
            var divisionId: Int = 0

            @SerializedName("categoryId")
            @Expose
            var categoryId: Int = 0

            @SerializedName("packingTypeId")
            @Expose
            var packingTypeId: Int = 0

            @SerializedName("mrp")
            @Expose
            var mrp: Double = 0.0

            @SerializedName("productType")
            @Expose
            var productType: Int = 0

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("dosageId")
            @Expose
            var dosageId: Int = 0

            @SerializedName("productTypeName")
            @Expose
            var productTypeName: String = ""

            @SerializedName("packingTypeName")
            @Expose
            var packingTypeName: String = ""

            @SerializedName("categoryName")
            @Expose
            var categoryName: String = ""

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("price")
            @Expose
            var price: Double = 0.0

            @SerializedName("priceToStockist")
            @Expose
            var priceToStockist: Double = 0.0

            @SerializedName("targetPrice")
            @Expose
            var targetPrice: Double = 0.0

            @SerializedName("status")
            @Expose
            var status: String = ""

            @SerializedName("preProductName")
            @Expose
            var preProductName: String = ""

            @SerializedName("stock")
            @Expose
            var stock: Double = 0.0

            @SerializedName("isDisabled")
            @Expose
            var isDisabled: Boolean = false

            @SerializedName("prevTransitStock")
            @Expose
            var prevTransitStock: Double = 0.0

            @SerializedName("brandId")
            @Expose
            var brandId: Int = 0

            @SerializedName("eDetailId")
            @Expose
            private var eDetailId: Int = 0

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("dosageName")
            @Expose
            var dosageName: String = ""

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("sampleRate")
            @Expose
            var sampleRate: Double = 0.0

            @SerializedName("samplePackSizeId")
            @Expose
            var samplePackSizeId: Int = 0

            @SerializedName("allowSample")
            @Expose
            var allowSample: Boolean = false

            @SerializedName("giftCategoryId")
            @Expose
            var giftCategoryId: String = ""

            @SerializedName("samplePackSizeName")
            @Expose
            var samplePackSizeName: String = ""

            @SerializedName("giftCategoryName")
            @Expose
            var giftCategoryName: String = ""

            @SerializedName("brandName")
            @Expose
            var brandName: String = ""

            @SerializedName("sampleProductName")
            @Expose
            var sampleProductName: String = ""

            @SerializedName("sampleProdCode")
            @Expose
            var sampleProdCode: String = ""

            @SerializedName("createdOn")
            @Expose
            var createdOn: String = ""

            @SerializedName("freeStock")
            @Expose
            var freeStock: Double = 0.0
            
        }
        class Qualification {
            @SerializedName("qualificationId")
            @Expose
            var qualificationId: Int = 0

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String = ""

            @SerializedName("qualificationGroup")
            @Expose
            var qualificationGroup: String = ""

            @SerializedName("qualificationCode")
            @Expose
            var qualificationCode: String = ""

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("preQualificationCode")
            @Expose
            var preQualificationCode: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("errorCode")
            @Expose
            var errorCode: String = ""
        }
        class Retailer {
            @SerializedName("retailerId")
            @Expose
            var retailerId: Int = 0

            @SerializedName("shopName")
            @Expose
            var shopName: String = ""

            @SerializedName("contactPerson")
            @Expose
            var contactPerson: String = ""

            @SerializedName("address1")
            @Expose
            var address1: String = ""

            @SerializedName("address2")
            @Expose
            var address2: String = ""

            @SerializedName("routeId")
            @Expose
            var routeId: Int = 0

            @SerializedName("cityId")
            @Expose
            var cityId: Int = 0

            @SerializedName("pincode")
            @Expose
            var pincode: String = ""

            @SerializedName("phoneNo")
            @Expose
            var phoneNo: String = ""

            @SerializedName("mobileNo")
            @Expose
            var mobileNo: String = ""

            @SerializedName("emailId")
            @Expose
            var emailId: String = ""

            @SerializedName("isHospitalChemist")
            @Expose
            var isHospitalChemist: Boolean = false

            @SerializedName("hospitalType")
            @Expose
            var hospitalType: Int = 0

            @SerializedName("hospitalName")
            @Expose
            var hospitalName: String = ""

            @SerializedName("drugLic1")
            @Expose
            var drugLic1: String = ""

            @SerializedName("drugLic2")
            @Expose
            var drugLic2: String = ""

            @SerializedName("visitDay")
            @Expose
            var visitDay: Int = 0

            @SerializedName("visitTime")
            @Expose
            var visitTime: String = ""

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: Int = 0

            @SerializedName("preShopName")
            @Expose
            var preShopName: String = ""

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("countryName")
            @Expose
            var countryName: String = ""

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String = ""

            @SerializedName("routeName")
            @Expose
            var routeName: String = ""

            @SerializedName("gstinNo")
            @Expose
            var gstinNo: String = ""

            @SerializedName("status")
            @Expose
            var status: String = ""

            @SerializedName("approveStatus")
            @Expose
            var approveStatus: String = ""

            @SerializedName("approveStatusName")
            @Expose
            var approveStatusName: String = ""

            @SerializedName("deactivateStatusName")
            @Expose
            var deactivateStatusName: String = ""

            @SerializedName("approveBy")
            @Expose
            var approveBy: Int = 0

            @SerializedName("rejectReason")
            @Expose
            var rejectReason: String = ""

            @SerializedName("active")
            @Expose
            var active: Boolean = false

            @SerializedName("approveByName")
            @Expose
            var approveByName: String = ""

            @SerializedName("createdBy")
            @Expose
            var createdBy: String = ""

            @SerializedName("approveDate")
            @Expose
            var approveDate: String = ""

            @SerializedName("strApproveDate")
            @Expose
            var strApproveDate: String = ""

            @SerializedName("createdDate")
            @Expose
            var createdDate: String = ""

            @SerializedName("strCreatedDate")
            @Expose
            var strCreatedDate: String = ""

            @SerializedName("statusType")
            @Expose
            var statusType: String = ""

            @SerializedName("strVisitTime")
            @Expose
            var strVisitTime: String = ""

            @SerializedName("type")
            @Expose
            var type: String = ""

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("employeeManagerId")
            @Expose
            var employeeManagerId: Int = 0

            @SerializedName("userType")
            @Expose
            var userType: String = ""

            @SerializedName("canBeEdited")
            @Expose
            var canBeEdited: Boolean = false

            @SerializedName("employeeName")
            @Expose
            var employeeName: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("latitude")
            @Expose
            var latitude: Double = 0.0

            @SerializedName("longitude")
            @Expose
            var longitude: Double = 0.0

            @SerializedName("isImageAdd")
            @Expose
            var isImageAdd: Boolean = false

            @SerializedName("imageName")
            @Expose
            var imageName: String = ""

            @SerializedName("imagePath")
            @Expose
            var imagePath: String = ""

            @SerializedName("imageExt")
            @Expose
            var imageExt: String = ""

            @SerializedName("urlPath")
            @Expose
            var urlPath: String = ""

            @SerializedName("savedFileName")
            @Expose
            var savedFileName: String = ""

            @SerializedName("preImageName")
            @Expose
            var preImageName: String = ""

            @SerializedName("incorporationDayTime")
            @Expose
            var incorporationDayTime: String = ""

            @SerializedName("amCore")
            @Expose
            var amCore: Boolean = false

            @SerializedName("rmCore")
            @Expose
            var rmCore: Boolean = false

            @SerializedName("mrCore")
            @Expose
            var mrCore: Boolean = false

            @SerializedName("anniversaryDateTime")
            @Expose
            var anniversaryDateTime: String = ""

            @SerializedName("birthDateTime")
            @Expose
            var birthDateTime: String = ""

            @SerializedName("code")
            @Expose
            var code: String = ""

            @SerializedName("birthDate")
            @Expose
            var birthDate: String = ""

            @SerializedName("anniversaryDate")
            @Expose
            var anniversaryDate: String = ""

            @SerializedName("incorporationDay")
            @Expose
            var incorporationDay: String = ""

            @SerializedName("nextApproval")
            @Expose
            var nextApproval: Int = 0

            @SerializedName("deactivateApproveBy")
            @Expose
            var deactivateApproveBy: String = ""

            @SerializedName("deactivateRejectReason")
            @Expose
            var deactivateRejectReason: String = ""

            @SerializedName("deactivateRequestDate")
            @Expose
            var deactivateRequestDate: String = ""

            @SerializedName("strDeactivateRequestDate")
            @Expose
            var strDeactivateRequestDate: String = ""

            @SerializedName("deactivateApproveStatus")
            @Expose
            var deactivateApproveStatus: String = ""

            @SerializedName("deactivateRemark")
            @Expose
            var deactivateRemark: String = ""

            @SerializedName("nextApproverName")
            @Expose
            var nextApproverName: String = ""

            @SerializedName("fieldStaffId")
            @Expose
            var fieldStaffId: Int = 0

            @SerializedName("fsCode")
            @Expose
            var fsCode: String = ""

            @SerializedName("requestId")
            @Expose
            var requestId: String = ""

            @SerializedName("empCode")
            @Expose
            var empCode: String = ""

            @SerializedName("designation")
            @Expose
            var designation: String = ""

            @SerializedName("division")
            @Expose
            var division: String = ""

            @SerializedName("region")
            @Expose
            var region: String = ""

            @SerializedName("zone")
            @Expose
            var zone: String = ""

            @SerializedName("mobileNo2")
            @Expose
            var mobileNo2: String = ""

            @SerializedName("fsCodeWithEmpName")
            @Expose
            var fsCodeWithEmpName: String = ""

            @SerializedName("chemCode")
            @Expose
            var chemCode: String = ""
        }
        class Route {
            @SerializedName("routeId")
            @Expose
            var routeId: Int = 0

            @SerializedName("routeName")
            @Expose
            var routeName: String = ""

            override fun toString(): String {
                return routeName
            }

            @SerializedName("routeType")
            @Expose
            var routeType: Int = 0

            @SerializedName("setNo")
            @Expose
            var setNo: Int = 0

            @SerializedName("distanceFrom")
            @Expose
            var distanceFrom: Double = 0.0

            @SerializedName("distanceTo")
            @Expose
            var distanceTo: Double = 0.0

            @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: Int = 0

            @SerializedName("remark")
            @Expose
            var remark: String = ""

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String = ""

            @SerializedName("routeTypeName")
            @Expose
            var routeTypeName: String = ""

            @SerializedName("preRouteName")
            @Expose
            var preRouteName: String = ""

            @SerializedName("totalDistance")
            @Expose
            var totalDistance: Double = 0.0

            @SerializedName("hqTypeId")
            @Expose
            var hqTypeId: Int = 0

            @SerializedName("hqTypeName")
            @Expose
            var hqTypeName: String = ""

            @SerializedName("cityId")
            @Expose
            var cityId: Int = 0

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("status")
            @Expose
            var status: String = ""

            @SerializedName("isBaseStation")
            @Expose
            var isBaseStation: Boolean = false

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("doctorCount")
            @Expose
            var doctorCount: Int = 0

            @SerializedName("retailerCount")
            @Expose
            var retailerCount: Int = 0

            @SerializedName("allowExpense")
            @Expose
            var allowExpense: Boolean = false

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("errorCode")
            @Expose
            var errorCode: String = ""

            @SerializedName("mrCoreDoctorCount")
            @Expose
            var mrCoreDoctorCount: Int = 0

            @SerializedName("mrCoreRetailerCount")
            @Expose
            var mrCoreRetailerCount: Int = 0

            @SerializedName("fieldStaffId")
            @Expose
            var fieldStaffId: Int = 0

            @SerializedName("zoneId")
            @Expose
            var zoneId: Int = 0

            @SerializedName("zoneName")
            @Expose
            var zoneName: String = ""

            @SerializedName("regionId")
            @Expose
            var regionId: Int = 0

            @SerializedName("regionName")
            @Expose
            var regionName: String = ""

            @SerializedName("fsCode")
            @Expose
            var fsCode: String = ""

            @SerializedName("linkedDivName")
            @Expose
            var linkedDivName: String = ""

            @SerializedName("pinCode")
            @Expose
            var pinCode: Int = 0
        }
        class Scheme {
            @SerializedName("schemeId")
            @Expose
            var schemeId: Int = 0

            @SerializedName("schemeDate")
            @Expose
            var schemeDate: String = ""

            @SerializedName("divisionId")
            @Expose
            var divisionId: Int = 0

            @SerializedName("schemeFor")
            @Expose
            var schemeFor: String = ""

            @SerializedName("schemeForId")
            @Expose
            var schemeForId: String = ""

            @SerializedName("salesQty")
            @Expose
            var salesQty: Int = 0

            @SerializedName("freeQty")
            @Expose
            var freeQty: Int = 0

            @SerializedName("validFrom")
            @Expose
            var validFrom: String = ""

            @SerializedName("validUpto")
            @Expose
            var validUpto: String = ""

            @SerializedName("productId")
            @Expose
            var productId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("productName")
            @Expose
            var productName: String = ""

            @SerializedName("schemeForName")
            @Expose
            var schemeForName: String = ""

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("schemeName")
            @Expose
            var schemeName: String = ""

            @SerializedName("schemeTypeName")
            @Expose
            var schemeTypeName: String = ""

            @SerializedName("isDisable")
            @Expose
            var isDisable: Boolean = false

            @SerializedName("isSchemeDisable")
            @Expose
            var isSchemeDisable: String = ""

            @SerializedName("isPobDone")
            @Expose
            var isPobDone: Boolean = false
        }
        class SettingDCR {
            @SerializedName("settingId")
            @Expose
            var settingId: Int = 0

            @SerializedName("roleType")
            @Expose
            var roleType: String = ""

            @SerializedName("sequentialDCR")
            @Expose
            var sequentialDCR: Boolean = false

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("hqType")
            @Expose
            var hqType: Int = 0

            @SerializedName("allowBackDate")
            @Expose
            var allowBackDate: Int = 0

            @SerializedName("isRouteDeviationApproval")
            @Expose
            var isRouteDeviationApproval: Boolean = false

            @SerializedName("isDoctorFencingRequired")
            @Expose
            var isDoctorFencingRequired: Boolean = false

            @SerializedName("isRetailerFencingRequired")
            @Expose
            var isRetailerFencingRequired: Boolean = false

            @SerializedName("roleTypeName")
            @Expose
            var roleTypeName: String = ""

            @SerializedName("hqTypeName")
            @Expose
            var hqTypeName: String = ""

            @SerializedName("isDCRApproval")
            @Expose
            var isDCRApproval: Boolean = false

            @SerializedName("isGeoLocationRequired")
            @Expose
            var isGeoLocationRequired: Boolean = false

            @SerializedName("reportingAllow")
            @Expose
            var reportingAllow: String = ""

            @SerializedName("isTPMandatory")
            @Expose
            var isTPMandatory: Boolean = false

            @SerializedName("isAllowBackDate")
            @Expose
            private var isAllowBackDate: Boolean = false

            @SerializedName("isRestrictedParty")
            @Expose
            var isRestrictedParty: Boolean = false

            @SerializedName("minCallPerDayDoc")
            @Expose
            var minCallPerDayDoc: Int = 0

            @SerializedName("minCallPerDayChem")
            @Expose
            var minCallPerDayChem: Int = 0

            @SerializedName("isRCPAMandatoryForChemistReport")
            @Expose
            var isRCPAMandatoryForChemistReport: Boolean = false

            @SerializedName("isSTPMandatory")
            @Expose
            var isSTPMandatory: Boolean = false

            @SerializedName("isProductReportingMandatory")
            @Expose
            var isProductReportingMandatory: Boolean = false

            @SerializedName("tpSubmission")
            @Expose
            var tpSubmission: Int = 0

            @SerializedName("planCompliancePercentage")
            @Expose
            var planCompliancePercentage: Double = 0.0

            @SerializedName("needDCRExpApp")
            @Expose
            var needDCRExpApp: Boolean = false

            @SerializedName("callPlanBackDays")
            @Expose
            var callPlanBackDays: Int = 0

            @SerializedName("isCallPlanMandatoryForDCR")
            @Expose
            var isCallPlanMandatoryForDCR: Boolean = false

            @SerializedName("isSelfieAttendanceRequired")
            @Expose
            var isSelfieAttendanceRequired: Boolean = false

            @SerializedName("isGiftReportingMandatory")
            @Expose
            var isGiftReportingMandatory: Boolean = false

            @SerializedName("isSampleReportingMandatory")
            @Expose
            var isSampleReportingMandatory: Boolean = false

            @SerializedName("minInclinicEffectivenessCall")
            @Expose
            var minInclinicEffectivenessCall: Int = 0

            @SerializedName("isBlockDCRAfterTPDeadline")
            @Expose
            var isBlockDCRAfterTPDeadline: Boolean = false

            @SerializedName("allowNegativeSampleAndGiftDist")
            @Expose
            var allowNegativeSampleAndGiftDist: Boolean = false

            @SerializedName("minChemistRCPA")
            @Expose
            var minChemistRCPA: Int = 0

            @SerializedName("expenseSubmissionDeadline")
            @Expose
            var expenseSubmissionDeadline: Int = 0

            @SerializedName("isBlockDCRWhenAppReqPending")
            @Expose
            var isBlockDCRWhenAppReqPending: Boolean = false

            @SerializedName("divisionId")
            @Expose
            var divisionId: Int = 0

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("expenseCalculateBy")
            @Expose
            var expenseCalculateBy: String = ""

            @SerializedName("isFMCGApplicable")
            @Expose
            var isFMCGApplicable: Boolean = false

            @SerializedName("isVisitRemarkMandatory")
            @Expose
            var isVisitRemarkMandatory: Boolean = false

            @SerializedName("totalWorkingHoursReq")
            @Expose
            var totalWorkingHoursReq: Double = 0.0

            fun getIsAllowBackDate(): Boolean? {
                return isAllowBackDate
            }


        }
        class Specialist {
            @SerializedName("specialistId")
            @Expose
            var specialistId: Int = 0

            @SerializedName("specialistName")
            @Expose
            var specialistName: String = ""

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("preSpecialistName")
            @Expose
            var preSpecialistName: String = ""
        }
        class Transport {
            @SerializedName("transportId")
            @Expose
            var transportId: Int = 0

            @SerializedName("transportName")
            @Expose
            var transportName: String = ""
        }
        class WorkType {
            @SerializedName("workId")
            @Expose
            var workId: Int = 0

            @SerializedName("workType")
            @Expose
            var workType: String = ""

            override fun toString(): String {
                return workType
            }
        }
        class WorkingWith {
            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("firstName")
            @Expose
            var firstName: String = ""

            @SerializedName("lastName")
            @Expose
            var lastName: String = ""

            @SerializedName("userName")
            @Expose
            var userName: String = ""

            @SerializedName("password")
            @Expose
            var password: String = ""

            @SerializedName("emailId")
            @Expose
            var emailId: String = ""

            @SerializedName("mobileNo")
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

            @SerializedName("stateId")
            @Expose
            var stateId: Int = 0

            @SerializedName("stateName")
            @Expose
            var stateName: String = ""

            @SerializedName("countryId")
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

            @SerializedName("division")
            @Expose
            var division: String = ""

            @SerializedName("headQuaterId")
            @Expose
            var headQuaterId: String = ""

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String = ""

            @SerializedName("hierachyId")
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
            var reportingManagerName: String = ""

            @SerializedName("reportingManagerEmail")
            @Expose
            var reportingManagerEmail: String = ""

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

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("compKey")
            @Expose
            var compKey: Int = 0

            @SerializedName("isFirstLogin")
            @Expose
            var isFirstLogin: Boolean = false

            @SerializedName("authPassword")
            @Expose
            var authPassword: String = ""

            @SerializedName("oldAuthPassword")
            @Expose
            var oldAuthPassword: String = ""

            @SerializedName("dashboardLayout")
            @Expose
            var dashboardLayout: String = ""

            @SerializedName("companyCode")
            @Expose
            var companyCode: String = ""

            @SerializedName("oldPassword")
            @Expose
            var oldPassword: String = ""

            @SerializedName("newPassword")
            @Expose
            var newPassword: String = ""

            @SerializedName("userType")
            @Expose
            var userType: String = ""

            @SerializedName("imageName")
            @Expose
            var imageName: String = ""

            @SerializedName("imagePath")
            @Expose
            var imagePath: String = ""

            @SerializedName("imageExt")
            @Expose
            var imageExt: String = ""

            @SerializedName("isAuthPassword")
            @Expose
            private var isAuthPassword: Boolean = false

            @SerializedName("roleType")
            @Expose
            var roleType: String = ""

            @SerializedName("roleName")
            @Expose
            var roleName: String = ""

            @SerializedName("prevRoleName")
            @Expose
            var prevRoleName: String = ""

            @SerializedName("roleTypeName")
            @Expose
            var roleTypeName: String = ""

            @SerializedName("linkedStates")
            @Expose
            var linkedStates: String = ""

            @SerializedName("oldReportingManagerName")
            @Expose
            var oldReportingManagerName: String = ""

            @SerializedName("expenseTemplateData")
            @Expose
            var expenseTemplateData: String = ""

            @SerializedName("isCheckIn")
            @Expose
            var isCheckIn: Boolean = false

            @SerializedName("lastDCRDate")
            @Expose
            var lastDCRDate: String = ""

            @SerializedName("strLastDCRDate")
            @Expose
            var strLastDCRDate: String = ""

            @SerializedName("mobileAppInstall")
            @Expose
            var mobileAppInstall: String = ""

            @SerializedName("passwordMd5Hash")
            @Expose
            var passwordMd5Hash: String = ""

            @SerializedName("haveToChangePassword")
            @Expose
            var haveToChangePassword: Boolean = false

            @SerializedName("cityName")
            @Expose
            var cityName: String = ""

            @SerializedName("canBeDeleted")
            @Expose
            var canBeDeleted: Boolean = false

            @SerializedName("absolutePath")
            @Expose
            var absolutePath: String = ""

            @SerializedName("disableSMSNotification")
            @Expose
            var disableSMSNotification: Boolean = false

            @SerializedName("prevFirstName")
            @Expose
            var prevFirstName: String = ""

            @SerializedName("prevLastName")
            @Expose
            var prevLastName: String = ""

            @SerializedName("prevUserName")
            @Expose
            var prevUserName: String = ""

            @SerializedName("fullName")
            @Expose
            var fullName: String = ""

            @SerializedName("headQuaterType")
            @Expose
            var headQuaterType: Int = 0

            @SerializedName("employeeCode")
            @Expose
            var employeeCode: String = ""

            @SerializedName("joiningDate")
            @Expose
            var joiningDate: String = ""

            @SerializedName("onFieldJobDate")
            @Expose
            var onFieldJobDate: String = ""

            @SerializedName("hierachyCode")
            @Expose
            var hierachyCode: String = ""

            @SerializedName("hierachyType")
            @Expose
            var hierachyType: String = ""

            @SerializedName("reportingManagerStatus")
            @Expose
            var reportingManagerStatus: String = ""

            @SerializedName("lastLoginDeviceId")
            @Expose
            var lastLoginDeviceId: String = ""

            @SerializedName("pushToken")
            @Expose
            var pushToken: String = ""

            @SerializedName("hierDesc")
            @Expose
            var hierDesc: String = ""

            @SerializedName("teamLevel")
            @Expose
            var teamLevel: Int = 0

            @SerializedName("divisionName")
            @Expose
            var divisionName: String = ""

            @SerializedName("linkedStateName")
            @Expose
            var linkedStateName: String = ""

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String = ""

            @SerializedName("nameWithCode")
            @Expose
            var nameWithCode: String = ""

            @SerializedName("receiveEmailUpdate")
            @Expose
            var receiveEmailUpdate: Boolean = false

            @SerializedName("mainHeadQuarter")
            @Expose
            var mainHeadQuarter: Int = 0

            @SerializedName("callDetails")
            @Expose
            var callDetails: String = ""

            @SerializedName("strDateOfBirth")
            @Expose
            var strDateOfBirth: String = ""

            @SerializedName("strJoiningDate")
            @Expose
            var strJoiningDate: String = ""

            @SerializedName("strOnFieldJobDate")
            @Expose
            var strOnFieldJobDate: String = ""

            @SerializedName("reportingHierachyCode")
            @Expose
            var reportingHierachyCode: String = ""

            @SerializedName("mobileAppVersionInUse")
            @Expose
            var mobileAppVersionInUse: String = ""

            @SerializedName("hierFixedType")
            @Expose
            var hierFixedType: String = ""

            @SerializedName("isFieldWorkingUser")
            @Expose
            var isFieldWorkingUser: Boolean = false

            @SerializedName("isAccessBlocked")
            @Expose
            var isAccessBlocked: Boolean = false

            @SerializedName("mPin")
            @Expose
            private var mPin: String = ""

            @SerializedName("isMpin")
            @Expose
            var isMpin: Boolean = false

            @SerializedName("fingerprint")
            @Expose
            var fingerprint: String = ""

            @SerializedName("fieldStaffId")
            @Expose
            var fieldStaffId: Int = 0

            @SerializedName("baseHier")
            @Expose
            var baseHier: Int = 0

            @SerializedName("fsCode")
            @Expose
            var fsCode: String = ""

            @SerializedName("isEmpLink")
            @Expose
            var isEmpLink: Boolean = false

            @SerializedName("zoneId")
            @Expose
            var zoneId: Int = 0

            @SerializedName("zoneName")
            @Expose
            var zoneName: String = ""

            @SerializedName("regionId")
            @Expose
            var regionId: Int = 0

            @SerializedName("regionName")
            @Expose
            var regionName: String = ""

            @SerializedName("deactivateDate")
            @Expose
            var deactivateDate: String = ""

            @SerializedName("level")
            @Expose
            var level: Int = 0

            @SerializedName("reportingFSId")
            @Expose
            var reportingFSId: Int = 0

            @SerializedName("lastLoginDate")
            @Expose
            var lastLoginDate: String = ""

            @SerializedName("strLastLoginDate")
            @Expose
            var strLastLoginDate: String = ""

            @SerializedName("fsCodeWithEmpName")
            @Expose
            var fsCodeWithEmpName: String = ""

            @SerializedName("isTPDraftExist")
            @Expose
            var isTPDraftExist: Boolean = false

            @SerializedName("rtpId")
            @Expose
            var rtpId: Int = 0

            @SerializedName("strDeactivateDate")
            @Expose
            var strDeactivateDate: String = ""

            @SerializedName("isPromoted")
            @Expose
            var isPromoted: Boolean = false

            @SerializedName("preHierachyId")
            @Expose
            var preHierachyId: Int = 0

            @SerializedName("transactionDate")
            @Expose
            var transactionDate: String = ""

            @SerializedName("isActive")
            @Expose
            private var isActive: Boolean = false

            @SerializedName("isDocDCRV2")
            @Expose
            var isDocDCRV2: Boolean = false

            @SerializedName("otp")
            @Expose
            var otp: String = ""

            @SerializedName("isGeoFencingApplicable")
            @Expose
            var isGeoFencingApplicable: Boolean = false

            @SerializedName("allowLocationUpdate")
            @Expose
            var allowLocationUpdate: Boolean = false

            @SerializedName("allowDoctorEdit")
            @Expose
            var allowDoctorEdit: Boolean = false

            @SerializedName("lastWorkingDate")
            @Expose
            var lastWorkingDate: String = ""

            @SerializedName("uanNo")
            @Expose
            var uanNo: String = ""

            @SerializedName("panNo")
            @Expose
            var panNo: String = ""

            @SerializedName("aadharNo")
            @Expose
            var aadharNo: String = ""

            @SerializedName("bankName")
            @Expose
            var bankName: String = ""

            @SerializedName("bankAccountNo")
            @Expose
            var bankAccountNo: String = ""

            @SerializedName("lastPasswordChangedDate")
            @Expose
            var lastPasswordChangedDate: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("weekOff")
            @Expose
            var weekOff: String = ""

        }

    }


}