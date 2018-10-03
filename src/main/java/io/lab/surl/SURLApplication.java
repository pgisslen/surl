package io.lab.surl;

import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
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
        // TODO: application initialization
    }

    @Override
    public void run(final SURLConfiguration configuration, final Environment environment) {
        final String driver = configuration.getDataSourceFactory().getDriverClass();

        log.info("Creating datasource with {}", driver);

        final DBI jdbi = new DBIFactory().build(environment, configuration.getDataSourceFactory(), driver);
        jdbi.registerContainerFactory(new OptionalContainerFactory());
        final UrlLookupDao urlLookupDao = jdbi.onDemand(UrlLookupDao.class);

        urlLookupDao.createTable();

        environment.jersey().register(new UrlShorternerResource(urlLookupDao, configuration.getServiceUrl()));
    }
}
