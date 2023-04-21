package org.vrr.simplecloudservice.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.dto.response.AuthenticationResponseDto;
import org.vrr.simplecloudservice.security.AuthService;

@RestController
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(
            @Valid @RequestBody AuthenticationRequestDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(new AuthenticationResponseDto(token));
    }

    @PostMapping("/logout")
    private ResponseEntity<Void> logout(HttpServletRequest request){
        authService.logout(request);
        return ResponseEntity.ok().build();
    }
}
