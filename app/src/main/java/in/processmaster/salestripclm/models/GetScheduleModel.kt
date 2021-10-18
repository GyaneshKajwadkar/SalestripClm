package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class GetScheduleModel {

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
        @SerializedName("meetingList")
        @Expose
        var meetingList: List<Meeting>? = null

        class Meeting {
            @SerializedName("meetingId")
            @Expose
            var meetingId: Int? = null

            @SerializedName("meetingLink")
            @Expose
            var meetingLink: String? = null

            @SerializedName("meetingDate")
            @Expose
            var meetingDate: String? = null

            @SerializedName("topic")
            @Expose
            var topic: String? = null

            @SerializedName("startTime")
            @Expose
            var startTime: String? = null

            @SerializedName("endTime")
            @Expose
            var endTime: String? = null

            @SerializedName("meetingType")
            @Expose
            var meetingType: String? = null

            @SerializedName("entryDate")
            @Expose
            var entryDate: String? = null

            @SerializedName("entryBy")
            @Expose
            var entryBy: Int? = null

            @SerializedName("duration")
            @Expose
            var duration: Long? = null

            @SerializedName("updateDate")
            @Expose
            var updateDate: String? = null

            @SerializedName("updateBy")
            @Expose
            var updateBy: Int? = null

            @SerializedName("empId")
            @Expose
            var empId: Int? = null

            @SerializedName("mode")
            @Expose
            var mode: Int? = null

            @SerializedName("type")
            @Expose
            var type: Int? = null

            @SerializedName("pre_Shedule")
            @Expose
            var preShedule: Boolean? = null

            @SerializedName("password")
            @Expose
            var password: Any? = null

            @SerializedName("default_Password")
            @Expose
            var defaultPassword: Boolean? = null

            @SerializedName("timezone")
            @Expose
            var timezone: String? = null

            @SerializedName("agenda")
            @Expose
            var agenda: Any? = null

            @SerializedName("zoomMeetingId")
            @Expose
            var zoomMeetingId: String? = null

            @SerializedName("strStartTime")
            @Expose
            var strStartTime: String? = null

            @SerializedName("strEndTime")
            @Expose
            var strEndTime: String? = null

            @SerializedName("description")
            @Expose
            var description: String? = null

            @SerializedName("isError")
            @Expose
            var isError: Boolean? = null

            @SerializedName("errorMessage")
            @Expose
            var errorMessage: Any? = null

            @SerializedName("doctorList")
            @Expose
            var doctorList: List<Doctor>? = null

            @SerializedName("employeeList")
            @Expose
            var employeeList: List<Employee>? = null

            class Employee {
                @SerializedName("meetingId")
                @Expose
                var meetingId: Int? = null

                @SerializedName("memberId")
                @Expose
                var memberId: Int? = null

                @SerializedName("memberType")
                @Expose
                var memberType: String? = null

                @SerializedName("emailId")
                @Expose
                var emailId: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null
            }
            class Doctor {
                @SerializedName("meetingId")
                @Expose
                var meetingId: Int? = null

                @SerializedName("memberId")
                @Expose
                var memberId: Int? = null

                @SerializedName("memberType")
                @Expose
                var memberType: String? = null

                @SerializedName("emailId")
                @Expose
                var emailId: String? = null

                @SerializedName("name")
                @Expose
                var name: String? = null
            }
        }
    }


}