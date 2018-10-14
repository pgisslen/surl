package io.lab.surl.core.mapper;

import io.lab.surl.api.CreateShortUrlRequest;
import io.lab.surl.api.CreateShortUrlResponse;
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
        final UrlDigest digest = request.getDigester().orElse(defaualtDigest);
        final String tinyUrl =
            TinyUrlCreator.createTinyUrl(request.getUrl(), digest);

        return UrlLookup.builder().url(request.getUrl()).key(tinyUrl).digest(digest).build();
    }

    public CreateShortUrlResponse toResponse(UrlLookup urlLookup) {
        return CreateShortUrlResponse.builder()
            .orignalUrl(urlLookup.getUrl())
            .tinyUrl(servieUrl + "/" + urlLookup.getKey())
            .urlDigest(urlLookup.getDigest())
            .build();
    }

}
