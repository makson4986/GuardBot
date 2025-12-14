package org.makson.guardbot.exceptions;

public class GuardsmanNotFoundException extends RuntimeException {
    public GuardsmanNotFoundException(String message) {
        super(message);
    }

    public GuardsmanNotFoundException() {
        super();
    }

    public GuardsmanNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuardsmanNotFoundException(Throwable cause) {
        super(cause);
    }
}
