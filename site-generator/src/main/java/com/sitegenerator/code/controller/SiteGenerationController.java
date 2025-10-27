package com.sitegenerator.code.controller;

import com.sitegenerator.code.domain.model.Site;
import com.sitegenerator.code.dto.SiteGenerationResponse;
import com.sitegenerator.code.service.SiteGenerationException;
import com.sitegenerator.code.service.SiteGenerationService;
import com.sitegenerator.code.service.ValidationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for site generation operations.
 * Provides endpoints for generating static sites from JSON descriptions.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class SiteGenerationController {
    
    private final SiteGenerationService siteGenerationService;
    
    public SiteGenerationController(SiteGenerationService siteGenerationService) {
        this.siteGenerationService = siteGenerationService;
    }
    
    /**
     * Generates a static site from a JSON file upload.
     * 
     * @param file the JSON file containing site description
     * @return response with generation details
     */
    @PostMapping("/generate")
    public ResponseEntity<?> generateSite(@RequestParam("file") MultipartFile file) {
        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("File cannot be empty"));
            }
            
            if (!file.getOriginalFilename().endsWith(".json")) {
                return ResponseEntity.badRequest()
                    .body(createErrorResponse("File must be a JSON file"));
            }
            
            // Parse JSON to Site object
            String jsonContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            Site site = parseJsonToSite(jsonContent);
            
            // Generate site
            SiteGenerationResponse response = siteGenerationService.generateSite(site);
            
            return ResponseEntity.ok(response);
            
        } catch (ValidationException e) {
            return ResponseEntity.badRequest()
                .body(createErrorResponse("Validation error: " + e.getMessage()));
        } catch (SiteGenerationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Generation error: " + e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("File processing error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Unexpected error: " + e.getMessage()));
        }
    }
    
    /**
     * Generates a static site from JSON content in request body.
     * 
     * @param site the site description
     * @return response with generation details
     */
    @PostMapping("/generate/json")
    public ResponseEntity<?> generateSiteFromJson(@Valid @RequestBody Site site) {
        try {
            SiteGenerationResponse response = siteGenerationService.generateSite(site);
            return ResponseEntity.ok(response);
            
        } catch (SiteGenerationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Generation error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Unexpected error: " + e.getMessage()));
        }
    }
    
    /**
     * Health check endpoint.
     * 
     * @return service status
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "site-generator");
        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * Get supported section types.
     * 
     * @return list of supported section types
     */
    @GetMapping("/section-types")
    public ResponseEntity<Map<String, Object>> getSupportedSectionTypes() {
        Map<String, Object> response = new HashMap<>();
        response.put("supportedTypes", new String[]{
            "hero", "skills", "form", "text", "image", "contact", "about"
        });
        response.put("description", "Supported section types for site generation");
        
        return ResponseEntity.ok(response);
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }
    
    private Site parseJsonToSite(String jsonContent) throws Exception {
        // Simple JSON parsing - in production, use Jackson ObjectMapper
        // This is a placeholder implementation
        throw new UnsupportedOperationException("JSON parsing not implemented yet. Use /generate/json endpoint instead.");
    }
}
