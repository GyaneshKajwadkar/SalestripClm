package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DailyDocVisitModel {

    @SerializedName("responseCode")
    @Expose
    private var responseCode: Int? = null

    @SerializedName("errorObj")
    @Expose
    private var errorObj: SyncModel.ErrorObj? = null

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
        this.responseCode = responseCode
    }

    fun getErrorObj(): SyncModel.ErrorObj? {
        return errorObj
    }

    fun setErrorObj(errorObj: SyncModel.ErrorObj?) {
        this.errorObj = errorObj
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
        var dcrDoctorlist: List<DcrDoctor>? = null

        class DcrDoctor {
            @SerializedName("dcrId")
            @Expose
            var dcrId: Int? = null

            @SerializedName("doctorId")
            @Expose
            var doctorId: Int? = null

            @SerializedName("isVisited")
            @Expose
            var isVisited: Boolean? = null

            @SerializedName("detailType")
            @Expose
            var detailType: String? = null

            @SerializedName("isDelete")
            @Expose
            var isDelete: Boolean? = null

            @SerializedName("isNextFollowUp")
            @Expose
            var isNextFollowUp: Boolean? = null

            @SerializedName("remark")
            @Expose
            var remark: String? = null

            @SerializedName("subject")
            @Expose
            var subject: String? = null

            @SerializedName("dateAndTime")
            @Expose
            var dateAndTime: String? = null

            @SerializedName("followUpRemark")
            @Expose
            var followUpRemark: Any? = null

            @SerializedName("workWith")
            @Expose
            var workWith: String? = null

            @SerializedName("metAtTime")
            @Expose
            var metAtTime: String? = null

            @SerializedName("latitude")
            @Expose
            var latitude: Double? = null

            @SerializedName("longitude")
            @Expose
            var longitude: Double? = null

            @SerializedName("reportedTime")
            @Expose
            var reportedTime: String? = null

            @SerializedName("strReportedTime")
            @Expose
            var strReportedTime: String? = null

            @SerializedName("addedThrough")
            @Expose
            var addedThrough: String? = null

            @SerializedName("isDocumentAdd")
            @Expose
            var isDocumentAdd: Boolean? = null

            @SerializedName("docName")
            @Expose
            var docName: Any? = null

            @SerializedName("productList")
            @Expose
            var productList: Any? = null

            @SerializedName("giftList")
            @Expose
            var giftList: Any? = null

            @SerializedName("sampleList")
            @Expose
            var sampleList: Any? = null

            @SerializedName("eDetailList")
            @Expose
            private var eDetailList: List<Any>? = null

            @SerializedName("doctorName")
            @Expose
            var doctorName: String? = null

            @SerializedName("qualificationName")
            @Expose
            var qualificationName: String? = null

            @SerializedName("cityName")
            @Expose
            var cityName: Any? = null

            @SerializedName("routeName")
            @Expose
            var routeName: String? = null

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: Any? = null

            @SerializedName("strDateAndTime")
            @Expose
            var strDateAndTime: Any? = null

            @SerializedName("visitFrequency")
            @Expose
            var visitFrequency: Int? = null

            @SerializedName("visitPurpose")
            @Expose
            var visitPurpose: Int? = null

            @SerializedName("mode")
            @Expose
            var mode: Int? = null

            @SerializedName("specialityName")
            @Expose
            var specialityName: String? = null

            @SerializedName("docExt")
            @Expose
            var docExt: Any? = null

            @SerializedName("docPath")
            @Expose
            var docPath: Any? = null

            @SerializedName("urlPath")
            @Expose
            var urlPath: Any? = null

            @SerializedName("imageUrl")
            @Expose
            var imageUrl: Any? = null

            @SerializedName("workWithName")
            @Expose
            var workWithName: String? = null

            @SerializedName("visitPurposeName")
            @Expose
            var visitPurposeName: String? = null

            @SerializedName("metAtTimeDisplay")
            @Expose
            var metAtTimeDisplay: String? = null

            @SerializedName("isEDetailing")
            @Expose
            var isEDetailing: Boolean? = null

            @SerializedName("productDetailCount")
            @Expose
            var productDetailCount: Int? = null

            @SerializedName("totalSampleValue")
            @Expose
            var totalSampleValue: Double? = null

            @SerializedName("totalAchValue")
            @Expose
            var totalAchValue: Double? = null

            @SerializedName("giftGivenCount")
            @Expose
            var giftGivenCount: Int? = null

            @SerializedName("totalGiftValue")
            @Expose
            var totalGiftValue: Double? = null

            @SerializedName("totalPrescriptionCount")
            @Expose
            var totalPrescriptionCount: Int? = null

            @SerializedName("freeMedicineCamp")
            @Expose
            var freeMedicineCamp: Boolean? = null

            @SerializedName("dcrDate")
            @Expose
            var dcrDate: String? = null

            @SerializedName("dcrMonth")
            @Expose
            var dcrMonth: Int? = null

            @SerializedName("dcrYear")
            @Expose
            var dcrYear: Int? = null

            @SerializedName("empId")
            @Expose
            var empId: Int? = null

            @SerializedName("dcrDetailId")
            @Expose
            var dcrDetailId: Int? = null

            @SerializedName("fiscalYear")
            @Expose
            var fiscalYear: Int? = null

            @SerializedName("doctorType")
            @Expose
            var doctorType: Int? = null

            @SerializedName("workingWithAM")
            @Expose
            var workingWithAM: Boolean? = null

            @SerializedName("workingWithRM")
            @Expose
            var workingWithRM: Boolean? = null

            @SerializedName("workingWithZM")
            @Expose
            var workingWithZM: Boolean? = null

            @SerializedName("categoryId")
            @Expose
            var categoryId: Int? = null

            @SerializedName("callTiming")
            @Expose
            var callTiming: String? = null

            @SerializedName("callTimingName")
            @Expose
            var callTimingName: String? = null

            @SerializedName("routeType")
            @Expose
            var routeType: Any? = null

            @SerializedName("categoryName")
            @Expose
            var categoryName: String? = null

            @SerializedName("callMediumType")
            @Expose
            var callMediumType: Int? = null

            @SerializedName("callMediumTypeName")
            @Expose
            var callMediumTypeName: String? = null

            @SerializedName("totalPOB")
            @Expose
            var totalPOB: Double? = null

            @SerializedName("isOnLocationReported")
            @Expose
            var isOnLocationReported: Boolean? = null

            @SerializedName("otherLocReportRemark")
            @Expose
            var otherLocReportRemark: Any? = null

            @SerializedName("partyLatitude")
            @Expose
            var partyLatitude: Double? = null

            @SerializedName("partyLongitude")
            @Expose
            var partyLongitude: Double? = null

            @SerializedName("partyDistance")
            @Expose
            var partyDistance: Int? = null

            @SerializedName("hospitalId")
            @Expose
            var hospitalId: Int? = null

            @SerializedName("hospitalName")
            @Expose
            var hospitalName: Any? = null

            @SerializedName("hospCode")
            @Expose
            var hospCode: Any? = null

            @SerializedName("isFeedback")
            @Expose
            var isFeedback: String? = null

            @SerializedName("eDetailing")
            @Expose
            private var eDetailing: String? = null

            @SerializedName("grade")
            @Expose
            var grade: Any? = null

            @SerializedName("docGrade")
            @Expose
            var docGrade: String? = null


        }
    }
}