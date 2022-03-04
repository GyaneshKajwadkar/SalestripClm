package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
            var visitPurpose: String = ""

            @SerializedName("productList")
            @Expose
            var productList: List<Product>? = ArrayList()

            @SerializedName("sampleList")
            @Expose
            var sampleList: List<Sample>? = null

            @SerializedName("giftList")
            @Expose
            var giftList: List<Gift>? = null

            @SerializedName("lastRCPADetails")
            @Expose
            var lastRCPADetails: List<LastRCPADetail>? = null


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

            class LastRCPADetail {
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
                var rcpaDetailId: Int? = null

                @SerializedName("brandValue")
                @Expose
                var brandValue: Double? = null

                @SerializedName("cP1")
                @Expose
                private var cP1: String? = null

                @SerializedName("cpRx1")
                @Expose
                var cpRx1: Double? = null

                @SerializedName("cP2")
                @Expose
                private var cP2: String? = null

                @SerializedName("cpRx2")
                @Expose
                var cpRx2: Double? = null

                @SerializedName("cP3")
                @Expose
                private var cP3: String? = null

                @SerializedName("cpRx3")
                @Expose
                var cpRx3: Double? = null

                @SerializedName("cP4")
                @Expose
                private var cP4: String? = null

                @SerializedName("cpRx4")
                @Expose
                var cpRx4: Double? = null

                @SerializedName("rcpaNo")
                @Expose
                var rcpaNo: Int? = null

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
                var dcrId: Int? = null

                @SerializedName("lastRCPADate")
                @Expose
                var lastRCPADate: String? = null

                fun getcP1(): String? {
                    return cP1
                }

                fun setcP1(cP1: String?) {
                    this.cP1 = cP1
                }

                fun getcP2(): String? {
                    return cP2
                }

                fun setcP2(cP2: String?) {
                    this.cP2 = cP2
                }

                fun getcP3(): String? {
                    return cP3
                }

                fun setcP3(cP3: String?) {
                    this.cP3 = cP3
                }

                fun getcP4(): String? {
                    return cP4
                }

                fun setcP4(cP4: String?) {
                    this.cP4 = cP4
                }
            }

            class LastPOBDetails {
                @SerializedName("assignedStockist")
                @Expose
                var assignedStockist: String? = null

                @SerializedName("remark")
                @Expose
                var remark: String? = null

                @SerializedName("lastPobDate")
                @Expose
                var lastPobDate: String? = null

                @SerializedName("strPobDate")
                @Expose
                var strPobDate: String? = null

                @SerializedName("totalPOB")
                @Expose
                var totalPOB: Double? = null

                @SerializedName("lastPOBDetailList")
                @Expose
                var lastPOBDetailList: List<LastPOBDetail>? = null

                class LastPOBDetail {
                    @SerializedName("productName")
                    @Expose
                    var productName: String? = null

                    @SerializedName("quantity")
                    @Expose
                    var quantity: Int? = null

                    @SerializedName("amount")
                    @Expose
                    var amount: Double? = null
                }
            }
            class Product {
                @SerializedName("productId")
                @Expose
                var productId: Int? = null

                @SerializedName("productName")
                @Expose
                var productName: String? = null

                @SerializedName("prodCode")
                @Expose
                var prodCode: String? = null

                @SerializedName("shortName")
                @Expose
                var shortName: String? = null

                @SerializedName("divisionId")
                @Expose
                var divisionId: Int? = null

                @SerializedName("categoryId")
                @Expose
                var categoryId: Int? = null

                @SerializedName("packingTypeId")
                @Expose
                var packingTypeId: Int? = null

                @SerializedName("mrp")
                @Expose
                var mrp: Double? = null

                @SerializedName("productType")
                @Expose
                var productType: Int? = null

                @SerializedName("empId")
                @Expose
                var empId: Int? = null

                @SerializedName("mode")
                @Expose
                var mode: Int? = null

                @SerializedName("dosageId")
                @Expose
                var dosageId: Int? = null

                @SerializedName("productTypeName")
                @Expose
                var productTypeName: String? = null

                @SerializedName("packingTypeName")
                @Expose
                var packingTypeName: String? = null

                @SerializedName("categoryName")
                @Expose
                var categoryName: String? = null

                @SerializedName("divisionName")
                @Expose
                var divisionName: String? = null

                @SerializedName("price")
                @Expose
                var price: Double? = null

                @SerializedName("priceToStockist")
                @Expose
                var priceToStockist: Double? = null

                @SerializedName("targetPrice")
                @Expose
                var targetPrice: Double? = null

                @SerializedName("status")
                @Expose
                var status: Any? = null

                @SerializedName("preProductName")
                @Expose
                var preProductName: Any? = null

                @SerializedName("stock")
                @Expose
                var stock: Double? = null

                @SerializedName("isDisabled")
                @Expose
                var isDisabled: Boolean? = null

                @SerializedName("prevTransitStock")
                @Expose
                var prevTransitStock: Double? = null

                @SerializedName("brandId")
                @Expose
                var brandId: Int? = null

                @SerializedName("eDetailId")
                @Expose
                private var eDetailId: Int? = null

                @SerializedName("isError")
                @Expose
                var isError: Boolean? = null

                @SerializedName("dosageName")
                @Expose
                var dosageName: String? = null

                @SerializedName("errorMessage")
                @Expose
                var errorMessage: String? = null

                @SerializedName("sampleRate")
                @Expose
                var sampleRate: Double? = null

                @SerializedName("samplePackSizeId")
                @Expose
                var samplePackSizeId: Int? = null

                @SerializedName("allowSample")
                @Expose
                var allowSample: Boolean? = null

                @SerializedName("giftCategoryId")
                @Expose
                var giftCategoryId: String? = null

                @SerializedName("samplePackSizeName")
                @Expose
                var samplePackSizeName: String? = null

                @SerializedName("giftCategoryName")
                @Expose
                var giftCategoryName: String? = null

                @SerializedName("brandName")
                @Expose
                var brandName: Any? = null

                @SerializedName("sampleProductName")
                @Expose
                var sampleProductName: String? = null

                @SerializedName("sampleProdCode")
                @Expose
                var sampleProdCode: String? = null

                @SerializedName("createdOn")
                @Expose
                var createdOn: Any? = null

                @SerializedName("freeStock")
                @Expose
                var freeStock: Double? = null

                fun geteDetailId(): Int? {
                    return eDetailId
                }

                fun seteDetailId(eDetailId: Int?) {
                    this.eDetailId = eDetailId
                }
            }
        }
    }
}