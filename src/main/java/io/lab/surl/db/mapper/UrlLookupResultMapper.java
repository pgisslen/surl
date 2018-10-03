package io.lab.surl.db.mapper;

import io.lab.surl.core.model.UrlDigest;
import io.lab.surl.core.model.UrlLookup;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

public class UrlLookupResultMapper implements ResultSetMapper<UrlLookup> {
    @Override
    public UrlLookup map(final int index, final ResultSet r, final StatementContext ctx) throws SQLException {
        return UrlLookup.builder()
            .key(r.getString("key"))
            .url(r.getString("url"))
            .digest(UrlDigest.valueOf(r.getString("digest")))
            .build();
    }
}
