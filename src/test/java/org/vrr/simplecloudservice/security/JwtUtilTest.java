package org.vrr.simplecloudservice.security;

import com.auth0.jwt.exceptions.JWTDecodeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vrr.simplecloudservice.properties.JwtProperties;
import org.vrr.simplecloudservice.security.jwt.impl.JwtUtilImpl;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtilImpl jwtUtil;

    @Mock
    private JwtProperties jwtProperties;

    private final Integer validity = 15;

    private final String issuer = "cloud";

    private final String secret = "secret";

    private UUID uuid = UUID.randomUUID();

    @BeforeEach
    void beforeEach(){
        Mockito.when(jwtProperties.getTokenValidity()).thenReturn(validity);
        Mockito.when(jwtProperties.getIssuer()).thenReturn(issuer);
        Mockito.when(jwtProperties.getTokenSecret()).thenReturn(secret);
    }

    @Test
    void generateToken_Should_Generate_Token(){
        String s = jwtUtil.generateToken(String.valueOf(uuid));
        Assertions.assertThat(s).isNotNull().hasSizeGreaterThan(10);
    }

    @Test
    void validateTokenAndRetrieveClaim(){
        String s = jwtUtil.generateToken(String.valueOf(uuid));
        String s1 = jwtUtil.validateTokenAndRetrieveClaim(s);
        Assertions.assertThat(s1).isEqualTo(String.valueOf(uuid));
    }

    @Test
    void validateTokenAndRetrieveClaim_Should_Throw_Exception_While_Incorrect_Token(){
        String s = jwtUtil.generateToken(String.valueOf(uuid));
        Assertions.assertThatExceptionOfType(JWTDecodeException.class)
                .isThrownBy(() -> jwtUtil.validateTokenAndRetrieveClaim(s.substring(2)));
    }

    @Test
    void getUuidFromToken_Should_Return_Correct_UUID(){
        String s = jwtUtil.generateToken(String.valueOf(uuid));
        Assertions.assertThat(jwtUtil.getUuidFromToken(s)).isEqualTo(String.valueOf(uuid));
    }

    @Test
    void getUuidFromToken_Should_Throw_Exception(){
        String s = jwtUtil.generateToken(String.valueOf(uuid));
        Assertions.assertThatExceptionOfType(JWTDecodeException.class)
                .isThrownBy(() -> jwtUtil.getUuidFromToken(s.substring(1)));
    }
}
