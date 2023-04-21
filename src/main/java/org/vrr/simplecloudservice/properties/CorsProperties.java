package org.vrr.simplecloudservice.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsProperties {

    private String mapping;

    private String[] origins;

    private String[] methods;

    private String[] headers;
}
