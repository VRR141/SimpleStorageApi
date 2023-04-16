package org.vrr.simplecloudservice.dto.request;

//TODO add validation

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDto {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String middleName;

}
