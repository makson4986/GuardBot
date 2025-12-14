package org.makson.guardbot.exceptions;

public class ChannelNoFoundException extends RuntimeException {
    public ChannelNoFoundException(String message) {
        super(message);
    }

    public ChannelNoFoundException() {
        super();
    }

    public ChannelNoFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelNoFoundException(Throwable cause) {
        super(cause);
    }
}
