package com.projects.tixtactoe.exception;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GameException.class)
    public ResponseEntity<String> handleGameException(GameException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
