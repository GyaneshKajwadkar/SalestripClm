package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class DoctorGraphModel {

    @SerializedName("responseCode")
    @Expose
    private var responseCode: Int? = null

    @SerializedName("errorObj")
    @Expose
    private var errorObj: SyncModel.ErrorObj? = null

    @SerializedName("data")
    @Expose
    private var data: Data? = null

    fun getResponseCode(): Int? {
        return responseCode
    }

    fun setResponseCode(responseCode: Int?) {
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
        var dcrCountList: List<DcrCount>? = null

        class DcrCount {
            @SerializedName("day")
            @Expose
            var day: Int? = null

            @SerializedName("visitedDoctor")
            @Expose
            var visitedDoctor: Int? = null

            @SerializedName("visitedRetailer")
            @Expose
            var visitedRetailer: Int? = null

            @SerializedName("type")
            @Expose
            var type: Any? = null

            @SerializedName("monthNo")
            @Expose
            var monthNo: Int? = null

            @SerializedName("monthName")
            @Expose
            var monthName: String? = null

            @SerializedName("total")
            @Expose
            var total: Int? = null

            @SerializedName("totalTarget")
            @Expose
            var totalTarget: Double? = null

            @SerializedName("totalPOB")
            @Expose
            var totalPOB: Double? = null

            @SerializedName("totalSample")
            @Expose
            var totalSample: Int? = null

            @SerializedName("empName")
            @Expose
            var empName: Any? = null

            @SerializedName("partyName")
            @Expose
            var partyName: Any? = null
        }
    }

}