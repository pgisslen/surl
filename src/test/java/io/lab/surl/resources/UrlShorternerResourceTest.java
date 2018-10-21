package io.lab.surl.resources;

import static io.lab.surl.core.model.UrlDigest.CRC32;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.lab.surl.api.CreateShortUrlRequest;
import io.lab.surl.api.ShortUrlResponse;
import io.lab.surl.core.model.UrlLookup;
import io.lab.surl.core.util.TinyUrlCreator;
import io.lab.surl.db.UrlLookupDao;
import io.lab.surl.exception.ErrorType;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.http.HttpStatus;
import org.junit.Test;

public class UrlShorternerResourceTest extends AbstractIntegrationTest {

    final String urlToLasVegas =
        "https://www.google.se/maps/place/Las+Vegas,+Nevada,+USA/@36.1251958,-115.3150815,"
            + "11z/data=!3m1!4b1!4m5!3m4!1s0x80beb782a4f57dd1:0x3accd5e6d5b379a3!8m2!3d36.1699412!4d-115.1398296";

    final String googleUrl = "https://www.google.se";

    @Test
    public void createSimpleUrlWithDefaultDigest() {

        final CreateShortUrlRequest request = CreateShortUrlRequest.withCrc32(googleUrl);
        final Response response = createTinyUrl(request);
        final ShortUrlResponse mappedResponse = getResponseEntityOrFail(response, ShortUrlResponse.class);
        assertResponseMatchesRequest(request, mappedResponse);
        assertRedirectSuccess(mappedResponse);
    }

    @Test
    public void createSimpleUrlWithMD5() {

        final CreateShortUrlRequest request = CreateShortUrlRequest.withMD5(googleUrl);
        final Response response = createTinyUrl(request);
        final ShortUrlResponse shortUrlResponse = getResponseEntityOrFail(response, ShortUrlResponse.class);
        assertResponseMatchesRequest(request, shortUrlResponse);
        assertRedirectSuccess(shortUrlResponse);
    }

    @Test
    public void createComplexUrlWithPost() {

        final Response response = createTinyUrl(urlToLasVegas);
        assertSuccessAndRedirect(response);
    }

    @Test
    public void createSameUrlTwiceShouldWork() {

        final String url = "http://www.dice.se";
        final CreateShortUrlRequest request = CreateShortUrlRequest.withCrc32(url);
        final ShortUrlResponse response1 = getResponseEntityOrFail(createTinyUrl(request), ShortUrlResponse.class);
        final ShortUrlResponse response2 = getResponseEntityOrFail(createTinyUrl(request), ShortUrlResponse.class);
        assertThat(response1).isEqualTo(response2);
    }

    @Test
    public void hashCollisionShouldFail() {
        final String url = "http://www.fz.se";
        final String key = TinyUrlCreator.withCrc32(url);
        final UrlLookup first = UrlLookup.builder()
            .key(key)
            .url(url + "/foo")
            .digest(CRC32)
            .build();

        final UrlLookupDao lookupDao = getDatabaseManager().getDao(UrlLookupDao.class);
        //Insert  "invalid" value direct into db
        lookupDao.insert(first);

        //Try to create a valid value. Should be rejected since the key points to other URL
        final CreateShortUrlRequest request = CreateShortUrlRequest.withCrc32(url);
        final Response response = createTinyUrl(request);
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_BAD_REQUEST);
    }

    @Test
    public void invalidDigestShouldFail() {

        final ObjectNode invalidDigest = new ObjectMapper().createObjectNode()
            .put("url", googleUrl)
            .put("digest", "FOO");
        final Response response =
            createWebTarget().path("/").request().post(Entity.entity(invalidDigest, MediaType.APPLICATION_JSON));
        final String entity = response.readEntity(String.class);
        assertThat(entity).contains("digest");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void missingUrlShouldFail() {
        final Response response = createTinyUrl(new CreateShortUrlRequest(null, null));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void invalidUrlShouldFail() {
        final Response response = createTinyUrl("!foo");
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void unknownShouldReturn404() {

        final Response response = createWebTarget().path("/" + "fadfdasffadsfd").request().get();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        final ErrorMessage errorMessage = response.readEntity(ErrorMessage.class);
        assertThat(errorMessage.getDetails()).isEqualTo(ErrorType.URL_NOT_FOUND.name());
    }

    private void assertResponseMatchesRequest(final CreateShortUrlRequest expected, final ShortUrlResponse actual) {
        assertThat(actual.getOrignalUrl()).isEqualTo(expected.getUrl());
        expected.getDigest()
            .ifPresent(digest -> assertThat(actual.getDigest()).isEqualTo(digest));
    }

    private Response createTinyUrl(final CreateShortUrlRequest request) {
        return createWebTarget().path("/").request().post(Entity.entity(request, MediaType.APPLICATION_JSON));
    }

    private Response createTinyUrl(final String targetUrl) {
        return createTinyUrl(CreateShortUrlRequest.withCrc32(targetUrl));
    }

    private void assertSuccessAndRedirect(final Response response) {
        final ShortUrlResponse shortCut = getResponseEntityOrFail(response, ShortUrlResponse.class);
        assertRedirectSuccess(shortCut);
    }

    private void assertRedirectSuccess(final ShortUrlResponse shortCut) {
        final Response redirectResponse = getClient().target(shortCut.getOrignalUrl()).request().get();
        assertThat(redirectResponse.getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    private <T> T getResponseEntityOrFail(Response response, Class<T> clazz) {
        assertSuccess(response);
        return response.readEntity(clazz);
    }

    private void assertSuccess(Response response) {
        if (response.getStatus() > 299) {
            fail(response.getStatus() + " " + response.readEntity(String.class));
        }
    }
}
