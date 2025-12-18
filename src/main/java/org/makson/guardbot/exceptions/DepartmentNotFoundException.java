package org.makson.guardbot.exceptions;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException() {
        super();
    }

    public DepartmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartmentNotFoundException(Throwable cause) {
        super(cause);
    }

    public DepartmentNotFoundException(String message) {
        super(message);
    }
}
