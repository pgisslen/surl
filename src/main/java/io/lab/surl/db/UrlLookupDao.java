package io.lab.surl.db;

import io.lab.surl.core.UrlLookup;
import java.util.Optional;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

public interface UrlLookupDao {

    @SqlUpdate("create table url_lookup (key varchar(64) primary key, url varchar(2000))")
    void createTable();

    @SqlUpdate("merge into url_lookup (key, url) values (:key, :url)")
    void insert(@BindBean UrlLookup urlLookup);

    @SingleValueResult
    @SqlQuery("select url from url_lookup where key = :key")
    Optional<String> findNameByKey(@Bind("key") String key);

}
