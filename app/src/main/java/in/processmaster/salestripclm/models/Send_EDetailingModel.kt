package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.SerializedName

class Send_EDetailingModel(val demo:Int =0) {
    @SerializedName("dcrId"              ) var dcrId              : Int?                   = null
    @SerializedName("doctorId"           ) var doctorId           : Int?                   = null
    @SerializedName("remark"             ) var remark             : String?                = null
    @SerializedName("followUpRemark"     ) var followUpRemark     : String?                = null
    @SerializedName("workWith"           ) var workWith           : String?                = null
    @SerializedName("addedThrough"       ) var addedThrough       : String?                = null
    @SerializedName("productList"        ) var productList        : ArrayList<ProductList> = arrayListOf()
    @SerializedName("giftList"           ) var giftList           : ArrayList<GiftList>    = arrayListOf()
    @SerializedName("sampleList"         ) var sampleList         : ArrayList<SampleList>  = arrayListOf()
    @SerializedName("eDetailList"        ) var eDetailList        : ArrayList<VisualAdsModel_Send> = arrayListOf()
    @SerializedName("productDetailCount" ) var productDetailCount : Int?                   = null
    @SerializedName("totalSampleValue"   ) var totalSampleValue   : Int?                   = null
    @SerializedName("totalAchValue"      ) var totalAchValue      : Int?                   = null
    @SerializedName("giftGivenCount"     ) var giftGivenCount     : Int?                   = null
    @SerializedName("totalGiftValue"     ) var totalGiftValue     : Int?                   = null
    @SerializedName("dcrDate"            ) var dcrDate            : String?                = null
    @SerializedName("empId"              ) var empId              : Int?                   = null
    @SerializedName("visitPurpose"       ) var visitPurpose       : Int?                   = null
    @SerializedName("mode"               ) var mode               : Int?                   = null
    @SerializedName("dcrMonth"           ) var dcrMonth           : Int?                   = null
    @SerializedName("dcrYear"            ) var dcrYear            : Int?                   = null
    @SerializedName("callTiming"         ) var callTiming         : String?                = null
    @SerializedName("routeType"          ) var routeType          : String?                = null
    @SerializedName("categoryName"       ) var categoryName       : String?                = null
    @SerializedName("pobObject"          ) var pobObject          : PobObj?                = null
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

    class PobObj{
        @SerializedName("pobId")            var pobId               : Int?                   = null
        @SerializedName("pobNo")            var pobNo               : String?                = null
        @SerializedName("pobDate")          var pobDate             : String?                = null
        @SerializedName("partyId")          var partyId             : Int?                   = null
        @SerializedName("employeeId")       var employeeId          : Int?                   = null
        @SerializedName("remark")           var remark              : String?                = ""
        @SerializedName("empId")            var empId               : Int?                   = 0
        @SerializedName("mode")             var mode                : Int?                   = 0
        @SerializedName("pobType")          var pobType             : String?                = "DOCTOR"
        @SerializedName("address")          var address             : String?                = ""
        @SerializedName("partyName")        var partyName           : String?                = ""
        @SerializedName("employeeName")     var employeeName        : String?                = ""
        @SerializedName("headQuaterName")   var headQuaterName      : String?                = ""
        @SerializedName("routeName")        var routeName           : String?                = ""
        @SerializedName("strPOBDate")       var strPOBDate          : String?                = ""
        @SerializedName("isProductWisePOB") var isProductWisePOB    : Boolean?               = true
        @SerializedName("totalPOB")         var totalPOB            : Double?                = null
        @SerializedName("cityName")         var cityName            : String?                = ""
        @SerializedName("stockistId")       var stockistId          : Int?                   = null
        @SerializedName("stockistName")     var stockistName        : String?                = ""
        @SerializedName("status")           var status              : String?                = ""
        @SerializedName("docName")          var docName             : String?                = ""
        @SerializedName("hospitalName")     var hospitalName        : String?                = ""
        @SerializedName("hospitalId")       var hospitalId          : Int?                   = 0
        @SerializedName("dcrId")            var dcrId               : Int?                   = null
        @SerializedName("pobDetailList")    var pobDetailList       : ArrayList<PobDetailList> = arrayListOf()

  class PobDetailList()
  {

      @SerializedName("productId")var productId        : Int?                   = 0
      @SerializedName("rate")     var rate             : Double?                = 0.0
      @SerializedName("qty")      var qty              : Int?                   = 0
      @SerializedName("amount")   var amount           : Double?                   = 0.0
      @SerializedName("schemeId") var schemeId         : Int?                   = 0
      @SerializedName("totalQty") var totalQty         : Int?                   = 0
      @SerializedName("freeQty")  var  freeQty         : Int?                   = 0
      @SerializedName("freeQtyMain")  var  freeQtyMain         : Int?                   = 0
      @SerializedName("salesQtyMain")  var  salesQtyMain         : Int?                   = 0
      @SerializedName("packingTypeName")  var  packingTypeName         : String?     = null
      @SerializedName("scheme")  var  scheme         : String?     = null
      @SerializedName("productName")  var  productName         : String?     = null
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
}