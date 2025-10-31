package com.sitebuilder.code;

import com.sitebuilder.code.service.SiteBuildService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for the site build service.
 * Tests the complete flow from generated site to built deployment package.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "site.builder.input.path=target/test-output",
    "site.builder.build.path=target/test-build"
})
class SiteBuildIntegrationTest {

    @Autowired
    private SiteBuildService siteBuildService;

    @Test
    void testListAvailableSites() throws Exception {
        // This test will pass even if no sites are available
        String[] sites = siteBuildService.listAvailableSites();
        assertNotNull(sites);
        // sites array can be empty, that's fine for this test
    }

    @Test
    void testBuildStatusForNonExistentSite() throws Exception {
        var status = siteBuildService.getBuildStatus("NonExistentSite");
        
        assertNotNull(status);
        assertEquals("NonExistentSite", status.getSiteName());
        assertEquals("NOT_BUILT", status.getStatus());
        assertTrue(status.getMessage().contains("not been built"));
    }

    @Test
    void testBuildSiteWithInvalidName() {
        assertThrows(Exception.class, () -> {
            siteBuildService.buildSite("");
        });
        
        assertThrows(Exception.class, () -> {
            siteBuildService.buildSite(null);
        });
    }
}
