package org.makson.guardbot.exceptions;

public class ChannelNotFoundException extends RuntimeException {
    public ChannelNotFoundException(String message) {
        super(message);
    }

    public ChannelNotFoundException() {
        super();
    }

    public ChannelNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChannelNotFoundException(Throwable cause) {
        super(cause);
    }
}
