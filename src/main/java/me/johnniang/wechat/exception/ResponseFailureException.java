package me.johnniang.wechat.exception;

import org.apache.http.HttpResponse;

/**
 * Response failure exception. (Response status does like 2xx)
 *
 * @author johnniang
 */
public class ResponseFailureException extends RuntimeException {

    private HttpResponse response;

    public ResponseFailureException(String message) {
        super(message);
    }

    public ResponseFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResponseFailureException setResponse(HttpResponse response) {
        this.response = response;
        return this;
    }
}
