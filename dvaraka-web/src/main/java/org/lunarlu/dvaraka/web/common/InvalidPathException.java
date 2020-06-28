package org.lunarlu.dvaraka.web.common;

/**
 * invalid path
 *
 * @author jiaoteng
 */
public class InvalidPathException extends DvarakaException {

    private final String path;


    public InvalidPathException(String path, String message) {
        super(message);
        this.path = path;
    }


    @Override
    public String toString() {
        return "InvalidPathException{" +
                "path='" + path + '\'' +
                "message='" + getMessage() + '\'' +
                '}';
    }
}
