package org.vrr.simplecloudservice.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.vrr.simplecloudservice.dto.request.RegistrationRequestDto;
import org.vrr.simplecloudservice.facade.CloudServiceFacade;

@RestController
@RequiredArgsConstructor
@Validated
public class RegistrationController {

    private final CloudServiceFacade facade;

    @PostMapping("/register")
    public ResponseEntity<Void> registerClient(@Valid @RequestBody RegistrationRequestDto dto){
        facade.registerClient(dto);
        return ResponseEntity.ok().build();
    }
}
