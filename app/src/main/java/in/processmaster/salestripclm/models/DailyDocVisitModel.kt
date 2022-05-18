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

        @SerializedName("message")
        @Expose
        var message: String? = null


        @SerializedName("dcrDoctorlist")
        @Expose
        var dcrDoctorlist: List<DcrDoctor>? = ArrayList()


        @SerializedName("dcrRetailerlist")
        @Expose
        var dcrRetailerlist: List<DcrDoctor>? = ArrayList()

        @SerializedName("retailerStockistDCRList")
        @Expose
        var retailerStockistDCRList: List<DcrDoctor>? = ArrayList()

        class DcrDoctor {
            @SerializedName("isOffline")
            @Expose
            var isOffline: Boolean? = false

            @SerializedName("shopName")
            @Expose
            var shopName: String? = null

            @SerializedName("dataSaveType")
            @Expose
            var dataSaveType: String? = null

            @SerializedName("dcrId")
            @Expose
            var dcrId: Int? = null

            @SerializedName("routeId")
            @Expose
            var routeId: String? = null

            @SerializedName("doctorId")
            @Expose
            var doctorId: Int? = null

         /*   @SerializedName("isVisited")
            @Expose
            var isVisited: Boolean? = null*/

            @SerializedName("detailType")
            @Expose
            var detailType: String? = null

            @SerializedName("retailerId")
            @Expose
            var retailerId: Int? = null

            @SerializedName("isRCPAFilled")
            @Expose
            var isRCPAFilled: Boolean? = null

         /*   @SerializedName("isDelete")
            @Expose
            var isDelete: Boolean? = false

            @SerializedName("isNextFollowUp")
            @Expose
            var isNextFollowUp: Boolean? = false*/

            @SerializedName("remark")
            @Expose
            var remark: String? =null

           /* @SerializedName("subject")
            @Expose
            var subject: String? = ""*/

            @SerializedName("dateAndTime")
            @Expose
            var dateAndTime: String? = null

            @SerializedName("followUpRemark")
            @Expose
            var followUpRemark: String? = null

            @SerializedName("workWith")
            @Expose
            var workWith: String? = ""

        /*    @SerializedName("metAtTime")
            @Expose
            var metAtTime: String? = ""*/

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

        /*    @SerializedName("isDocumentAdd")
            @Expose
            var isDocumentAdd: Boolean? = false*/

            @SerializedName("docName")
            @Expose
            var docName: String? = null

            @SerializedName("productList")
            @Expose
            var productList: ArrayList<ProductList>? = null

            @SerializedName("giftList")
            @Expose
            var giftList: ArrayList<GiftList>? = null

            @SerializedName("sampleList")
            @Expose
            var sampleList: ArrayList<SampleList>? =null

            @SerializedName("eDetailList")
            @Expose
            var eDetailList: ArrayList<VisualAdsModel_Send>? = null

            @SerializedName("rcpaList")
            @Expose
            var RCPAList: ArrayList<Rcpavo>? = null


            @SerializedName("doctorName")
            @Expose
            var doctorName: String? = null

       /*     @SerializedName("qualificationName")
            @Expose
            var qualificationName: String? = ""

            @SerializedName("cityName")
            @Expose
            var cityName: String? = ""*/

            @SerializedName("routeName")
            @Expose
            var routeName: String? = null

            @SerializedName("headQuaterName")
            @Expose
            var headQuaterName: String? = null

            @SerializedName("strDateAndTime")
            @Expose
            var strDateAndTime: String? = null
/*
            @SerializedName("visitFrequency")
            @Expose
            var visitFrequency: Int? = 0*/

            @SerializedName("visitPurpose")
            @Expose
            var visitPurpose: Int? = null

            @SerializedName("mode")
            @Expose
            var mode: Int? = null

            @SerializedName("specialityName")
            @Expose
            var specialityName: String? = null

       /*     @SerializedName("docExt")
            @Expose
            var docExt: String? = ""

            @SerializedName("docPath")
            @Expose
            var docPath: String? = ""

            @SerializedName("urlPath")
            @Expose
            var urlPath: String? = ""*/

        /*    @SerializedName("imageUrl")
            @Expose
            var imageUrl: String? = ""*/

            @SerializedName("workWithName")
            @Expose
            var workWithName: String? = null

            @SerializedName("visitPurposeName")
            @Expose
            var visitPurposeName: String? = null

/*            @SerializedName("metAtTimeDisplay")
            @Expose
            var metAtTimeDisplay: String? = ""*/

      /*      @SerializedName("isEDetailing")
            @Expose
            var isEDetailing: Boolean? = null*/

            @SerializedName("productDetailCount")
            @Expose
            var productDetailCount: Int? = null

            @SerializedName("totalSampleValue")
            @Expose
            var totalSampleValue: Double? = null

        /*    @SerializedName("totalAchValue")
            @Expose
            var totalAchValue: Double? = 0.0*/

            @SerializedName("giftGivenCount")
            @Expose
            var giftGivenCount: Int? = null

            @SerializedName("totalGiftValue")
            @Expose
            var totalGiftValue: Double? = null

            @SerializedName("totalPrescriptionCount")
            @Expose
            var totalPrescriptionCount: Int? = null

        /*    @SerializedName("freeMedicineCamp")
            @Expose
            var freeMedicineCamp: Boolean? = false*/

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
            var dcrDetailId: Int? =null

          /*  @SerializedName("fiscalYear")
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
*/
            @SerializedName("callTiming")
            @Expose
            var callTiming: String? = null


            @SerializedName("callTimingName")
            @Expose
            var callTimingName: String? = null

            @SerializedName("partyLatitude")
            @Expose
            var partyLatitude: Double? = null

            @SerializedName("partyLongitude")
            @Expose
            var partyLongitude: Double? = null

            @SerializedName("partyDistance")
            @Expose
            var partyDistance: Int? =null


           /*

            @SerializedName("routeType")
            @Expose
            var routeType: Any? = null

            @SerializedName("categoryName")
            @Expose
            var categoryName: String? = ""

            @SerializedName("callMediumType")
            @Expose
            var callMediumType: Int? = 0*/

            @SerializedName("callMediumTypeName")
            @Expose
            var callMediumTypeName: String? = null

            @SerializedName("totalPOB")
            @Expose
            var totalPOB: Double? = null

           /* @SerializedName("isOnLocationReported")
            @Expose
            var isOnLocationReported: Boolean? = false

            @SerializedName("otherLocReportRemark")
            @Expose
            var otherLocReportRemark: String? = ""

            @SerializedName("hospitalId")
            @Expose
            var hospitalId: Int? = 0

            @SerializedName("hospitalName")
            @Expose
            var hospitalName: String? = ""

            @SerializedName("hospCode")
            @Expose
            var hospCode: String? = ""
*/
            @SerializedName("isFeedback")
            @Expose
            var isFeedback: String? = null

         /*   @SerializedName("eDetailing")
            @Expose
            private var eDetailing: String? = ""

            @SerializedName("grade")
            @Expose
            var grade: String? = ""

            @SerializedName("docGrade")
            @Expose
            var docGrade: String? = ""
*/
            @SerializedName("pobObject")
            @Expose

            var pobObject  : PobObj?   = null


            class PobObj{
                @SerializedName("pobId")            var pobId               : Int?                   = null
                @SerializedName("pobNo")            var pobNo               : String?                = null
                @SerializedName("pobDate")          var pobDate             : String?                = null
                @SerializedName("partyId")          var partyId             : Int?                   = null
                @SerializedName("employeeId")       var employeeId          : Int?                   = null
                @SerializedName("remark")           var remark              : String?                = null
                @SerializedName("empId")            var empId               : Int?                   = null
                @SerializedName("mode")             var mode                : Int?                   = null
              //  @SerializedName("pobType")          var pobType             : String?                = "DOCTOR"
              //  @SerializedName("address")          var address             : String?                = ""
              //  @SerializedName("partyName")        var partyName           : String?                = ""
              //  @SerializedName("employeeName")     var employeeName        : String?                = ""
                @SerializedName("headQuaterName")   var headQuaterName      : String?                = null
                @SerializedName("routeName")        var routeName           : String?                = null
              // @SerializedName("strPOBDate")       var strPOBDate          : String?                = ""
              // @SerializedName("isProductWisePOB") var isProductWisePOB    : Boolean?               = true
              // @SerializedName("totalPOB")         var totalPOB            : Double?                = null
              // @SerializedName("cityName")         var cityName            : String?                = ""
                @SerializedName("stockistId")       var stockistId          : Int?                   = null
               // @SerializedName("stockistName")     var stockistName        : String?                = ""
                @SerializedName("status")           var status              : String?                = null
              //  @SerializedName("docName")          var docName             : String?                = ""
              //  @SerializedName("hospitalName")     var hospitalName        : String?                = ""
              //  @SerializedName("hospitalId")       var hospitalId          : Int?                   = 0
              //  @SerializedName("dcrId")            var dcrId               : Int?                   = null
                @SerializedName("pobDetailList")    var pobDetailList       : ArrayList<PobDetailList>? = null

                class PobDetailList()
                {
                  /*  @SerializedName("srNo")
                    @Expose
                     var srNo: Int? = null*/

                    @SerializedName("productId")
                    @Expose
                     var productId: Int? = null

                    @SerializedName("rate")
                    @Expose
                     var rate: Double? = null

                    @SerializedName("qty")
                    @Expose
                     var qty: Int? = null

                    @SerializedName("amount")
                    @Expose
                     var amount: Double? = null

                    @SerializedName("pobId")
                    @Expose
                    var pobId: Int? = null

             /*

                    @SerializedName("packingTypeName")
                    @Expose
                     var packingTypeName: String? = null

                    @SerializedName("category")
                    @Expose
                     var category: String? = null*/

                    @SerializedName("productName")
                    @Expose
                     var productName: String? = null

                    @SerializedName("pobNo")
                    @Expose
                    var pobNo: Any? = null

                /*

                    @SerializedName("pobType")
                    @Expose
                     var pobType: Any? = null

                    @SerializedName("partyName")
                    @Expose
                     var partyName: Any? = null

                    @SerializedName("totalPOB")
                    @Expose
                     var totalPOB: Double? = null

                    @SerializedName("isProductWisePOB")
                    @Expose
                     var isProductWisePOB: Boolean? = null*/

                    @SerializedName("schemeId")
                    @Expose
                     var schemeId: Int? = null

                    @SerializedName("freeQty")
                    @Expose
                     var freeQty: Int? = null

                    @SerializedName("totalQty")
                    @Expose
                     var totalQty: Int? = null

                  /*  @SerializedName("schemeName")
                    @Expose
                     var schemeName: String? = null*/

                    @SerializedName("schemeSalesQty")
                    @Expose
                     var schemeSalesQty: Int? = null

                    @SerializedName("schemeFreeQty")
                    @Expose
                     var schemeFreeQty: Int? = null

                   /* @SerializedName("isSchemeExists")
                    @Expose
                     var isSchemeExists: Boolean? = null*/

                    @SerializedName("schemeNameWithQty")
                    @Expose
                     var schemeNameWithQty: String? = null

                    @SerializedName("isSaved")
                    @Expose
                    var isSaved=false;
                  //  var freeQtyMain: Int?= 0
                  //  var salesQtyMain: Int?= 0
                }
            }


            class EdetailingList{
                @SerializedName("brandId")
                @Expose
                private var brandId: Int? = null

                @SerializedName("doctorId")
                @Expose
                private var doctorId: Int? = null

                @SerializedName("empId")
                @Expose
                private var empId: Int? = null

                @SerializedName("duration")
                @Expose
                private var duration: Int? = null

                @SerializedName("startDate")
                @Expose
                private var startDate: String? = null

                @SerializedName("endDate")
                @Expose
                private var endDate: String? = null

                @SerializedName("transactionDate")
                @Expose
                private var transactionDate: String? = null

                @SerializedName("doctorName")
                @Expose
                private var doctorName: String? = null

                @SerializedName("qualificationName")
                @Expose
                private var qualificationName: String? = null

                @SerializedName("cityName")
                @Expose
                private var cityName: String? = null

                @SerializedName("routeName")
                @Expose
                private var routeName: String? = null

                @SerializedName("hqName")
                @Expose
                private var hqName: String? = null

                @SerializedName("specialityName")
                @Expose
                private var specialityName: String? = null

                @SerializedName("visitFrequency")
                @Expose
                private var visitFrequency: String? = null

                @SerializedName("brandName")
                @Expose
                private var brandName: String? = null

                @SerializedName("divisionName")
                @Expose
                private var divisionName: String? = null

                @SerializedName("fileTransList")
                @Expose
                private var fileTransList: List<Any?>? = null

                @SerializedName("transactionId")
                @Expose
                private var transactionId: Int? = null

                @SerializedName("feedback")
                @Expose
                private var feedback: String? = null

                @SerializedName("rating")
                @Expose
                private var rating: Double? = null

                fun getBrandId(): Int? {
                    return brandId
                }

                fun setBrandId(brandId: Int?) {
                    this.brandId = brandId
                }

                fun getDoctorId(): Int? {
                    return doctorId
                }

                fun setDoctorId(doctorId: Int?) {
                    this.doctorId = doctorId
                }

                fun getEmpId(): Int? {
                    return empId
                }

                fun setEmpId(empId: Int?) {
                    this.empId = empId
                }

                fun getDuration(): Int? {
                    return duration
                }

                fun setDuration(duration: Int?) {
                    this.duration = duration
                }

                fun getStartDate(): String? {
                    return startDate
                }

                fun setStartDate(startDate: String?) {
                    this.startDate = startDate
                }

                fun getEndDate(): String? {
                    return endDate
                }

                fun setEndDate(endDate: String?) {
                    this.endDate = endDate
                }

                fun getTransactionDate(): String? {
                    return transactionDate
                }

                fun setTransactionDate(transactionDate: String?) {
                    this.transactionDate = transactionDate
                }

                fun getDoctorName(): String? {
                    return doctorName
                }

                fun setDoctorName(doctorName: String?) {
                    this.doctorName = doctorName
                }

                fun getQualificationName(): String? {
                    return qualificationName
                }

                fun setQualificationName(qualificationName: String?) {
                    this.qualificationName = qualificationName
                }

                fun getCityName(): String? {
                    return cityName
                }

                fun setCityName(cityName: String?) {
                    this.cityName = cityName
                }

                fun getRouteName(): String? {
                    return routeName
                }

                fun setRouteName(routeName: String?) {
                    this.routeName = routeName
                }

                fun getHqName(): String? {
                    return hqName
                }

                fun setHqName(hqName: String?) {
                    this.hqName = hqName
                }

                fun getSpecialityName(): String? {
                    return specialityName
                }

                fun setSpecialityName(specialityName: String?) {
                    this.specialityName = specialityName
                }

                fun getVisitFrequency(): String? {
                    return visitFrequency
                }

                fun setVisitFrequency(visitFrequency: String?) {
                    this.visitFrequency = visitFrequency
                }

                fun getBrandName(): String? {
                    return brandName
                }

                fun setBrandName(brandName: String?) {
                    this.brandName = brandName
                }

                fun getDivisionName(): String? {
                    return divisionName
                }

                fun setDivisionName(divisionName: String?) {
                    this.divisionName = divisionName
                }

                fun getFileTransList(): List<Any?>? {
                    return fileTransList
                }

                fun setFileTransList(fileTransList: List<Any?>?) {
                    this.fileTransList = fileTransList
                }

                fun getTransactionId(): Int? {
                    return transactionId
                }

                fun setTransactionId(transactionId: Int?) {
                    this.transactionId = transactionId
                }

                fun getFeedback(): String? {
                    return feedback
                }

                fun setFeedback(feedback: String?) {
                    this.feedback = feedback
                }

                fun getRating(): Double? {
                    return rating
                }

                fun setRating(rating: Double?) {
                    this.rating = rating
                }
            }

            class GiftList {
                @SerializedName("dcrId"             ) var dcrId             : Int?    = null
                @SerializedName("detailId"          ) var detailId          : Int?    = null
                @SerializedName("productId"         ) var productId         : Int?    = null
                @SerializedName("qty"               ) var qty               : Int?    = null
                @SerializedName("prescriptionCount" ) var prescriptionCount : Int?    = null
                @SerializedName("type"              ) var type              : String? = null
                @SerializedName("empId"             ) var empId             : Int?    = null
            }

            class SampleList {
                @SerializedName("dcrId"     ) var dcrId     : Int? = null
                @SerializedName("productId" ) var productId : Int? = null
                @SerializedName("qty"       ) var qty       : Int? = null
                @SerializedName("empId"     ) var empId     : Int? = null
            }

            class ProductList {

                @SerializedName("dcrId")
                var dcrId: Int? = null
                @SerializedName("doctorId")
                var doctorId: Int? = null
                @SerializedName("productId")
                var productId: Int? = null
                @SerializedName("remark")
                var remark: String? = null
            }

            class Rcpavo {
                @SerializedName("dcrId")
                var dcrId: Int? = null

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

                @SerializedName("strRcpaDate")
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
                var rCPADetailList: ArrayList<RCPADetail>? = null


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

                    @SerializedName("cP1")
                    @Expose
                    var cp1: String? = null

                    @SerializedName("cpRx1")
                    @Expose
                    var cPRx1: Int? = null

                    @SerializedName("cP2")
                    @Expose
                    var cp2: String? = null

                    @SerializedName("cpRx2")
                    @Expose
                    var cPRx2: Int? = null

                    @SerializedName("cP3")
                    @Expose
                    var cp3: String? = null

                    @SerializedName("cpRx3")
                    @Expose
                    var cPRx3: Int? = null

                    @SerializedName("cP4")
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
    }
}