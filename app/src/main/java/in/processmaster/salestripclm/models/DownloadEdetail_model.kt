package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class DownloadEdetail_model : Serializable {

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

    class Data : Serializable {
        @SerializedName("eDetailingImagesList")
        @Expose
         var eDetailingImagesList: List<EDetailingImages> = ArrayList()

        @SerializedName("eDetailingReferenceList")
        @Expose
         var eDetailingReferenceList: List<Any> = ArrayList()

        class EDetailingImages: Serializable {

            @SerializedName("eDetailId")
            @Expose
            var eDetailId: Int = 0

            @SerializedName("fileId")
            @Expose
            var fileId: Int = 0

            @SerializedName("fileSize")
            @Expose
            var fileSize: Int = 0

            @SerializedName("fileName")
            @Expose
            var fileName: String = ""

            @SerializedName("filePath")
            @Expose
            var filePath: String = ""

            @SerializedName("fileType")
            @Expose
            var fileType: String = ""


            @SerializedName("fileOrder")
            @Expose
            var fileOrder: Int = 0

        }
    }


}