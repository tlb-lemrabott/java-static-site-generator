package com.sitegenerator.code.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a complete website structure.
 * This is the root entity that contains all pages and site metadata.
 */
public class Site {
    
    @NotBlank(message = "Site name cannot be blank")
    @Size(min = 1, max = 100, message = "Site name must be between 1 and 100 characters")
    @JsonProperty("siteName")
    private String siteName;
    
    @NotEmpty(message = "Site must have at least one page")
    @Valid
    @JsonProperty("pages")
    private List<Page> pages;
    
    // Default constructor for Jackson
    public Site() {}
    
    public Site(String siteName, List<Page> pages) {
        this.siteName = siteName;
        this.pages = pages;
    }
    
    public String getSiteName() {
        return siteName;
    }
    
    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
    
    public List<Page> getPages() {
        return pages;
    }
    
    public void setPages(List<Page> pages) {
        this.pages = pages;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Site site = (Site) o;
        return Objects.equals(siteName, site.siteName) && Objects.equals(pages, site.pages);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(siteName, pages);
    }
    
    @Override
    public String toString() {
        return "Site{" +
                "siteName='" + siteName + '\'' +
                ", pages=" + pages +
                '}';
    }
}
