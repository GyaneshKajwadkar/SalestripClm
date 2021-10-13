package in.processmaster.salestripclm.models;

import java.util.ArrayList;

public class VisualAdsModel_Send {
    String startDate,endDate,empId,doctorId,brandId;
    boolean isEnd;
    ArrayList<childData>childDataArray;

    public ArrayList<childData> getChildDataArray() {
        return childDataArray;
    }

    public void setChildDataArray(ArrayList<childData> childDataArray) {
        this.childDataArray = childDataArray;
    }

    public boolean isEnd()
    {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

    //   String startShowingTime,endShowingTime;
    int id;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static class childData
    {
        String fileId, viewTime;
        boolean isLike;
        String comment;

        public String getFileId() {
            return fileId;
        }

        public void setFileId(String fileId) {
            this.fileId = fileId;
        }

        public String getViewTime() {
            return viewTime;
        }

        public void setViewTime(String viewTime) {
            this.viewTime = viewTime;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }
    }


}
