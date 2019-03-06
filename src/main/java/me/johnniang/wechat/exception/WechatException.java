package me.johnniang.wechat.exception;

/**
 * Wechat exception.
 *
 * @author johnniang
 */
public class WechatException extends RuntimeException {

    /**
     * Error data.
     */
    private Object data;

    public WechatException(String message) {
        super(message);
    }

    public WechatException(String message, Throwable cause) {
        super(message, cause);
    }

    public WechatException setData(Object data) {
        this.data = data;
        return this;
    }
}
