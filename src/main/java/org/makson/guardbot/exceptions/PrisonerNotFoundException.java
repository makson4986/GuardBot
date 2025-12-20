package org.makson.guardbot.exceptions;

public class PrisonerNotFoundException extends RuntimeException {
    public PrisonerNotFoundException(String message) {
        super(message);
    }

    public PrisonerNotFoundException() {
        super();
    }

    public PrisonerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrisonerNotFoundException(Throwable cause) {
        super(cause);
    }
}
