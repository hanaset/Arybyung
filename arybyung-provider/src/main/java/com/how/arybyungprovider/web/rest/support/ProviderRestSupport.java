package com.how.arybyungprovider.web.rest.support;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ProviderRestSupport {

    protected String redirect(String url) {
        return "redirect:".concat(url);
    }

    protected static <T> ResponseEntity<?> response(T data) {
        return ResponseEntity.ok(
                ImmutableMap.of(
                        "status", HttpStatus.OK.value(),
                        "data", data
                )
        );
    }

    protected static <T> ResponseEntity<?> providerResponseException(String code, String msg) {
        return new ResponseEntity<>(
                ImmutableMap.of(
                        "code", code,
                        "msg", msg,
                        "data", "{}"
                ), HttpStatus.BAD_REQUEST
        );
    }
}
