package com.how.arybyungobserver.web.rest.support;

import com.google.common.collect.ImmutableMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ObserverRestSupport {

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
}
