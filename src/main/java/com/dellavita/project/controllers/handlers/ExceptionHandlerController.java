
package com.dellavita.project.controllers.handlers;

import java.time.Instant;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dellavita.project.services.exceptions.ContaNaoAprovadaException;
import com.dellavita.project.services.exceptions.CredenciaisInvalidasException;
import com.dellavita.project.services.exceptions.RecursoNaoEncontradoException;
import com.dellavita.project.services.exceptions.RegistroDuplicadoException;

import jakarta.validation.ConstraintViolationException;

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

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroPadrao> handleRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPadrao> handleArgumentoInvalido(IllegalArgumentException ex) {
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> handleValidacao(MethodArgumentNotValidException ex) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(erro -> erro.getDefaultMessage())
                .orElse("Dados de cadastro inválidos.");
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.BAD_REQUEST.value(), mensagem);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroPadrao> handleRestricaoValidacao(ConstraintViolationException ex) {
        String mensagem = ex.getConstraintViolations().stream()
                .findFirst()
                .map(violacao -> violacao.getMessage())
                .orElse("Dados inválidos.");
        ErroPadrao erro = new ErroPadrao(Instant.now(), HttpStatus.BAD_REQUEST.value(), mensagem);
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroPadrao> handleCorpoInvalido(HttpMessageNotReadableException ex) {
        ErroPadrao erro = new ErroPadrao(
                Instant.now(),
                HttpStatus.BAD_REQUEST.value(),
                "O formato de um ou mais campos é inválido.");
        return ResponseEntity.badRequest().body(erro);
    }

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
