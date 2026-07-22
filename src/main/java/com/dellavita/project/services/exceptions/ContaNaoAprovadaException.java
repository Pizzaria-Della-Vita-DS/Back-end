
package com.dellavita.project.services.exceptions;

public class ContaNaoAprovadaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ContaNaoAprovadaException(String mensagem) {
        super(mensagem);
    }
}