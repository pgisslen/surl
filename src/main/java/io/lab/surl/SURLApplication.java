package io.lab.surl;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.lab.surl.db.UrlLookupDao;
import io.lab.surl.resources.UrlShorternerResource;
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
        final UrlLookupDao urlLookupDao = jdbi.onDemand(UrlLookupDao.class);

        if (configuration.isMemDb()) {
            urlLookupDao.createTable();
        }

        environment.jersey().register(new UrlShorternerResource(urlLookupDao, configuration.getServiceUrl()));
    }
}
