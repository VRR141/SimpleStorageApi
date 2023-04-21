package org.vrr.simplecloudservice.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.vrr.simplecloudservice.configuration.CorsConfiguration;
import org.vrr.simplecloudservice.properties.CorsProperties;
import org.vrr.simplecloudservice.properties.JwtProperties;
import org.vrr.simplecloudservice.security.jwt.impl.JwtFilter;

public class AbstractControllerTest {

    @MockBean
    private JwtFilter jwtFilter;

    @MockBean
    private JwtProperties jwtProperties;

    @MockBean
    private CorsProperties corsProperties;

    @MockBean
    private CorsConfiguration corsConfiguration;
}
