package com.sitegenerator.code.service.impl;

import com.sitegenerator.code.domain.model.Page;
import com.sitegenerator.code.domain.model.Section;
import com.sitegenerator.code.domain.model.Site;
import com.sitegenerator.code.dto.SiteGenerationResponse;
import com.sitegenerator.code.service.SiteGenerationException;
import com.sitegenerator.code.service.SiteGenerationService;
import com.sitegenerator.code.service.ValidationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implementation of site generation service.
 * Handles the core business logic for converting site descriptions to HTML files.
 */
@Service
public class SiteGenerationServiceImpl implements SiteGenerationService {
    
    private final TemplateEngine templateEngine;
    
    @Value("${site.generator.output.path:output}")
    private String outputPath;
    
    // Supported section types
    private static final Set<String> SUPPORTED_SECTION_TYPES = Set.of(
        "hero", "skills", "form", "text", "image", "contact", "about"
    );
    
    public SiteGenerationServiceImpl(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    @Override
    public SiteGenerationResponse generateSite(Site site) throws SiteGenerationException {
        try {
            // Validate site structure
            validateSite(site);
            
            // Create output directory
            Path siteOutputPath = createSiteOutputDirectory(site.getSiteName());
            
            // Generate pages
            int pagesGenerated = generatePages(site, siteOutputPath);
            
            // Generate assets
            generateAssets(siteOutputPath);
            
            // Generate site config
            generateSiteConfig(site, siteOutputPath);
            
            return new SiteGenerationResponse(
                site.getSiteName(),
                siteOutputPath.toString(),
                pagesGenerated,
                "Site generated successfully"
            );
            
        } catch (Exception e) {
            throw new SiteGenerationException("Failed to generate site: " + e.getMessage(), e);
        }
    }
    
    @Override
    public void validateSite(Site site) throws ValidationException {
        if (site == null) {
            throw new ValidationException("Site cannot be null");
        }
        
        if (StringUtils.isBlank(site.getSiteName())) {
            throw new ValidationException("Site name cannot be blank");
        }
        
        if (site.getPages() == null || site.getPages().isEmpty()) {
            throw new ValidationException("Site must have at least one page");
        }
        
        // Validate each page
        for (Page page : site.getPages()) {
            validatePage(page);
        }
        
        // Check for duplicate slugs
        long uniqueSlugs = site.getPages().stream()
            .map(Page::getSlug)
            .distinct()
            .count();
        
        if (uniqueSlugs != site.getPages().size()) {
            throw new ValidationException("Page slugs must be unique");
        }
    }
    
    private void validatePage(Page page) throws ValidationException {
        if (StringUtils.isBlank(page.getTitle())) {
            throw new ValidationException("Page title cannot be blank");
        }
        
        if (StringUtils.isBlank(page.getSlug())) {
            throw new ValidationException("Page slug cannot be blank");
        }
        
        if (page.getSections() == null || page.getSections().isEmpty()) {
            throw new ValidationException("Page must have at least one section");
        }
        
        // Validate each section
        for (Section section : page.getSections()) {
            validateSection(section);
        }
    }
    
    private void validateSection(Section section) throws ValidationException {
        if (StringUtils.isBlank(section.getType())) {
            throw new ValidationException("Section type cannot be blank");
        }
        
        if (!SUPPORTED_SECTION_TYPES.contains(section.getType())) {
            throw new ValidationException("Unsupported section type: " + section.getType() + 
                ". Supported types: " + SUPPORTED_SECTION_TYPES);
        }
    }
    
    private Path createSiteOutputDirectory(String siteName) throws IOException {
        Path sitePath = Paths.get(outputPath, siteName);
        Files.createDirectories(sitePath);
        
        // Create assets subdirectory
        Path assetsPath = sitePath.resolve("assets");
        Files.createDirectories(assetsPath);
        
        return sitePath;
    }
    
    private int generatePages(Site site, Path outputPath) throws IOException {
        int pagesGenerated = 0;
        
        for (Page page : site.getPages()) {
            String htmlContent = generatePageHtml(page, site);
            
            String fileName = "index".equals(page.getSlug()) ? "index.html" : page.getSlug() + ".html";
            Path pagePath = outputPath.resolve(fileName);
            
            try (FileWriter writer = new FileWriter(pagePath.toFile())) {
                writer.write(htmlContent);
            }
            
            pagesGenerated++;
        }
        
        return pagesGenerated;
    }
    
    private String generatePageHtml(Page page, Site site) {
        Context context = new Context();
        context.setVariable("site", site);
        context.setVariable("page", page);
        context.setVariable("sections", page.getSections());
        
        return templateEngine.process("page-template", context);
    }
    
    private void generateAssets(Path outputPath) throws IOException {
        Path assetsPath = outputPath.resolve("assets");
        
        // Generate CSS
        generateCss(assetsPath);
        
        // Generate JavaScript
        generateJavaScript(assetsPath);
    }
    
    private void generateCss(Path assetsPath) throws IOException {
        String cssContent = """
            /* Generated CSS for static site */
            body {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                line-height: 1.6;
                margin: 0;
                padding: 0;
                color: #333;
            }
            
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 20px;
            }
            
            .hero {
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                color: white;
                padding: 100px 0;
                text-align: center;
            }
            
            .hero h1 {
                font-size: 3rem;
                margin-bottom: 1rem;
            }
            
            .hero p {
                font-size: 1.2rem;
                opacity: 0.9;
            }
            
            .section {
                padding: 60px 0;
            }
            
            .skills {
                background-color: #f8f9fa;
            }
            
            .skills ul {
                list-style: none;
                padding: 0;
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
            }
            
            .skills li {
                background: #007bff;
                color: white;
                padding: 8px 16px;
                border-radius: 20px;
                font-size: 0.9rem;
            }
            
            .form {
                background-color: #f8f9fa;
            }
            
            .form-group {
                margin-bottom: 20px;
            }
            
            .form-group label {
                display: block;
                margin-bottom: 5px;
                font-weight: 500;
            }
            
            .form-group input,
            .form-group textarea {
                width: 100%;
                padding: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-size: 1rem;
            }
            
            .form-group textarea {
                height: 120px;
                resize: vertical;
            }
            
            .btn {
                background: #007bff;
                color: white;
                padding: 12px 24px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 1rem;
            }
            
            .btn:hover {
                background: #0056b3;
            }
            """;
        
        Path cssPath = assetsPath.resolve("styles.css");
        try (FileWriter writer = new FileWriter(cssPath.toFile())) {
            writer.write(cssContent);
        }
    }
    
    private void generateJavaScript(Path assetsPath) throws IOException {
        String jsContent = """
            // Generated JavaScript for static site
            document.addEventListener('DOMContentLoaded', function() {
                console.log('Static site loaded successfully');
                
                // Add smooth scrolling for anchor links
                document.querySelectorAll('a[href^="#"]').forEach(anchor => {
                    anchor.addEventListener('click', function (e) {
                        e.preventDefault();
                        const target = document.querySelector(this.getAttribute('href'));
                        if (target) {
                            target.scrollIntoView({
                                behavior: 'smooth'
                            });
                        }
                    });
                });
                
                // Add form handling
                const forms = document.querySelectorAll('form');
                forms.forEach(form => {
                    form.addEventListener('submit', function(e) {
                        e.preventDefault();
                        alert('Form submitted! (This is a demo)');
                    });
                });
            });
            """;
        
        Path jsPath = assetsPath.resolve("script.js");
        try (FileWriter writer = new FileWriter(jsPath.toFile())) {
            writer.write(jsContent);
        }
    }
    
    private void generateSiteConfig(Site site, Path outputPath) throws IOException {
        Map<String, Object> config = new HashMap<>();
        config.put("siteName", site.getSiteName());
        config.put("pages", site.getPages().size());
        config.put("generatedAt", System.currentTimeMillis());
        
        // Convert to JSON (simplified for now)
        String configJson = String.format("""
            {
                "siteName": "%s",
                "pages": %d,
                "generatedAt": %d
            }
            """, site.getSiteName(), site.getPages().size(), System.currentTimeMillis());
        
        Path configPath = outputPath.resolve("config.json");
        try (FileWriter writer = new FileWriter(configPath.toFile())) {
            writer.write(configJson);
        }
    }
}
