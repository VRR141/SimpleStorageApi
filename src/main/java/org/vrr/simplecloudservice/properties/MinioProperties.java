package org.vrr.simplecloudservice.properties;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:minio.properties")
@Getter
public class MinioProperties {

    @Value("${minio.user}")
    private String user;

    @Value("${minio.password}")
    private String password;

    @Value("${minio.url}")
    private String url;
}
