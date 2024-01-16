package org.example.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class ExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public ResponseEntity<String> exceptionHandler(Exception ex) {
        LOGGER.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({RuntimeException.class})
    public ResponseEntity<String> exceptionHandler(RuntimeException ex) {
        LOGGER.error("Exception: {}", ex.getMessage(), ex);
        return ResponseEntity.status(500).body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({HttpClientErrorException.BadRequest.class, HttpClientErrorException.NotFound.class, TypeMismatchException.class, HandlerMethodValidationException.class})
    public ResponseEntity<String> validationExceptionHandler(RuntimeException ex) {
        LOGGER.error("BadRequest: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<String> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
        LOGGER.error("BadRequest: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<String> noSuchElementExceptionHandler(NoSuchElementException ex) {
        LOGGER.error("BadRequest: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler({HttpMessageConversionException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<String> conversionFailedExceptionHandler(HttpMessageConversionException ex) {
        LOGGER.error("BadRequest: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
