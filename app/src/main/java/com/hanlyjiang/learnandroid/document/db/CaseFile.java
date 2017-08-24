package com.hanlyjiang.learnandroid.document.db;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "CASE_FILE".
 */
public class CaseFile {

    private Long id;
    /** Not-null value. */
    private String fileId;
    /** Not-null value. */
    private String fileName;
     /** 文件下载路径 **/
    private String fileUrl;
     /** 文件本地路径 **/
    private String fileLocalPath;
     /** 下载时间 **/
    private java.util.Date downloadDate;
     /** 最后更新事件 **/
    private java.util.Date updateDate;
     /** 最后更新md5 **/
    private String md5;
     /** 下载状态 **/
    private String downloadState;

    public CaseFile() {
    }

    public CaseFile(Long id) {
        this.id = id;
    }

    public CaseFile(Long id, String fileId, String fileName, String fileUrl, String fileLocalPath, java.util.Date downloadDate, java.util.Date updateDate, String md5, String downloadState) {
        this.id = id;
        this.fileId = fileId;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileLocalPath = fileLocalPath;
        this.downloadDate = downloadDate;
        this.updateDate = updateDate;
        this.md5 = md5;
        this.downloadState = downloadState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getFileId() {
        return fileId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /** Not-null value. */
    public String getFileName() {
        return fileName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileLocalPath() {
        return fileLocalPath;
    }

    public void setFileLocalPath(String fileLocalPath) {
        this.fileLocalPath = fileLocalPath;
    }

    public java.util.Date getDownloadDate() {
        return downloadDate;
    }

    public void setDownloadDate(java.util.Date downloadDate) {
        this.downloadDate = downloadDate;
    }

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(String downloadState) {
        this.downloadState = downloadState;
    }

}
