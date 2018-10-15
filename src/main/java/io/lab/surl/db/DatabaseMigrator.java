package io.lab.surl.db;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.skife.jdbi.v2.DBI;

public class DatabaseMigrator {

    public static void update(final DBI jdbi) {
        try {
            final Liquibase liquibase = new Liquibase(
                "migrations.yml",
                new FileSystemResourceAccessor(),
                new JdbcConnection(jdbi.open().getConnection())
            );
            liquibase.update("");
        } catch (LiquibaseException e) {
            throw new IllegalStateException("Failed to migrate DB. Catastrophic failure", e);
        }
    }
}
