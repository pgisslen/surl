package io.lab.surl.exception;

import lombok.Getter;

public class SystemException extends RuntimeException {

    @Getter
    private final ErrorType errorType;

    public SystemException(final ErrorType errorType, final String message) {
        super(message);
        this.errorType = errorType;
    }

}
