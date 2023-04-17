package org.vrr.simplecloudservice.rest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vrr.simplecloudservice.security.AuthProvider;
import org.vrr.simplecloudservice.security.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
public class HealthController {

    private final AuthService authService;

    @GetMapping
    public String get() {
        return "OK";
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request){
        authService.logout(request);
        return ResponseEntity.ok().build();
    }
}
