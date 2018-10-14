package io.lab.surl.exception;

import lombok.Getter;
import org.eclipse.jetty.http.HttpStatus;

public enum ErrorType {

    URL_NOT_FOUND(HttpStatus.NOT_FOUND_404),
    URL_COLLISION(HttpStatus.BAD_REQUEST_400);

    ErrorType(final int responseCode) {
        this.responseCode = responseCode;
    }

    @Getter
    final int responseCode;

}
