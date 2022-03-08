package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DailyDocVisitModel {

    @SerializedName("responseCode")
    @Expose
    private var responseCode: Int?? = null

    @SerializedName("errorObj")
    @Expose
    private var errorObj: SyncModel.ErrorObj = SyncModel.ErrorObj()

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getResponseCode(): Int?? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int??) {
        this.responseCode = responseCode
    }

    fun getErrorObj(): SyncModel.ErrorObj? {
        return errorObj
    }

    fun setErrorObj(errorObj: SyncModel.ErrorObj?) {
        this.errorObj = errorObj!!
    }

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class Data {
        @SerializedName("dcrDoctorlist")
        @Expose
        var dcrDoctorlist: List<DcrDoctor>? = ArrayList()

        class DcrDoctor {
            @SerializedName("dcrId")
            @Expose
            var dcrId: Int? = 0

            @SerializedName("doctorId")
            @Expose
            var doctorId: Int? = 0

            @SerializedName("isVisited")
            @Expose
            var isVisited: Boolean? = false

            @SerializedName("detailType")
            @Expose
            var detailType: String? = ""

            @SerializedName("isDelete")
            @Expose
            var isDelete: Boolean? = false

            @SerializedName("isNextFollowUp")
            @Expose
            var isNextFollowUp: Boolean? = false

            @SerializedName("remark")
            @Expose
            var remark: String? = ""

            @SerializedName("subject")
            @Expose
            var subject: String? = ""

            @SerializedName("dateAndTime")
            @Expose
            var dateAndTime: String? = ""

            @SerializedName("followUpRemark")
            @Expose
            var followUpRemark: String? = ""

            @SerializedName("workWith")
            @Expose
            var workWith: String? = ""

            @SerializedName("metAtTime")
            @Expose
            var metAtTime: String? = ""

            @SerializedName("latitude")
            @Expose
            var latitude: Double? = 0.0

            @SerializedName("longitude")
            @Expose
            var longitude: Double? = 0.0

            @SerializedName("reportedTime")
            @Expose
            var reportedTime: String? = ""

            @SerializedName("strReportedTime")
            @Expose
            var strReportedTime: String? = ""

            @SerializedName("addedThrough")
            @Expose
            var addedThrough: String? = ""

            @SerializedName("isDocumentAdd")
            @Expose
            var isDocumentAdd: Boolean? = false

            @SerializedName("docName")
            @Expose
            var docName: String? = ""

            @SerializedName("productList")
            @Expose
            var productList: ArrayList<Any>? = ArrayList()

            @SerializedName("giftList")
            @Expose
            var giftList: ArrayList<Any>? = ArrayList()

            @SerializedName("sampleList")
            @Expose
            var sampleList: ArrayList<Any>? = ArrayList()

            @SerializedName("eDetailList")
            @Expose
            private var eDetailList: ArrayList<Any> = ArrayList()

            @SerializedName("doctorName")
            @Expose
            var doctorName: String? = ""

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String? = ""

            @SerializedName("cityName")
            @Expose
            var cityName: String? = ""

            @SerializedName("routeName")
            @Expose
            var routeName: String? = ""

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String? = ""

            @SerializedName("strDateAndTime")
            @Expose
            var strDateAndTime: String? = ""

            @SerializedName("visitFrequency")
            @Expose
            var visitFrequency: Int? = 0

            @SerializedName("visitPurpose")
            @Expose
            var visitPurpose: Int? = 0

            @SerializedName("mode")
            @Expose
            var mode: Int? = 0

            @SerializedName("specialityName")
            @Expose
            var specialityName: String? = ""

            @SerializedName("docExt")
            @Expose
            var docExt: String? = ""

            @SerializedName("docPath")
            @Expose
            var docPath: String? = ""

            @SerializedName("urlPath")
            @Expose
            var urlPath: String? = ""

            @SerializedName("imageUrl")
            @Expose
            var imageUrl: String? = ""

            @SerializedName("workWithName")
            @Expose
            var workWithName: String? = ""

            @SerializedName("visitPurposeName")
            @Expose
            var visitPurposeName: String? = ""

            @SerializedName("metAtTimeDisplay")
            @Expose
            var metAtTimeDisplay: String? = ""

            @SerializedName("isEDetailing")
            @Expose
            var isEDetailing: Boolean? = false

            @SerializedName("productDetailCount")
            @Expose
            var productDetailCount: Int? = 0

            @SerializedName("totalSampleValue")
            @Expose
            var totalSampleValue: Double? = 0.0

            @SerializedName("totalAchValue")
            @Expose
            var totalAchValue: Double? = 0.0

            @SerializedName("giftGivenCount")
            @Expose
            var giftGivenCount: Int? = 0

            @SerializedName("totalGiftValue")
            @Expose
            var totalGiftValue: Double? = 0.0

            @SerializedName("totalPrescriptionCount")
            @Expose
            var totalPrescriptionCount: Int? = 0

            @SerializedName("freeMedicineCamp")
            @Expose
            var freeMedicineCamp: Boolean? = false

            @SerializedName("dcrDate")
            @Expose
            var dcrDate: String? = ""

            @SerializedName("dcrMonth")
            @Expose
            var dcrMonth: Int? = 0

            @SerializedName("dcrYear")
            @Expose
            var dcrYear: Int? = 0

            @SerializedName("empId")
            @Expose
            var empId: Int? = 0

            @SerializedName("dcrDetailId")
            @Expose
            var dcrDetailId: Int? = 0

            @SerializedName("fiscalYear")
            @Expose
            var fiscalYear: Int? = 0

            @SerializedName("doctorType")
            @Expose
            var doctorType: Int? = 0

            @SerializedName("workingWithAM")
            @Expose
            var workingWithAM: Boolean? = false

            @SerializedName("workingWithRM")
            @Expose
            var workingWithRM: Boolean? = false

            @SerializedName("workingWithZM")
            @Expose
            var workingWithZM: Boolean? = false

            @SerializedName("categoryId")
            @Expose
            var categoryId: Int? = 0

            @SerializedName("callTiming")
            @Expose
            var callTiming: String? = ""

            @SerializedName("callTimingName")
            @Expose
            var callTimingName: String? = ""

            @SerializedName("routeType")
            @Expose
            var routeType: Any? = null

            @SerializedName("categoryName")
            @Expose
            var categoryName: String? = ""

            @SerializedName("callMediumType")
            @Expose
            var callMediumType: Int? = 0

            @SerializedName("callMediumTypeName")
            @Expose
            var callMediumTypeName: String? = ""

            @SerializedName("totalPOB")
            @Expose
            var totalPOB: Double? = 0.0

            @SerializedName("isOnLocationReported")
            @Expose
            var isOnLocationReported: Boolean? = false

            @SerializedName("otherLocReportRemark")
            @Expose
            var otherLocReportRemark: String? = ""

            @SerializedName("partyLatitude")
            @Expose
            var partyLatitude: Double? = 0.0

            @SerializedName("partyLongitude")
            @Expose
            var partyLongitude: Double? = 0.0

            @SerializedName("partyDistance")
            @Expose
            var partyDistance: Int? = 0

            @SerializedName("hospitalId")
            @Expose
            var hospitalId: Int? = 0

            @SerializedName("hospitalName")
            @Expose
            var hospitalName: String? = ""

            @SerializedName("hospCode")
            @Expose
            var hospCode: String? = ""

            @SerializedName("isFeedback")
            @Expose
            var isFeedback: String? = ""

            @SerializedName("eDetailing")
            @Expose
            private var eDetailing: String? = ""

            @SerializedName("grade")
            @Expose
            var grade: String? = ""

            @SerializedName("docGrade")
            @Expose
            var docGrade: String? = ""


        }
    }
}