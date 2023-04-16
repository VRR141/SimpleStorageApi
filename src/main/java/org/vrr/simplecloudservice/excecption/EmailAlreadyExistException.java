package org.vrr.simplecloudservice.excecption;

public class EmailAlreadyExistException extends CloudServiceException{

    private static final String MESSAGE = "Email %s already exist";

    public EmailAlreadyExistException(String email) {
        super(String.format(MESSAGE, email));
    }
}
