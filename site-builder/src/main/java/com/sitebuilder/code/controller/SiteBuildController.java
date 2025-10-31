package com.sitebuilder.code.controller;

import com.sitebuilder.code.dto.SiteBuildResponse;
import com.sitebuilder.code.service.SiteBuildException;
import com.sitebuilder.code.service.SiteBuildService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for site building operations.
 * Provides endpoints for building and managing static sites.
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class SiteBuildController {
    
    private final SiteBuildService siteBuildService;
    
    public SiteBuildController(SiteBuildService siteBuildService) {
        this.siteBuildService = siteBuildService;
    }
    
    /**
     * Builds a site from the generated output.
     * 
     * @param siteName the name of the site to build
     * @return response with build details
     */
    @GetMapping("/build")
    public ResponseEntity<?> buildSite(@RequestParam("siteName") String siteName) {
        try {
            SiteBuildResponse response = siteBuildService.buildSite(siteName);
            return ResponseEntity.ok(response);
            
        } catch (SiteBuildException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("Build error: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Unexpected error: " + e.getMessage()));
        }
    }
    
    /**
     * Lists all available sites for building.
     * 
     * @return array of available site names
     */
    @GetMapping("/sites")
    public ResponseEntity<?> listAvailableSites() {
        try {
            String[] sites = siteBuildService.listAvailableSites();
            Map<String, Object> response = new HashMap<>();
            response.put("sites", sites);
            response.put("count", sites.length);
            response.put("message", "Available sites retrieved successfully");
            
            return ResponseEntity.ok(response);
            
        } catch (SiteBuildException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Failed to list sites: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("Unexpected error: " + e.getMessage()));
        }
    }
    
    /**
     * Gets the build status for a specific site.
     * 
     * @param siteName the name of the site
     * @return build status information
     */
    @GetMapping("/status/{siteName}")
    public ResponseEntity<?> getBuildStatus(@PathVariable String siteName) {
        try {
            SiteBuildResponse response = siteBuildService.getBuildStatus(siteName);
            return ResponseEntity.ok(response);
            
        } catch (SiteBuildException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse("Status check error: " + e.getMessage()));
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
        status.put("service", "site-builder");
        status.put("timestamp", String.valueOf(System.currentTimeMillis()));
        
        return ResponseEntity.ok(status);
    }
    
    /**
     * Get deployment information.
     * 
     * @return deployment options and instructions
     */
    @GetMapping("/deployment-info")
    public ResponseEntity<Map<String, Object>> getDeploymentInfo() {
        Map<String, Object> info = new HashMap<>();
        
        Map<String, String> platforms = new HashMap<>();
        platforms.put("GitHub Pages", "Push to GitHub repository and enable Pages");
        platforms.put("Netlify", "Drag and drop build directory or connect repository");
        platforms.put("Vercel", "Use Vercel CLI or connect repository");
        platforms.put("Apache/Nginx", "Upload files to web server");
        
        info.put("supportedPlatforms", platforms);
        info.put("message", "Built sites include configuration files for all major platforms");
        
        return ResponseEntity.ok(info);
    }
    
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        error.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return error;
    }
}
