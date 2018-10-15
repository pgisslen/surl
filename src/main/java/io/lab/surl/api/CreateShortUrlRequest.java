package io.lab.surl.api;

import io.lab.surl.core.model.UrlDigest;
import io.lab.surl.validators.ValidEnum;
import io.lab.surl.validators.ValidUrl;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Optional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;

@Data
@ApiModel("CreateShortCutRequest")
public class CreateShortUrlRequest {

    @NotNull
    @Size(max = 2000)
    @ValidUrl
    @ApiModelProperty(value = "Valid HTTP(s) URL", example = "http://www.google.com")
    private final String url;
    @ApiModelProperty(allowEmptyValue = true    )
    @ValidEnum(targetClassType = UrlDigest.class)
    private final String digest;

    public Optional<UrlDigest> getDigest() {
        return UrlDigest.parse(digest);
    }

    public static CreateShortUrlRequest withCrc32(@NonNull String url) {
        return new CreateShortUrlRequest(url, UrlDigest.CRC32.name());
    }

    public static CreateShortUrlRequest withMD5(@NonNull String url) {
        return new CreateShortUrlRequest(url, UrlDigest.MD5.name());
    }

}
