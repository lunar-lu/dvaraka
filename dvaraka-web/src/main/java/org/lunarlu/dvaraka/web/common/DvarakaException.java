package org.lunarlu.dvaraka.web.common;

/**
 * @author jiaoteng
 */
public class DvarakaException extends RuntimeException {

    public DvarakaException() {
    }

    public DvarakaException(String message) {
        super(message);
    }

    public DvarakaException(String message, Throwable cause) {
        super(message, cause);
    }

    public DvarakaException(Throwable cause) {
        super(cause);
    }

    public DvarakaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
