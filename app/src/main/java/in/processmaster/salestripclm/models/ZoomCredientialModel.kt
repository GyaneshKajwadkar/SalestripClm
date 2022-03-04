package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ZoomCredientialModel {

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
        @SerializedName("credentialData")
        @Expose
        var credentialData: CredentialData? = null
    }

    class CredentialData {
        @SerializedName("empId")
        @Expose
        var empId: Int = 0

        @SerializedName("secretKey")
        @Expose
        var secretKey: String = ""

        @SerializedName("apiKey")
        @Expose
        var apiKey: String = ""

        @SerializedName("entryDate")
        @Expose
        var entryDate: String = ""

        @SerializedName("entryBy")
        @Expose
        var entryBy: Int = 0

        @SerializedName("updateDate")
        @Expose
        var updateDate: String = ""

        @SerializedName("updateBy")
        @Expose
        var updateBy: Int = 0

        @SerializedName("userName")
        @Expose
        var userName: String = ""

        @SerializedName("password")
        @Expose
        var password: String = ""

        @SerializedName("jwtToken")
        @Expose
        var jwtToken: String = ""
    }
}