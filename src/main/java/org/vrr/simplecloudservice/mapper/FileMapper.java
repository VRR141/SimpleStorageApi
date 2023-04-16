package org.vrr.simplecloudservice.mapper;

import org.mapstruct.Mapper;
import org.vrr.simplecloudservice.domain.MinioFile;
import org.vrr.simplecloudservice.dto.response.FileResponseDto;

@Mapper(componentModel = "spring")
public interface FileMapper {

    FileResponseDto mapMinioToFileResponseDto(MinioFile minioFile);
}
