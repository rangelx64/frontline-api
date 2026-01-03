package com.ffx64.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {

    public InternalServerErrorException() {
        super("Ocorreu um erro interno no servidor.");
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
