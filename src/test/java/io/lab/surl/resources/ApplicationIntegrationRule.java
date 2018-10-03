package io.lab.surl.resources;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import io.lab.surl.SURLApplication;
import io.lab.surl.SURLConfiguration;

public class ApplicationIntegrationRule extends DropwizardAppRule<SURLConfiguration> {

    private static ApplicationIntegrationRule INSTANCE;

    private ApplicationIntegrationRule() {
        super(SURLApplication.class, ResourceHelpers.resourceFilePath("integration_test_config.yml"));
    }

    public static ApplicationIntegrationRule getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApplicationIntegrationRule();
        }
        return INSTANCE;
    }
}
