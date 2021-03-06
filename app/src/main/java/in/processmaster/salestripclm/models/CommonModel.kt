package `in`.processmaster.salestripclm.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CommonModel {

    class SaveDcrModel{
         val dataSaveType="D"
         var dcrDate=""
            get() = field
            set(value) { field = value }
         var remark=""
            get() = field
            set(value) { field = value }
         var workingType=""
            get() = field
            set(value) { field = value }
         var empId=0
            get() = field
            set(value) { field = value }
         var employeeId=0
            get() = field
            set(value) { field = value }
         var endingStation=0
            get() = field
            set(value) { field = value }
         var startingStation=0
            get() = field
            set(value) { field = value }
         val mode=1
        var monthNo=0
            get() = field
            set(value) { field = value }
        var year=0
            get() = field
            set(value) { field = value }
        var routeId=""
            get() = field
            set(value) { field = value }
        var dayCount=""
            get() = field
            set(value) { field = value }
        var additionalActivityRemark=""
            get() = field
            set(value) { field = value }
        var dcrType=0
            get() = field
            set(value) { field = value }
        var accompaniedWith=0
            get() = field
            set(value) { field = value }
        var objectiveOfDay=""
            get() = field
            set(value) { field = value }
        var feedBack=""
            get() = field
            set(value) { field = value }
        var additionalActivityName=""
            get() = field
            set(value) { field = value }
        var additionalActivityId=0
            get() = field
            set(value) { field = value }

        var OtherDCR=0
            get() = field
            set(value) { field = value }

        var routeName=""
            get() = field
            set(value) { field = value }

    }

    class QuantityModel{
        @SerializedName("responseCode")
        @Expose
        private var responseCode: Int? = null
        @SerializedName("errorObj")
        @Expose
        private var errorObj: ErrorObj = ErrorObj()
        @SerializedName("data")
        @Expose
        private var data: Data = Data()
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
            this.errorObj = errorObj!!
        }
        fun getData(): Data? {
            return data
        }
        fun setData(data: Data?) {
            this.data = data!!
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
            @SerializedName("employeeSampleBalanceList")
            @Expose
            var employeeSampleBalanceList: List<EmployeeSampleBalance>? = null

            class EmployeeSampleBalance {
                @SerializedName("productId")
                @Expose
                var productId: Int? = null

                @SerializedName("actualBalanceQty")
                @Expose
                var actualBalanceQty: Int? = null

                @SerializedName("newBalanceQty")
                @Expose
                var newBalanceQty: Int? = null

                @SerializedName("productName")
                @Expose
                var productName: String? = null

                @SerializedName("productType")
                @Expose
                var productType: String? = null
            }
        }

    }

  /*  class SaveRcpaDetail{
        var ownBrand=""
            get() = field
            set(value) { field = value }
        var rxUnit=""
            get() = field
            set(value) { field = value }
        var competitor1=""
            get() = field
            set(value) { field = value }
        var competitor2=""
            get() = field
            set(value) { field = value }
        var competitor3=""
            get() = field
            set(value) { field = value }
        var competitor4=""
            get() = field
            set(value) { field = value }
        var cp1Rxunit=""
            get() = field
            set(value) { field = value }
        var cp2Rxunit=""
            get() = field
            set(value) { field = value }
        var cp3Rxunit=""
            get() = field
            set(value) { field = value }
        var cp4Rxunit=""
            get() = field
            set(value) { field = value }

    }*/

    class FilterModel
    {
        var categoryName=""
            get() = field
            set(value) { field = value }
        var isSelected=false
            get() = field
            set(value) { field = value }
    }

    class SendDeviceDetailModel
    {
        var userId=0
            get() = field
            set(value) { field = value }
        var manufacturer=""
            get() = field
            set(value) { field = value }
        var model=""
            get() = field
            set(value) { field = value }
        var osVersion=0
            get() = field
            set(value) { field = value }
        var mobileAppVersion=""
            get() = field
            set(value) { field = value }

    }

    /*if(value==null || value.isEmpty())field = ""
else field = value*/

}