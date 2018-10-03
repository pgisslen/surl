package io.lab.surl.core;

import org.apache.commons.codec.digest.DigestUtils;

public class UrlLookupBuilder {

    public static String createUrlShortcutKey(final String url) {
        return DigestUtils.md2Hex(url).toLowerCase();
    }
}
