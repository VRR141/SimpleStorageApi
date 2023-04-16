package org.vrr.simplecloudservice.excecption;

public class CloudServiceException extends RuntimeException{

    public CloudServiceException() {
        super();
    }

    public CloudServiceException(String message) {
        super(message);
    }

    public CloudServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudServiceException(Throwable cause) {
        super(cause);
    }
}
