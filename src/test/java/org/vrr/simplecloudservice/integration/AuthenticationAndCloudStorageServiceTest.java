package org.vrr.simplecloudservice.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.vrr.simplecloudservice.domain.Client;
import org.vrr.simplecloudservice.domain.MinioFile;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.dto.request.RegistrationRequestDto;
import org.vrr.simplecloudservice.dto.response.AuthenticationResponseDto;
import org.vrr.simplecloudservice.repo.ClientProfileRepository;
import org.vrr.simplecloudservice.service.CloudStorageService;
import org.vrr.simplecloudservice.util.JsonStringConverter;

import java.nio.charset.StandardCharsets;
import java.util.List;

class AuthenticationAndCloudStorageServiceTest extends AbstractCloudStorageServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CloudStorageService cloudStorageService;

    @Autowired
    private ClientProfileRepository clientProfileRepository;

    private final String REGISTRATION_ROUTE = "/register";

    private final String AUTHENTICATION_ROUTE = "/login";

    private final String UPLOAD_FILE_ROUTE = "/file";

    private final String GET_FILE_LIST_ROUTE = "/list";

    private final String GET_FILE_ROUTE = "/file";

    private final String DELETE_FILE_ROUTE = "/file";

    private static final String email = "somemail@mail.ru";

    private static final String password = "Q1w2E3r4";

    private final String ORIGIN_HEADER = "Origin";

    private static RegistrationRequestDto dto;

    private String authToken;

    private final String AUTH_TOKEN_HEADER = "auth-token";

    private final String FILE_NAME = "test.json";

    private final String FILE_NAME_REQUEST_PARAM = "filename";

    private final String BEARER = "Bearer ";

    private final String LIST_PARAM = "limit";

    private String clientUuid;

    private final String INCORRECT_ORIGIN_VALUE = "http://localhost:1323/";


    @BeforeAll
    static void setUpBeforeClass(){
        dto = getDto();
    }

    @Test
    void registration_And_Uploading_Files_Test() throws Exception {
        registration();
        authentication();
        uploadFile();
        getListFiles();
        getFile();
        deleteFile();
    }


    void registration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTRATION_ROUTE)
                        .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        Client clientByEmail = clientProfileRepository.findClientByEmail(email);
        clientUuid = String.valueOf(clientByEmail.getUuid());
    }

    void authentication() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(AUTHENTICATION_ROUTE)
                        .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(authDto())))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        AuthenticationResponseDto authenticationResponseDto = JsonStringConverter.jsonToObject(contentAsString, AuthenticationResponseDto.class);
        authToken = BEARER + authenticationResponseDto.getAuthToken();
        Assertions.assertNotNull(authToken);
        Assertions.assertTrue(authToken.length() > BEARER.length());
    }

    void uploadFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart(UPLOAD_FILE_ROUTE)
                .part(getMockPart())
                .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                .header(AUTH_TOKEN_HEADER, authToken)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param(FILE_NAME_REQUEST_PARAM, FILE_NAME)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    void getListFiles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_FILE_LIST_ROUTE)
                .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                .header(AUTH_TOKEN_HEADER, authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .param(LIST_PARAM, "3")
        ).andExpect(MockMvcResultMatchers.status().isOk());
        List<MinioFile> limitedList = cloudStorageService.getLimitedList(clientUuid, 3);
        Assertions.assertFalse(limitedList.isEmpty());
        Assertions.assertEquals(limitedList.get(0).getFileName(), FILE_NAME);
    }

    void getFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(GET_FILE_ROUTE)
                .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                .header(AUTH_TOKEN_HEADER, authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .param(FILE_NAME_REQUEST_PARAM, FILE_NAME)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    void deleteFile() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete(DELETE_FILE_ROUTE)
                .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                .header(AUTH_TOKEN_HEADER, authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .param(FILE_NAME_REQUEST_PARAM, FILE_NAME)
        ).andExpect(MockMvcResultMatchers.status().isOk());
        List<MinioFile> limitedList = cloudStorageService.getLimitedList(clientUuid, 3);
        Assertions.assertTrue(limitedList.isEmpty());
    }

    @Test
    void runner() throws Exception {
        cors_Should_Return_403_While_Missing_Origin();
        registration_Should_Return_200();
        login_Should_Return_400_While_Incorrect_Credentials();
    }

    void registration_Should_Return_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTRATION_ROUTE)
                        .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }


    void login_Should_Return_400_While_Incorrect_Credentials() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post(AUTHENTICATION_ROUTE)
                        .header(ORIGIN_HEADER, ORIGIN_HEADER_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(incorrectDto())))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    void cors_Should_Return_403_While_Missing_Origin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(REGISTRATION_ROUTE)
                        .header(ORIGIN_HEADER, INCORRECT_ORIGIN_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(dto)))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    private MockPart getMockPart(){
        return new MockPart("file", FILE_NAME, "some content".getBytes());
    }

    private AuthenticationRequestDto incorrectDto(){
        return AuthenticationRequestDto.builder()
                .email(dto.getEmail())
                .password(dto.getPassword() + "1231asd")
                .build();
    }

    private AuthenticationRequestDto authDto(){
        return AuthenticationRequestDto.builder()
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    private static RegistrationRequestDto getDto(){
        return RegistrationRequestDto.builder()
                .email(email)
                .password(password)
                .firstName("Name")
                .lastName("Lastname")
                .middleName("Patronymic")
                .build();
    }
}
