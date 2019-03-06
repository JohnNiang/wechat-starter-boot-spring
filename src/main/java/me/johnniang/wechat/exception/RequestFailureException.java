package me.johnniang.wechat.exception;

/**
 * Request failure exception.
 *
 * @author johnniang
 */
public class RequestFailureException extends RuntimeException {

    private String requestUrl;

    private String method;

    public RequestFailureException(String message) {
        super(message);
    }

    public RequestFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestFailureException setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }

    public RequestFailureException setMethod(String method) {
        this.method = method;
        return this;
    }
}
