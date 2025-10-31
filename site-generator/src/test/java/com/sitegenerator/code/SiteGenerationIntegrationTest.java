package com.sitegenerator.code;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitegenerator.code.domain.model.Page;
import com.sitegenerator.code.domain.model.Section;
import com.sitegenerator.code.domain.model.Site;
import com.sitegenerator.code.service.SiteGenerationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the site generation service.
 * Tests the complete flow from site description to HTML generation.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "site.generator.output.path=target/test-output"
})
class SiteGenerationIntegrationTest {

    @Autowired
    private SiteGenerationService siteGenerationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCompleteSiteGeneration() throws Exception {
        // Create test site
        Site testSite = createTestSite();
        
        // Generate site
        var response = siteGenerationService.generateSite(testSite);
        
        // Verify response
        assertNotNull(response);
        assertEquals("TestPortfolio", response.getSiteName());
        assertEquals(2, response.getPagesGenerated());
        assertTrue(response.getMessage().contains("successfully"));
        assertNotNull(response.getOutputPath());
    }

    @Test
    void testSiteValidation() {
        // Test null site
        assertThrows(Exception.class, () -> {
            siteGenerationService.validateSite(null);
        });
        
        // Test site with blank name
        Site invalidSite = new Site("", Arrays.asList());
        assertThrows(Exception.class, () -> {
            siteGenerationService.validateSite(invalidSite);
        });
        
        // Test site with no pages
        Site siteWithNoPages = new Site("TestSite", Arrays.asList());
        assertThrows(Exception.class, () -> {
            siteGenerationService.validateSite(siteWithNoPages);
        });
    }

    @Test
    void testJsonSerialization() throws Exception {
        Site testSite = createTestSite();
        
        // Serialize to JSON
        String json = objectMapper.writeValueAsString(testSite);
        assertNotNull(json);
        assertTrue(json.contains("TestPortfolio"));
        assertTrue(json.contains("Home"));
        assertTrue(json.contains("hero"));
        
        // Deserialize from JSON
        Site deserializedSite = objectMapper.readValue(json, Site.class);
        assertEquals(testSite.getSiteName(), deserializedSite.getSiteName());
        assertEquals(testSite.getPages().size(), deserializedSite.getPages().size());
    }

    private Site createTestSite() {
        // Create hero section
        Section heroSection = new Section("hero");
        heroSection.setHeading("Welcome to My Portfolio!");
        heroSection.setText("I'm a passionate software developer.");

        // Create skills section
        Section skillsSection = new Section("skills");
        skillsSection.setItems(Arrays.asList("Java", "Spring Boot", "Thymeleaf", "Maven"));

        // Create home page
        Page homePage = new Page("Home", "index", Arrays.asList(heroSection, skillsSection));

        // Create contact section
        Section contactSection = new Section("form");
        contactSection.setFields(Arrays.asList("name", "email", "message"));

        // Create contact page
        Page contactPage = new Page("Contact", "contact", Arrays.asList(contactSection));

        // Create site
        return new Site("TestPortfolio", Arrays.asList(homePage, contactPage));
    }
}
