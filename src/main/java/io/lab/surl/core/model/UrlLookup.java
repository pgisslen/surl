package io.lab.surl.core.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class UrlLookup {

    @NonNull
    private final String key;
    @NonNull
    private final String url;
    @NonNull
    private final UrlDigest digest;

}
