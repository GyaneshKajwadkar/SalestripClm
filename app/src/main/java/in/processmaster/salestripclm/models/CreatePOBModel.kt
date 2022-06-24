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
    }

}