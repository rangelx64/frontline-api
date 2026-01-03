package com.ffx64.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ServerExistsException extends RuntimeException {
    public ServerExistsException() {
        super("Server já existente");
    }

    public ServerExistsException(String message) {
        super(message);
    }
}
