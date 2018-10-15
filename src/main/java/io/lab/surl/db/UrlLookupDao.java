package io.lab.surl.db;

import io.lab.surl.core.model.UrlLookup;
import java.util.Optional;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.SingleValueResult;

public interface UrlLookupDao {

    @SqlUpdate("insert into url_lookup (key, url, digest) VALUES (:key, :url, :digest)")
    void insert(@BindBean UrlLookup urlLookup);

    @SingleValueResult
    @SqlQuery("select * from url_lookup where key = :key")
    Optional<UrlLookup> findByKey(@Bind("key") String key);

}
