package org.vrr.simplecloudservice.security.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.vrr.simplecloudservice.repo.ClientProfileRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final ClientProfileRepository clientProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthorizedUser authorizedUser = new AuthorizedUser(username, getPassword(username));
        log.info("Authorized User successfully loaded");
        return authorizedUser;
    }

    private String getPassword(String username){
        return clientProfileRepository
                .findByUuid(username)
                .getPassword();
    }
}
