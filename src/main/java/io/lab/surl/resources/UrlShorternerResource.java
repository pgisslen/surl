package io.lab.surl.resources;

import static io.lab.surl.exception.ErrorType.URL_NOT_FOUND;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import io.lab.surl.api.CreateShortUrlRequest;
import io.lab.surl.api.ShortUrlResponse;
import io.lab.surl.core.manager.UrlLookupManager;
import io.lab.surl.core.mapper.UrlLookupDtoMapper;
import io.lab.surl.core.model.UrlLookup;
import io.lab.surl.exception.SystemException;
import io.swagger.annotations.Api;
import java.net.URI;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Api("/")
@Path("/")
@Produces(APPLICATION_JSON)
@Consumes(APPLICATION_JSON)
@AllArgsConstructor
@Slf4j
public class UrlShorternerResource {

    @NonNull
    private final UrlLookupManager urlLookupManager;

    @NonNull
    private final UrlLookupDtoMapper urlLookupDtoMapper;

    @POST
    public ShortUrlResponse createShort(@NotNull @Valid CreateShortUrlRequest request) {

        final UrlLookup urlLookup = urlLookupDtoMapper.toUrlLookup(request);
        urlLookupManager.createShort(urlLookup);
        return urlLookupDtoMapper.toResponse(urlLookup);
    }

    @GET
    @Path("{key}")
    public Response redirect(@PathParam("key") @Size(min = 8, max = 64) final String key) {

        log.info("Finding {}", key);

        final UrlLookup lookup = urlLookupManager.findByKey(key)
            .orElseThrow(() -> new SystemException(URL_NOT_FOUND, "No URL found for " + key));

        log.info("Redirecting to {} ", lookup);

        return Response.seeOther(URI.create(lookup.getUrl())).build();
    }
}
