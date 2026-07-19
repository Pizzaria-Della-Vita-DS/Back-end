package com.dellavita.project.services.exceptions;

public class RegistroDuplicadoException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RegistroDuplicadoException(String mensagem) {
        super(mensagem);
    }
}