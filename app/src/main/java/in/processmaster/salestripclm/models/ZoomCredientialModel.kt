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
        var empId: Int? = null

        @SerializedName("secretKey")
        @Expose
        var secretKey: String? = null

        @SerializedName("apiKey")
        @Expose
        var apiKey: String? = null

        @SerializedName("entryDate")
        @Expose
        var entryDate: String? = null

        @SerializedName("entryBy")
        @Expose
        var entryBy: Int? = null

        @SerializedName("updateDate")
        @Expose
        var updateDate: String? = null

        @SerializedName("updateBy")
        @Expose
        var updateBy: Int? = null

        @SerializedName("userName")
        @Expose
        var userName: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("jwtToken")
        @Expose
        var jwtToken: String? = null
    }
}