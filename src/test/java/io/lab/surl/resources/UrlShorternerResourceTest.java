package io.lab.surl.resources;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UrlShorternerResourceTest extends AbstractIntegrationTest {

    final String urlToLasVegas =
        "https://www.google.se/maps/place/Las+Vegas,+Nevada,+USA/@36.1251958,-115.3150815,"
            + "11z/data=!3m1!4b1!4m5!3m4!1s0x80beb782a4f57dd1:0x3accd5e6d5b379a3!8m2!3d36.1699412!4d-115.1398296";

    final String googleUrl = "https://www.google.se";

    @Test
    public void createSimpleUrlWithPost() {

        final Response response = createUrlWithPost(googleUrl);
        assertSuccessAndRedirect(response);
    }

    @Test
    public void createComplexUrlWithPost() {

        final Response response = createUrlWithPost(urlToLasVegas);
        assertSuccessAndRedirect(response);
    }

    @Test
    public void createTwiceShouldWork() {

        final String url = "http://www.dice.se";

        final Response response1 = createUrlWithGet(url);
        final Response response2 = createUrlWithGet(url);

        Assertions.assertThat(response1.readEntity(String.class)).isEqualTo(response2.readEntity(String.class));
    }

    @Test
    public void createComplexUrlWithGet() {

        final Response response = createUrlWithGet(urlToLasVegas);
        assertSuccessAndRedirect(response);
    }

    @Test
    public void invalidUrlShouldFail() {

        final Response response = createUrlWithGet("!foo");
        Assertions.assertThat(response.getStatus()).isEqualTo(Status.BAD_REQUEST.getStatusCode());
    }

    private Response createUrlWithGet(final String targetUrl) {
        return createWebTarget().path("/create").queryParam("url", targetUrl).request().get();
    }

    private Response createUrlWithPost(final String targetUrl) {
        return createWebTarget().path("/").request().post(Entity.entity(targetUrl, MediaType.WILDCARD));
    }

    private void assertSuccessAndRedirect(final Response response) {
        Assertions.assertThat(response.getStatus()).isEqualTo(200);
        final String shortCut = response.readEntity(String.class);
        final Response redirectResponse = getClient().target(shortCut).request().get();
        Assertions.assertThat(redirectResponse.getStatus()).isEqualTo(200);
    }

}
