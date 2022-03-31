package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DoctorGraphModel {

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
        @SerializedName("dcrCountList")
        @Expose
        var dcrCountList: List<DcrCount> = ArrayList()

        class DcrCount {
         /*   @SerializedName("day")
            @Expose
            var day: Int? = 0*/

            @SerializedName("visitedDoctor")
            @Expose
            var visitedDoctor: Int? = 0

            @SerializedName("visitedRetailer")
            @Expose
            var visitedRetailer: Int? = 0

            @SerializedName("type")
            @Expose
            var type: String = ""

    /*        @SerializedName("monthNo")
            @Expose
            var monthNo: Int? = 0*/

            @SerializedName("monthName")
            @Expose
            var monthName: String = ""

         /*   @SerializedName("total")
            @Expose
            var total: Int? = 0

            @SerializedName("totalTarget")
            @Expose
            var totalTarget: Double? = 0.0

            @SerializedName("totalPOB")
            @Expose
            var totalPOB: Double? = 0.0

            @SerializedName("totalSample")
            @Expose
            var totalSample: Int? = 0

            @SerializedName("empName")
            @Expose
            var empName: String = ""

            @SerializedName("partyName")
            @Expose
            var partyName: String = ""*/
        }
    }

}