package org.vrr.simplecloudservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.vrr.simplecloudservice.util.pattern.ValidationPattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationRequestDto {

    @NotEmpty
    @NotNull
    @Pattern(regexp = ValidationPattern.EMAIL_PATTERN)
    @JsonProperty("login")
    private String email;

    @NotNull
    @NotEmpty
    private String password;
}
