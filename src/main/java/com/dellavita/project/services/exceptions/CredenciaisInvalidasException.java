package com.dellavita.project.services.exceptions;

public class CredenciaisInvalidasException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}