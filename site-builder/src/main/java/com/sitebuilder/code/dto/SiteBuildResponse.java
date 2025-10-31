package com.sitebuilder.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for site build response.
 * Contains metadata about the built site.
 */
public class SiteBuildResponse {
    
    @JsonProperty("siteName")
    private String siteName;
    
    @JsonProperty("buildPath")
    private String buildPath;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("buildTime")
    private long buildTime;
    
    @JsonProperty("fileCount")
    private int fileCount;
    
    // Default constructor for Jackson
    public SiteBuildResponse() {}
    
    public SiteBuildResponse(String siteName, String buildPath, String status, String message, long buildTime, int fileCount) {
        this.siteName = siteName;
        this.buildPath = buildPath;
        this.status = status;
        this.message = message;
        this.buildTime = buildTime;
        this.fileCount = fileCount;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public String getBuildPath() {
        return buildPath;
    }
    
    public void setBuildPath(String buildPath) {
        this.buildPath = buildPath;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public long getBuildTime() {
        return buildTime;
    }
    
    public void setBuildTime(long buildTime) {
        this.buildTime = buildTime;
    }
    
    public int getFileCount() {
        return fileCount;
    }
    
    public void setFileCount(int fileCount) {
        this.fileCount = fileCount;
    }
    
    @Override
    public String toString() {
        return "SiteBuildResponse{" +
                "siteName='" + siteName + '\'' +
                ", buildPath='" + buildPath + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", buildTime=" + buildTime +
                ", fileCount=" + fileCount +
                '}';
    }
}
