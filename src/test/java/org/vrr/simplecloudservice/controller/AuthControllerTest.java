package org.vrr.simplecloudservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.vrr.simplecloudservice.dto.request.AuthenticationRequestDto;
import org.vrr.simplecloudservice.handler.DefaultRestExceptionHandler;
import org.vrr.simplecloudservice.rest.AuthenticationController;
import org.vrr.simplecloudservice.security.AuthService;
import org.vrr.simplecloudservice.util.JsonStringConverter;

import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

@WebMvcTest(AuthenticationController.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest extends AbstractControllerTest{

    @MockBean
    private AuthService authService;

    private MockMvc mockMvc;

    private final String LOGIN_ROUTE = "/login";

    private final String LOGOUT_ROUTE = "/logout";

    @BeforeEach
    void beforeEach(){
        mockMvc = getMockMvc();
    }

    @ParameterizedTest
    @MethodSource("dtoFactory")
    void login_Should_Correct_Authenticate_While_Correct_RequestBody(AuthenticationRequestDto dto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ROUTE)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding(StandardCharsets.UTF_8)
                .content(JsonStringConverter.asJsonString(dto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @ParameterizedTest
    @MethodSource("incorrectDtoFactory")
    void login_Should_Return_400_While_Not_Valid_RequestBody(AuthenticationRequestDto dto) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @ParameterizedTest
    @MethodSource("dtoFactory")
    void login_Should_Return_400_While_Incorrect_Credentials(AuthenticationRequestDto dto) throws Exception {
        Mockito.when(authService.login(dto)).thenThrow(new BadCredentialsException("bad credentials"));
        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .content(JsonStringConverter.asJsonString(dto)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void logout_Should_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(LOGOUT_ROUTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    Stream<AuthenticationRequestDto> incorrectDtoFactory(){
        return Stream.of(
                AuthenticationRequestDto.builder()
                        .email("234@mail.RU")
                        .password(null)
                        .build(),
                AuthenticationRequestDto.builder()
                        .email("234@mail.RU")
                        .password("")
                        .build(),
                AuthenticationRequestDto.builder()
                        .email("234@mail")
                        .password("1235")
                        .build(),
                AuthenticationRequestDto.builder()
                        .email("")
                        .password("1235")
                        .build(),
                AuthenticationRequestDto.builder()
                        .email(null)
                        .password("1235")
                        .build()
        );
    }


    Stream<AuthenticationRequestDto> dtoFactory(){
        return Stream.of(
                AuthenticationRequestDto.builder()
                        .email("234@mail.RU")
                        .password("243")
                        .build(),
                AuthenticationRequestDto.builder()
                        .email("sdfsdf@gmail.com")
                        .password("123")
                        .build()
        );
    }


    private MockMvc getMockMvc(){
        return MockMvcBuilders
                .standaloneSetup(new AuthenticationController(authService))
                .setControllerAdvice(new DefaultRestExceptionHandler())
                .build();
    }
}
