package org.makson.guardbot.exceptions;

public class GuildNotFoundException extends RuntimeException {
    public GuildNotFoundException() {
        super();
    }

    public GuildNotFoundException(String message) {
        super(message);
    }

    public GuildNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuildNotFoundException(Throwable cause) {
        super(cause);
    }
}
