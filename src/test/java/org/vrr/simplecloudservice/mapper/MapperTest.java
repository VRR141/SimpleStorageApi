package org.vrr.simplecloudservice.mapper;

import jakarta.validation.constraints.Min;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vrr.simplecloudservice.domain.ClientProfile;
import org.vrr.simplecloudservice.domain.MinioFile;
import org.vrr.simplecloudservice.dto.request.RegistrationRequestDto;
import org.vrr.simplecloudservice.dto.response.FileResponseDto;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MapperTest {

    @InjectMocks
    private ClientProfileMapperImpl profileMapper;

    @InjectMocks
    private FileMapperImpl fileMapper;

    @ParameterizedTest
    @MethodSource("dtoFactory")
    void mapRegistrationRequestDtoToClientProfile_Should_Map(RegistrationRequestDto dto){
        ClientProfile actual = profileMapper.mapRegistrationRequestDtoToClientProfile(dto);
        assertThat(actual.getClient().getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(actual.getClient().getLastName()).isEqualTo(dto.getLastName());
        assertThat(actual.getClient().getMiddleName()).isEqualTo(dto.getMiddleName());
        assertThat(actual.getPassword()).isEqualTo(dto.getPassword());
        assertThat(actual.getEmail()).isEqualTo(dto.getEmail());
        assertThat(actual.getId()).isNull();
        assertThat(actual.getClient().getId()).isNull();
        assertThat(actual.getClient()).isNotNull();
    }

    @ParameterizedTest
    @MethodSource("minioFileFactory")
    void mapMinioToFileResponseDto_Should_Map(MinioFile file){
        FileResponseDto dto = fileMapper.mapMinioToFileResponseDto(file);
        Assertions.assertThat(dto.getFileName()).isEqualTo(file.getFileName());
        Assertions.assertThat(dto.getSize()).isEqualTo(file.getSize());
    }

    static Stream<RegistrationRequestDto> dtoFactory(){
        return Stream.of(
                RegistrationRequestDto.builder()
                        .email("1@mail.ru")
                        .password("q1w2e3")
                        .lastName("last")
                        .firstName("first")
                        .middleName("middle")
                        .build(),
                RegistrationRequestDto.builder()
                        .email("2@mail.ru")
                        .password("r4t5y")
                        .lastName("last2")
                        .firstName("first2")
                        .middleName("middle2")
                        .build()
        );
    }

    static Stream<MinioFile> minioFileFactory() {
        return Stream.of(
                MinioFile.builder()
                        .fileName("fileName.txt")
                        .size(74568456L)
                        .build(),
                MinioFile.builder()
                        .fileName("fileName345.txt")
                        .size(234234L)
                        .build()
        );
    }

}
