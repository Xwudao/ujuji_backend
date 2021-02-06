package com.ujuji.navigation.exception;

import com.ujuji.navigation.util.ResultCode;

public class MyException extends RuntimeException {
    private String message;
    private ResultCode code;

    public MyException() {
        super();
    }

    public MyException(String message) {
        super(message);
        this.message = message;
    }

    public MyException(ResultCode code) {
        super(code.getMsg());
        this.message = code.getMsg();
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }
}
