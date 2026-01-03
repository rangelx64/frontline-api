package com.ffx64.api.exception;

public class ClanMembersLimitException extends RuntimeException {

    public ClanMembersLimitException() {
        super("Usuário já faz parte de um clã");
    }
}
