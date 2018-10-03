package io.lab.surl.core.mapper;

import io.lab.surl.api.CreateShortUrlRequest;
import io.lab.surl.api.ShortUrlResponse;
import io.lab.surl.core.model.UrlDigest;
import io.lab.surl.core.model.UrlLookup;
import io.lab.surl.core.util.TinyUrlCreator;
import lombok.AllArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
public class UrlLookupDtoMapper {

    @NonNull
    private final UrlDigest defaualtDigest;
    @NonNull
    private final String servieUrl;

    public UrlLookup toUrlLookup(CreateShortUrlRequest request) {
        final UrlDigest digest = request.getDigest().orElse(defaualtDigest);
        final String tinyUrl =
            TinyUrlCreator.createTinyUrl(request.getUrl(), digest);

        return UrlLookup.builder().url(request.getUrl()).key(tinyUrl).digest(digest).build();
    }

    public ShortUrlResponse toResponse(UrlLookup urlLookup) {
        return ShortUrlResponse.builder()
            .orignalUrl(urlLookup.getUrl())
            .tinyUrl(servieUrl + "/" + urlLookup.getKey())
            .digest(urlLookup.getDigest())
            .build();
    }

}
