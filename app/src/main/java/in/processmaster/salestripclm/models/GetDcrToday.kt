package `in`.processmaster.salestripclm.models

import com.google.gson.annotations.SerializedName

class GetDcrToday {

    @SerializedName("responseCode" ) var responseCode : Int?      = null
    @SerializedName("errorObj"     ) var errorObj     : ErrorObj? = ErrorObj()
    @SerializedName("data"         ) var data         : Data?     = Data()

    data class ErrorObj (
        @SerializedName("errorMessage" ) var errorMessage : String? = null,
        @SerializedName("fldErrors"    ) var fldErrors    : String? = null
    )

     class Data {
         @SerializedName("dcrData")
         var dcrData: DcrData? = DcrData()
         @SerializedName("isCPExiest")
         var isCPExiest: Boolean? = false

         class DcrData {

             @SerializedName("dcrId")
             var dcrId: Int? = null
             @SerializedName("dcrDate")
             var dcrDate: String? = null
             @SerializedName("otherDCR")
             var otherDCR: Int? = null
           /*  @SerializedName("dcrType")
             var dcrType: Int? = null
             */
             @SerializedName("empId")
             var empId: Int? = null
             /*@SerializedName("employeeId")
             var employeeId: Int? = null*/
             @SerializedName("mode")
             var mode: Int? = null
             @SerializedName("routeId")
             var routeId: String? = null
             @SerializedName("remark")
             var remark: String? = null
        /*     @SerializedName("otherRemark")
             var otherRemark: String? = null
             @SerializedName("transportMode")
             var transportMode: Int? = null*/
             @SerializedName("routeName")
             var routeName: String? = null
          /*   @SerializedName("strDCRDate")
             var strDCRDate: String? = null*/
             @SerializedName("dataSaveType")
             var dataSaveType: String = "D"
          /*   @SerializedName("isRouteDeviated")
             var isRouteDeviated: Boolean? = null
             @SerializedName("concatEmployeeId")
             var concatEmployeeId: String? = null
             @SerializedName("doctorDCRList")
             var doctorDCRList: ArrayList<DailyDocVisitModel.Data.DcrDoctor>? = null
             @SerializedName("retailerDCRList")
             var retailerDCRList: ArrayList<String> =
                 arrayListOf()
             @SerializedName("stockistDCRList")
             var stockistDCRList: ArrayList<String> =
                 arrayListOf()
             @SerializedName("crmActivityList")
             var crmActivityList: ArrayList<String> =
                 arrayListOf()
             @SerializedName("dailyExpenseList")
             var dailyExpenseList: ArrayList<String> =
                 arrayListOf()
             @SerializedName("jfwExecutionList")
             var jfwExecutionList: ArrayList<String> = arrayListOf()
             @SerializedName("inclinicEffCallList")
             var inclinicEffCallList: ArrayList<String> = arrayListOf()
             @SerializedName("strengthList")
             var strengthList: ArrayList<String> = arrayListOf()
             @SerializedName("areaOfImprovementList")
             var areaOfImprovementList: ArrayList<String> =
                 arrayListOf()
             @SerializedName("onFieldJobDate")
             var onFieldJobDate: String? = null
             @SerializedName("dayCount")
             var dayCount: Int? = null
             @SerializedName("monthNo")
             var monthNo: Int? = null*/
           /*  @SerializedName("year")
             var year: Int? = null
             @SerializedName("monthName")
             var monthName: String? = null
             @SerializedName("rtpApproveStatus")
             var rtpApproveStatus: String? = null
             @SerializedName("weekDay")
             var weekDay: String? = null
             @SerializedName("cssClass")
             var cssClass: String? = null
             @SerializedName("reportingManager")
             var reportingManager: Int? = null
             @SerializedName("employeeName")
             var employeeName: String? = null
             @SerializedName("rtpId")
             var rtpId: Int? = null
             @SerializedName("otherDCRName")
             var otherDCRName: String? = null
             @SerializedName("transportModeName")
             var transportModeName: String? = null
             @SerializedName("reportingManagerName")
             var reportingManagerName: String? = null
             @SerializedName("headQuaterId")
             var headQuaterId: Int? = null
             @SerializedName("routeType")
             var routeType: Int? = null
             @SerializedName("routeDistance")
             var routeDistance: Int? = null
             @SerializedName("visitedDoctor")
             var visitedDoctor: Int? = null
             @SerializedName("visitedRetailer")
             var visitedRetailer: Int? = null
             @SerializedName("visitedStockist")
             var visitedStockist: Int? = null
             @SerializedName("totalPOB")
             var totalPOB: Int? = null
             @SerializedName("backColor")
             var backColor: String? = null
             @SerializedName("foreColor")
             var foreColor: String? = null
             @SerializedName("isReviewed")
             var isReviewed: Boolean? = null
             @SerializedName("dcsType")
             var dcsType: String? = null
             @SerializedName("isVisited")
             var isVisited: Boolean? = null
             @SerializedName("unlockStatus")
             var unlockStatus: String? = null
             @SerializedName("currentState")
             var currentState: String? = null
             @SerializedName("partyId")
             var partyId: Int? = null
             @SerializedName("partyName")
             var partyName: String? = null
             @SerializedName("isLeaveHoliday")
             var isLeaveHoliday: Boolean? = null
             @SerializedName("isRTPFilled")
             var isRTPFilled: Boolean? = null
             @SerializedName("isLeave")
             var isLeave: Boolean? = null
             @SerializedName("isHoliday")
             var isHoliday: Boolean? = null
             @SerializedName("isDCRApproval")
             var isDCRApproval: Boolean? = null
             @SerializedName("approveStatus")
             var approveStatus: String? = null
             @SerializedName("approveBy")
             var approveBy: Int? = null
             @SerializedName("approveStatusName")
             var approveStatusName: String? = null
             @SerializedName("rejectReason")
             var rejectReason: String? = null
             @SerializedName("strHeadQuaterId")
             var strHeadQuaterId: String? = null
             @SerializedName("hierarchyType")
             var hierarchyType: String? = null
             @SerializedName("deletedBy")
             var deletedBy: String? = null
             @SerializedName("dataSaveTypeName")
             var dataSaveTypeName: String? = null
             @SerializedName("isForcefullyEdit")
             var isForcefullyEdit: Boolean? = null
             @SerializedName("submittedDate")
             var submittedDate: String? = null
             @SerializedName("strSubmittedDate")
             var strSubmittedDate: String? = null
             @SerializedName("pendingCount")
             var pendingCount: Int? = null
             @SerializedName("hierarchyDesc")
             var hierarchyDesc: String? = null
             @SerializedName("hierarchyCode")
             var hierarchyCode: String? = null
             @SerializedName("linkedStateName")
             var linkedStateName: String? = null*/
             @SerializedName("headQuaterName")
             var headQuaterName: String? = null
            /* @SerializedName("absolutePath")
             var absolutePath: String? = null
             @SerializedName("gender")
             var gender: Int? = null
             @SerializedName("startingStation")
             var startingStation: Int? = null
             @SerializedName("endingStation")
             var endingStation: Int? = null
             @SerializedName("workingType")
             var workingType: String? = null
             @SerializedName("workingTypeName")
             var workingTypeName: String? = null
             @SerializedName("actualRouteName")
             var actualRouteName: String? = null
             @SerializedName("isProductDisplay")
             var isProductDisplay: String? = null
             @SerializedName("startingStationName")
             var startingStationName: String? = null
             @SerializedName("endingStationName")
             var endingStationName: String? = null
             @SerializedName("fiscalYear")
             var fiscalYear: Int? = null
             @SerializedName("accompaniedWith")
             var accompaniedWith: Int? = null
             @SerializedName("dailyExpense")
             var dailyExpense: Int? = null
             @SerializedName("crmActivityCount")
             var crmActivityCount: Int? = null
             @SerializedName("mobileCSSClass")
             var mobileCSSClass: String? = null
             @SerializedName("additionalActivityRemark")
             var additionalActivityRemark: String? = null
             @SerializedName("additionalActivityId")
             var additionalActivityId: Int? = null
             @SerializedName("residentialStatus")
             var residentialStatus: Int? = null
             @SerializedName("objectiveOfDay")
             var objectiveOfDay: String? = null
             @SerializedName("feedBack")
             var feedBack: String? = null
             @SerializedName("planCompliancePer")
             var planCompliancePer: Int? = null
             @SerializedName("isPlanCompliance")
             var isPlanCompliance: Boolean? = null
             @SerializedName("minCallAlert")
             var minCallAlert: String? = null
             @SerializedName("nextApproverId")
             var nextApproverId: Int? = null
             @SerializedName("nextApproval")
             var nextApproval: Int? = null
             @SerializedName("deleteStatus")
             var deleteStatus: String? = null
             @SerializedName("deleteStatusName")
             var deleteStatusName: String? = null
             @SerializedName("deleteNextApprover")
             var deleteNextApprover: String? = null
             @SerializedName("additionalActivityName")
             var additionalActivityName: String? = null
             @SerializedName("fsCode")
             var fsCode: String? = null
             @SerializedName("empCode")
             var empCode: String? = null
             @SerializedName("designation")
             var designation: String? = null*/
           /*  @SerializedName("division")
             var division: String? = null
             @SerializedName("region")
             var region: String? = null
             @SerializedName("zone")
             var zone: String? = null
             @SerializedName("approvedDate")
             var approvedDate: String? = null
             @SerializedName("strApprovedDate")
             var strApprovedDate: String? = null
             @SerializedName("nextApproverName")
             var nextApproverName: String? = null
             @SerializedName("inclinicEffCall")
             var inclinicEffCall: Int? = null
             @SerializedName("isJFWCompleted")
             var isJFWCompleted: Boolean? = null
             @SerializedName("isAOICompleted")
             var isAOICompleted: Boolean? = null
             @SerializedName("isStrengthsCompleted")
             var isStrengthsCompleted: Boolean? = null
             @SerializedName("accompaniedWithName")
             var accompaniedWithName: String? = null
             @SerializedName("totalOtherLocationDoc")
             var totalOtherLocationDoc: Int? = null
             @SerializedName("totalOtherLocationRet")
             var totalOtherLocationRet: Int? = null
             @SerializedName("unlockApproverName")
             var unlockApproverName: String? = null
             @SerializedName("divisionName")
             var divisionName: String? = null
             @SerializedName("totalHospVisit")
             var totalHospVisit: Int? = null
             @SerializedName("backDays")
             var backDays: Int? = null*/

         }
     }


}