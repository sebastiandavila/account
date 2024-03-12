package com.sofka.account.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
public class GlobalException {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("La cuenta no se encontró con el ID especificado");
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Object> insufficientBalanceException(InsufficientBalanceException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Saldo insuficiente para realizar este movimiento");
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNoSuchElementException(ResourceNotFoundException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No se encontró cliente con el ID especificado");
    }
}
