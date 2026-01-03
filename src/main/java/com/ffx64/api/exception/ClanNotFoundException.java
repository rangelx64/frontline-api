package com.ffx64.api.exception;

public class ClanNotFoundException extends RuntimeException {

    public ClanNotFoundException() {
        super("Clã não encontrado");
    }
}
