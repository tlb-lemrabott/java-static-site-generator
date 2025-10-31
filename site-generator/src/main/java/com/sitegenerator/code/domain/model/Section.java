package com.sitegenerator.code.domain.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Domain model representing a section within a page.
 * Sections are polymorphic and can have different types (hero, skills, form, etc.)
 * with varying content structures.
 */
public class Section {
    
    @NotBlank(message = "Section type cannot be blank")
    @Size(min = 1, max = 50, message = "Section type must be between 1 and 50 characters")
    @JsonProperty("type")
    private String type;
    
    // Flexible content structure - different section types will have different properties
    @JsonProperty("heading")
    private String heading;
    
    @JsonProperty("text")
    private String text;
    
    @JsonProperty("items")
    private List<String> items;
    
    @JsonProperty("fields")
    private List<String> fields;
    
    @JsonProperty("content")
    private Map<String, Object> content;
    
    // Default constructor for Jackson
    public Section() {}
    
    public Section(String type) {
        this.type = type;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getHeading() {
        return heading;
    }
    
    public void setHeading(String heading) {
        this.heading = heading;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public List<String> getItems() {
        return items;
    }
    
    public void setItems(List<String> items) {
        this.items = items;
    }
    
    public List<String> getFields() {
        return fields;
    }
    
    public void setFields(List<String> fields) {
        this.fields = fields;
    }
    
    public Map<String, Object> getContent() {
        return content;
    }
    
    public void setContent(Map<String, Object> content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(type, section.type) && 
               Objects.equals(heading, section.heading) && 
               Objects.equals(text, section.text) && 
               Objects.equals(items, section.items) && 
               Objects.equals(fields, section.fields) && 
               Objects.equals(content, section.content);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, heading, text, items, fields, content);
    }
    
    @Override
    public String toString() {
        return "Section{" +
                "type='" + type + '\'' +
                ", heading='" + heading + '\'' +
                ", text='" + text + '\'' +
                ", items=" + items +
                ", fields=" + fields +
                ", content=" + content +
                '}';
    }
}
