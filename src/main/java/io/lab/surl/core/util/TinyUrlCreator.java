package io.lab.surl.core.util;

import io.lab.surl.core.model.UrlDigest;
import java.util.zip.CRC32;
import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;

public class TinyUrlCreator {

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

    public static String createTinyUrl(@NonNull String url, @NonNull UrlDigest digest) {
        final String tinyUrl;
        switch (digest) {
            case MD5:
                tinyUrl = TinyUrlCreator.withMd5(url);
                break;
            case CRC32:
                tinyUrl = TinyUrlCreator.withCrc32(url);
                break;
            default:
                throw new IllegalArgumentException("Invalid digest: " + digest);

        }
        return tinyUrl;
    }

}
