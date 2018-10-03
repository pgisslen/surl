package io.lab.surl.core.model;

import java.util.Optional;
import java.util.stream.Stream;

public enum UrlDigest {

    CRC32, MD5;

    public static Optional<UrlDigest> parse(String value) {
        return Stream
            .of(UrlDigest.values())
            .filter(digest -> digest.name().equalsIgnoreCase(value))
            .findFirst();
    }
}
