package org.vrr.simplecloudservice.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.dto.response.AuthenticationResponseDto;
import org.vrr.simplecloudservice.security.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<AuthenticationResponseDto> login(
            @RequestBody AuthenticationRequestDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(new AuthenticationResponseDto(token));
    }
}
