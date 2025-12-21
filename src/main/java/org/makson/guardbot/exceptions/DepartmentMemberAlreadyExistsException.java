package org.makson.guardbot.exceptions;

public class DepartmentMemberAlreadyExistsException extends RuntimeException {
    public DepartmentMemberAlreadyExistsException() {
        super();
    }

    public DepartmentMemberAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DepartmentMemberAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    public DepartmentMemberAlreadyExistsException(String message) {
        super(message);
    }
}
