package in.processmaster.salestripclm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

class TeamsModell {

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

    public class Data {

        @SerializedName("employeeList")
        @Expose
        private ArrayList<Employee> employeeList = null;

        public ArrayList<Employee> getEmployeeList() {
            return employeeList;
        }

        public void setEmployeeList(ArrayList<Employee> employeeList) {
            this.employeeList = employeeList;
        }
        public class Employee {

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
            private String hierachyType;
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
            private String divisionName;
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
            @SerializedName("zoneId")
            @Expose
            private Integer zoneId;
            @SerializedName("strDeactivationDate")
            @Expose
            private Object strDeactivationDate;

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

            public String getHierachyType() {
                return hierachyType;
            }

            public void setHierachyType(String hierachyType) {
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

            public String getDivisionName() {
                return divisionName;
            }

            public void setDivisionName(String divisionName) {
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

            public Integer getZoneId() {
                return zoneId;
            }

            public void setZoneId(Integer zoneId) {
                this.zoneId = zoneId;
            }

            public Object getStrDeactivationDate() {
                return strDeactivationDate;
            }

            public void setStrDeactivationDate(Object strDeactivationDate) {
                this.strDeactivationDate = strDeactivationDate;
            }

        }
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

}
