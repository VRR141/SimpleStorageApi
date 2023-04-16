package org.vrr.simplecloudservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.vrr.simplecloudservice.domain.ClientProfile;
import org.vrr.simplecloudservice.dto.request.RegistrationRequestDto;

@Mapper(componentModel = "spring")
public interface ClientProfileMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "firstName", target = "client.firstName")
    @Mapping(source = "lastName", target = "client.lastName")
    @Mapping(source = "middleName", target = "client.middleName")
    ClientProfile mapRegistrationRequestDtoToClientProfile(RegistrationRequestDto dto);
}
