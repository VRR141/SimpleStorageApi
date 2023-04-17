package org.vrr.simplecloudservice.excecption;

public class InvalidFileIdentifierException extends CloudServiceException{

    private static final String MESSAGE = "Invalid file name data";

    public InvalidFileIdentifierException() {
        super(MESSAGE);
    }
}
