package io.lab.surl.core.manager;

import static io.lab.surl.exception.ErrorType.URL_COLLISION;

import io.lab.surl.core.model.UrlLookup;
import io.lab.surl.db.UrlLookupDao;
import io.lab.surl.exception.SystemException;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.skife.jdbi.v2.sqlobject.Bind;

@Slf4j
@AllArgsConstructor
public class UrlLookupManager {

    @NonNull
    private final UrlLookupDao urlLookupDao;

    public void createShort(@NonNull UrlLookup urlLookup) {

        final Optional<UrlLookup> lookup = findByKey(urlLookup.getKey());
        if (lookup.isPresent()) {
            if (!lookup.get().getUrl().equalsIgnoreCase(urlLookup.getUrl())) {
                throw new SystemException(URL_COLLISION, "Shortcut already exist for another URL. Try another Digest");
            }
            log.info("Key {} already exist. Do nothing", lookup.get().getKey());
        } else {
            urlLookupDao.insert(urlLookup);
            log.info("Created {}", urlLookup);
        }

    }

    public Optional<UrlLookup> findByKey(@Bind("key") String key) {
        return urlLookupDao.findByKey(key);
    }
}
