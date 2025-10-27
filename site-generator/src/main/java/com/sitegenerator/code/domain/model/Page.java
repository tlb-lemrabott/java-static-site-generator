package com.sitegenerator.code.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

/**
 * Domain model representing a single page within a website.
 * Each page has a title, slug for URL generation, and a list of sections.
 */
public class Page {
    
    @NotBlank(message = "Page title cannot be blank")
    @Size(min = 1, max = 200, message = "Page title must be between 1 and 200 characters")
    @JsonProperty("title")
    private String title;
    
    @NotBlank(message = "Page slug cannot be blank")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Page slug must contain only lowercase letters, numbers, and hyphens")
    @Size(min = 1, max = 100, message = "Page slug must be between 1 and 100 characters")
    @JsonProperty("slug")
    private String slug;
    
    @NotEmpty(message = "Page must have at least one section")
    @Valid
    @JsonProperty("sections")
    private List<Section> sections;
    
    // Default constructor for Jackson
    public Page() {}
    
    public Page(String title, String slug, List<Section> sections) {
        this.title = title;
        this.slug = slug;
        this.sections = sections;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getSlug() {
        return slug;
    }
    
    public void setSlug(String slug) {
        this.slug = slug;
    }
    
    public List<Section> getSections() {
        return sections;
    }
    
    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equals(title, page.title) && 
               Objects.equals(slug, page.slug) && 
               Objects.equals(sections, page.sections);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(title, slug, sections);
    }
    
    @Override
    public String toString() {
        return "Page{" +
                "title='" + title + '\'' +
                ", slug='" + slug + '\'' +
                ", sections=" + sections +
                '}';
    }
}
