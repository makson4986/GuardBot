package org.makson.guardbot.exceptions;

public class ModalFieldIsEmptyException extends RuntimeException {
    public ModalFieldIsEmptyException() {
        super();
    }

    public ModalFieldIsEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModalFieldIsEmptyException(Throwable cause) {
        super(cause);
    }

    public ModalFieldIsEmptyException(String message) {
        super(message);
    }
}
