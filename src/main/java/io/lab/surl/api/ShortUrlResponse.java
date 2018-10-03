package io.lab.surl.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.lab.surl.core.model.UrlDigest;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
public class ShortUrlResponse {

    @NonNull
    private final String orignalUrl;
    @NonNull
    private final String tinyUrl;
    @NonNull
    private final UrlDigest digest;

    @Builder
    @JsonCreator
    public ShortUrlResponse(
        @JsonProperty("orignalUrl") final String orignalUrl,
        @JsonProperty("tinyUrl") final String tinyUrl,
        @JsonProperty("digest") final UrlDigest digest) {
        this.orignalUrl = orignalUrl;
        this.tinyUrl = tinyUrl;
        this.digest = digest;
    }
}

