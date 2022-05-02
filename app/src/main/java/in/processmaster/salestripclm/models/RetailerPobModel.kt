package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RetailerPobModel
{
    @SerializedName("dcrId")
    @Expose
     var dCRId: Int? = null

    @SerializedName("retailerId")
    @Expose
     var retailerId: Int? = null

    @SerializedName("detailType")
    @Expose
     var detailType: String? = null

    @SerializedName("shopName")
    @Expose
     var shopName: String? = null

    @SerializedName("contactPerson")
    @Expose
     var contactPerson: String? = null

    @SerializedName("headQuaterName")
    @Expose
     var headQuaterName: String? = null

    @SerializedName("workWith")
    @Expose
     var workWith: String? = null

    @SerializedName("metAtTime")
    @Expose
     var metAtTime: String? = null

    @SerializedName("mode")
    @Expose
     var mode: Int? = null

    @SerializedName("visitPurpose")
    @Expose
     var visitPurpose: Int? = null

    @SerializedName("routeName")
    @Expose
     var routeName: String? = null

    @SerializedName("latitude")
    @Expose
     var latitude: Double? = null

    @SerializedName("longitude")
    @Expose
     var longitude: Double? = null

    @SerializedName("reportedTime")
    @Expose
     var reportedTime: String? = null

    @SerializedName("addedThrough")
    @Expose
     var addedThrough: String? = null

    @SerializedName("routeId")
    @Expose
     var routeId: String? = null

    @SerializedName("transportMode")
    @Expose
     var transportMode: Int? = null

    @SerializedName("industryType")
    @Expose
     var industryType: String? = null

    @SerializedName("strReportedTime")
    @Expose
     var strReportedTime: String? = null

    @SerializedName("remark")
    @Expose
     var remark: String? = null

    @SerializedName("isDocumentAdd")
    @Expose
     var isDocumentAdd: Boolean? = null

    @SerializedName("urlPath")
    @Expose
     var urlPath: String? = null

    @SerializedName("docPath")
    @Expose
     var docPath: String? = null

    @SerializedName("docName")
    @Expose
     var docName: String? = null

    @SerializedName("docExt")
    @Expose
     var docExt: String? = null

    @SerializedName("imageUrl")
    @Expose
     var imageUrl: String? = null

    @SerializedName("workWithName")
    @Expose
     var workWithName: String? = null

    @SerializedName("visitPurposeName")
    @Expose
     var visitPurposeName: String? = null

    @SerializedName("dcrDate")
    @Expose
     var dCRDate: String? = null

    @SerializedName("dcrMonth")
    @Expose
     var dCRMonth: Int? = null

    @SerializedName("dcrYear")
    @Expose
     var dCRYear: Int? = null

    @SerializedName("empId")
    @Expose
     var empId: Int? = null

    @SerializedName("fiscalYear")
    @Expose
     var fiscalYear: Int? = null

    @SerializedName("callTiming")
    @Expose
     var callTiming: String? = null

    @SerializedName("callTimingName")
    @Expose
     var callTimingName: String? = null

    @SerializedName("routeType")
    @Expose
     var routeType: String? = null

    @SerializedName("totalPOB")
    @Expose
     var totalPOB: Int? = null

    @SerializedName("isOnLocationReported")
    @Expose
     var isOnLocationReported: Boolean? = null

    @SerializedName("otherLocReportRemark")
    @Expose
     var otherLocReportRemark: String? = null

    @SerializedName("partyLatitude")
    @Expose
     var partyLatitude: Double? = null

    @SerializedName("partyLongitude")
    @Expose
     var partyLongitude: Double? = null

    @SerializedName("partyDistance")
    @Expose
     var partyDistance: Int? = null

    @SerializedName("isRCPAFilled")
    @Expose
     var isRCPAFilled: Boolean? = null

    @SerializedName("headQuarterId")
    @Expose
     var headQuarterId: Int? = null

    @SerializedName("isOffline")
    @Expose
     var isOffline: Boolean? = null

    @SerializedName("rcpaList")
    @Expose
     var RCPAList: ArrayList<Rcpavo>? = null

    @SerializedName("giftList ")
    @Expose
    var GiftList : ArrayList<DailyDocVisitModel.Data.DcrDoctor.GiftList>? = null

    @SerializedName("pobObject")
    @Expose
    var  POBObject: DailyDocVisitModel.Data.DcrDoctor.PobObj?= null



    class Rcpavo {
        @SerializedName("rcpaId")
        @Expose
        var rCPAId: Int? = null

        @SerializedName("docName")
        @Expose
        var docName: String? = null

        @SerializedName("docId")
        @Expose
        var docId: Int? = null

        @SerializedName("empId")
        @Expose
        var empId: Int? = null

        @SerializedName("retailerId")
        @Expose
        var retailerId: Int? = null

        @SerializedName("retailerName")
        @Expose
        var retailerName: String? = null

        @SerializedName("brandName")
        @Expose
        var brandName: String? = null

        @SerializedName("rcpaDate")
        @Expose
        var rCPADate: String? = null

        @SerializedName("strRCPADate")
        @Expose
        var strRCPADate: String? = null

        @SerializedName("entryBy")
        @Expose
        var entryBy: String? = null

        @SerializedName("updateBy")
        @Expose
        var updateBy: String? = null

        @SerializedName("updateDate")
        @Expose
        var updateDate: String? = null

        @SerializedName("mode")
        @Expose
        var mode: Int? = null

        @SerializedName("rcpaDetailList")
        @Expose
        var rCPADetailList: List<RCPADetail>? = null


        class RCPADetail {
            @SerializedName("brandId")
            @Expose
            var brandId: Int? = null

            @SerializedName("brandName")
            @Expose
            var brandName: String? = null

            @SerializedName("brandUnits")
            @Expose
            var brandUnits: Int? = null

            @SerializedName("rcpaDetailId")
            @Expose
            var rCPADetailId: String? = null

            @SerializedName("brandValue")
            @Expose
            var brandValue: String? = null

            @SerializedName("cp1")
            @Expose
            var cp1: String? = null

            @SerializedName("cpRx1")
            @Expose
            var cPRx1: Int? = null

            @SerializedName("cp2")
            @Expose
            var cp2: String? = null

            @SerializedName("cpRx2")
            @Expose
            var cPRx2: Int? = null

            @SerializedName("cp3")
            @Expose
            var cp3: String? = null

            @SerializedName("cpRx3")
            @Expose
            var cPRx3: Int? = null

            @SerializedName("cp4")
            @Expose
            var cp4: String? = null

            @SerializedName("cpRx4")
            @Expose
            var cPRx4: Int? = null

            @SerializedName("rcpaNo")
            @Expose
            var rCPANo: Int? = null

            @SerializedName("docName")
            @Expose
            var docName: String? = null

            @SerializedName("retailerName")
            @Expose
            var retailerName: String? = null

            @SerializedName("docId")
            @Expose
            var docId: Int? = null

            @SerializedName("retailerId")
            @Expose
            var retailerId: Int? = null

            @SerializedName("dcrId")
            @Expose
            var dCRId: Int? = null

            @SerializedName("lastRCPADate")
            @Expose
            var lastRCPADate: String? = null
        }

    }

}