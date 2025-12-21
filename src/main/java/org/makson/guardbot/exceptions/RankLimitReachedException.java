package org.makson.guardbot.exceptions;

public class RankLimitReachedException extends RuntimeException {
    public RankLimitReachedException(String message) {
        super(message);
    }

    public RankLimitReachedException() {
        super();
    }

    public RankLimitReachedException(String message, Throwable cause) {
        super(message, cause);
    }

    public RankLimitReachedException(Throwable cause) {
        super(cause);
    }
}
