package org.makson.guardbot.exceptions;

public class ReportParseException extends RuntimeException {
    public ReportParseException(String message) {
        super(message);
    }

    public ReportParseException() {
        super();
    }

    public ReportParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReportParseException(Throwable cause) {
        super(cause);
    }
}
