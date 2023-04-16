package org.vrr.simplecloudservice.excecption;

public class ClientNotFoundByEmailException extends CloudServiceException{

    private static final String  MESSAGE = "Client not found by email %s";


    public ClientNotFoundByEmailException(String email) {
        super(String.format(MESSAGE, email));
    }
}
