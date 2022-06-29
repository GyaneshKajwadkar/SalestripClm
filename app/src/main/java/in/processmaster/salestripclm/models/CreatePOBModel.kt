package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CreatePOBModel {

    @SerializedName("responseCode")
    @Expose
    private var responseCode: Int?? = null

    @SerializedName("errorObj")
    @Expose
    private var errorObj: SyncModel.ErrorObj? = null

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
        this.errorObj = errorObj
    }

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class Data {
        @SerializedName("pobList")
        @Expose
        var pobList: List<pobObject> = ArrayList()

        @SerializedName("pobData")
        @Expose
        var pobData: pobObject? = null

        class pobObject{

            @SerializedName("pobId")            var pobId               : Int?                   = null
            @SerializedName("pobNo")            var pobNo               : String?                = null
            @SerializedName("pobDate")          var pobDate             : String?                = null
            @SerializedName("partyId")          var partyId             : Int?                   = null
            @SerializedName("employeeId")       var employeeId          : Int?                   = null
            @SerializedName("remark")           var remark              : String?                = null
            @SerializedName("empId")            var empId               : Int?                   = null
            @SerializedName("mode")             var mode                : Int?                   = null
            @SerializedName("headQuaterName")   var headQuaterName      : String?                = null
            @SerializedName("routeName")        var routeName           : String?                = null
            @SerializedName("stockistId")       var stockistId          : Int?                   = null
            @SerializedName("status")           var status              : String?                = null
            @SerializedName("pobDetailList")    var pobDetailList  : ArrayList<DailyDocVisitModel.Data.DcrDoctor.PobObj.PobDetailList>? = null
            @SerializedName("pobType")          var pobType             : String?                = "DOCTOR"
            @SerializedName("strPOBDate")       var strPOBDate          : String?                = ""
            @SerializedName("totalPOB")         var totalPOB            : Double?                = null
            @SerializedName("stockistName")     var stockistName        : String?                = ""
            @SerializedName("isProductWisePOB") var isProductWisePOB    : Boolean?               = true
         //   @SerializedName("docName")          var docName             : String?                = ""
            @SerializedName("partyName")        var partyName           : String?                = ""
            var randomNumber : Int =0
            //  @SerializedName("hospitalName")     var hospitalName        : String?                = ""
            //  @SerializedName("hospitalId")       var hospitalId          : Int?                   = 0
            //  @SerializedName("dcrId")            var dcrId               : Int?                   = null
            // @SerializedName("stockistName")     var stockistName        : String?                = ""
            // @SerializedName("isProductWisePOB") var isProductWisePOB    : Boolean?               = true
            // @SerializedName("cityName")         var cityName            : String?                = ""
            //  @SerializedName("address")          var address             : String?                = ""
            //
            //  @SerializedName("employeeName")     var employeeName        : String?                = ""

        }

        //=============================== SOB =============================================

        @SerializedName("sobList")
        @Expose
        var sobList: List<SobObject>? = ArrayList()

        @SerializedName("sobData")
        @Expose
        var sobData: SobObject? = null

        class SobObject{

            @SerializedName("sobId")            var sobId               : Int?                   = null
            @SerializedName("sobNo")            var sobNo               : String?                = null
            @SerializedName("sobDate")          var sobDate             : String?                = null
            @SerializedName("partyId")          var partyId             : Int?                   = null
            @SerializedName("employeeId")       var employeeId          : Int?                   = null
            @SerializedName("remark")           var remark              : String?                = null
            @SerializedName("empId")            var empId               : Int?                   = null
            @SerializedName("mode")             var mode                : Int?                   = null
            @SerializedName("headQuaterName")   var headQuaterName      : String?                = null
            @SerializedName("routeName")        var routeName           : String?                = null
            @SerializedName("stockistId")       var stockistId          : Int?                   = null
            @SerializedName("status")           var status              : String?                = null
            @SerializedName("sobDetailList")    var sobDetailList  : ArrayList<SobDetailList>? = null
            @SerializedName("sobType")          var sobType             : String?                = "DOCTOR"
            @SerializedName("strSOBDate")       var strSOBDate          : String?                = ""
            @SerializedName("totalSOB")         var totalSOB            : Double?                = null
            @SerializedName("stockistName")     var stockistName        : String?                = ""
            @SerializedName("isProductWiseSOB") var isProductWiseSOB    : Boolean?               = true
            @SerializedName("partyName")        var partyName           : String?                = ""
            var randomNumber : Int =0

            class SobDetailList()
            {
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

                @SerializedName("sobId")
                @Expose
                var pobId: Int? = null

                @SerializedName("productName")
                @Expose
                var productName: String? = null

                @SerializedName("sobNo")
                @Expose
                var pobNo: Any? = null

                @SerializedName("schemeId")
                @Expose
                var schemeId: Int? = null

                @SerializedName("freeQty")
                @Expose
                var freeQty: Int? = null

                @SerializedName("totalQty")
                @Expose
                var totalQty: Int? = null


                @SerializedName("schemeSalesQty")
                @Expose
                var schemeSalesQty: Int? = null

                @SerializedName("schemeFreeQty")
                @Expose
                var schemeFreeQty: Int? = null

                @SerializedName("schemeNameWithQty")
                @Expose
                var schemeNameWithQty: String? = null

                @SerializedName("isSaved")
                @Expose
                var isSaved=false;
                //  var freeQtyMain: Int?= 0
                //  var salesQtyMain: Int?= 0
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
            var isProductWisePOB: Boolean? = null

            @SerializedName("packingTypeName")
           @Expose
            var packingTypeName: String? = null

           @SerializedName("category")
           @Expose
            var category: String? = null

            @SerializedName("srNo")
           @Expose
            var srNo: Int? = null

            @SerializedName("schemeName")
           @Expose
            var schemeName: String? = null

            */
            }
        }

    }

}