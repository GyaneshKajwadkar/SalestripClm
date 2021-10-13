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
        private var eDetailingImagesList: List<EDetailingImages>? = null

        @SerializedName("eDetailingReferenceList")
        @Expose
        private var eDetailingReferenceList: List<Any>? = null
        fun geteDetailingImagesList(): List<EDetailingImages>? {
            return eDetailingImagesList
        }

        fun seteDetailingImagesList(eDetailingImagesList: List<EDetailingImages>?) {
            this.eDetailingImagesList = eDetailingImagesList
        }

        fun geteDetailingReferenceList(): List<Any>? {
            return eDetailingReferenceList
        }

        fun seteDetailingReferenceList(eDetailingReferenceList: List<Any>?) {
            this.eDetailingReferenceList = eDetailingReferenceList
        }

        class EDetailingImages: Serializable {

            @SerializedName("eDetailId")
            @Expose
            private var eDetailId: Int? = null

            @SerializedName("fileId")
            @Expose
            var fileId: Int? = null

            @SerializedName("fileSize")
            @Expose
            var fileSize: Int? = null

            @SerializedName("fileName")
            @Expose
            var fileName: String? = null

            @SerializedName("filePath")
            @Expose
            var filePath: String? = null

            @SerializedName("fileType")
            @Expose
            var fileType: String? = null


            @SerializedName("fileOrder")
            @Expose
            var fileOrder: Int? = null

            fun geteDetailId(): Int? {
                return eDetailId
            }

            fun seteDetailId(eDetailId: Int?) {
                this.eDetailId = eDetailId
            }
        }
    }


}