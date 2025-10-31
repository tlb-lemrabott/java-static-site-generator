package com.sitegenerator.code.service;

/**
 * Exception thrown when site generation fails.
 */
public class SiteGenerationException extends Exception {
    
    public SiteGenerationException(String message) {
        super(message);
    }
    
    public SiteGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
