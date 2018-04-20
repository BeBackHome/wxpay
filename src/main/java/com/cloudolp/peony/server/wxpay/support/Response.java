package com.cloudolp.peony.server.wxpay.support;

/**
 * Created by chao.zeng on 2016/10/10.
 */
public class Response<T> {
    public static final Response AUTH_FAIL = new Response(Error.AUTH_FAIL);
    public static final Response PARAMS_ERROR = new Response(Error.PARAMS_ERROR);
    public static final Response NEVER_MEASURE = new Response(Error.NEVER_MEASURE);
    private Status status= Status.SUCCESS;
    private String errorCode;
    private String errorMsg;
    private T result;
    public Response(){

    }

    public Response(Error error) {
        this.status = Status.FAIL;
        this.errorCode = error.name();
        this.errorMsg = error.msg;
    }
    public Response(Status status, String errorMsg){
        this.status=status;
        this.errorMsg=errorMsg;
    }

    public Response(Status status, String errorCode, String errorMsg) {
        this.status=status;
        this.errorCode = errorCode;
        this.errorMsg=errorMsg;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public enum Status {
        SUCCESS, FAIL
    }
}
