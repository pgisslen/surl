package io.lab.surl.api;

import io.lab.surl.core.model.UrlDigest;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class CreateShortUrlResponse {

    @NonNull
    private final String orignalUrl;
    @NonNull
    private final String tinyUrl;
    @NonNull
    private final UrlDigest urlDigest;
}

