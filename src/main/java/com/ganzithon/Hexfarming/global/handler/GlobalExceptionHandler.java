package com.ganzithon.Hexfarming.global.handler;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getStatusCode(), exception.getReason()), exception.getStatusCode());
    }

    @Getter
    public static class ErrorResponse {
        private HttpStatusCode status;
        private String message;

        public ErrorResponse(HttpStatusCode status, String message) {
            this.status = status;
            this.message = message;
        }
    }
}
