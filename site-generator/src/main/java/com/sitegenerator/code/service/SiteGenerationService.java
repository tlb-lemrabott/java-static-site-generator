package com.sitegenerator.code.service;

import com.sitegenerator.code.domain.model.Site;
import com.sitegenerator.code.dto.SiteGenerationResponse;

/**
 * Service interface for site generation operations.
 * Follows the Service Layer pattern for business logic separation.
 */
public interface SiteGenerationService {
    
    /**
     * Generates static HTML files from a site description.
     * 
     * @param site the site description containing pages and sections
     * @return response containing generation metadata
     * @throws SiteGenerationException if generation fails
     */
    SiteGenerationResponse generateSite(Site site) throws SiteGenerationException;
    
    /**
     * Validates the site structure before generation.
     * 
     * @param site the site to validate
     * @throws ValidationException if validation fails
     */
    void validateSite(Site site) throws ValidationException;
}
