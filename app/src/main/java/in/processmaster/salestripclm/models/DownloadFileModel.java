package in.processmaster.salestripclm.models;

import java.io.Serializable;

public class DownloadFileModel implements Serializable {

    //Getter setter of downloaded file
    String filePath;
    String fileName;
    String fileUrl;
    String downloadType;
    int brandId;
    String brandName;
    int eDetailingId;
    String setContentType;
    int fileId;

    // Favourite section
    String favFilePath;
    boolean favFile;
    String favFileName;

    public String getFavFileName() {
        return favFileName;
    }

    public void setFavFileName(String favFileName) {
        this.favFileName = favFileName;
    }

    public String getFavFilePath() {
        return favFilePath;
    }

    public void setFavFilePath(String favFilePath) {
        this.favFilePath = favFilePath;
    }

    public boolean isFavFile() {
        return favFile;
    }

    public boolean getFavFile() {
        return favFile;
    }

    public void setFavFile(boolean favFile) {
        this.favFile = favFile;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }




    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public DownloadEdetail_model.Data.EDetailingImages getModel() {
        return model;
    }

    public void setModel(DownloadEdetail_model.Data.EDetailingImages model) {
        this.model = model;
    }

    DownloadEdetail_model.Data.EDetailingImages model;

    public String getFileDirectoryPath() {
        return fileDirectoryPath;
    }

    public void setFileDirectoryPath(String fileDirectoryPath) {
        this.fileDirectoryPath = fileDirectoryPath;
    }

    String fileDirectoryPath;

    public String getSetContentType() {
        return setContentType;
    }

    public void setSetContentType(String setContentType) {
        this.setContentType = setContentType;
    }



    public int geteDetailingId() {
        return eDetailingId;
    }

    public void seteDetailingId(int eDetailingId) {
        this.eDetailingId = eDetailingId;
    }

    public String getZipExtractFilePath() {
        return zipExtractFilePath;
    }

    public void setZipExtractFilePath(String zipExtractFilePath) {
        this.zipExtractFilePath = zipExtractFilePath;
    }

    int doctorId;
    String zipExtractFilePath;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
}
