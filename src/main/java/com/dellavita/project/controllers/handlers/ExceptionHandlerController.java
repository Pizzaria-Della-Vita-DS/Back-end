
package com.dellavita.project.controllers.handlers;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dellavita.project.services.exceptions.ContaNaoAprovadaException;
import com.dellavita.project.services.exceptions.CredenciaisInvalidasException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErroPadrao> handleRegistroDuplicado(RegistroDuplicadoException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.CONFLICT.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ResponseEntity<ErroPadrao> handleCredenciaisInvalidas(CredenciaisInvalidasException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(erro);
    }

    @ExceptionHandler(ContaNaoAprovadaException.class)
    public ResponseEntity<ErroPadrao> handleContaNaoAprovada(ContaNaoAprovadaException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPadrao> handleArgumentoInvalido(IllegalArgumentException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Rede de segurança contra concorrência: se dois pedidos passarem pela checagem
    // de duplicidade do service ao mesmo tempo, a constraint UNIQUE do banco ainda
    // barra o registro — aqui só traduzimos isso numa resposta amigável.
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroPadrao> handleIntegridade(DataIntegrityViolationException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.CONFLICT.value(), "Já existe um registro com esses dados.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }

    public static class ErroPadrao {
        private Instant timestamp;
        private Integer status;
        private String mensagem;

        public ErroPadrao(Instant timestamp, Integer status, String mensagem) {
            this.timestamp = timestamp;
            this.status = status;
            this.mensagem = mensagem;
        }

        public Instant getTimestamp() {
            return timestamp;
        }

        public Integer getStatus() {
            return status;
        }

        public String getMensagem() {
            return mensagem;
        }
    }
}