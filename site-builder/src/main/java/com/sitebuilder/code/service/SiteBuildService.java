package com.sitebuilder.code.service;

import com.sitebuilder.code.dto.SiteBuildResponse;

/**
 * Service interface for site building operations.
 * Handles packaging and optimization of generated sites.
 */
public interface SiteBuildService {
    
    /**
     * Builds a site from the generated output.
     * 
     * @param siteName the name of the site to build
     * @return response containing build metadata
     * @throws SiteBuildException if build fails
     */
    SiteBuildResponse buildSite(String siteName) throws SiteBuildException;
    
    /**
     * Lists available sites for building.
     * 
     * @return array of available site names
     * @throws SiteBuildException if listing fails
     */
    String[] listAvailableSites() throws SiteBuildException;
    
    /**
     * Gets build status for a specific site.
     * 
     * @param siteName the name of the site
     * @return build status information
     * @throws SiteBuildException if status check fails
     */
    SiteBuildResponse getBuildStatus(String siteName) throws SiteBuildException;
}
