package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import `in`.processmaster.salestripclm.models.SyncModel.Data.Product

class PreCallModel {

    @SerializedName("responseCode")
    @Expose
    private var responseCode: Int? = null

    @SerializedName("errorObj")
    @Expose
    private var errorObj: ErrorObj? = null

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
        this.responseCode = responseCode
    }

    fun getErrorObj(): ErrorObj? {
        return errorObj
    }

    fun setErrorObj(errorObj: ErrorObj?) {
        this.errorObj = errorObj
    }

    fun getData(): Data? {
        return data
    }

    fun setData(data: Data?) {
        this.data = data
    }

    class ErrorObj {
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String? = null

        @SerializedName("fldErrors")
        @Expose
        var fldErrors: Any? = null
    }

    class Data {
        @SerializedName("lastVisitSummary")
        @Expose
        var lastVisitSummary: LastVisitSummary? = null

        class LastVisitSummary {
            @SerializedName("workWithName")
            @Expose
            var workWithName: String? = null

            @SerializedName("dcrId")
            @Expose
            var dcrId: Int? = null

            @SerializedName("strDcrDate")
            @Expose
            var strDcrDate: String? = null

            @SerializedName("visitPurpose")
            @Expose
            var visitPurpose: String? = null

            @SerializedName("productList")
            @Expose
            var productList: List<Product>? = null

            @SerializedName("sampleList")
            @Expose
            var sampleList: List<Sample>? = null

            @SerializedName("giftList")
            @Expose
            var giftList: List<Gift>? = null

            @SerializedName("docLastRCPADetail")
            @Expose
            var docLastRCPADetail: DocLastRCPADetail? = null

            @SerializedName("lastPOBDetails")
            @Expose
            var lastPOBDetails: LastPOBDetails? = null

            @SerializedName("reportedTime")
            @Expose
            var reportedTime: String? = null

            @SerializedName("strReportedTime")
            @Expose
            var strReportedTime: String? = null

            @SerializedName("remarks")
            @Expose
            var remarks: String? = null


            class Sample {
                @SerializedName("dcrId")
                @Expose
                var dcrId: Int? = null

                @SerializedName("detailId")
                @Expose
                var detailId: Int? = null

                @SerializedName("detailName")
                @Expose
                var detailName: String? = null

                @SerializedName("detailType")
                @Expose
                var detailType: String? = null

                @SerializedName("productId")
                @Expose
                var productId: Int? = null

                @SerializedName("productName")
                @Expose
                var productName: String? = null

                @SerializedName("productType")
                @Expose
                var productType: String? = null

                @SerializedName("qty")
                @Expose
                var qty: Int? = null

                @SerializedName("preQty")
                @Expose
                var preQty: Double? = null

                @SerializedName("total")
                @Expose
                var total: Double? = null

                @SerializedName("mode")
                @Expose
                var mode: Int? = null

                @SerializedName("rate")
                @Expose
                var rate: Double? = null
            }

            class Gift {
                @SerializedName("dcrId")
                @Expose
                var dcrId: Int? = null

                @SerializedName("detailId")
                @Expose
                var detailId: Int? = null

                @SerializedName("detailName")
                @Expose
                var detailName: String? = null

                @SerializedName("detailType")
                @Expose
                var detailType: String? = null

                @SerializedName("productId")
                @Expose
                var productId: Int? = null

                @SerializedName("productName")
                @Expose
                var productName: String? = null

                @SerializedName("productType")
                @Expose
                var productType: String? = null

                @SerializedName("qty")
                @Expose
                var qty: Int? = null

                @SerializedName("preQty")
                @Expose
                var preQty: Double? = null

                @SerializedName("total")
                @Expose
                var total: Double? = null

                @SerializedName("mode")
                @Expose
                var mode: Int? = null

                @SerializedName("rate")
                @Expose
                var rate: Double? = null
            }

            class DocLastRCPADetail {
                @SerializedName("rcpaId")
                @Expose
                var rcpaId: Int? = null

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
                var rcpaDate: String? = null

                @SerializedName("strRCPADate")
                @Expose
                var strRCPADate: String? = null

                @SerializedName("entryBy")
                @Expose
                var entryBy: Int? = null

                @SerializedName("updateBy")
                @Expose
                var updateBy: Int? = null

                @SerializedName("updateDate")
                @Expose
                var updateDate: String? = null

                @SerializedName("mode")
                @Expose
                var mode: Int? = null

                @SerializedName("rcpaDetailList")
                @Expose
                var rcpaDetailList: List<Any>? = null

                @SerializedName("ownSales")
                @Expose
                var ownSales: Int? = null

                @SerializedName("counterSales")
                @Expose
                var counterSales: Int? = null

                @SerializedName("isEditMode")
                @Expose
                var isEditMode: Boolean? = null
            }

            class LastPOBDetails {
                @SerializedName("assignedStockist")
                @Expose
                var assignedStockist: Any? = null

                @SerializedName("remark")
                @Expose
                var remark: Any? = null

                @SerializedName("lastPobDate")
                @Expose
                var lastPobDate: String? = null

                @SerializedName("totalPOB")
                @Expose
                var totalPOB: Double? = null

                @SerializedName("lastPOBDetailList")
                @Expose
                var lastPOBDetailList: List<Any>? = null
            }
        }
    }

}