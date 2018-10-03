package io.lab.surl.resources;

import io.lab.surl.core.UrlLookup;
import io.lab.surl.core.UrlLookupBuilder;
import io.lab.surl.db.UrlLookupDao;
import java.net.URI;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@Slf4j
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.TEXT_PLAIN)
public class UrlShorternerResource {

    @NonNull
    private final UrlLookupDao urlLookupDao;
    @NotNull
    private final String serviceUrl;

    @POST
    public String createShort(@NotNull @URL final String url) {

        final String key = UrlLookupBuilder.createUrlShortcutKey(url);
        urlLookupDao.insert(new UrlLookup(key, url));
        final String shortCutUrl = serviceUrl + "/" + key;
        log.info("Created url shortcut: {} -> {}", key, shortCutUrl);
        return shortCutUrl;
    }

    @GET
    @Path("create")
    public String createShortWithGet(@NotNull @QueryParam("url") @URL final String url) {
        return createShort(url);
    }

    @GET
    @Path("{key}")
    public Response redirect(@PathParam("key") final String key) {

        log.info("Finding {}", key);

        final String fullUrl = urlLookupDao.findNameByKey(key)
            .orElseThrow(() -> new NotFoundException("No URL found for " + key));

        log.info("Found mapping for key {} -> {}", key, fullUrl);

        return Response.seeOther(URI.create(fullUrl)).build();
    }
}
