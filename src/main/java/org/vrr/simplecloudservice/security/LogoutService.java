package org.vrr.simplecloudservice.security;

public interface LogoutService {

    void logout(String username, String jwt);

    boolean checkLogoutExistence(String username, String jwt);
}
