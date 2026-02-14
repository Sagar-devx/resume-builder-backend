package com.sagar.resume.Exception;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidationExceptions(MethodArgumentNotValidException ex)
    {
        log.info("Inside GlobalExceptionHandler - handleValidationException()");
       Map<String,String> errors = new HashMap<>();
       ex.getBindingResult().getAllErrors().forEach(error->{
           String fieldName = ((FieldError)error).getField();
           String errorMessagae = error.getDefaultMessage();
           errors.put(fieldName,errorMessagae);
       });

       Map<String,Object> response = new HashMap<>();
       response.put("message","Validation failed");
       response.put("errors",errors);

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<Map<String,Object>> handleResourceExistsException(ResourceExistsException ex)
    {
        log.info("Inside GlobalExceptionHandler - handleResourceExistsException()");
        Map<String,Object> response = new HashMap<>();
        response.put("message","Resource exists");
        response.put("status",HttpStatus.CONFLICT);
        response.put("error",ex.getMessage());

        return  ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGenericException(Exception ex)
    {
        log.info("Inside GlobalExceptionHandler - handleGenericException()");
        Map<String,Object> response = new HashMap<>();
        response.put("message","Some went wrong. Contact administrator");
        response.put("error",ex.getMessage());
        return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

}
