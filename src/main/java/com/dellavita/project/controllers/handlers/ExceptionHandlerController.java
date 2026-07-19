package com.dellavita.project.controllers.handlers;

import java.time.Instant;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dellavita.project.services.exceptions.CredenciaisInvalidasException;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
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

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroPadrao> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroPadrao> handleConstraintViolation(ConstraintViolationException ex) {
        String mensagem = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining("; "));

        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.BAD_REQUEST.value(), mensagem);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
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