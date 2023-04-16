package org.vrr.simplecloudservice.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileDto implements Serializable {

    private String title;

    private String description;

    private MultipartFile file;

    private String url;

    private Long size;

    private String filename;

}
