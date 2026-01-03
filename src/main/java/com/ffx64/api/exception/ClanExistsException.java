package com.ffx64.api.exception;

public class ClanExistsException extends RuntimeException {

    public ClanExistsException() {
        super("Clã já existe");
    }
}
