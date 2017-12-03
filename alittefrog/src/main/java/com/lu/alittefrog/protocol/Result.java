package com.lu.alittefrog.protocol;

import java.io.Serializable;

public class Result implements Serializable {
    private Object result;
    private RuntimeException exception;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(RuntimeException exception) {
        this.exception = exception;
    }
}
