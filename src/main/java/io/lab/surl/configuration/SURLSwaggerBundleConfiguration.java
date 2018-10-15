package io.lab.surl.configuration;

import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SURLSwaggerBundleConfiguration extends SwaggerBundle<SURLConfiguration> {

    @Override
    protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(final SURLConfiguration configuration) {
        return configuration.getSwaggerBundleConfiguration();
    }
}
