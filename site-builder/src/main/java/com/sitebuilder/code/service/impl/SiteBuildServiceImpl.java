package com.sitebuilder.code.service.impl;

import com.sitebuilder.code.dto.SiteBuildResponse;
import com.sitebuilder.code.service.SiteBuildException;
import com.sitebuilder.code.service.SiteBuildService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of site build service.
 * Handles packaging and optimization of generated sites for deployment.
 */
@Service
public class SiteBuildServiceImpl implements SiteBuildService {
    
    @Value("${site.builder.input.path:output}")
    private String inputPath;
    
    @Value("${site.builder.build.path:build}")
    private String buildPath;
    
    @Override
    public SiteBuildResponse buildSite(String siteName) throws SiteBuildException {
        if (StringUtils.isBlank(siteName)) {
            throw new SiteBuildException("Site name cannot be blank");
        }
        
        long startTime = System.currentTimeMillis();
        
        try {
            // Validate input site exists
            Path inputSitePath = Paths.get(inputPath, siteName);
            if (!Files.exists(inputSitePath)) {
                throw new SiteBuildException("Site '" + siteName + "' not found in input directory");
            }
            
            // Create build directory
            Path buildSitePath = createBuildDirectory(siteName);
            
            // Copy and optimize files
            int fileCount = copyAndOptimizeFiles(inputSitePath, buildSitePath);
            
            // Generate deployment files
            generateDeploymentFiles(buildSitePath, siteName);
            
            long buildTime = System.currentTimeMillis() - startTime;
            
            return new SiteBuildResponse(
                siteName,
                buildSitePath.toString(),
                "SUCCESS",
                "Site built successfully",
                buildTime,
                fileCount
            );
            
        } catch (IOException e) {
            throw new SiteBuildException("Failed to build site: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String[] listAvailableSites() throws SiteBuildException {
        try {
            Path inputDir = Paths.get(inputPath);
            if (!Files.exists(inputDir)) {
                return new String[0];
            }
            
            List<String> sites = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(inputDir, Files::isDirectory)) {
                for (Path path : stream) {
                    sites.add(path.getFileName().toString());
                }
            }
            
            return sites.toArray(new String[0]);
            
        } catch (IOException e) {
            throw new SiteBuildException("Failed to list available sites: " + e.getMessage(), e);
        }
    }
    
    @Override
    public SiteBuildResponse getBuildStatus(String siteName) throws SiteBuildException {
        if (StringUtils.isBlank(siteName)) {
            throw new SiteBuildException("Site name cannot be blank");
        }
        
        try {
            Path buildSitePath = Paths.get(buildPath, siteName);
            
            if (!Files.exists(buildSitePath)) {
                return new SiteBuildResponse(
                    siteName,
                    null,
                    "NOT_BUILT",
                    "Site has not been built yet",
                    0,
                    0
                );
            }
            
            // Count files in build directory
            int fileCount = countFiles(buildSitePath);
            
            return new SiteBuildResponse(
                siteName,
                buildSitePath.toString(),
                "BUILT",
                "Site is ready for deployment",
                0,
                fileCount
            );
            
        } catch (IOException e) {
            throw new SiteBuildException("Failed to get build status: " + e.getMessage(), e);
        }
    }
    
    private Path createBuildDirectory(String siteName) throws IOException {
        Path buildSitePath = Paths.get(buildPath, siteName);
        
        // Remove existing build if it exists
        if (Files.exists(buildSitePath)) {
            deleteDirectory(buildSitePath);
        }
        
        Files.createDirectories(buildSitePath);
        return buildSitePath;
    }
    
    private int copyAndOptimizeFiles(Path sourcePath, Path targetPath) throws IOException {
        final int[] fileCount = {0};
        
        Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path relativePath = sourcePath.relativize(file);
                Path targetFile = targetPath.resolve(relativePath);
                
                // Create parent directories
                Files.createDirectories(targetFile.getParent());
                
                // Copy file
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                
                // Optimize based on file type
                optimizeFile(targetFile);
                
                fileCount[0]++;
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path relativePath = sourcePath.relativize(dir);
                Path targetDir = targetPath.resolve(relativePath);
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }
        });
        
        return fileCount[0];
    }
    
    private void optimizeFile(Path file) throws IOException {
        String fileName = file.getFileName().toString().toLowerCase();
        
        if (fileName.endsWith(".html")) {
            optimizeHtmlFile(file);
        } else if (fileName.endsWith(".css")) {
            optimizeCssFile(file);
        } else if (fileName.endsWith(".js")) {
            optimizeJsFile(file);
        }
    }
    
    private void optimizeHtmlFile(Path file) throws IOException {
        String content = Files.readString(file);
        
        // Basic HTML optimization
        content = content.replaceAll("\\s+", " "); // Collapse whitespace
        content = content.replaceAll(">\\s+<", "><"); // Remove whitespace between tags
        
        Files.writeString(file, content);
    }
    
    private void optimizeCssFile(Path file) throws IOException {
        String content = Files.readString(file);
        
        // Basic CSS optimization
        content = content.replaceAll("\\s+", " "); // Collapse whitespace
        content = content.replaceAll(";\\s*}", "}"); // Remove semicolon before closing brace
        content = content.replaceAll("\\s*{\\s*", "{"); // Remove spaces around opening brace
        content = content.replaceAll("\\s*}\\s*", "}"); // Remove spaces around closing brace
        
        Files.writeString(file, content);
    }
    
    private void optimizeJsFile(Path file) throws IOException {
        String content = Files.readString(file);
        
        // Basic JS optimization
        content = content.replaceAll("\\s+", " "); // Collapse whitespace
        content = content.replaceAll(";\\s*}", "}"); // Remove semicolon before closing brace
        
        Files.writeString(file, content);
    }
    
    private void generateDeploymentFiles(Path buildPath, String siteName) throws IOException {
        // Generate .htaccess for Apache servers
        generateHtaccess(buildPath);
        
        // Generate netlify.toml for Netlify deployment
        generateNetlifyConfig(buildPath, siteName);
        
        // Generate README for deployment instructions
        generateDeploymentReadme(buildPath, siteName);
    }
    
    private void generateHtaccess(Path buildPath) throws IOException {
        String htaccessContent = """
            # Apache configuration for static site
            RewriteEngine On
            
            # Handle client routing
            RewriteCond %{REQUEST_FILENAME} !-f
            RewriteCond %{REQUEST_FILENAME} !-d
            RewriteRule ^(.*)$ index.html [QSA,L]
            
            # Enable compression
            <IfModule mod_deflate.c>
                AddOutputFilterByType DEFLATE text/plain
                AddOutputFilterByType DEFLATE text/html
                AddOutputFilterByType DEFLATE text/xml
                AddOutputFilterByType DEFLATE text/css
                AddOutputFilterByType DEFLATE application/xml
                AddOutputFilterByType DEFLATE application/xhtml+xml
                AddOutputFilterByType DEFLATE application/rss+xml
                AddOutputFilterByType DEFLATE application/javascript
                AddOutputFilterByType DEFLATE application/x-javascript
            </IfModule>
            
            # Set cache headers
            <IfModule mod_expires.c>
                ExpiresActive On
                ExpiresByType text/css "access plus 1 month"
                ExpiresByType application/javascript "access plus 1 month"
                ExpiresByType image/png "access plus 1 month"
                ExpiresByType image/jpg "access plus 1 month"
                ExpiresByType image/jpeg "access plus 1 month"
                ExpiresByType image/gif "access plus 1 month"
                ExpiresByType image/svg+xml "access plus 1 month"
            </IfModule>
            """;
        
        Files.writeString(buildPath.resolve(".htaccess"), htaccessContent);
    }
    
    private void generateNetlifyConfig(Path buildPath, String siteName) throws IOException {
        String netlifyContent = """
            [build]
              publish = "."
            
            [[redirects]]
              from = "/*"
              to = "/index.html"
              status = 200
            
            [build.environment]
              NODE_VERSION = "18"
            """;
        
        Files.writeString(buildPath.resolve("netlify.toml"), netlifyContent);
    }
    
    private void generateDeploymentReadme(Path buildPath, String siteName) throws IOException {
        String readmeContent = String.format("""
            # %s - Deployment Instructions
            
            This directory contains the built static site ready for deployment.
            
            ## Deployment Options
            
            ### 1. GitHub Pages
            1. Push this directory to a GitHub repository
            2. Enable GitHub Pages in repository settings
            3. Select source as "Deploy from a branch"
            4. Choose the branch containing this directory
            
            ### 2. Netlify
            1. Drag and drop this directory to Netlify
            2. Or connect your GitHub repository
            3. Set build command to empty (already built)
            4. Set publish directory to this directory
            
            ### 3. Vercel
            1. Install Vercel CLI: `npm i -g vercel`
            2. Run `vercel` in this directory
            3. Follow the prompts
            
            ### 4. Apache/Nginx
            1. Upload all files to your web server
            2. Configure your web server to serve static files
            3. Use the included .htaccess file for Apache
            
            ## Files Included
            - HTML pages
            - CSS and JavaScript assets
            - Configuration files for various platforms
            - Deployment instructions
            
            Generated by Static Site Generator
            """, siteName);
        
        Files.writeString(buildPath.resolve("README.md"), readmeContent);
    }
    
    private int countFiles(Path directory) throws IOException {
        final int[] count = {0};
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                count[0]++;
                return FileVisitResult.CONTINUE;
            }
        });
        return count[0];
    }
    
    private void deleteDirectory(Path directory) throws IOException {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
