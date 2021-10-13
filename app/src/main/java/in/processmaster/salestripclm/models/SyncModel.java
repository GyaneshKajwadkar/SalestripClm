package in.processmaster.salestripclm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SyncModel {

    @SerializedName("responseCode")
    @Expose
    private Integer responseCode;
    @SerializedName("errorObj")
    @Expose
    private ErrorObj errorObj;
    @SerializedName("data")
    @Expose
    private Data data;

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public ErrorObj getErrorObj() {
        return errorObj;
    }

    public void setErrorObj(ErrorObj errorObj) {
        this.errorObj = errorObj;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class ErrorObj {

        @SerializedName("errorMessage")
        @Expose
        private String errorMessage;
        @SerializedName("fldErrors")
        @Expose
        private Object fldErrors;

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }

        public Object getFldErrors() {
            return fldErrors;
        }

        public void setFldErrors(Object fldErrors) {
            this.fldErrors = fldErrors;
        }

    }
    public class Data {

        @SerializedName("settingDCR")
        @Expose
        private SettingDCR settingDCR;
        @SerializedName("headQuarterList")
        @Expose
        private ArrayList<HeadQuarter> headQuarterList = null;
        @SerializedName("routeList")
        @Expose
        private ArrayList<Route> routeList = null;
        @SerializedName("specialistList")
        @Expose
        private ArrayList<Specialist> specialistList = null;
        @SerializedName("qualificationList")
        @Expose
        private ArrayList<Qualification> qualificationList = null;
        @SerializedName("cityList")
        @Expose
        private ArrayList<City> cityList = null;
        @SerializedName("transportList")
        @Expose
        private ArrayList<Transport> transportList = null;
        @SerializedName("workTypeList")
        @Expose
        private ArrayList<WorkType> workTypeList = null;
        @SerializedName("retailerList")
        @Expose
        private ArrayList<Retailer> retailerList = null;
        @SerializedName("doctorList")
        @Expose
        private ArrayList<Doctor> doctorList = null;
        @SerializedName("productList")
        @Expose
        private ArrayList<Product> productList = null;
        @SerializedName("brandList")
        @Expose
        private ArrayList<Brand> brandList = null;
        @SerializedName("workingWithList")
        @Expose
        private ArrayList<WorkingWith> workingWithList = null;
        @SerializedName("doctorFieldConfigDict")
        @Expose
        private String doctorFieldConfigDict;
        @SerializedName("retailerFieldConfigDict")
        @Expose
        private String retailerFieldConfigDict;
        @SerializedName("configurationSetting")
        @Expose
        private String configurationSetting;
        @SerializedName("holidayList")
        @Expose
        private ArrayList<Holiday> holidayList = null;

        public SettingDCR getSettingDCR() {
            return settingDCR;
        }

        public void setSettingDCR(SettingDCR settingDCR) {
            this.settingDCR = settingDCR;
        }

        public ArrayList<HeadQuarter> getHeadQuarterList() {
            return headQuarterList;
        }

        public void setHeadQuarterList(ArrayList<HeadQuarter> headQuarterList) {
            this.headQuarterList = headQuarterList;
        }

        public ArrayList<Route> getRouteList() {
            return routeList;
        }

        public void setRouteList(ArrayList<Route> routeList) {
            this.routeList = routeList;
        }

        public ArrayList<Specialist> getSpecialistList() {
            return specialistList;
        }

        public void setSpecialistList(ArrayList<Specialist> specialistList) {
            this.specialistList = specialistList;
        }

        public ArrayList<Qualification> getQualificationList() {
            return qualificationList;
        }

        public void setQualificationList(ArrayList<Qualification> qualificationList) {
            this.qualificationList = qualificationList;
        }

        public ArrayList<City> getCityList() {
            return cityList;
        }

        public void setCityList(ArrayList<City> cityList) {
            this.cityList = cityList;
        }

        public ArrayList<Transport> getTransportList() {
            return transportList;
        }

        public void setTransportList(ArrayList<Transport> transportList) {
            this.transportList = transportList;
        }

        public ArrayList<WorkType> getWorkTypeList() {
            return workTypeList;
        }

        public void setWorkTypeList(ArrayList<WorkType> workTypeList) {
            this.workTypeList = workTypeList;
        }

        public ArrayList<Retailer> getRetailerList() {
            return retailerList;
        }

        public void setRetailerList(ArrayList<Retailer> retailerList) {
            this.retailerList = retailerList;
        }

        public ArrayList<Doctor> getDoctorList() {
            return doctorList;
        }

        public void setDoctorList(ArrayList<Doctor> doctorList) {
            this.doctorList = doctorList;
        }

        public ArrayList<Product> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<Product> productList) {
            this.productList = productList;
        }

        public ArrayList<Brand> getBrandList() {
            return brandList;
        }

        public void setBrandList(ArrayList<Brand> brandList) {
            this.brandList = brandList;
        }

        public ArrayList<WorkingWith> getWorkingWithList() {
            return workingWithList;
        }

        public void setWorkingWithList(ArrayList<WorkingWith> workingWithList) {
            this.workingWithList = workingWithList;
        }

        public String getDoctorFieldConfigDict() {
            return doctorFieldConfigDict;
        }

        public void setDoctorFieldConfigDict(String doctorFieldConfigDict) {
            this.doctorFieldConfigDict = doctorFieldConfigDict;
        }

        public String getRetailerFieldConfigDict() {
            return retailerFieldConfigDict;
        }

        public void setRetailerFieldConfigDict(String retailerFieldConfigDict) {
            this.retailerFieldConfigDict = retailerFieldConfigDict;
        }

        public String getConfigurationSetting() {
            return configurationSetting;
        }

        public void setConfigurationSetting(String configurationSetting) {
            this.configurationSetting = configurationSetting;
        }

        public ArrayList<Holiday> getHolidayList() {
            return holidayList;
        }

        public void setHolidayList(ArrayList<Holiday> holidayList) {
            this.holidayList = holidayList;
        }
        public class SettingDCR {

            @SerializedName("settingId")
            @Expose
            private Integer settingId;
            @SerializedName("roleType")
            @Expose
            private String roleType;
            @SerializedName("sequentialDCR")
            @Expose
            private Boolean sequentialDCR;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("hqType")
            @Expose
            private Integer hqType;
            @SerializedName("allowBackDate")
            @Expose
            private Integer allowBackDate;
            @SerializedName("isRouteDeviationApproval")
            @Expose
            private Boolean isRouteDeviationApproval;
            @SerializedName("isDoctorFencingRequired")
            @Expose
            private Boolean isDoctorFencingRequired;
            @SerializedName("isRetailerFencingRequired")
            @Expose
            private Boolean isRetailerFencingRequired;
            @SerializedName("roleTypeName")
            @Expose
            private String roleTypeName;
            @SerializedName("hqTypeName")
            @Expose
            private Object hqTypeName;
            @SerializedName("isDCRApproval")
            @Expose
            private Boolean isDCRApproval;
            @SerializedName("isGeoLocationRequired")
            @Expose
            private Boolean isGeoLocationRequired;
            @SerializedName("reportingAllow")
            @Expose
            private String reportingAllow;
            @SerializedName("isConsolidateTeamData")
            @Expose
            private Boolean isConsolidateTeamData;
            @SerializedName("isTPMandatory")
            @Expose
            private Boolean isTPMandatory;
            @SerializedName("isAllowBackDate")
            @Expose
            private Boolean isAllowBackDate;
            @SerializedName("isRestrictedParty")
            @Expose
            private Boolean isRestrictedParty;
            @SerializedName("minCallPerDayDoc")
            @Expose
            private Integer minCallPerDayDoc;
            @SerializedName("minCallPerDayChem")
            @Expose
            private Integer minCallPerDayChem;
            @SerializedName("isRCPAMandatoryForChemistReport")
            @Expose
            private Boolean isRCPAMandatoryForChemistReport;
            @SerializedName("isSTPMandatory")
            @Expose
            private Boolean isSTPMandatory;
            @SerializedName("isProductReportingMandatory")
            @Expose
            private Boolean isProductReportingMandatory;
            @SerializedName("tpSubmission")
            @Expose
            private Integer tpSubmission;
            @SerializedName("planCompliancePercentage")
            @Expose
            private Double planCompliancePercentage;
            @SerializedName("needDCRExpApp")
            @Expose
            private Boolean needDCRExpApp;
            @SerializedName("callPlanBackDays")
            @Expose
            private Integer callPlanBackDays;
            @SerializedName("isCallPlanMandatoryForDCR")
            @Expose
            private Boolean isCallPlanMandatoryForDCR;
            @SerializedName("isSelfieAttendanceRequired")
            @Expose
            private Boolean isSelfieAttendanceRequired;
            @SerializedName("isSampleReportingMandatory")
            @Expose
            private Boolean isSampleReportingMandatory;
            @SerializedName("isGiftReportingMandatory")
            @Expose
            private Boolean isGiftReportingMandatory;
            @SerializedName("minInclinicEffectivenessCall")
            @Expose
            private Integer minInclinicEffectivenessCall;
            @SerializedName("isBlockDCRAfterTPDeadline")
            @Expose
            private Boolean isBlockDCRAfterTPDeadline;
            @SerializedName("allowNegativeSampleAndGiftDist")
            @Expose
            private Boolean allowNegativeSampleAndGiftDist;
            @SerializedName("minChemistRCPA")
            @Expose
            private Integer minChemistRCPA;

            public Integer getSettingId() {
                return settingId;
            }

            public void setSettingId(Integer settingId) {
                this.settingId = settingId;
            }

            public String getRoleType() {
                return roleType;
            }

            public void setRoleType(String roleType) {
                this.roleType = roleType;
            }

            public Boolean getSequentialDCR() {
                return sequentialDCR;
            }

            public void setSequentialDCR(Boolean sequentialDCR) {
                this.sequentialDCR = sequentialDCR;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getHqType() {
                return hqType;
            }

            public void setHqType(Integer hqType) {
                this.hqType = hqType;
            }

            public Integer getAllowBackDate() {
                return allowBackDate;
            }

            public void setAllowBackDate(Integer allowBackDate) {
                this.allowBackDate = allowBackDate;
            }

            public Boolean getIsRouteDeviationApproval() {
                return isRouteDeviationApproval;
            }

            public void setIsRouteDeviationApproval(Boolean isRouteDeviationApproval) {
                this.isRouteDeviationApproval = isRouteDeviationApproval;
            }

            public Boolean getIsDoctorFencingRequired() {
                return isDoctorFencingRequired;
            }

            public void setIsDoctorFencingRequired(Boolean isDoctorFencingRequired) {
                this.isDoctorFencingRequired = isDoctorFencingRequired;
            }

            public Boolean getIsRetailerFencingRequired() {
                return isRetailerFencingRequired;
            }

            public void setIsRetailerFencingRequired(Boolean isRetailerFencingRequired) {
                this.isRetailerFencingRequired = isRetailerFencingRequired;
            }

            public String getRoleTypeName() {
                return roleTypeName;
            }

            public void setRoleTypeName(String roleTypeName) {
                this.roleTypeName = roleTypeName;
            }

            public Object getHqTypeName() {
                return hqTypeName;
            }

            public void setHqTypeName(Object hqTypeName) {
                this.hqTypeName = hqTypeName;
            }

            public Boolean getIsDCRApproval() {
                return isDCRApproval;
            }

            public void setIsDCRApproval(Boolean isDCRApproval) {
                this.isDCRApproval = isDCRApproval;
            }

            public Boolean getIsGeoLocationRequired() {
                return isGeoLocationRequired;
            }

            public void setIsGeoLocationRequired(Boolean isGeoLocationRequired) {
                this.isGeoLocationRequired = isGeoLocationRequired;
            }

            public String getReportingAllow() {
                return reportingAllow;
            }

            public void setReportingAllow(String reportingAllow) {
                this.reportingAllow = reportingAllow;
            }

            public Boolean getIsConsolidateTeamData() {
                return isConsolidateTeamData;
            }

            public void setIsConsolidateTeamData(Boolean isConsolidateTeamData) {
                this.isConsolidateTeamData = isConsolidateTeamData;
            }

            public Boolean getIsTPMandatory() {
                return isTPMandatory;
            }

            public void setIsTPMandatory(Boolean isTPMandatory) {
                this.isTPMandatory = isTPMandatory;
            }

            public Boolean getIsAllowBackDate() {
                return isAllowBackDate;
            }

            public void setIsAllowBackDate(Boolean isAllowBackDate) {
                this.isAllowBackDate = isAllowBackDate;
            }

            public Boolean getIsRestrictedParty() {
                return isRestrictedParty;
            }

            public void setIsRestrictedParty(Boolean isRestrictedParty) {
                this.isRestrictedParty = isRestrictedParty;
            }

            public Integer getMinCallPerDayDoc() {
                return minCallPerDayDoc;
            }

            public void setMinCallPerDayDoc(Integer minCallPerDayDoc) {
                this.minCallPerDayDoc = minCallPerDayDoc;
            }

            public Integer getMinCallPerDayChem() {
                return minCallPerDayChem;
            }

            public void setMinCallPerDayChem(Integer minCallPerDayChem) {
                this.minCallPerDayChem = minCallPerDayChem;
            }

            public Boolean getIsRCPAMandatoryForChemistReport() {
                return isRCPAMandatoryForChemistReport;
            }

            public void setIsRCPAMandatoryForChemistReport(Boolean isRCPAMandatoryForChemistReport) {
                this.isRCPAMandatoryForChemistReport = isRCPAMandatoryForChemistReport;
            }

            public Boolean getIsSTPMandatory() {
                return isSTPMandatory;
            }

            public void setIsSTPMandatory(Boolean isSTPMandatory) {
                this.isSTPMandatory = isSTPMandatory;
            }

            public Boolean getIsProductReportingMandatory() {
                return isProductReportingMandatory;
            }

            public void setIsProductReportingMandatory(Boolean isProductReportingMandatory) {
                this.isProductReportingMandatory = isProductReportingMandatory;
            }

            public Integer getTpSubmission() {
                return tpSubmission;
            }

            public void setTpSubmission(Integer tpSubmission) {
                this.tpSubmission = tpSubmission;
            }

            public Double getPlanCompliancePercentage() {
                return planCompliancePercentage;
            }

            public void setPlanCompliancePercentage(Double planCompliancePercentage) {
                this.planCompliancePercentage = planCompliancePercentage;
            }

            public Boolean getNeedDCRExpApp() {
                return needDCRExpApp;
            }

            public void setNeedDCRExpApp(Boolean needDCRExpApp) {
                this.needDCRExpApp = needDCRExpApp;
            }

            public Integer getCallPlanBackDays() {
                return callPlanBackDays;
            }

            public void setCallPlanBackDays(Integer callPlanBackDays) {
                this.callPlanBackDays = callPlanBackDays;
            }

            public Boolean getIsCallPlanMandatoryForDCR() {
                return isCallPlanMandatoryForDCR;
            }

            public void setIsCallPlanMandatoryForDCR(Boolean isCallPlanMandatoryForDCR) {
                this.isCallPlanMandatoryForDCR = isCallPlanMandatoryForDCR;
            }

            public Boolean getIsSelfieAttendanceRequired() {
                return isSelfieAttendanceRequired;
            }

            public void setIsSelfieAttendanceRequired(Boolean isSelfieAttendanceRequired) {
                this.isSelfieAttendanceRequired = isSelfieAttendanceRequired;
            }

            public Boolean getIsSampleReportingMandatory() {
                return isSampleReportingMandatory;
            }

            public void setIsSampleReportingMandatory(Boolean isSampleReportingMandatory) {
                this.isSampleReportingMandatory = isSampleReportingMandatory;
            }

            public Boolean getIsGiftReportingMandatory() {
                return isGiftReportingMandatory;
            }

            public void setIsGiftReportingMandatory(Boolean isGiftReportingMandatory) {
                this.isGiftReportingMandatory = isGiftReportingMandatory;
            }

            public Integer getMinInclinicEffectivenessCall() {
                return minInclinicEffectivenessCall;
            }

            public void setMinInclinicEffectivenessCall(Integer minInclinicEffectivenessCall) {
                this.minInclinicEffectivenessCall = minInclinicEffectivenessCall;
            }

            public Boolean getIsBlockDCRAfterTPDeadline() {
                return isBlockDCRAfterTPDeadline;
            }

            public void setIsBlockDCRAfterTPDeadline(Boolean isBlockDCRAfterTPDeadline) {
                this.isBlockDCRAfterTPDeadline = isBlockDCRAfterTPDeadline;
            }

            public Boolean getAllowNegativeSampleAndGiftDist() {
                return allowNegativeSampleAndGiftDist;
            }

            public void setAllowNegativeSampleAndGiftDist(Boolean allowNegativeSampleAndGiftDist) {
                this.allowNegativeSampleAndGiftDist = allowNegativeSampleAndGiftDist;
            }

            public Integer getMinChemistRCPA() {
                return minChemistRCPA;
            }

            public void setMinChemistRCPA(Integer minChemistRCPA) {
                this.minChemistRCPA = minChemistRCPA;
            }

        }
        public class HeadQuarter {

            @SerializedName("headQuaterId")
            @Expose
            private Integer headQuaterId;
            @SerializedName("headQuaterName")
            @Expose
            private String headQuaterName;
            @SerializedName("headQuaterCode")
            @Expose
            private String headQuaterCode;
            @SerializedName("contactPerson")
            @Expose
            private String contactPerson;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("cityId")
            @Expose
            private Integer cityId;
            @SerializedName("pinCode")
            @Expose
            private String pinCode;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("fax")
            @Expose
            private String fax;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("active")
            @Expose
            private Boolean active;
            @SerializedName("stateName")
            @Expose
            private String stateName;
            @SerializedName("countryName")
            @Expose
            private Object countryName;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("headQuaterType")
            @Expose
            private Integer headQuaterType;
            @SerializedName("cityName")
            @Expose
            private String cityName;
            @SerializedName("optSelect")
            @Expose
            private Boolean optSelect;
            @SerializedName("preHeadQuaterCode")
            @Expose
            private Object preHeadQuaterCode;
            @SerializedName("headQuaterTypeName")
            @Expose
            private Object headQuaterTypeName;
            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("preHeadQuaterName")
            @Expose
            private Object preHeadQuaterName;
            @SerializedName("specialityWiseCount")
            @Expose
            private SpecialityWiseCount specialityWiseCount;
            @SerializedName("totalCount")
            @Expose
            private Integer totalCount;
            @SerializedName("isTotalRow")
            @Expose
            private Boolean isTotalRow;
            @SerializedName("regionId")
            @Expose
            private Object regionId;
            @SerializedName("regionName")
            @Expose
            private Object regionName;
            @SerializedName("zoneName")
            @Expose
            private Object zoneName;
            @SerializedName("zoneId")
            @Expose
            private Integer zoneId;
            @SerializedName("zoneCode")
            @Expose
            private Object zoneCode;
            @SerializedName("regionCode")
            @Expose
            private Object regionCode;

            public Integer getHeadQuaterId() {
                return headQuaterId;
            }

            public void setHeadQuaterId(Integer headQuaterId) {
                this.headQuaterId = headQuaterId;
            }

            public String getHeadQuaterName() {
                return headQuaterName;
            }

            public void setHeadQuaterName(String headQuaterName) {
                this.headQuaterName = headQuaterName;
            }

            public String getHeadQuaterCode() {
                return headQuaterCode;
            }

            public void setHeadQuaterCode(String headQuaterCode) {
                this.headQuaterCode = headQuaterCode;
            }

            public String getContactPerson() {
                return contactPerson;
            }

            public void setContactPerson(String contactPerson) {
                this.contactPerson = contactPerson;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public Integer getCityId() {
                return cityId;
            }

            public void setCityId(Integer cityId) {
                this.cityId = cityId;
            }

            public String getPinCode() {
                return pinCode;
            }

            public void setPinCode(String pinCode) {
                this.pinCode = pinCode;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getFax() {
                return fax;
            }

            public void setFax(String fax) {
                this.fax = fax;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public String getStateName() {
                return stateName;
            }

            public void setStateName(String stateName) {
                this.stateName = stateName;
            }

            public Object getCountryName() {
                return countryName;
            }

            public void setCountryName(Object countryName) {
                this.countryName = countryName;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Integer getHeadQuaterType() {
                return headQuaterType;
            }

            public void setHeadQuaterType(Integer headQuaterType) {
                this.headQuaterType = headQuaterType;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public Boolean getOptSelect() {
                return optSelect;
            }

            public void setOptSelect(Boolean optSelect) {
                this.optSelect = optSelect;
            }

            public Object getPreHeadQuaterCode() {
                return preHeadQuaterCode;
            }

            public void setPreHeadQuaterCode(Object preHeadQuaterCode) {
                this.preHeadQuaterCode = preHeadQuaterCode;
            }

            public Object getHeadQuaterTypeName() {
                return headQuaterTypeName;
            }

            public void setHeadQuaterTypeName(Object headQuaterTypeName) {
                this.headQuaterTypeName = headQuaterTypeName;
            }

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Object getPreHeadQuaterName() {
                return preHeadQuaterName;
            }

            public void setPreHeadQuaterName(Object preHeadQuaterName) {
                this.preHeadQuaterName = preHeadQuaterName;
            }

            public SpecialityWiseCount getSpecialityWiseCount() {
                return specialityWiseCount;
            }

            public void setSpecialityWiseCount(SpecialityWiseCount specialityWiseCount) {
                this.specialityWiseCount = specialityWiseCount;
            }

            public Integer getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(Integer totalCount) {
                this.totalCount = totalCount;
            }

            public Boolean getIsTotalRow() {
                return isTotalRow;
            }

            public void setIsTotalRow(Boolean isTotalRow) {
                this.isTotalRow = isTotalRow;
            }

            public Object getRegionId() {
                return regionId;
            }

            public void setRegionId(Object regionId) {
                this.regionId = regionId;
            }

            public Object getRegionName() {
                return regionName;
            }

            public void setRegionName(Object regionName) {
                this.regionName = regionName;
            }

            public Object getZoneName() {
                return zoneName;
            }

            public void setZoneName(Object zoneName) {
                this.zoneName = zoneName;
            }

            public Integer getZoneId() {
                return zoneId;
            }

            public void setZoneId(Integer zoneId) {
                this.zoneId = zoneId;
            }

            public Object getZoneCode() {
                return zoneCode;
            }

            public void setZoneCode(Object zoneCode) {
                this.zoneCode = zoneCode;
            }

            public Object getRegionCode() {
                return regionCode;
            }

            public void setRegionCode(Object regionCode) {
                this.regionCode = regionCode;
            }
            public class SpecialityWiseCount {


            }

        }
        public class Route {

            @SerializedName("routeId")
            @Expose
            private Integer routeId;
            @SerializedName("routeName")
            @Expose
            private String routeName;
            @SerializedName("routeType")
            @Expose
            private Integer routeType;
            @SerializedName("distanceFrom")
            @Expose
            private Double distanceFrom;
            @SerializedName("distanceTo")
            @Expose
            private Double distanceTo;
            @SerializedName("headQuaterId")
            @Expose
            private Integer headQuaterId;
            @SerializedName("remark")
            @Expose
            private String remark;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("headQuaterName")
            @Expose
            private String headQuaterName;
            @SerializedName("routeTypeName")
            @Expose
            private String routeTypeName;
            @SerializedName("preRouteName")
            @Expose
            private String preRouteName;
            @SerializedName("totalDistance")
            @Expose
            private Double totalDistance;
            @SerializedName("hqTypeId")
            @Expose
            private Integer hqTypeId;
            @SerializedName("hqTypeName")
            @Expose
            private String hqTypeName;
            @SerializedName("cityId")
            @Expose
            private Integer cityId;
            @SerializedName("cityName")
            @Expose
            private String cityName;
            @SerializedName("stateName")
            @Expose
            private String stateName;
            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("doctorCount")
            @Expose
            private Integer doctorCount;
            @SerializedName("retailerCount")
            @Expose
            private Integer retailerCount;
            @SerializedName("allowExpense")
            @Expose
            private Boolean allowExpense;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("errorCode")
            @Expose
            private Object errorCode;
            @SerializedName("shortName")
            @Expose
            private Object shortName;
            @SerializedName("specialityWiseCount")
            @Expose
            private SpecialityWiseCount__1 specialityWiseCount;
            @SerializedName("totalCount")
            @Expose
            private Integer totalCount;
            @SerializedName("specialityName")
            @Expose
            private Object specialityName;
            @SerializedName("docCount")
            @Expose
            private Integer docCount;
            @SerializedName("isTotalRow")
            @Expose
            private Boolean isTotalRow;
            @SerializedName("speciality")
            @Expose
            private Integer speciality;

            public Integer getRouteId() {
                return routeId;
            }

            public void setRouteId(Integer routeId) {
                this.routeId = routeId;
            }

            public String getRouteName() {
                return routeName;
            }

            public void setRouteName(String routeName) {
                this.routeName = routeName;
            }

            public Integer getRouteType() {
                return routeType;
            }

            public void setRouteType(Integer routeType) {
                this.routeType = routeType;
            }

            public Double getDistanceFrom() {
                return distanceFrom;
            }

            public void setDistanceFrom(Double distanceFrom) {
                this.distanceFrom = distanceFrom;
            }

            public Double getDistanceTo() {
                return distanceTo;
            }

            public void setDistanceTo(Double distanceTo) {
                this.distanceTo = distanceTo;
            }

            public Integer getHeadQuaterId() {
                return headQuaterId;
            }

            public void setHeadQuaterId(Integer headQuaterId) {
                this.headQuaterId = headQuaterId;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public String getHeadQuaterName() {
                return headQuaterName;
            }

            public void setHeadQuaterName(String headQuaterName) {
                this.headQuaterName = headQuaterName;
            }

            public String getRouteTypeName() {
                return routeTypeName;
            }

            public void setRouteTypeName(String routeTypeName) {
                this.routeTypeName = routeTypeName;
            }

            public String getPreRouteName() {
                return preRouteName;
            }

            public void setPreRouteName(String preRouteName) {
                this.preRouteName = preRouteName;
            }

            public Double getTotalDistance() {
                return totalDistance;
            }

            public void setTotalDistance(Double totalDistance) {
                this.totalDistance = totalDistance;
            }

            public Integer getHqTypeId() {
                return hqTypeId;
            }

            public void setHqTypeId(Integer hqTypeId) {
                this.hqTypeId = hqTypeId;
            }

            public String getHqTypeName() {
                return hqTypeName;
            }

            public void setHqTypeName(String hqTypeName) {
                this.hqTypeName = hqTypeName;
            }

            public Integer getCityId() {
                return cityId;
            }

            public void setCityId(Integer cityId) {
                this.cityId = cityId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getStateName() {
                return stateName;
            }

            public void setStateName(String stateName) {
                this.stateName = stateName;
            }

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Integer getDoctorCount() {
                return doctorCount;
            }

            public void setDoctorCount(Integer doctorCount) {
                this.doctorCount = doctorCount;
            }

            public Integer getRetailerCount() {
                return retailerCount;
            }

            public void setRetailerCount(Integer retailerCount) {
                this.retailerCount = retailerCount;
            }

            public Boolean getAllowExpense() {
                return allowExpense;
            }

            public void setAllowExpense(Boolean allowExpense) {
                this.allowExpense = allowExpense;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Object getErrorCode() {
                return errorCode;
            }

            public void setErrorCode(Object errorCode) {
                this.errorCode = errorCode;
            }

            public Object getShortName() {
                return shortName;
            }

            public void setShortName(Object shortName) {
                this.shortName = shortName;
            }

            public SpecialityWiseCount__1 getSpecialityWiseCount() {
                return specialityWiseCount;
            }

            public void setSpecialityWiseCount(SpecialityWiseCount__1 specialityWiseCount) {
                this.specialityWiseCount = specialityWiseCount;
            }

            public Integer getTotalCount() {
                return totalCount;
            }

            public void setTotalCount(Integer totalCount) {
                this.totalCount = totalCount;
            }

            public Object getSpecialityName() {
                return specialityName;
            }

            public void setSpecialityName(Object specialityName) {
                this.specialityName = specialityName;
            }

            public Integer getDocCount() {
                return docCount;
            }

            public void setDocCount(Integer docCount) {
                this.docCount = docCount;
            }

            public Boolean getIsTotalRow() {
                return isTotalRow;
            }

            public void setIsTotalRow(Boolean isTotalRow) {
                this.isTotalRow = isTotalRow;
            }

            public Integer getSpeciality() {
                return speciality;
            }

            public void setSpeciality(Integer speciality) {
                this.speciality = speciality;
            }

            public class SpecialityWiseCount__1 {


            }
        }
        public class Specialist {

            @SerializedName("specialistId")
            @Expose
            private Integer specialistId;
            @SerializedName("specialistName")
            @Expose
            private String specialistName;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("preSpecialistName")
            @Expose
            private String preSpecialistName;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("isError")
            @Expose
            private Boolean isError;

            public Integer getSpecialistId() {
                return specialistId;
            }

            public void setSpecialistId(Integer specialistId) {
                this.specialistId = specialistId;
            }

            public String getSpecialistName() {
                return specialistName;
            }

            public void setSpecialistName(String specialistName) {
                this.specialistName = specialistName;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public String getPreSpecialistName() {
                return preSpecialistName;
            }

            public void setPreSpecialistName(String preSpecialistName) {
                this.preSpecialistName = preSpecialistName;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

        }
        public class Qualification {

            @SerializedName("qualificationId")
            @Expose
            private Integer qualificationId;
            @SerializedName("qualificationName")
            @Expose
            private String qualificationName;
            @SerializedName("qualificationCode")
            @Expose
            private String qualificationCode;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("preQualificationCode")
            @Expose
            private String preQualificationCode;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("isError")
            @Expose
            private Boolean isError;

            public Integer getQualificationId() {
                return qualificationId;
            }

            public void setQualificationId(Integer qualificationId) {
                this.qualificationId = qualificationId;
            }

            public String getQualificationName() {
                return qualificationName;
            }

            public void setQualificationName(String qualificationName) {
                this.qualificationName = qualificationName;
            }

            public String getQualificationCode() {
                return qualificationCode;
            }

            public void setQualificationCode(String qualificationCode) {
                this.qualificationCode = qualificationCode;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public String getPreQualificationCode() {
                return preQualificationCode;
            }

            public void setPreQualificationCode(String preQualificationCode) {
                this.preQualificationCode = preQualificationCode;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

        }
        public class City {

            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("cityId")
            @Expose
            private Integer cityId;
            @SerializedName("cityName")
            @Expose
            private String cityName;
            @SerializedName("stdCode")
            @Expose
            private String stdCode;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("stateName")
            @Expose
            private String stateName;
            @SerializedName("countryName")
            @Expose
            private String countryName;
            @SerializedName("countryId")
            @Expose
            private Integer countryId;
            @SerializedName("preCityName")
            @Expose
            private String preCityName;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Integer getCityId() {
                return cityId;
            }

            public void setCityId(Integer cityId) {
                this.cityId = cityId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getStdCode() {
                return stdCode;
            }

            public void setStdCode(String stdCode) {
                this.stdCode = stdCode;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public String getStateName() {
                return stateName;
            }

            public void setStateName(String stateName) {
                this.stateName = stateName;
            }

            public String getCountryName() {
                return countryName;
            }

            public void setCountryName(String countryName) {
                this.countryName = countryName;
            }

            public Integer getCountryId() {
                return countryId;
            }

            public void setCountryId(Integer countryId) {
                this.countryId = countryId;
            }

            public String getPreCityName() {
                return preCityName;
            }

            public void setPreCityName(String preCityName) {
                this.preCityName = preCityName;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

        }
        public class Transport {

            @SerializedName("transportId")
            @Expose
            private Integer transportId;
            @SerializedName("transportName")
            @Expose
            private String transportName;

            public Integer getTransportId() {
                return transportId;
            }

            public void setTransportId(Integer transportId) {
                this.transportId = transportId;
            }

            public String getTransportName() {
                return transportName;
            }

            public void setTransportName(String transportName) {
                this.transportName = transportName;
            }

        }
        public class WorkType {

            @SerializedName("workId")
            @Expose
            private Integer workId;
            @SerializedName("workType")
            @Expose
            private String workType;

            public Integer getWorkId() {
                return workId;
            }

            public void setWorkId(Integer workId) {
                this.workId = workId;
            }

            public String getWorkType() {
                return workType;
            }

            public void setWorkType(String workType) {
                this.workType = workType;
            }

        }
        public class Retailer {

            @SerializedName("retailerId")
            @Expose
            private Integer retailerId;
            @SerializedName("shopName")
            @Expose
            private String shopName;
            @SerializedName("contactPerson")
            @Expose
            private String contactPerson;
            @SerializedName("address1")
            @Expose
            private String address1;
            @SerializedName("address2")
            @Expose
            private String address2;
            @SerializedName("routeId")
            @Expose
            private Integer routeId;
            @SerializedName("cityId")
            @Expose
            private Integer cityId;
            @SerializedName("pincode")
            @Expose
            private String pincode;
            @SerializedName("phoneNo")
            @Expose
            private String phoneNo;
            @SerializedName("mobileNo")
            @Expose
            private String mobileNo;
            @SerializedName("emailId")
            @Expose
            private String emailId;
            @SerializedName("isHospitalChemist")
            @Expose
            private Boolean isHospitalChemist;
            @SerializedName("hospitalType")
            @Expose
            private Integer hospitalType;
            @SerializedName("hospitalName")
            @Expose
            private String hospitalName;
            @SerializedName("drugLic1")
            @Expose
            private String drugLic1;
            @SerializedName("drugLic2")
            @Expose
            private String drugLic2;
            @SerializedName("visitDay")
            @Expose
            private Integer visitDay;
            @SerializedName("visitTime")
            @Expose
            private String visitTime;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("headQuaterId")
            @Expose
            private Integer headQuaterId;
            @SerializedName("preShopName")
            @Expose
            private Object preShopName;
            @SerializedName("cityName")
            @Expose
            private String cityName;
            @SerializedName("stateName")
            @Expose
            private Object stateName;
            @SerializedName("countryName")
            @Expose
            private Object countryName;
            @SerializedName("headQuaterName")
            @Expose
            private String headQuaterName;
            @SerializedName("routeName")
            @Expose
            private String routeName;
            @SerializedName("gstinNo")
            @Expose
            private String gstinNo;
            @SerializedName("approveStatus")
            @Expose
            private Object approveStatus;
            @SerializedName("approveStatusName")
            @Expose
            private String approveStatusName;
            @SerializedName("approveBy")
            @Expose
            private Integer approveBy;
            @SerializedName("rejectReason")
            @Expose
            private String rejectReason;
            @SerializedName("active")
            @Expose
            private Boolean active;
            @SerializedName("approveByName")
            @Expose
            private Object approveByName;
            @SerializedName("createdBy")
            @Expose
            private Object createdBy;
            @SerializedName("approveDate")
            @Expose
            private String approveDate;
            @SerializedName("strApproveDate")
            @Expose
            private Object strApproveDate;
            @SerializedName("createdDate")
            @Expose
            private String createdDate;
            @SerializedName("strCreatedDate")
            @Expose
            private Object strCreatedDate;
            @SerializedName("statusType")
            @Expose
            private Object statusType;
            @SerializedName("strVisitTime")
            @Expose
            private Object strVisitTime;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("employeeManagerId")
            @Expose
            private Integer employeeManagerId;
            @SerializedName("userType")
            @Expose
            private Object userType;
            @SerializedName("canBeEdited")
            @Expose
            private Boolean canBeEdited;
            @SerializedName("employeeName")
            @Expose
            private Object employeeName;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("latitude")
            @Expose
            private Double latitude;
            @SerializedName("longitude")
            @Expose
            private Double longitude;
            @SerializedName("isImageAdd")
            @Expose
            private Boolean isImageAdd;
            @SerializedName("imageName")
            @Expose
            private Object imageName;
            @SerializedName("imagePath")
            @Expose
            private Object imagePath;
            @SerializedName("imageExt")
            @Expose
            private Object imageExt;
            @SerializedName("urlPath")
            @Expose
            private Object urlPath;
            @SerializedName("savedFileName")
            @Expose
            private Object savedFileName;
            @SerializedName("preImageName")
            @Expose
            private Object preImageName;
            @SerializedName("entryDate")
            @Expose
            private String entryDate;
            @SerializedName("strEntryDate")
            @Expose
            private Object strEntryDate;
            @SerializedName("status")
            @Expose
            private Object status;
            @SerializedName("deactivateApproveBy")
            @Expose
            private Object deactivateApproveBy;
            @SerializedName("deactivateRejectReason")
            @Expose
            private Object deactivateRejectReason;
            @SerializedName("deactivateRequestDate")
            @Expose
            private String deactivateRequestDate;
            @SerializedName("strDeactivateRequestDate")
            @Expose
            private Object strDeactivateRequestDate;
            @SerializedName("deactivateApproveStatus")
            @Expose
            private Object deactivateApproveStatus;
            @SerializedName("deactivateStatusName")
            @Expose
            private String deactivateStatusName;
            @SerializedName("deactivateRemark")
            @Expose
            private Object deactivateRemark;
            @SerializedName("requestId")
            @Expose
            private Object requestId;
            @SerializedName("linkedEmployeeList")
            @Expose
            private ArrayList<Object> linkedEmployeeList = null;
            @SerializedName("hierDesc")
            @Expose
            private Object hierDesc;
            @SerializedName("empHierarchyType")
            @Expose
            private Object empHierarchyType;
            @SerializedName("empGender")
            @Expose
            private Integer empGender;
            @SerializedName("empName")
            @Expose
            private Object empName;
            @SerializedName("division")
            @Expose
            private Object division;
            @SerializedName("designation")
            @Expose
            private Object designation;
            @SerializedName("errorCode")
            @Expose
            private Object errorCode;

            public Integer getRetailerId() {
                return retailerId;
            }

            public void setRetailerId(Integer retailerId) {
                this.retailerId = retailerId;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getContactPerson() {
                return contactPerson;
            }

            public void setContactPerson(String contactPerson) {
                this.contactPerson = contactPerson;
            }

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getAddress2() {
                return address2;
            }

            public void setAddress2(String address2) {
                this.address2 = address2;
            }

            public Integer getRouteId() {
                return routeId;
            }

            public void setRouteId(Integer routeId) {
                this.routeId = routeId;
            }

            public Integer getCityId() {
                return cityId;
            }

            public void setCityId(Integer cityId) {
                this.cityId = cityId;
            }

            public String getPincode() {
                return pincode;
            }

            public void setPincode(String pincode) {
                this.pincode = pincode;
            }

            public String getPhoneNo() {
                return phoneNo;
            }

            public void setPhoneNo(String phoneNo) {
                this.phoneNo = phoneNo;
            }

            public String getMobileNo() {
                return mobileNo;
            }

            public void setMobileNo(String mobileNo) {
                this.mobileNo = mobileNo;
            }

            public String getEmailId() {
                return emailId;
            }

            public void setEmailId(String emailId) {
                this.emailId = emailId;
            }

            public Boolean getIsHospitalChemist() {
                return isHospitalChemist;
            }

            public void setIsHospitalChemist(Boolean isHospitalChemist) {
                this.isHospitalChemist = isHospitalChemist;
            }

            public Integer getHospitalType() {
                return hospitalType;
            }

            public void setHospitalType(Integer hospitalType) {
                this.hospitalType = hospitalType;
            }

            public String getHospitalName() {
                return hospitalName;
            }

            public void setHospitalName(String hospitalName) {
                this.hospitalName = hospitalName;
            }

            public String getDrugLic1() {
                return drugLic1;
            }

            public void setDrugLic1(String drugLic1) {
                this.drugLic1 = drugLic1;
            }

            public String getDrugLic2() {
                return drugLic2;
            }

            public void setDrugLic2(String drugLic2) {
                this.drugLic2 = drugLic2;
            }

            public Integer getVisitDay() {
                return visitDay;
            }

            public void setVisitDay(Integer visitDay) {
                this.visitDay = visitDay;
            }

            public String getVisitTime() {
                return visitTime;
            }

            public void setVisitTime(String visitTime) {
                this.visitTime = visitTime;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Integer getHeadQuaterId() {
                return headQuaterId;
            }

            public void setHeadQuaterId(Integer headQuaterId) {
                this.headQuaterId = headQuaterId;
            }

            public Object getPreShopName() {
                return preShopName;
            }

            public void setPreShopName(Object preShopName) {
                this.preShopName = preShopName;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public Object getStateName() {
                return stateName;
            }

            public void setStateName(Object stateName) {
                this.stateName = stateName;
            }

            public Object getCountryName() {
                return countryName;
            }

            public void setCountryName(Object countryName) {
                this.countryName = countryName;
            }

            public String getHeadQuaterName() {
                return headQuaterName;
            }

            public void setHeadQuaterName(String headQuaterName) {
                this.headQuaterName = headQuaterName;
            }

            public String getRouteName() {
                return routeName;
            }

            public void setRouteName(String routeName) {
                this.routeName = routeName;
            }

            public String getGstinNo() {
                return gstinNo;
            }

            public void setGstinNo(String gstinNo) {
                this.gstinNo = gstinNo;
            }

            public Object getApproveStatus() {
                return approveStatus;
            }

            public void setApproveStatus(Object approveStatus) {
                this.approveStatus = approveStatus;
            }

            public String getApproveStatusName() {
                return approveStatusName;
            }

            public void setApproveStatusName(String approveStatusName) {
                this.approveStatusName = approveStatusName;
            }

            public Integer getApproveBy() {
                return approveBy;
            }

            public void setApproveBy(Integer approveBy) {
                this.approveBy = approveBy;
            }

            public String getRejectReason() {
                return rejectReason;
            }

            public void setRejectReason(String rejectReason) {
                this.rejectReason = rejectReason;
            }

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public Object getApproveByName() {
                return approveByName;
            }

            public void setApproveByName(Object approveByName) {
                this.approveByName = approveByName;
            }

            public Object getCreatedBy() {
                return createdBy;
            }

            public void setCreatedBy(Object createdBy) {
                this.createdBy = createdBy;
            }

            public String getApproveDate() {
                return approveDate;
            }

            public void setApproveDate(String approveDate) {
                this.approveDate = approveDate;
            }

            public Object getStrApproveDate() {
                return strApproveDate;
            }

            public void setStrApproveDate(Object strApproveDate) {
                this.strApproveDate = strApproveDate;
            }

            public String getCreatedDate() {
                return createdDate;
            }

            public void setCreatedDate(String createdDate) {
                this.createdDate = createdDate;
            }

            public Object getStrCreatedDate() {
                return strCreatedDate;
            }

            public void setStrCreatedDate(Object strCreatedDate) {
                this.strCreatedDate = strCreatedDate;
            }

            public Object getStatusType() {
                return statusType;
            }

            public void setStatusType(Object statusType) {
                this.statusType = statusType;
            }

            public Object getStrVisitTime() {
                return strVisitTime;
            }

            public void setStrVisitTime(Object strVisitTime) {
                this.strVisitTime = strVisitTime;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Integer getEmployeeManagerId() {
                return employeeManagerId;
            }

            public void setEmployeeManagerId(Integer employeeManagerId) {
                this.employeeManagerId = employeeManagerId;
            }

            public Object getUserType() {
                return userType;
            }

            public void setUserType(Object userType) {
                this.userType = userType;
            }

            public Boolean getCanBeEdited() {
                return canBeEdited;
            }

            public void setCanBeEdited(Boolean canBeEdited) {
                this.canBeEdited = canBeEdited;
            }

            public Object getEmployeeName() {
                return employeeName;
            }

            public void setEmployeeName(Object employeeName) {
                this.employeeName = employeeName;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }

            public Boolean getIsImageAdd() {
                return isImageAdd;
            }

            public void setIsImageAdd(Boolean isImageAdd) {
                this.isImageAdd = isImageAdd;
            }

            public Object getImageName() {
                return imageName;
            }

            public void setImageName(Object imageName) {
                this.imageName = imageName;
            }

            public Object getImagePath() {
                return imagePath;
            }

            public void setImagePath(Object imagePath) {
                this.imagePath = imagePath;
            }

            public Object getImageExt() {
                return imageExt;
            }

            public void setImageExt(Object imageExt) {
                this.imageExt = imageExt;
            }

            public Object getUrlPath() {
                return urlPath;
            }

            public void setUrlPath(Object urlPath) {
                this.urlPath = urlPath;
            }

            public Object getSavedFileName() {
                return savedFileName;
            }

            public void setSavedFileName(Object savedFileName) {
                this.savedFileName = savedFileName;
            }

            public Object getPreImageName() {
                return preImageName;
            }

            public void setPreImageName(Object preImageName) {
                this.preImageName = preImageName;
            }

            public String getEntryDate() {
                return entryDate;
            }

            public void setEntryDate(String entryDate) {
                this.entryDate = entryDate;
            }

            public Object getStrEntryDate() {
                return strEntryDate;
            }

            public void setStrEntryDate(Object strEntryDate) {
                this.strEntryDate = strEntryDate;
            }

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public Object getDeactivateApproveBy() {
                return deactivateApproveBy;
            }

            public void setDeactivateApproveBy(Object deactivateApproveBy) {
                this.deactivateApproveBy = deactivateApproveBy;
            }

            public Object getDeactivateRejectReason() {
                return deactivateRejectReason;
            }

            public void setDeactivateRejectReason(Object deactivateRejectReason) {
                this.deactivateRejectReason = deactivateRejectReason;
            }

            public String getDeactivateRequestDate() {
                return deactivateRequestDate;
            }

            public void setDeactivateRequestDate(String deactivateRequestDate) {
                this.deactivateRequestDate = deactivateRequestDate;
            }

            public Object getStrDeactivateRequestDate() {
                return strDeactivateRequestDate;
            }

            public void setStrDeactivateRequestDate(Object strDeactivateRequestDate) {
                this.strDeactivateRequestDate = strDeactivateRequestDate;
            }

            public Object getDeactivateApproveStatus() {
                return deactivateApproveStatus;
            }

            public void setDeactivateApproveStatus(Object deactivateApproveStatus) {
                this.deactivateApproveStatus = deactivateApproveStatus;
            }

            public String getDeactivateStatusName() {
                return deactivateStatusName;
            }

            public void setDeactivateStatusName(String deactivateStatusName) {
                this.deactivateStatusName = deactivateStatusName;
            }

            public Object getDeactivateRemark() {
                return deactivateRemark;
            }

            public void setDeactivateRemark(Object deactivateRemark) {
                this.deactivateRemark = deactivateRemark;
            }

            public Object getRequestId() {
                return requestId;
            }

            public void setRequestId(Object requestId) {
                this.requestId = requestId;
            }

            public ArrayList<Object> getLinkedEmployeeList() {
                return linkedEmployeeList;
            }

            public void setLinkedEmployeeList(ArrayList<Object> linkedEmployeeList) {
                this.linkedEmployeeList = linkedEmployeeList;
            }

            public Object getHierDesc() {
                return hierDesc;
            }

            public void setHierDesc(Object hierDesc) {
                this.hierDesc = hierDesc;
            }

            public Object getEmpHierarchyType() {
                return empHierarchyType;
            }

            public void setEmpHierarchyType(Object empHierarchyType) {
                this.empHierarchyType = empHierarchyType;
            }

            public Integer getEmpGender() {
                return empGender;
            }

            public void setEmpGender(Integer empGender) {
                this.empGender = empGender;
            }

            public Object getEmpName() {
                return empName;
            }

            public void setEmpName(Object empName) {
                this.empName = empName;
            }

            public Object getDivision() {
                return division;
            }

            public void setDivision(Object division) {
                this.division = division;
            }

            public Object getDesignation() {
                return designation;
            }

            public void setDesignation(Object designation) {
                this.designation = designation;
            }

            public Object getErrorCode() {
                return errorCode;
            }

            public void setErrorCode(Object errorCode) {
                this.errorCode = errorCode;
            }

        }
        public class Doctor {

            @SerializedName("doctorId")
            @Expose
            private Integer doctorId;
            @SerializedName("routeId")
            @Expose
            private Integer routeId;
            @SerializedName("monday")
            @Expose
            private Boolean monday;
            @SerializedName("tuesday")
            @Expose
            private Boolean tuesday;
            @SerializedName("wednesday")
            @Expose
            private Boolean wednesday;
            @SerializedName("thursday")
            @Expose
            private Boolean thursday;
            @SerializedName("friday")
            @Expose
            private Boolean friday;
            @SerializedName("saturday")
            @Expose
            private Boolean saturday;
            @SerializedName("sunday")
            @Expose
            private Boolean sunday;
            @SerializedName("morning")
            @Expose
            private String morning;
            @SerializedName("evening")
            @Expose
            private String evening;
            @SerializedName("entryBy")
            @Expose
            private Integer entryBy;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("doctorName")
            @Expose
            private String doctorName;
            @SerializedName("hqId")
            @Expose
            private Integer hqId;
            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("stateName")
            @Expose
            private Object stateName;
            @SerializedName("hqName")
            @Expose
            private String hqName;
            @SerializedName("routeName")
            @Expose
            private String routeName;
            @SerializedName("strMorning")
            @Expose
            private String strMorning;
            @SerializedName("strEvening")
            @Expose
            private String strEvening;
            @SerializedName("visitFrequency")
            @Expose
            private Integer visitFrequency;
            @SerializedName("mobileNo")
            @Expose
            private String mobileNo;
            @SerializedName("emailId")
            @Expose
            private String emailId;
            @SerializedName("cityName")
            @Expose
            private String cityName;
            @SerializedName("specialityName")
            @Expose
            private String specialityName;
            @SerializedName("qualificationName")
            @Expose
            private String qualificationName;
            @SerializedName("grade")
            @Expose
            private Object grade;
            @SerializedName("class")
            @Expose
            private Object _class;
            @SerializedName("territory")
            @Expose
            private Object territory;
            @SerializedName("firstName")
            @Expose
            private Object firstName;
            @SerializedName("latitude")
            @Expose
            private Double latitude;
            @SerializedName("longitude")
            @Expose
            private Double longitude;
            @SerializedName("linkedBrandList")
            @Expose
            private ArrayList<LinkedBrand> linkedBrandList = null;

            public Integer getDoctorId() {
                return doctorId;
            }

            public void setDoctorId(Integer doctorId) {
                this.doctorId = doctorId;
            }

            public Integer getRouteId() {
                return routeId;
            }

            public void setRouteId(Integer routeId) {
                this.routeId = routeId;
            }

            public Boolean getMonday() {
                return monday;
            }

            public void setMonday(Boolean monday) {
                this.monday = monday;
            }

            public Boolean getTuesday() {
                return tuesday;
            }

            public void setTuesday(Boolean tuesday) {
                this.tuesday = tuesday;
            }

            public Boolean getWednesday() {
                return wednesday;
            }

            public void setWednesday(Boolean wednesday) {
                this.wednesday = wednesday;
            }

            public Boolean getThursday() {
                return thursday;
            }

            public void setThursday(Boolean thursday) {
                this.thursday = thursday;
            }

            public Boolean getFriday() {
                return friday;
            }

            public void setFriday(Boolean friday) {
                this.friday = friday;
            }

            public Boolean getSaturday() {
                return saturday;
            }

            public void setSaturday(Boolean saturday) {
                this.saturday = saturday;
            }

            public Boolean getSunday() {
                return sunday;
            }

            public void setSunday(Boolean sunday) {
                this.sunday = sunday;
            }

            public String getMorning() {
                return morning;
            }

            public void setMorning(String morning) {
                this.morning = morning;
            }

            public String getEvening() {
                return evening;
            }

            public void setEvening(String evening) {
                this.evening = evening;
            }

            public Integer getEntryBy() {
                return entryBy;
            }

            public void setEntryBy(Integer entryBy) {
                this.entryBy = entryBy;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public String getDoctorName() {
                return doctorName;
            }

            public void setDoctorName(String doctorName) {
                this.doctorName = doctorName;
            }

            public Integer getHqId() {
                return hqId;
            }

            public void setHqId(Integer hqId) {
                this.hqId = hqId;
            }

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Object getStateName() {
                return stateName;
            }

            public void setStateName(Object stateName) {
                this.stateName = stateName;
            }

            public String getHqName() {
                return hqName;
            }

            public void setHqName(String hqName) {
                this.hqName = hqName;
            }

            public String getRouteName() {
                return routeName;
            }

            public void setRouteName(String routeName) {
                this.routeName = routeName;
            }

            public String getStrMorning() {
                return strMorning;
            }

            public void setStrMorning(String strMorning) {
                this.strMorning = strMorning;
            }

            public String getStrEvening() {
                return strEvening;
            }

            public void setStrEvening(String strEvening) {
                this.strEvening = strEvening;
            }

            public Integer getVisitFrequency() {
                return visitFrequency;
            }

            public void setVisitFrequency(Integer visitFrequency) {
                this.visitFrequency = visitFrequency;
            }

            public String getMobileNo() {
                return mobileNo;
            }

            public void setMobileNo(String mobileNo) {
                this.mobileNo = mobileNo;
            }

            public String getEmailId() {
                return emailId;
            }

            public void setEmailId(String emailId) {
                this.emailId = emailId;
            }

            public String getCityName() {
                return cityName;
            }

            public void setCityName(String cityName) {
                this.cityName = cityName;
            }

            public String getSpecialityName() {
                return specialityName;
            }

            public void setSpecialityName(String specialityName) {
                this.specialityName = specialityName;
            }

            public String getQualificationName() {
                return qualificationName;
            }

            public void setQualificationName(String qualificationName) {
                this.qualificationName = qualificationName;
            }

            public Object getGrade() {
                return grade;
            }

            public void setGrade(Object grade) {
                this.grade = grade;
            }

            public Object getClass_() {
                return _class;
            }

            public void setClass_(Object _class) {
                this._class = _class;
            }

            public Object getTerritory() {
                return territory;
            }

            public void setTerritory(Object territory) {
                this.territory = territory;
            }

            public Object getFirstName() {
                return firstName;
            }

            public void setFirstName(Object firstName) {
                this.firstName = firstName;
            }

            public Double getLatitude() {
                return latitude;
            }

            public void setLatitude(Double latitude) {
                this.latitude = latitude;
            }

            public Double getLongitude() {
                return longitude;
            }

            public void setLongitude(Double longitude) {
                this.longitude = longitude;
            }

            public ArrayList<LinkedBrand> getLinkedBrandList() {
                return linkedBrandList;
            }

            public void setLinkedBrandList(ArrayList<LinkedBrand> linkedBrandList) {
                this.linkedBrandList = linkedBrandList;
            }

            public class LinkedBrand {

                @SerializedName("brandId")
                @Expose
                private Integer brandId;
                @SerializedName("brandName")
                @Expose
                private String brandName;
                @SerializedName("divisionId")
                @Expose
                private Integer divisionId;
                @SerializedName("userId")
                @Expose
                private Integer userId;
                @SerializedName("mode")
                @Expose
                private Integer mode;
                @SerializedName("preBrandName")
                @Expose
                private Object preBrandName;
                @SerializedName("divisionName")
                @Expose
                private String divisionName;
                @SerializedName("doctorId")
                @Expose
                private Integer doctorId;
                @SerializedName("eDetailId")
                @Expose
                private Integer eDetailId;
                @SerializedName("errorMessage")
                @Expose
                private Object errorMessage;
                @SerializedName("isError")
                @Expose
                private Boolean isError;
                @SerializedName("priorityOrder")
                @Expose
                private Integer priorityOrder;

                public Integer getBrandId() {
                    return brandId;
                }

                public void setBrandId(Integer brandId) {
                    this.brandId = brandId;
                }

                public String getBrandName() {
                    return brandName;
                }

                public void setBrandName(String brandName) {
                    this.brandName = brandName;
                }

                public Integer getDivisionId() {
                    return divisionId;
                }

                public void setDivisionId(Integer divisionId) {
                    this.divisionId = divisionId;
                }

                public Integer getUserId() {
                    return userId;
                }

                public void setUserId(Integer userId) {
                    this.userId = userId;
                }

                public Integer getMode() {
                    return mode;
                }

                public void setMode(Integer mode) {
                    this.mode = mode;
                }

                public Object getPreBrandName() {
                    return preBrandName;
                }

                public void setPreBrandName(Object preBrandName) {
                    this.preBrandName = preBrandName;
                }

                public String getDivisionName() {
                    return divisionName;
                }

                public void setDivisionName(String divisionName) {
                    this.divisionName = divisionName;
                }

                public Integer getDoctorId() {
                    return doctorId;
                }

                public void setDoctorId(Integer doctorId) {
                    this.doctorId = doctorId;
                }

                public Integer geteDetailId() {
                    return eDetailId;
                }

                public void seteDetailId(Integer eDetailId) {
                    this.eDetailId = eDetailId;
                }

                public Object getErrorMessage() {
                    return errorMessage;
                }

                public void setErrorMessage(Object errorMessage) {
                    this.errorMessage = errorMessage;
                }

                public Boolean getIsError() {
                    return isError;
                }

                public void setIsError(Boolean isError) {
                    this.isError = isError;
                }

                public Integer getPriorityOrder() {
                    return priorityOrder;
                }

                public void setPriorityOrder(Integer priorityOrder) {
                    this.priorityOrder = priorityOrder;
                }

            }


        }
        public class Product {

            @SerializedName("productId")
            @Expose
            private Integer productId;
            @SerializedName("productName")
            @Expose
            private String productName;
            @SerializedName("divisionId")
            @Expose
            private Integer divisionId;
            @SerializedName("categoryId")
            @Expose
            private Integer categoryId;
            @SerializedName("packingTypeId")
            @Expose
            private Integer packingTypeId;
            @SerializedName("mrp")
            @Expose
            private Double mrp;
            @SerializedName("productType")
            @Expose
            private Integer productType;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("dosageId")
            @Expose
            private Integer dosageId;
            @SerializedName("productTypeName")
            @Expose
            private String productTypeName;
            @SerializedName("packingTypeName")
            @Expose
            private String packingTypeName;
            @SerializedName("categoryName")
            @Expose
            private String categoryName;
            @SerializedName("divisionName")
            @Expose
            private String divisionName;
            @SerializedName("prodCode")
            @Expose
            private String prodCode;
            @SerializedName("shortName")
            @Expose
            private Object shortName;
            @SerializedName("price")
            @Expose
            private Double price;
            @SerializedName("priceToStockist")
            @Expose
            private Double priceToStockist;
            @SerializedName("allowSample")
            @Expose
            private Boolean allowSample;
            @SerializedName("samplePackSizeId")
            @Expose
            private Integer samplePackSizeId;
            @SerializedName("samplePackTypeName")
            @Expose
            private String samplePackTypeName;
            @SerializedName("sampleRate")
            @Expose
            private Double sampleRate;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("mfgId")
            @Expose
            private Integer mfgId;
            @SerializedName("dcrPrdType")
            @Expose
            private String dcrPrdType;
            @SerializedName("smpPckTyp")
            @Expose
            private Integer smpPckTyp;
            @SerializedName("smpMRP")
            @Expose
            private Double smpMRP;
            @SerializedName("preProductName")
            @Expose
            private String preProductName;
            @SerializedName("stock")
            @Expose
            private Double stock;
            @SerializedName("isDisabled")
            @Expose
            private Boolean isDisabled;
            @SerializedName("prevTransitStock")
            @Expose
            private Double prevTransitStock;
            @SerializedName("brandId")
            @Expose
            private Integer brandId;
            @SerializedName("eDetailId")
            @Expose
            private Integer eDetailId;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("dosageName")
            @Expose
            private String dosageName;
            @SerializedName("errorMessage")
            @Expose
            private String errorMessage;
            @SerializedName("sampleProductName")
            @Expose
            private Object sampleProductName;
            @SerializedName("sampleProdCode")
            @Expose
            private Object sampleProdCode;
            @SerializedName("giftCategoryId")
            @Expose
            private Integer giftCategoryId;
            @SerializedName("brandName")
            @Expose
            private Object brandName;
            @SerializedName("coreCompetitorTwo")
            @Expose
            private String coreCompetitorTwo;
            @SerializedName("coreCompetitorOne")
            @Expose
            private String coreCompetitorOne;
            @SerializedName("coreCompOneCompany")
            @Expose
            private String coreCompOneCompany;
            @SerializedName("coreCompTwoCompany")
            @Expose
            private String coreCompTwoCompany;

            public Integer getProductId() {
                return productId;
            }

            public void setProductId(Integer productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public Integer getDivisionId() {
                return divisionId;
            }

            public void setDivisionId(Integer divisionId) {
                this.divisionId = divisionId;
            }

            public Integer getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
            }

            public Integer getPackingTypeId() {
                return packingTypeId;
            }

            public void setPackingTypeId(Integer packingTypeId) {
                this.packingTypeId = packingTypeId;
            }

            public Double getMrp() {
                return mrp;
            }

            public void setMrp(Double mrp) {
                this.mrp = mrp;
            }

            public Integer getProductType() {
                return productType;
            }

            public void setProductType(Integer productType) {
                this.productType = productType;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Integer getDosageId() {
                return dosageId;
            }

            public void setDosageId(Integer dosageId) {
                this.dosageId = dosageId;
            }

            public String getProductTypeName() {
                return productTypeName;
            }

            public void setProductTypeName(String productTypeName) {
                this.productTypeName = productTypeName;
            }

            public String getPackingTypeName() {
                return packingTypeName;
            }

            public void setPackingTypeName(String packingTypeName) {
                this.packingTypeName = packingTypeName;
            }

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            public String getDivisionName() {
                return divisionName;
            }

            public void setDivisionName(String divisionName) {
                this.divisionName = divisionName;
            }

            public String getProdCode() {
                return prodCode;
            }

            public void setProdCode(String prodCode) {
                this.prodCode = prodCode;
            }

            public Object getShortName() {
                return shortName;
            }

            public void setShortName(Object shortName) {
                this.shortName = shortName;
            }

            public Double getPrice() {
                return price;
            }

            public void setPrice(Double price) {
                this.price = price;
            }

            public Double getPriceToStockist() {
                return priceToStockist;
            }

            public void setPriceToStockist(Double priceToStockist) {
                this.priceToStockist = priceToStockist;
            }

            public Boolean getAllowSample() {
                return allowSample;
            }

            public void setAllowSample(Boolean allowSample) {
                this.allowSample = allowSample;
            }

            public Integer getSamplePackSizeId() {
                return samplePackSizeId;
            }

            public void setSamplePackSizeId(Integer samplePackSizeId) {
                this.samplePackSizeId = samplePackSizeId;
            }

            public String getSamplePackTypeName() {
                return samplePackTypeName;
            }

            public void setSamplePackTypeName(String samplePackTypeName) {
                this.samplePackTypeName = samplePackTypeName;
            }

            public Double getSampleRate() {
                return sampleRate;
            }

            public void setSampleRate(Double sampleRate) {
                this.sampleRate = sampleRate;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public Integer getMfgId() {
                return mfgId;
            }

            public void setMfgId(Integer mfgId) {
                this.mfgId = mfgId;
            }

            public String getDcrPrdType() {
                return dcrPrdType;
            }

            public void setDcrPrdType(String dcrPrdType) {
                this.dcrPrdType = dcrPrdType;
            }

            public Integer getSmpPckTyp() {
                return smpPckTyp;
            }

            public void setSmpPckTyp(Integer smpPckTyp) {
                this.smpPckTyp = smpPckTyp;
            }

            public Double getSmpMRP() {
                return smpMRP;
            }

            public void setSmpMRP(Double smpMRP) {
                this.smpMRP = smpMRP;
            }

            public String getPreProductName() {
                return preProductName;
            }

            public void setPreProductName(String preProductName) {
                this.preProductName = preProductName;
            }

            public Double getStock() {
                return stock;
            }

            public void setStock(Double stock) {
                this.stock = stock;
            }

            public Boolean getIsDisabled() {
                return isDisabled;
            }

            public void setIsDisabled(Boolean isDisabled) {
                this.isDisabled = isDisabled;
            }

            public Double getPrevTransitStock() {
                return prevTransitStock;
            }

            public void setPrevTransitStock(Double prevTransitStock) {
                this.prevTransitStock = prevTransitStock;
            }

            public Integer getBrandId() {
                return brandId;
            }

            public void setBrandId(Integer brandId) {
                this.brandId = brandId;
            }

            public Integer geteDetailId() {
                return eDetailId;
            }

            public void seteDetailId(Integer eDetailId) {
                this.eDetailId = eDetailId;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public String getDosageName() {
                return dosageName;
            }

            public void setDosageName(String dosageName) {
                this.dosageName = dosageName;
            }

            public String getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Object getSampleProductName() {
                return sampleProductName;
            }

            public void setSampleProductName(Object sampleProductName) {
                this.sampleProductName = sampleProductName;
            }

            public Object getSampleProdCode() {
                return sampleProdCode;
            }

            public void setSampleProdCode(Object sampleProdCode) {
                this.sampleProdCode = sampleProdCode;
            }

            public Integer getGiftCategoryId() {
                return giftCategoryId;
            }

            public void setGiftCategoryId(Integer giftCategoryId) {
                this.giftCategoryId = giftCategoryId;
            }

            public Object getBrandName() {
                return brandName;
            }

            public void setBrandName(Object brandName) {
                this.brandName = brandName;
            }

            public String getCoreCompetitorTwo() {
                return coreCompetitorTwo;
            }

            public void setCoreCompetitorTwo(String coreCompetitorTwo) {
                this.coreCompetitorTwo = coreCompetitorTwo;
            }

            public String getCoreCompetitorOne() {
                return coreCompetitorOne;
            }

            public void setCoreCompetitorOne(String coreCompetitorOne) {
                this.coreCompetitorOne = coreCompetitorOne;
            }

            public String getCoreCompOneCompany() {
                return coreCompOneCompany;
            }

            public void setCoreCompOneCompany(String coreCompOneCompany) {
                this.coreCompOneCompany = coreCompOneCompany;
            }

            public String getCoreCompTwoCompany() {
                return coreCompTwoCompany;
            }

            public void setCoreCompTwoCompany(String coreCompTwoCompany) {
                this.coreCompTwoCompany = coreCompTwoCompany;
            }

        }
        public class Brand {

            @SerializedName("brandId")
            @Expose
            private Integer brandId;
            @SerializedName("brandName")
            @Expose
            private String brandName;
            @SerializedName("divisionId")
            @Expose
            private Integer divisionId;
            @SerializedName("userId")
            @Expose
            private Integer userId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("preBrandName")
            @Expose
            private String preBrandName;
            @SerializedName("divisionName")
            @Expose
            private String divisionName;
            @SerializedName("doctorId")
            @Expose
            private Integer doctorId;
            @SerializedName("eDetailId")
            @Expose
            private Integer eDetailId;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("priorityOrder")
            @Expose
            private Integer priorityOrder;

            public Integer getBrandId() {
                return brandId;
            }

            public void setBrandId(Integer brandId) {
                this.brandId = brandId;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public Integer getDivisionId() {
                return divisionId;
            }

            public void setDivisionId(Integer divisionId) {
                this.divisionId = divisionId;
            }

            public Integer getUserId() {
                return userId;
            }

            public void setUserId(Integer userId) {
                this.userId = userId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public String getPreBrandName() {
                return preBrandName;
            }

            public void setPreBrandName(String preBrandName) {
                this.preBrandName = preBrandName;
            }

            public String getDivisionName() {
                return divisionName;
            }

            public void setDivisionName(String divisionName) {
                this.divisionName = divisionName;
            }

            public Integer getDoctorId() {
                return doctorId;
            }

            public void setDoctorId(Integer doctorId) {
                this.doctorId = doctorId;
            }

            public Integer geteDetailId() {
                return eDetailId;
            }

            public void seteDetailId(Integer eDetailId) {
                this.eDetailId = eDetailId;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public Integer getPriorityOrder() {
                return priorityOrder;
            }

            public void setPriorityOrder(Integer priorityOrder) {
                this.priorityOrder = priorityOrder;
            }

        }
        public class WorkingWith {

            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("firstName")
            @Expose
            private String firstName;
            @SerializedName("lastName")
            @Expose
            private String lastName;
            @SerializedName("userName")
            @Expose
            private String userName;
            @SerializedName("password")
            @Expose
            private String password;
            @SerializedName("emailId")
            @Expose
            private String emailId;
            @SerializedName("mobileNo")
            @Expose
            private String mobileNo;
            @SerializedName("dateOfBirth")
            @Expose
            private String dateOfBirth;
            @SerializedName("doa")
            @Expose
            private String doa;
            @SerializedName("gender")
            @Expose
            private Integer gender;
            @SerializedName("address1")
            @Expose
            private String address1;
            @SerializedName("address2")
            @Expose
            private String address2;
            @SerializedName("cityId")
            @Expose
            private Integer cityId;
            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("stateName")
            @Expose
            private Object stateName;
            @SerializedName("countryId")
            @Expose
            private Integer countryId;
            @SerializedName("countryName")
            @Expose
            private Object countryName;
            @SerializedName("pinCode")
            @Expose
            private Integer pinCode;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("division")
            @Expose
            private String division;
            @SerializedName("headQuaterId")
            @Expose
            private String headQuaterId;
            @SerializedName("headQuaterName")
            @Expose
            private String headQuaterName;
            @SerializedName("hierachyId")
            @Expose
            private Integer hierachyId;
            @SerializedName("hierachyName")
            @Expose
            private Object hierachyName;
            @SerializedName("reportingHierachy")
            @Expose
            private Integer reportingHierachy;
            @SerializedName("reportingManager")
            @Expose
            private Integer reportingManager;
            @SerializedName("qualificationId")
            @Expose
            private Integer qualificationId;
            @SerializedName("marriedStatus")
            @Expose
            private Integer marriedStatus;
            @SerializedName("reportingManagerName")
            @Expose
            private String reportingManagerName;
            @SerializedName("reportingManagerEmail")
            @Expose
            private String reportingManagerEmail;
            @SerializedName("hierachyLevel")
            @Expose
            private Integer hierachyLevel;
            @SerializedName("roleId")
            @Expose
            private Integer roleId;
            @SerializedName("active")
            @Expose
            private Boolean active;
            @SerializedName("entryBy")
            @Expose
            private Integer entryBy;
            @SerializedName("updateBy")
            @Expose
            private Integer updateBy;
            @SerializedName("deleteBy")
            @Expose
            private Integer deleteBy;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("compKey")
            @Expose
            private Integer compKey;
            @SerializedName("isFirstLogin")
            @Expose
            private Boolean isFirstLogin;
            @SerializedName("authPassword")
            @Expose
            private String authPassword;
            @SerializedName("oldAuthPassword")
            @Expose
            private String oldAuthPassword;
            @SerializedName("dashboardLayout")
            @Expose
            private String dashboardLayout;
            @SerializedName("companyCode")
            @Expose
            private String companyCode;
            @SerializedName("oldPassword")
            @Expose
            private String oldPassword;
            @SerializedName("newPassword")
            @Expose
            private String newPassword;
            @SerializedName("userType")
            @Expose
            private String userType;
            @SerializedName("imageName")
            @Expose
            private String imageName;
            @SerializedName("imagePath")
            @Expose
            private String imagePath;
            @SerializedName("imageExt")
            @Expose
            private String imageExt;
            @SerializedName("isAuthPassword")
            @Expose
            private Boolean isAuthPassword;
            @SerializedName("roleType")
            @Expose
            private String roleType;
            @SerializedName("roleName")
            @Expose
            private Object roleName;
            @SerializedName("prevRoleName")
            @Expose
            private Object prevRoleName;
            @SerializedName("roleTypeName")
            @Expose
            private String roleTypeName;
            @SerializedName("linkedStates")
            @Expose
            private String linkedStates;
            @SerializedName("deactivationDate")
            @Expose
            private String deactivationDate;
            @SerializedName("oldReportingManagerName")
            @Expose
            private Object oldReportingManagerName;
            @SerializedName("expenseTemplateData")
            @Expose
            private Object expenseTemplateData;
            @SerializedName("isCheckIn")
            @Expose
            private Boolean isCheckIn;
            @SerializedName("allowDoctorEdit")
            @Expose
            private Boolean allowDoctorEdit;
            @SerializedName("lastDCRDate")
            @Expose
            private String lastDCRDate;
            @SerializedName("strLastDCRDate")
            @Expose
            private Object strLastDCRDate;
            @SerializedName("mobileAppInstall")
            @Expose
            private Object mobileAppInstall;
            @SerializedName("lastLoginDate")
            @Expose
            private String lastLoginDate;
            @SerializedName("strLastLoginDate")
            @Expose
            private Object strLastLoginDate;
            @SerializedName("mPin")
            @Expose
            private Object mPin;
            @SerializedName("fingerprint")
            @Expose
            private Object fingerprint;
            @SerializedName("workingHeadQuarter")
            @Expose
            private String workingHeadQuarter;
            @SerializedName("cityName")
            @Expose
            private Object cityName;
            @SerializedName("canBeDeleted")
            @Expose
            private Boolean canBeDeleted;
            @SerializedName("absolutePath")
            @Expose
            private String absolutePath;
            @SerializedName("disableSMSNotification")
            @Expose
            private Boolean disableSMSNotification;
            @SerializedName("prevFirstName")
            @Expose
            private String prevFirstName;
            @SerializedName("prevLastName")
            @Expose
            private String prevLastName;
            @SerializedName("prevUserName")
            @Expose
            private String prevUserName;
            @SerializedName("fullName")
            @Expose
            private String fullName;
            @SerializedName("headQuaterType")
            @Expose
            private Integer headQuaterType;
            @SerializedName("employeeCode")
            @Expose
            private String employeeCode;
            @SerializedName("joiningDate")
            @Expose
            private String joiningDate;
            @SerializedName("onFieldJobDate")
            @Expose
            private String onFieldJobDate;
            @SerializedName("hierachyCode")
            @Expose
            private String hierachyCode;
            @SerializedName("hierachyType")
            @Expose
            private Object hierachyType;
            @SerializedName("reportingManagerStatus")
            @Expose
            private Object reportingManagerStatus;
            @SerializedName("lastLoginDeviceId")
            @Expose
            private Object lastLoginDeviceId;
            @SerializedName("pushToken")
            @Expose
            private Object pushToken;
            @SerializedName("hierDesc")
            @Expose
            private String hierDesc;
            @SerializedName("teamLevel")
            @Expose
            private Integer teamLevel;
            @SerializedName("divisionName")
            @Expose
            private Object divisionName;
            @SerializedName("linkedStateName")
            @Expose
            private String linkedStateName;
            @SerializedName("qualificationName")
            @Expose
            private Object qualificationName;
            @SerializedName("nameWithCode")
            @Expose
            private String nameWithCode;
            @SerializedName("receiveEmailUpdate")
            @Expose
            private Boolean receiveEmailUpdate;
            @SerializedName("mainHeadQuarter")
            @Expose
            private Integer mainHeadQuarter;
            @SerializedName("callDetails")
            @Expose
            private Object callDetails;
            @SerializedName("strDateOfBirth")
            @Expose
            private Object strDateOfBirth;
            @SerializedName("strJoiningDate")
            @Expose
            private Object strJoiningDate;
            @SerializedName("strOnFieldJobDate")
            @Expose
            private Object strOnFieldJobDate;
            @SerializedName("reportingHierachyCode")
            @Expose
            private Object reportingHierachyCode;
            @SerializedName("mobileAppVersionInUse")
            @Expose
            private Object mobileAppVersionInUse;
            @SerializedName("previousHierarchyType")
            @Expose
            private Object previousHierarchyType;
            @SerializedName("month")
            @Expose
            private Integer month;
            @SerializedName("year")
            @Expose
            private Integer year;
            @SerializedName("promotionDate")
            @Expose
            private String promotionDate;
            @SerializedName("previousPost")
            @Expose
            private Integer previousPost;
            @SerializedName("previousReportingManager")
            @Expose
            private Integer previousReportingManager;
            @SerializedName("isExpenseApprovalReq")
            @Expose
            private Boolean isExpenseApprovalReq;
            @SerializedName("expenseApproveBy")
            @Expose
            private Integer expenseApproveBy;
            @SerializedName("isMpin")
            @Expose
            private Boolean isMpin;
            @SerializedName("ledgerCode")
            @Expose
            private String ledgerCode;
            @SerializedName("isFieldWorkingUser")
            @Expose
            private Boolean isFieldWorkingUser;
            @SerializedName("isAccessBlocked")
            @Expose
            private Boolean isAccessBlocked;
            @SerializedName("otp")
            @Expose
            private Object otp;
            @SerializedName("isGeoFencingApplicable")
            @Expose
            private Boolean isGeoFencingApplicable;
            @SerializedName("allowLocationUpdate")
            @Expose
            private Boolean allowLocationUpdate;
            @SerializedName("enableSelfieAttendance")
            @Expose
            private Boolean enableSelfieAttendance;
            @SerializedName("regionId")
            @Expose
            private Integer regionId;

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getPassword() {
                return password;
            }

            public void setPassword(String password) {
                this.password = password;
            }

            public String getEmailId() {
                return emailId;
            }

            public void setEmailId(String emailId) {
                this.emailId = emailId;
            }

            public String getMobileNo() {
                return mobileNo;
            }

            public void setMobileNo(String mobileNo) {
                this.mobileNo = mobileNo;
            }

            public String getDateOfBirth() {
                return dateOfBirth;
            }

            public void setDateOfBirth(String dateOfBirth) {
                this.dateOfBirth = dateOfBirth;
            }

            public String getDoa() {
                return doa;
            }

            public void setDoa(String doa) {
                this.doa = doa;
            }

            public Integer getGender() {
                return gender;
            }

            public void setGender(Integer gender) {
                this.gender = gender;
            }

            public String getAddress1() {
                return address1;
            }

            public void setAddress1(String address1) {
                this.address1 = address1;
            }

            public String getAddress2() {
                return address2;
            }

            public void setAddress2(String address2) {
                this.address2 = address2;
            }

            public Integer getCityId() {
                return cityId;
            }

            public void setCityId(Integer cityId) {
                this.cityId = cityId;
            }

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Object getStateName() {
                return stateName;
            }

            public void setStateName(Object stateName) {
                this.stateName = stateName;
            }

            public Integer getCountryId() {
                return countryId;
            }

            public void setCountryId(Integer countryId) {
                this.countryId = countryId;
            }

            public Object getCountryName() {
                return countryName;
            }

            public void setCountryName(Object countryName) {
                this.countryName = countryName;
            }

            public Integer getPinCode() {
                return pinCode;
            }

            public void setPinCode(Integer pinCode) {
                this.pinCode = pinCode;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getDivision() {
                return division;
            }

            public void setDivision(String division) {
                this.division = division;
            }

            public String getHeadQuaterId() {
                return headQuaterId;
            }

            public void setHeadQuaterId(String headQuaterId) {
                this.headQuaterId = headQuaterId;
            }

            public String getHeadQuaterName() {
                return headQuaterName;
            }

            public void setHeadQuaterName(String headQuaterName) {
                this.headQuaterName = headQuaterName;
            }

            public Integer getHierachyId() {
                return hierachyId;
            }

            public void setHierachyId(Integer hierachyId) {
                this.hierachyId = hierachyId;
            }

            public Object getHierachyName() {
                return hierachyName;
            }

            public void setHierachyName(Object hierachyName) {
                this.hierachyName = hierachyName;
            }

            public Integer getReportingHierachy() {
                return reportingHierachy;
            }

            public void setReportingHierachy(Integer reportingHierachy) {
                this.reportingHierachy = reportingHierachy;
            }

            public Integer getReportingManager() {
                return reportingManager;
            }

            public void setReportingManager(Integer reportingManager) {
                this.reportingManager = reportingManager;
            }

            public Integer getQualificationId() {
                return qualificationId;
            }

            public void setQualificationId(Integer qualificationId) {
                this.qualificationId = qualificationId;
            }

            public Integer getMarriedStatus() {
                return marriedStatus;
            }

            public void setMarriedStatus(Integer marriedStatus) {
                this.marriedStatus = marriedStatus;
            }

            public String getReportingManagerName() {
                return reportingManagerName;
            }

            public void setReportingManagerName(String reportingManagerName) {
                this.reportingManagerName = reportingManagerName;
            }

            public String getReportingManagerEmail() {
                return reportingManagerEmail;
            }

            public void setReportingManagerEmail(String reportingManagerEmail) {
                this.reportingManagerEmail = reportingManagerEmail;
            }

            public Integer getHierachyLevel() {
                return hierachyLevel;
            }

            public void setHierachyLevel(Integer hierachyLevel) {
                this.hierachyLevel = hierachyLevel;
            }

            public Integer getRoleId() {
                return roleId;
            }

            public void setRoleId(Integer roleId) {
                this.roleId = roleId;
            }

            public Boolean getActive() {
                return active;
            }

            public void setActive(Boolean active) {
                this.active = active;
            }

            public Integer getEntryBy() {
                return entryBy;
            }

            public void setEntryBy(Integer entryBy) {
                this.entryBy = entryBy;
            }

            public Integer getUpdateBy() {
                return updateBy;
            }

            public void setUpdateBy(Integer updateBy) {
                this.updateBy = updateBy;
            }

            public Integer getDeleteBy() {
                return deleteBy;
            }

            public void setDeleteBy(Integer deleteBy) {
                this.deleteBy = deleteBy;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Integer getCompKey() {
                return compKey;
            }

            public void setCompKey(Integer compKey) {
                this.compKey = compKey;
            }

            public Boolean getIsFirstLogin() {
                return isFirstLogin;
            }

            public void setIsFirstLogin(Boolean isFirstLogin) {
                this.isFirstLogin = isFirstLogin;
            }

            public String getAuthPassword() {
                return authPassword;
            }

            public void setAuthPassword(String authPassword) {
                this.authPassword = authPassword;
            }

            public String getOldAuthPassword() {
                return oldAuthPassword;
            }

            public void setOldAuthPassword(String oldAuthPassword) {
                this.oldAuthPassword = oldAuthPassword;
            }

            public String getDashboardLayout() {
                return dashboardLayout;
            }

            public void setDashboardLayout(String dashboardLayout) {
                this.dashboardLayout = dashboardLayout;
            }

            public String getCompanyCode() {
                return companyCode;
            }

            public void setCompanyCode(String companyCode) {
                this.companyCode = companyCode;
            }

            public String getOldPassword() {
                return oldPassword;
            }

            public void setOldPassword(String oldPassword) {
                this.oldPassword = oldPassword;
            }

            public String getNewPassword() {
                return newPassword;
            }

            public void setNewPassword(String newPassword) {
                this.newPassword = newPassword;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public String getImageName() {
                return imageName;
            }

            public void setImageName(String imageName) {
                this.imageName = imageName;
            }

            public String getImagePath() {
                return imagePath;
            }

            public void setImagePath(String imagePath) {
                this.imagePath = imagePath;
            }

            public String getImageExt() {
                return imageExt;
            }

            public void setImageExt(String imageExt) {
                this.imageExt = imageExt;
            }

            public Boolean getIsAuthPassword() {
                return isAuthPassword;
            }

            public void setIsAuthPassword(Boolean isAuthPassword) {
                this.isAuthPassword = isAuthPassword;
            }

            public String getRoleType() {
                return roleType;
            }

            public void setRoleType(String roleType) {
                this.roleType = roleType;
            }

            public Object getRoleName() {
                return roleName;
            }

            public void setRoleName(Object roleName) {
                this.roleName = roleName;
            }

            public Object getPrevRoleName() {
                return prevRoleName;
            }

            public void setPrevRoleName(Object prevRoleName) {
                this.prevRoleName = prevRoleName;
            }

            public String getRoleTypeName() {
                return roleTypeName;
            }

            public void setRoleTypeName(String roleTypeName) {
                this.roleTypeName = roleTypeName;
            }

            public String getLinkedStates() {
                return linkedStates;
            }

            public void setLinkedStates(String linkedStates) {
                this.linkedStates = linkedStates;
            }

            public String getDeactivationDate() {
                return deactivationDate;
            }

            public void setDeactivationDate(String deactivationDate) {
                this.deactivationDate = deactivationDate;
            }

            public Object getOldReportingManagerName() {
                return oldReportingManagerName;
            }

            public void setOldReportingManagerName(Object oldReportingManagerName) {
                this.oldReportingManagerName = oldReportingManagerName;
            }

            public Object getExpenseTemplateData() {
                return expenseTemplateData;
            }

            public void setExpenseTemplateData(Object expenseTemplateData) {
                this.expenseTemplateData = expenseTemplateData;
            }

            public Boolean getIsCheckIn() {
                return isCheckIn;
            }

            public void setIsCheckIn(Boolean isCheckIn) {
                this.isCheckIn = isCheckIn;
            }

            public Boolean getAllowDoctorEdit() {
                return allowDoctorEdit;
            }

            public void setAllowDoctorEdit(Boolean allowDoctorEdit) {
                this.allowDoctorEdit = allowDoctorEdit;
            }

            public String getLastDCRDate() {
                return lastDCRDate;
            }

            public void setLastDCRDate(String lastDCRDate) {
                this.lastDCRDate = lastDCRDate;
            }

            public Object getStrLastDCRDate() {
                return strLastDCRDate;
            }

            public void setStrLastDCRDate(Object strLastDCRDate) {
                this.strLastDCRDate = strLastDCRDate;
            }

            public Object getMobileAppInstall() {
                return mobileAppInstall;
            }

            public void setMobileAppInstall(Object mobileAppInstall) {
                this.mobileAppInstall = mobileAppInstall;
            }

            public String getLastLoginDate() {
                return lastLoginDate;
            }

            public void setLastLoginDate(String lastLoginDate) {
                this.lastLoginDate = lastLoginDate;
            }

            public Object getStrLastLoginDate() {
                return strLastLoginDate;
            }

            public void setStrLastLoginDate(Object strLastLoginDate) {
                this.strLastLoginDate = strLastLoginDate;
            }

            public Object getmPin() {
                return mPin;
            }

            public void setmPin(Object mPin) {
                this.mPin = mPin;
            }

            public Object getFingerprint() {
                return fingerprint;
            }

            public void setFingerprint(Object fingerprint) {
                this.fingerprint = fingerprint;
            }

            public String getWorkingHeadQuarter() {
                return workingHeadQuarter;
            }

            public void setWorkingHeadQuarter(String workingHeadQuarter) {
                this.workingHeadQuarter = workingHeadQuarter;
            }

            public Object getCityName() {
                return cityName;
            }

            public void setCityName(Object cityName) {
                this.cityName = cityName;
            }

            public Boolean getCanBeDeleted() {
                return canBeDeleted;
            }

            public void setCanBeDeleted(Boolean canBeDeleted) {
                this.canBeDeleted = canBeDeleted;
            }

            public String getAbsolutePath() {
                return absolutePath;
            }

            public void setAbsolutePath(String absolutePath) {
                this.absolutePath = absolutePath;
            }

            public Boolean getDisableSMSNotification() {
                return disableSMSNotification;
            }

            public void setDisableSMSNotification(Boolean disableSMSNotification) {
                this.disableSMSNotification = disableSMSNotification;
            }

            public String getPrevFirstName() {
                return prevFirstName;
            }

            public void setPrevFirstName(String prevFirstName) {
                this.prevFirstName = prevFirstName;
            }

            public String getPrevLastName() {
                return prevLastName;
            }

            public void setPrevLastName(String prevLastName) {
                this.prevLastName = prevLastName;
            }

            public String getPrevUserName() {
                return prevUserName;
            }

            public void setPrevUserName(String prevUserName) {
                this.prevUserName = prevUserName;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public Integer getHeadQuaterType() {
                return headQuaterType;
            }

            public void setHeadQuaterType(Integer headQuaterType) {
                this.headQuaterType = headQuaterType;
            }

            public String getEmployeeCode() {
                return employeeCode;
            }

            public void setEmployeeCode(String employeeCode) {
                this.employeeCode = employeeCode;
            }

            public String getJoiningDate() {
                return joiningDate;
            }

            public void setJoiningDate(String joiningDate) {
                this.joiningDate = joiningDate;
            }

            public String getOnFieldJobDate() {
                return onFieldJobDate;
            }

            public void setOnFieldJobDate(String onFieldJobDate) {
                this.onFieldJobDate = onFieldJobDate;
            }

            public String getHierachyCode() {
                return hierachyCode;
            }

            public void setHierachyCode(String hierachyCode) {
                this.hierachyCode = hierachyCode;
            }

            public Object getHierachyType() {
                return hierachyType;
            }

            public void setHierachyType(Object hierachyType) {
                this.hierachyType = hierachyType;
            }

            public Object getReportingManagerStatus() {
                return reportingManagerStatus;
            }

            public void setReportingManagerStatus(Object reportingManagerStatus) {
                this.reportingManagerStatus = reportingManagerStatus;
            }

            public Object getLastLoginDeviceId() {
                return lastLoginDeviceId;
            }

            public void setLastLoginDeviceId(Object lastLoginDeviceId) {
                this.lastLoginDeviceId = lastLoginDeviceId;
            }

            public Object getPushToken() {
                return pushToken;
            }

            public void setPushToken(Object pushToken) {
                this.pushToken = pushToken;
            }

            public String getHierDesc() {
                return hierDesc;
            }

            public void setHierDesc(String hierDesc) {
                this.hierDesc = hierDesc;
            }

            public Integer getTeamLevel() {
                return teamLevel;
            }

            public void setTeamLevel(Integer teamLevel) {
                this.teamLevel = teamLevel;
            }

            public Object getDivisionName() {
                return divisionName;
            }

            public void setDivisionName(Object divisionName) {
                this.divisionName = divisionName;
            }

            public String getLinkedStateName() {
                return linkedStateName;
            }

            public void setLinkedStateName(String linkedStateName) {
                this.linkedStateName = linkedStateName;
            }

            public Object getQualificationName() {
                return qualificationName;
            }

            public void setQualificationName(Object qualificationName) {
                this.qualificationName = qualificationName;
            }

            public String getNameWithCode() {
                return nameWithCode;
            }

            public void setNameWithCode(String nameWithCode) {
                this.nameWithCode = nameWithCode;
            }

            public Boolean getReceiveEmailUpdate() {
                return receiveEmailUpdate;
            }

            public void setReceiveEmailUpdate(Boolean receiveEmailUpdate) {
                this.receiveEmailUpdate = receiveEmailUpdate;
            }

            public Integer getMainHeadQuarter() {
                return mainHeadQuarter;
            }

            public void setMainHeadQuarter(Integer mainHeadQuarter) {
                this.mainHeadQuarter = mainHeadQuarter;
            }

            public Object getCallDetails() {
                return callDetails;
            }

            public void setCallDetails(Object callDetails) {
                this.callDetails = callDetails;
            }

            public Object getStrDateOfBirth() {
                return strDateOfBirth;
            }

            public void setStrDateOfBirth(Object strDateOfBirth) {
                this.strDateOfBirth = strDateOfBirth;
            }

            public Object getStrJoiningDate() {
                return strJoiningDate;
            }

            public void setStrJoiningDate(Object strJoiningDate) {
                this.strJoiningDate = strJoiningDate;
            }

            public Object getStrOnFieldJobDate() {
                return strOnFieldJobDate;
            }

            public void setStrOnFieldJobDate(Object strOnFieldJobDate) {
                this.strOnFieldJobDate = strOnFieldJobDate;
            }

            public Object getReportingHierachyCode() {
                return reportingHierachyCode;
            }

            public void setReportingHierachyCode(Object reportingHierachyCode) {
                this.reportingHierachyCode = reportingHierachyCode;
            }

            public Object getMobileAppVersionInUse() {
                return mobileAppVersionInUse;
            }

            public void setMobileAppVersionInUse(Object mobileAppVersionInUse) {
                this.mobileAppVersionInUse = mobileAppVersionInUse;
            }

            public Object getPreviousHierarchyType() {
                return previousHierarchyType;
            }

            public void setPreviousHierarchyType(Object previousHierarchyType) {
                this.previousHierarchyType = previousHierarchyType;
            }

            public Integer getMonth() {
                return month;
            }

            public void setMonth(Integer month) {
                this.month = month;
            }

            public Integer getYear() {
                return year;
            }

            public void setYear(Integer year) {
                this.year = year;
            }

            public String getPromotionDate() {
                return promotionDate;
            }

            public void setPromotionDate(String promotionDate) {
                this.promotionDate = promotionDate;
            }

            public Integer getPreviousPost() {
                return previousPost;
            }

            public void setPreviousPost(Integer previousPost) {
                this.previousPost = previousPost;
            }

            public Integer getPreviousReportingManager() {
                return previousReportingManager;
            }

            public void setPreviousReportingManager(Integer previousReportingManager) {
                this.previousReportingManager = previousReportingManager;
            }

            public Boolean getIsExpenseApprovalReq() {
                return isExpenseApprovalReq;
            }

            public void setIsExpenseApprovalReq(Boolean isExpenseApprovalReq) {
                this.isExpenseApprovalReq = isExpenseApprovalReq;
            }

            public Integer getExpenseApproveBy() {
                return expenseApproveBy;
            }

            public void setExpenseApproveBy(Integer expenseApproveBy) {
                this.expenseApproveBy = expenseApproveBy;
            }

            public Boolean getIsMpin() {
                return isMpin;
            }

            public void setIsMpin(Boolean isMpin) {
                this.isMpin = isMpin;
            }

            public String getLedgerCode() {
                return ledgerCode;
            }

            public void setLedgerCode(String ledgerCode) {
                this.ledgerCode = ledgerCode;
            }

            public Boolean getIsFieldWorkingUser() {
                return isFieldWorkingUser;
            }

            public void setIsFieldWorkingUser(Boolean isFieldWorkingUser) {
                this.isFieldWorkingUser = isFieldWorkingUser;
            }

            public Boolean getIsAccessBlocked() {
                return isAccessBlocked;
            }

            public void setIsAccessBlocked(Boolean isAccessBlocked) {
                this.isAccessBlocked = isAccessBlocked;
            }

            public Object getOtp() {
                return otp;
            }

            public void setOtp(Object otp) {
                this.otp = otp;
            }

            public Boolean getIsGeoFencingApplicable() {
                return isGeoFencingApplicable;
            }

            public void setIsGeoFencingApplicable(Boolean isGeoFencingApplicable) {
                this.isGeoFencingApplicable = isGeoFencingApplicable;
            }

            public Boolean getAllowLocationUpdate() {
                return allowLocationUpdate;
            }

            public void setAllowLocationUpdate(Boolean allowLocationUpdate) {
                this.allowLocationUpdate = allowLocationUpdate;
            }

            public Boolean getEnableSelfieAttendance() {
                return enableSelfieAttendance;
            }

            public void setEnableSelfieAttendance(Boolean enableSelfieAttendance) {
                this.enableSelfieAttendance = enableSelfieAttendance;
            }

            public Integer getRegionId() {
                return regionId;
            }

            public void setRegionId(Integer regionId) {
                this.regionId = regionId;
            }

        }
        public class Holiday {

            @SerializedName("holidayId")
            @Expose
            private Integer holidayId;
            @SerializedName("holidayName")
            @Expose
            private String holidayName;
            @SerializedName("holidayFrom")
            @Expose
            private String holidayFrom;
            @SerializedName("holidayTo")
            @Expose
            private String holidayTo;
            @SerializedName("preHolidayName")
            @Expose
            private Object preHolidayName;
            @SerializedName("strHolidayFrom")
            @Expose
            private Object strHolidayFrom;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("holidayType")
            @Expose
            private Object holidayType;
            @SerializedName("stateId")
            @Expose
            private Integer stateId;
            @SerializedName("state")
            @Expose
            private Object state;
            @SerializedName("holidayDate")
            @Expose
            private String holidayDate;
            @SerializedName("holidayTypeValue")
            @Expose
            private String holidayTypeValue;
            @SerializedName("strHolidayTo")
            @Expose
            private Object strHolidayTo;
            @SerializedName("canBeEdit")
            @Expose
            private Boolean canBeEdit;
            @SerializedName("monthName")
            @Expose
            private Object monthName;
            @SerializedName("errorMessage")
            @Expose
            private Object errorMessage;
            @SerializedName("isError")
            @Expose
            private Boolean isError;
            @SerializedName("year")
            @Expose
            private Integer year;
            @SerializedName("month")
            @Expose
            private Integer month;
            @SerializedName("day")
            @Expose
            private Integer day;

            public Integer getHolidayId() {
                return holidayId;
            }

            public void setHolidayId(Integer holidayId) {
                this.holidayId = holidayId;
            }

            public String getHolidayName() {
                return holidayName;
            }

            public void setHolidayName(String holidayName) {
                this.holidayName = holidayName;
            }

            public String getHolidayFrom() {
                return holidayFrom;
            }

            public void setHolidayFrom(String holidayFrom) {
                this.holidayFrom = holidayFrom;
            }

            public String getHolidayTo() {
                return holidayTo;
            }

            public void setHolidayTo(String holidayTo) {
                this.holidayTo = holidayTo;
            }

            public Object getPreHolidayName() {
                return preHolidayName;
            }

            public void setPreHolidayName(Object preHolidayName) {
                this.preHolidayName = preHolidayName;
            }

            public Object getStrHolidayFrom() {
                return strHolidayFrom;
            }

            public void setStrHolidayFrom(Object strHolidayFrom) {
                this.strHolidayFrom = strHolidayFrom;
            }

            public Integer getEmpId() {
                return empId;
            }

            public void setEmpId(Integer empId) {
                this.empId = empId;
            }

            public Integer getMode() {
                return mode;
            }

            public void setMode(Integer mode) {
                this.mode = mode;
            }

            public Object getHolidayType() {
                return holidayType;
            }

            public void setHolidayType(Object holidayType) {
                this.holidayType = holidayType;
            }

            public Integer getStateId() {
                return stateId;
            }

            public void setStateId(Integer stateId) {
                this.stateId = stateId;
            }

            public Object getState() {
                return state;
            }

            public void setState(Object state) {
                this.state = state;
            }

            public String getHolidayDate() {
                return holidayDate;
            }

            public void setHolidayDate(String holidayDate) {
                this.holidayDate = holidayDate;
            }

            public String getHolidayTypeValue() {
                return holidayTypeValue;
            }

            public void setHolidayTypeValue(String holidayTypeValue) {
                this.holidayTypeValue = holidayTypeValue;
            }

            public Object getStrHolidayTo() {
                return strHolidayTo;
            }

            public void setStrHolidayTo(Object strHolidayTo) {
                this.strHolidayTo = strHolidayTo;
            }

            public Boolean getCanBeEdit() {
                return canBeEdit;
            }

            public void setCanBeEdit(Boolean canBeEdit) {
                this.canBeEdit = canBeEdit;
            }

            public Object getMonthName() {
                return monthName;
            }

            public void setMonthName(Object monthName) {
                this.monthName = monthName;
            }

            public Object getErrorMessage() {
                return errorMessage;
            }

            public void setErrorMessage(Object errorMessage) {
                this.errorMessage = errorMessage;
            }

            public Boolean getIsError() {
                return isError;
            }

            public void setIsError(Boolean isError) {
                this.isError = isError;
            }

            public Integer getYear() {
                return year;
            }

            public void setYear(Integer year) {
                this.year = year;
            }

            public Integer getMonth() {
                return month;
            }

            public void setMonth(Integer month) {
                this.month = month;
            }

            public Integer getDay() {
                return day;
            }

            public void setDay(Integer day) {
                this.day = day;
            }

        }

    }

}
