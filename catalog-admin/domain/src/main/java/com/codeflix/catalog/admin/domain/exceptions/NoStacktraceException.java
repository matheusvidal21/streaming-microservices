package com.codeflix.catalog.admin.domain.exceptions;

public class NoStacktraceException extends RuntimeException {

    public NoStacktraceException(final String message, final Throwable cause) {
        super(message, cause, true, false);
    }

    public NoStacktraceException(final String message) {
        this(message, null);
    }

}
