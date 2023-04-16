package org.vrr.simplecloudservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AuthenticationResponseDto {

    @JsonProperty("auth-token")
    private String authToken;
}
