package io.lab.surl.core.model;

import static io.lab.surl.core.util.TinyUrlCreator.withCrc32;
import static io.lab.surl.core.util.TinyUrlCreator.withMd5;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class UrlLookupBuilderTest {

    final String urlToLasVegas =
        "https://www.google.se/maps/place/Las+Vegas,+Nevada,+USA/@36.1251958,-115.3150815,"
            + "11z/data=!3m1!4b1!4m5!3m4!1s0x80beb782a4f57dd1:0x3accd5e6d5b379a3!8m2!3d36.1699412!4d-115.1398296";

    @Test
    public void shouldBeShort() {
        int expected = 32;
        Assertions.assertThat(withCrc32(urlToLasVegas).length()).isLessThan(expected);

    }

    @Test
    public void createSameTwiceShouldReturnSame() {
        Assertions.assertThat(withCrc32(urlToLasVegas)).isEqualTo(withCrc32(urlToLasVegas));
    }

    @Test
    public void createWithMd5() {
        Assertions.assertThat(withMd5(urlToLasVegas)).isEqualTo(withMd5(urlToLasVegas));
    }
}
