package com.ffx64.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TermsNotAccepted extends RuntimeException {
    public TermsNotAccepted() {
        super("Para continuar, você precisa aceitar os termos de uso.");
    }

    public TermsNotAccepted(String message) {
        super(message);
    }
}
