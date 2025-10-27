package com.sitegenerator.code.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for site generation response.
 * Contains metadata about the generated site.
 */
public class SiteGenerationResponse {
    
    @NotBlank
    @JsonProperty("siteName")
    private String siteName;
    
    @NotBlank
    @JsonProperty("outputPath")
    private String outputPath;
    
    @JsonProperty("pagesGenerated")
    private int pagesGenerated;
    
    @JsonProperty("message")
    private String message;
    
    // Default constructor for Jackson
    public SiteGenerationResponse() {}
    
    public SiteGenerationResponse(String siteName, String outputPath, int pagesGenerated, String message) {
        this.siteName = siteName;
        this.outputPath = outputPath;
        this.pagesGenerated = pagesGenerated;
        this.message = message;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public String getOutputPath() {
        return outputPath;
    }
    
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
    
    public int getPagesGenerated() {
        return pagesGenerated;
    }
    
    public void setPagesGenerated(int pagesGenerated) {
        this.pagesGenerated = pagesGenerated;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "SiteGenerationResponse{" +
                "siteName='" + siteName + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", pagesGenerated=" + pagesGenerated +
                ", message='" + message + '\'' +
                '}';
    }
}
