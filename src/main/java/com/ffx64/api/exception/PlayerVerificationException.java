package com.ffx64.api.exception;

public class PlayerVerificationException extends RuntimeException {
    public PlayerVerificationException() {
        super("Ocorreu um erro ao buscar informações do jogador");
    }

    public PlayerVerificationException(String message) {
        super(message);
    }
}
