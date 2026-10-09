package com.datavault.personal_data_vault.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Generated;

@JsonInclude(value=JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(true, message, data, null);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<T>(true, message, null, null);
    }

    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<T>(false, message, null, errorCode);
    }

    @Generated
    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<T>();
    }

    @Generated
    public boolean isSuccess() {
        return this.success;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public T getData() {
        return this.data;
    }

    @Generated
    public String getErrorCode() {
        return this.errorCode;
    }

    @Generated
    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setData(T data) {
        this.data = data;
    }

    @Generated
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Generated
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ApiResponse)) {
            return false;
        }
        ApiResponse other = (ApiResponse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.isSuccess() != other.isSuccess()) {
            return false;
        }
        String this$message = this.getMessage();
        String other$message = other.getMessage();
        if (this$message == null ? other$message != null : !this$message.equals(other$message)) {
            return false;
        }
        Object this$data = this.getData();
        Object other$data = other.getData();
        if (this$data == null ? other$data != null : !this$data.equals(other$data)) {
            return false;
        }
        String this$errorCode = this.getErrorCode();
        String other$errorCode = other.getErrorCode();
        return !(this$errorCode == null ? other$errorCode != null : !this$errorCode.equals(other$errorCode));
    }

    @Generated
    protected boolean canEqual(Object other) {
        return other instanceof ApiResponse;
    }

    @Generated
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + (this.isSuccess() ? 79 : 97);
        String $message = this.getMessage();
        result = result * 59 + ($message == null ? 43 : $message.hashCode());
        T $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        String $errorCode = this.getErrorCode();
        result = result * 59 + ($errorCode == null ? 43 : $errorCode.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        return "ApiResponse(success=" + this.isSuccess() + ", message=" + this.getMessage() + ", data=" + String.valueOf(this.getData()) + ", errorCode=" + this.getErrorCode() + ")";
    }

    @Generated
    public ApiResponse() {
    }

    @Generated
    public ApiResponse(boolean success, String message, T data, String errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    @Generated
    public static class ApiResponseBuilder<T> {
        @Generated
        private boolean success;
        @Generated
        private String message;
        @Generated
        private T data;
        @Generated
        private String errorCode;

        @Generated
        ApiResponseBuilder() {
        }

        @Generated
        public ApiResponseBuilder<T> success(boolean success) {
            this.success = success;
            return this;
        }

        @Generated
        public ApiResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        @Generated
        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        @Generated
        public ApiResponseBuilder<T> errorCode(String errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        @Generated
        public ApiResponse<T> build() {
            return new ApiResponse<T>(this.success, this.message, this.data, this.errorCode);
        }

        @Generated
        public String toString() {
            return "ApiResponse.ApiResponseBuilder(success=" + this.success + ", message=" + this.message + ", data=" + String.valueOf(this.data) + ", errorCode=" + this.errorCode + ")";
        }
    }
}
