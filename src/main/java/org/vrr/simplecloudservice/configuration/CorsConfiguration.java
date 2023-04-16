package org.vrr.simplecloudservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.vrr.simplecloudservice.properties.CorsProperties;

@Configuration
@RequiredArgsConstructor
public class CorsConfiguration implements WebMvcConfigurer {

    private final CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping(corsProperties.getMapping())
                .allowCredentials(true)
                .allowedOrigins(corsProperties.getOrigins())
                .allowedMethods(corsProperties.getMethods())
                .allowedHeaders(corsProperties.getHeaders());
    }
}
