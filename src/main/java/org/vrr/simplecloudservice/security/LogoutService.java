package org.vrr.simplecloudservice.security;

public interface LogoutService {

    void logout(String jwt);

    boolean checkLogoutExistence(String jwt);
}
