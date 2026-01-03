package com.ffx64.api.exception;

public class ClanMembersNotFoundException extends RuntimeException {

    public ClanMembersNotFoundException() {
        super("Membro do clã não encontrado");
    }
}
