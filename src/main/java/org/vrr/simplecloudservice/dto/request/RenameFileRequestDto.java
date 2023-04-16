package org.vrr.simplecloudservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RenameFileRequestDto {

    @JsonProperty(value = "filename")
    private String newFileName;
}
