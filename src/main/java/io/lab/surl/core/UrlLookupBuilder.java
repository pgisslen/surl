package io.lab.surl.core;

import java.util.zip.CRC32;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;

/**
 *
 * Build keys from URL.
 *
 * Inspiration from https://blog.codinghorror.com/url-shortening-hashes-in-practice/
 *
 */
public class UrlLookupBuilder {

    /**
     * Less risk of collisions. Almost none.
     * @param url URL you wan to shorten
     * @return A pretty unique string, same result for same url
     */
    public static String withMd5(@NonNull final String url) {

        return DigestUtils.md5Hex(url).toLowerCase();
    }

    /**
     * A bit higher risk of collisions, but probably ok.
     *
     * @param url URL you wan to shorten
     * @return A pretty unique string, same result for same url
     */
    public static String withCrc32(@NonNull final String url) {

        final CRC32 crc = new CRC32();
        crc.update(url.getBytes());
        return Long.toHexString(crc.getValue());
    }
}
