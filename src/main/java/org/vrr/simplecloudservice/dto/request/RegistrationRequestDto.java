package org.vrr.simplecloudservice.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vrr.simplecloudservice.util.pattern.ValidationPattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationRequestDto {

    @Pattern(regexp = ValidationPattern.EMAIL_PATTERN)
    @NotNull
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String firstName;

    @NotNull
    @NotEmpty
    private String lastName;

    private String middleName;

}
