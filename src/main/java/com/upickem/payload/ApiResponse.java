package com.upickem.payload;

public class ApiResponse<T> {
    private Boolean success;
    private T message;

    public ApiResponse(Boolean success, T message) {
        this.success = success;
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
