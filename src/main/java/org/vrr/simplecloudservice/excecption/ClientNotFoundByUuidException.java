package org.vrr.simplecloudservice.excecption;

public class ClientNotFoundByUuidException extends CloudServiceException{

    private final static String MESSAGE = "Client not found by uuid %s";

    public ClientNotFoundByUuidException(String uuid) {
        super(String.format(MESSAGE, uuid));
    }
}
