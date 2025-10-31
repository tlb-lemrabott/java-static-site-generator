package com.sitebuilder.code.service;

/**
 * Exception thrown when site building fails.
 */
public class SiteBuildException extends Exception {
    
    public SiteBuildException(String message) {
        super(message);
    }
    
    public SiteBuildException(String message, Throwable cause) {
        super(message, cause);
    }
}
