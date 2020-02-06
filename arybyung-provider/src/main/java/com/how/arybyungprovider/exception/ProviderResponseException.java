package com.how.arybyungprovider.exception;

public class ProviderResponseException extends RuntimeException {

    private String code;

    public ProviderResponseException(String code, String msg) {
        super(msg);
        this.code = code;
    }

    public String getCode() { return this.code; }
}
