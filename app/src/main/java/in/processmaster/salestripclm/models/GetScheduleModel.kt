package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetScheduleModel :Serializable {

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

    class ErrorObj :Serializable {
        @SerializedName("errorMessage")
        @Expose
        var errorMessage: String? = null

        @SerializedName("fldErrors")
        @Expose
        var fldErrors: Any? = null
    }
    class Data :Serializable {
        @SerializedName("meetingList")
        @Expose
        var meetingList: List<Meeting>? = null

        class Meeting :Serializable{
            @SerializedName("meetingId")
            @Expose
            var meetingId: Int = 0

            @SerializedName("meetingLink")
            @Expose
            var meetingLink: String = ""

            @SerializedName("meetingDate")
            @Expose
            var meetingDate: String = ""

            @SerializedName("topic")
            @Expose
            var topic: String = ""

            @SerializedName("startTime")
            @Expose
            var startTime: String = ""

            @SerializedName("endTime")
            @Expose
            var endTime: String = ""

            @SerializedName("meetingType")
            @Expose
            var meetingType: String = ""

            @SerializedName("entryDate")
            @Expose
            var entryDate: String = ""

            @SerializedName("entryBy")
            @Expose
            var entryBy: Int = 0

            @SerializedName("duration")
            @Expose
            var duration: Long = 0

            @SerializedName("updateDate")
            @Expose
            var updateDate: String = ""

            @SerializedName("updateBy")
            @Expose
            var updateBy: Int = 0

            @SerializedName("empId")
            @Expose
            var empId: Int = 0

            @SerializedName("mode")
            @Expose
            var mode: Int = 0

            @SerializedName("type")
            @Expose
            var type: Int = 0

            @SerializedName("pre_Shedule")
            @Expose
            var preShedule: Boolean = false

            @SerializedName("password")
            @Expose
            var password: String = ""

            @SerializedName("default_Password")
            @Expose
            var defaultPassword: Boolean = false

            @SerializedName("timezone")
            @Expose
            var timezone: String = ""

            @SerializedName("agenda")
            @Expose
            var agenda: String = ""

            @SerializedName("zoomMeetingId")
            @Expose
            var zoomMeetingId: String = ""

            @SerializedName("strStartTime")
            @Expose
            var strStartTime: String = ""

            @SerializedName("strEndTime")
            @Expose
            var strEndTime: String = ""

            @SerializedName("description")
            @Expose
            var description: String = ""

            @SerializedName("isError")
            @Expose
            var isError: Boolean = false

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: String = ""

            @SerializedName("doctorList")
            @Expose
            var doctorList: List<Doctor> = ArrayList()

            @SerializedName("employeeList")
            @Expose
            var employeeList: List<Employee> = ArrayList()

            class Employee :Serializable {
                @SerializedName("meetingId")
                @Expose
                var meetingId: Int = 0

                @SerializedName("memberId")
                @Expose
                var memberId: Int = 0

                @SerializedName("memberType")
                @Expose
                var memberType: String = ""

                @SerializedName("emailId")
                @Expose
                var emailId: String = ""

                @SerializedName("name")
                @Expose
                var name: String = ""
            }
            class Doctor :Serializable {
                @SerializedName("meetingId")
                @Expose
                var meetingId: Int = 0

                @SerializedName("memberId")
                @Expose
                var memberId: Int = 0

                @SerializedName("memberType")
                @Expose
                var memberType: String = ""

                @SerializedName("emailId")
                @Expose
                var emailId: String = ""

                @SerializedName("name")
                @Expose
                var name: String = ""
            }
        }
    }


}