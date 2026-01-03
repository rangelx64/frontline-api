package com.ffx64.api.exception;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Não autorizado");
    }

    public UnauthorizedException(String message) {
        super(message);
    }
}
