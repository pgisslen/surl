package io.lab.surl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lab.surl.configuration.SURLConfiguration;
import io.lab.surl.configuration.SURLSwaggerBundleConfiguration;
import io.lab.surl.core.manager.UrlLookupManager;
import io.lab.surl.core.mapper.UrlLookupDtoMapper;
import io.lab.surl.db.DatabaseMigrator;
import io.lab.surl.db.UrlLookupDao;
import io.lab.surl.db.mapper.UrlLookupResultMapper;
import io.lab.surl.exception.SystemExceptionMapper;
import io.lab.surl.resources.UrlShorternerResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;

@Slf4j
public class SURLApplication extends Application<SURLConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SURLApplication().run(args);
    }

    @Override
    public String getName() {
        return "sUrl";
    }

    @Override
    public void initialize(final Bootstrap<SURLConfiguration> bootstrap) {

        final ObjectMapper mapper = bootstrap.getObjectMapper();
        mapper.registerModule(new ParameterNamesModule());
        mapper.registerModule(new Jdk8Module());
        //Load Swagger
        bootstrap.addBundle(new SURLSwaggerBundleConfiguration());
        //Load Liquibase
        bootstrap.addBundle(new MigrationsBundle<SURLConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(SURLConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final SURLConfiguration configuration, final Environment environment) {
        final String driver = configuration.getDataSourceFactory().getDriverClass();

        log.info("Creating datasource with {}", driver);

        final DBI jdbi = new DBIFactory().build(environment, configuration.getDataSourceFactory(), driver);
        jdbi.registerContainerFactory(new OptionalContainerFactory());
        jdbi.registerMapper(new UrlLookupResultMapper());

        DatabaseMigrator.update(jdbi);

        final UrlLookupDao urlLookupDao = jdbi.onDemand(UrlLookupDao.class);
        final UrlLookupManager urlLookupManager = new UrlLookupManager(urlLookupDao);
        final UrlLookupDtoMapper mapper =
            new UrlLookupDtoMapper(configuration.getDefaultDigest(), configuration.getServiceUrl());


        //Add custom mapper for "service specific" errors. Will be mapped into standard dropwizard error message
        //For other exception. Rely on Dropwizard Default exception mappers.
        environment.jersey().register(new SystemExceptionMapper());

        // adds the /swagger.json and /swagger.yaml resource
        environment.jersey().register(new ApiListingResource());
        environment.jersey().register(new SwaggerSerializers());
        environment.jersey().register(new UrlShorternerResource(urlLookupManager, mapper));
    }
}
