package hu.gehorvath.recipes.error;

import hu.gehorvath.recipes.exception.ResponseException;

import java.util.Date;

public class APIError {
    private String message;
    private Integer status;
    private Date when;
    private Class<? extends ResponseException> exception;
    private Class<? extends Throwable> originalException;

    public APIError(ResponseException e) {
        this.message = e.getMessage();
        this.status = e.getStatus().value();
        this.when = e.getTime();
        this.exception = e.getClass();
        this.originalException = e.getCause() == null ? null : e.getCause().getClass();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public Class<? extends ResponseException> getException() {
        return exception;
    }

    public void setException(Class<? extends ResponseException> exception) {
        this.exception = exception;
    }

    public Class<? extends Throwable> getOriginalException() {
        return originalException;
    }

    public void setOriginalException(Class<? extends Throwable> originalException) {
        this.originalException = originalException;
    }
}
