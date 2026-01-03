package com.ffx64.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RoundNotFoundException extends RuntimeException {
    public RoundNotFoundException() {
        super("Ocorreu um erro ao buscar a partida");
    }

    public RoundNotFoundException(String message) {
        super(message);
    }
}
