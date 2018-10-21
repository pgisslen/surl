package io.lab.surl.db;

import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.OptionalContainerFactory;
import io.dropwizard.lifecycle.Managed;
import io.dropwizard.setup.Environment;
import io.lab.surl.configuration.SURLConfiguration;
import io.lab.surl.db.mapper.UrlLookupResultMapper;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.DBI;

@Slf4j
public class DatabaseManager implements Managed {

    private final DBI jdbi;

    public DatabaseManager(final SURLConfiguration configuration, final Environment environment ) {
        final String driverClass = configuration.getDataSourceFactory().getDriverClass();
        log.info("Creating datasource with {}", driverClass);

        jdbi = new DBIFactory().build(environment, configuration.getDataSourceFactory(), driverClass);
        jdbi.registerContainerFactory(new OptionalContainerFactory());
        jdbi.registerMapper(new UrlLookupResultMapper());
    }

    @Override
    public void start()  {
        DatabaseMigrator.update(jdbi);
    }

    @Override
    public void stop()  {
        //NOOP
    }

    public <T> T getDao(Class<T> daoClass){
        return jdbi.onDemand(daoClass);
    }
}
