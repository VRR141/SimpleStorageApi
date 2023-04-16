package org.vrr.simplecloudservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vrr.simplecloudservice.facade.CloudServiceFacade;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private final CloudServiceFacade facade;

    @GetMapping
    public String get() {
        facade.checkOk();
        return "OK";
    }
}
