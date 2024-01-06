package com.dzb.exception;

public class ServiceException extends RuntimeException{
    private String code;
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String code,String message) {
        super(message);
        this.code=code;
    }

}
