package com.andrewsha.marketplace.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> handleAnyException(Exception e) {
        CustomExceptionBody appException = new CustomExceptionBody(e.getClass().getSimpleName(),
                e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, ZonedDateTime.now());
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }

    @ExceptionHandler(value = {ProductServiceException.class})
    public ResponseEntity<?> handleProductServiceException(ProductServiceException e) {
        CustomExceptionBody appException = new CustomExceptionBody(e.getClass().getSimpleName(),
                e.getMessage(), HttpStatus.BAD_REQUEST, ZonedDateTime.now());

        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        List<CustomExceptionBody> errors = new ArrayList<>();
        for (ObjectError objectError : e.getAllErrors()) {
            errors.add(new CustomExceptionBody("Validation Error",
                    objectError.getObjectName() + " : " + objectError.getDefaultMessage(),
                    HttpStatus.BAD_REQUEST, ZonedDateTime.now()));
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = { HttpMessageNotReadableException.class })
    public ResponseEntity<?> handleProductServiceException(HttpMessageNotReadableException e) {
        CustomExceptionBody appException = new CustomExceptionBody(e.getClass().getSimpleName(),
                e.getMessage().substring(0, e.getMessage().indexOf(":")), HttpStatus.BAD_REQUEST, ZonedDateTime.now());
        return new ResponseEntity<>(appException, appException.getHttpStatus());
    }
}
