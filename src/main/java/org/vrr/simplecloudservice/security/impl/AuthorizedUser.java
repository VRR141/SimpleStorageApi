package org.vrr.simplecloudservice.security.impl;


import org.springframework.security.core.userdetails.User;

import java.util.Collections;

public class AuthorizedUser extends User {

    public AuthorizedUser(String username, String password) {
        super(username, password, Collections.emptyList());
    }
}
