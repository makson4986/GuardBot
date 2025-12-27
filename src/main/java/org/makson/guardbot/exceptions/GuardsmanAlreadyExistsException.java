package org.makson.guardbot.exceptions;

public class GuardsmanAlreadyExistsException extends RuntimeException {
    public GuardsmanAlreadyExistsException(String message) {
        super(message);
    }

    public GuardsmanAlreadyExistsException() {
        super();
    }

    public GuardsmanAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuardsmanAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
