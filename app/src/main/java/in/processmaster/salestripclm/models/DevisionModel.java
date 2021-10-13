package in.processmaster.salestripclm.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class DevisionModel  {
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

    public class Data implements Serializable{

        @SerializedName("eDetailingList")
        @Expose
        private ArrayList<EDetailing> eDetailingList = null;

        public ArrayList<EDetailing> geteDetailingList() {
            return eDetailingList;
        }

        public void seteDetailingList(ArrayList<EDetailing> eDetailingList) {
            this.eDetailingList = eDetailingList;
        }
        public class EDetailing implements Serializable {

            String filePath;
            int isSaved;

            public String getFilePath() {
                return filePath;
            }

            public void setFilePath(String filePath) {
                this.filePath = filePath;
            }

            public int getIsSaved() {
                return isSaved;
            }

            public void setIsSaved(int isSaved) {
                this.isSaved = isSaved;
            }


            @SerializedName("eDetailId")
            @Expose
            private Integer eDetailId;
            @SerializedName("brandId")
            @Expose
            private Integer brandId;
            @SerializedName("empId")
            @Expose
            private Integer empId;
            @SerializedName("mode")
            @Expose
            private Integer mode;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("isPublish")
            @Expose
            private Boolean isPublish;
            @SerializedName("eretailDetailList")
            @Expose
            private Object eretailDetailList;
            @SerializedName("eretailReferenceList")
            @Expose
            private Object eretailReferenceList;
            @SerializedName("brandName")
            @Expose
            private String brandName;
            @SerializedName("divisionName")
            @Expose
            private String divisionName;
            @SerializedName("reviseId")
            @Expose
            private Integer reviseId;
            @SerializedName("isReviseData")
            @Expose
            private Boolean isReviseData;

            public Integer geteDetailId() {
                return eDetailId;
            }

            public void seteDetailId(Integer eDetailId) {
                this.eDetailId = eDetailId;
            }

            public Integer getBrandId() {
                return brandId;
            }

            public void setBrandId(Integer brandId) {
                this.brandId = brandId;
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

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Boolean getIsPublish() {
                return isPublish;
            }

            public void setIsPublish(Boolean isPublish) {
                this.isPublish = isPublish;
            }

            public Object getEretailDetailList() {
                return eretailDetailList;
            }

            public void setEretailDetailList(Object eretailDetailList) {
                this.eretailDetailList = eretailDetailList;
            }

            public Object getEretailReferenceList() {
                return eretailReferenceList;
            }

            public void setEretailReferenceList(Object eretailReferenceList) {
                this.eretailReferenceList = eretailReferenceList;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            public String getDivisionName() {
                return divisionName;
            }

            public void setDivisionName(String divisionName) {
                this.divisionName = divisionName;
            }

            public Integer getReviseId() {
                return reviseId;
            }

            public void setReviseId(Integer reviseId) {
                this.reviseId = reviseId;
            }

            public Boolean getIsReviseData() {
                return isReviseData;
            }

            public void setIsReviseData(Boolean isReviseData) {
                this.isReviseData = isReviseData;
            }

        }
    }

    public class ErrorObj implements Serializable{

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