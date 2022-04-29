package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RetailerPobModel
{
    @SerializedName("DCRId")
    @Expose
     var dCRId: Int? = null

    @SerializedName("RetailerId")
    @Expose
     var retailerId: Int? = null

    @SerializedName("DetailType")
    @Expose
     var detailType: String? = null

    @SerializedName("ShopName")
    @Expose
     var shopName: String? = null

    @SerializedName("ContactPerson")
    @Expose
     var contactPerson: String? = null

    @SerializedName("HeadQuaterName")
    @Expose
     var headQuaterName: String? = null

    @SerializedName("WorkWith")
    @Expose
     var workWith: String? = null

    @SerializedName("MetAtTime")
    @Expose
     var metAtTime: String? = null

    @SerializedName("Mode")
    @Expose
     var mode: Int? = null

    @SerializedName("VisitPurpose")
    @Expose
     var visitPurpose: Int? = null

    @SerializedName("RouteName")
    @Expose
     var routeName: String? = null

    @SerializedName("Latitude")
    @Expose
     var latitude: Double? = null

    @SerializedName("Longitude")
    @Expose
     var longitude: Double? = null

    @SerializedName("ReportedTime")
    @Expose
     var reportedTime: String? = null

    @SerializedName("AddedThrough")
    @Expose
     var addedThrough: String? = null

    @SerializedName("RouteId")
    @Expose
     var routeId: String? = null

    @SerializedName("TransportMode")
    @Expose
     var transportMode: Int? = null

    @SerializedName("IndustryType")
    @Expose
     var industryType: String? = null

    @SerializedName("StrReportedTime")
    @Expose
     var strReportedTime: String? = null

    @SerializedName("Remark")
    @Expose
     var remark: String? = null

    @SerializedName("IsDocumentAdd")
    @Expose
     var isDocumentAdd: Boolean? = null

    @SerializedName("UrlPath")
    @Expose
     var urlPath: String? = null

    @SerializedName("DocPath")
    @Expose
     var docPath: String? = null

    @SerializedName("DocName")
    @Expose
     var docName: String? = null

    @SerializedName("DocExt")
    @Expose
     var docExt: String? = null

    @SerializedName("ImageUrl")
    @Expose
     var imageUrl: String? = null

    @SerializedName("WorkWithName")
    @Expose
     var workWithName: String? = null

    @SerializedName("VisitPurposeName")
    @Expose
     var visitPurposeName: String? = null

    @SerializedName("DCRDate")
    @Expose
     var dCRDate: String? = null

    @SerializedName("DCRMonth")
    @Expose
     var dCRMonth: Int? = null

    @SerializedName("DCRYear")
    @Expose
     var dCRYear: Int? = null

    @SerializedName("EmpId")
    @Expose
     var empId: Int? = null

    @SerializedName("FiscalYear")
    @Expose
     var fiscalYear: Int? = null

    @SerializedName("CallTiming")
    @Expose
     var callTiming: String? = null

    @SerializedName("CallTimingName")
    @Expose
     var callTimingName: String? = null

    @SerializedName("RouteType")
    @Expose
     var routeType: String? = null

    @SerializedName("TotalPOB")
    @Expose
     var totalPOB: Int? = null

    @SerializedName("IsOnLocationReported")
    @Expose
     var isOnLocationReported: Boolean? = null

    @SerializedName("OtherLocReportRemark")
    @Expose
     var otherLocReportRemark: String? = null

    @SerializedName("PartyLatitude")
    @Expose
     var partyLatitude: Double? = null

    @SerializedName("PartyLongitude")
    @Expose
     var partyLongitude: Double? = null

    @SerializedName("PartyDistance")
    @Expose
     var partyDistance: Int? = null

    @SerializedName("IsRCPAFilled")
    @Expose
     var isRCPAFilled: Boolean? = null

    @SerializedName("HeadQuarterId")
    @Expose
     var headQuarterId: Int? = null

    @SerializedName("IsOffline")
    @Expose
     var isOffline: Boolean? = null

    @SerializedName("RCPAList")
    @Expose
     var RCPAList: ArrayList<Rcpavo>? = null

    @SerializedName("GiftList ")
    @Expose
    var GiftList : ArrayList<DailyDocVisitModel.Data.DcrDoctor.GiftList>? = null

    @SerializedName("POBObject")
    @Expose
    var  POBObject: DailyDocVisitModel.Data.DcrDoctor.PobObj?= null



    class Rcpavo {
        @SerializedName("RCPAId")
        @Expose
        var rCPAId: Int? = null

        @SerializedName("DocName")
        @Expose
        var docName: String? = null

        @SerializedName("DocId")
        @Expose
        var docId: Int? = null

        @SerializedName("EmpId")
        @Expose
        var empId: Int? = null

        @SerializedName("RetailerId")
        @Expose
        var retailerId: Int? = null

        @SerializedName("RetailerName")
        @Expose
        var retailerName: String? = null

        @SerializedName("BrandName")
        @Expose
        var brandName: String? = null

        @SerializedName("RCPADate")
        @Expose
        var rCPADate: String? = null

        @SerializedName("strRCPADate")
        @Expose
        var strRCPADate: String? = null

        @SerializedName("EntryBy")
        @Expose
        var entryBy: String? = null

        @SerializedName("UpdateBy")
        @Expose
        var updateBy: String? = null

        @SerializedName("UpdateDate")
        @Expose
        var updateDate: String? = null

        @SerializedName("Mode")
        @Expose
        var mode: Int? = null

        @SerializedName("RCPADetailList")
        @Expose
        var rCPADetailList: List<RCPADetail>? = null


        class RCPADetail {
            @SerializedName("BrandId")
            @Expose
            var brandId: Int? = null

            @SerializedName("BrandName")
            @Expose
            var brandName: String? = null

            @SerializedName("BrandUnits")
            @Expose
            var brandUnits: Int? = null

            @SerializedName("RCPADetailId")
            @Expose
            var rCPADetailId: String? = null

            @SerializedName("BrandValue")
            @Expose
            var brandValue: String? = null

            @SerializedName("CP1")
            @Expose
            var cp1: String? = null

            @SerializedName("CPRx1")
            @Expose
            var cPRx1: Int? = null

            @SerializedName("CP2")
            @Expose
            var cp2: String? = null

            @SerializedName("CPRx2")
            @Expose
            var cPRx2: Int? = null

            @SerializedName("CP3")
            @Expose
            var cp3: String? = null

            @SerializedName("CPRx3")
            @Expose
            var cPRx3: Int? = null

            @SerializedName("CP4")
            @Expose
            var cp4: String? = null

            @SerializedName("CPRx4")
            @Expose
            var cPRx4: Int? = null

            @SerializedName("RCPANo")
            @Expose
            var rCPANo: Int? = null

            @SerializedName("DocName")
            @Expose
            var docName: String? = null

            @SerializedName("RetailerName")
            @Expose
            var retailerName: String? = null

            @SerializedName("DocId")
            @Expose
            var docId: Int? = null

            @SerializedName("RetailerId")
            @Expose
            var retailerId: Int? = null

            @SerializedName("DCRId")
            @Expose
            var dCRId: Int? = null

            @SerializedName("LastRCPADate")
            @Expose
            var lastRCPADate: String? = null
        }

    }

}