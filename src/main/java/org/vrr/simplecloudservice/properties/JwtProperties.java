package org.vrr.simplecloudservice.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:jwt.properties")
@Getter
@Data
public class JwtProperties {

    @Value("${jwt.token.secret}")
    private String tokenSecret;

    @Value("${jwt.token.validity}")
    private Integer tokenValidity;

    @Value("${jwt.token.issuer}")
    private String issuer;

    @Value("${jwt.filter.bearer.name}")
    private String bearerName;

    @Value("${jwt.filter.bearer.length}")
    private Integer bearerLength;

    @Value("${jwt.filter.header.authorization}")
    private String authorizationHeader;
}
