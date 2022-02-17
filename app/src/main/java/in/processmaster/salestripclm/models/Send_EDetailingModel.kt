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
    @SerializedName("dcrYear"            ) var dcrYear            : Int?                = null
    @SerializedName("callTiming"         ) var callTiming         : String?                = null
    @SerializedName("routeType"          ) var routeType          : String?                = null
    @SerializedName("categoryName"       ) var categoryName       : String?                = null
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