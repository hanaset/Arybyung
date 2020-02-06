package com.how.arybyungprovider.web.rest.advice;

import com.how.arybyungprovider.exception.ProviderResponseException;
import com.how.arybyungprovider.web.rest.support.ProviderRestSupport;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProviderApiRestAdvice extends ProviderRestSupport {

    @ExceptionHandler(ProviderResponseException.class)
    public ResponseEntity handleProviderResponseException(ProviderResponseException ex) {
        return providerResponseException(ex.getCode(), ex.getMessage());
    }
}
