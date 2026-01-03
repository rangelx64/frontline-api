package com.ffx64.api.exception;

public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException() {
        super("Player não encontrado");
    }

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
