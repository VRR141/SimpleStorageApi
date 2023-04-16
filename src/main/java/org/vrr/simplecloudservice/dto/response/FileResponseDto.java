package org.vrr.simplecloudservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FileResponseDto {

    @JsonProperty("filename")
    private String fileName;

    private Long size;
}
