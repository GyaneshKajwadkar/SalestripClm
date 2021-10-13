package in.processmaster.salestripclm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel
{
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("empId")
    @Expose
    private Integer empId;
    @SerializedName("isFirstLogin")
    @Expose
    private String isFirstLogin;
    @SerializedName("menuList")
    @Expose
    private String menuList;
    @SerializedName("employeeObj")
    @Expose
    private String employeeObj;
    @SerializedName("configurationSetting")
    @Expose
    private String configurationSetting;
    @SerializedName("expiredAt")
    @Expose
    private String expiredAt;
    @SerializedName("roleType")
    @Expose
    private String roleType;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("compName")
    @Expose
    private String compName;
    @SerializedName("industryType")
    @Expose
    private String industryType;
    @SerializedName("empName")
    @Expose
    private String empName;
    @SerializedName("compAddress")
    @Expose
    private String compAddress;
    @SerializedName("emailId")
    @Expose
    private String emailId;
    @SerializedName("hierarchyId")
    @Expose
    private Integer hierarchyId;
    @SerializedName("companyLogo")
    @Expose
    private String companyLogo;
    @SerializedName("isCheckIn")
    @Expose
    private Boolean isCheckIn;
    @SerializedName("currentDate")
    @Expose
    private String currentDate;
    @SerializedName("isMpin")
    @Expose
    private Boolean isMpin;
    @SerializedName("as:client_id")
    @Expose
    private String asClientId;
    @SerializedName("userName")
    @Expose
    private String userName;
    @SerializedName("compCode")
    @Expose
    private String compCode;
    @SerializedName("runsIn")
    @Expose
    private String runsIn;
    @SerializedName(".issued")
    @Expose
    private String issued;
    @SerializedName(".expires")
    @Expose
    private String expires;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getEmpId() {
        return empId;
    }

    public void setEmpId(Integer empId) {
        this.empId = empId;
    }

    public String getIsFirstLogin() {
        return isFirstLogin;
    }

    public void setIsFirstLogin(String isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    public String getMenuList() {
        return menuList;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public String getEmployeeObj() {
        return employeeObj;
    }

    public void setEmployeeObj(String employeeObj) {
        this.employeeObj = employeeObj;
    }

    public String getConfigurationSetting() {
        return configurationSetting;
    }

    public void setConfigurationSetting(String configurationSetting) {
        this.configurationSetting = configurationSetting;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getRoleType() {
        return roleType;
    }

    public void setRoleType(String roleType) {
        this.roleType = roleType;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getCompAddress() {
        return compAddress;
    }

    public void setCompAddress(String compAddress) {
        this.compAddress = compAddress;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(Integer hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public String getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(String companyLogo) {
        this.companyLogo = companyLogo;
    }

    public Boolean getIsCheckIn() {
        return isCheckIn;
    }

    public void setIsCheckIn(Boolean isCheckIn) {
        this.isCheckIn = isCheckIn;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Boolean getIsMpin() {
        return isMpin;
    }

    public void setIsMpin(Boolean isMpin) {
        this.isMpin = isMpin;
    }

    public String getAsClientId() {
        return asClientId;
    }

    public void setAsClientId(String asClientId) {
        this.asClientId = asClientId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCompCode() {
        return compCode;
    }

    public void setCompCode(String compCode) {
        this.compCode = compCode;
    }

    public String getRunsIn() {
        return runsIn;
    }

    public void setRunsIn(String runsIn) {
        this.runsIn = runsIn;
    }

    public String getIssued() {
        return issued;
    }

    public void setIssued(String issued) {
        this.issued = issued;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

}