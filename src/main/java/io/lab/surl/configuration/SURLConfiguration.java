package io.lab.surl.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.db.PooledDataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import io.lab.surl.core.model.UrlDigest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SURLConfiguration extends Configuration implements DatabaseConfiguration<SURLConfiguration> {

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory dataSourceFactory;

    @Valid
    @NotNull
    @JsonProperty
    private String serviceUrl;

    @Valid
    @NotNull
    @JsonProperty
    private UrlDigest defaultDigest = UrlDigest.CRC32;

    @JsonProperty
    private boolean memDb = true;

    @Override
    public PooledDataSourceFactory getDataSourceFactory(final SURLConfiguration configuration) {
        return dataSourceFactory;
    }

    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        final SwaggerBundleConfiguration swaggerBundleConfiguration = new SwaggerBundleConfiguration();
        swaggerBundleConfiguration.setResourcePackage("io.lab.surl.resources");
        return swaggerBundleConfiguration;
    }
}
