package org.vrr.simplecloudservice.configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vrr.simplecloudservice.properties.MinioProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {

    private final MinioProperties minioProperties;

    @Bean
    MinioClient minioClient() {
        return new MinioClient.Builder()
                .credentials(minioProperties.getUser(), minioProperties.getPassword())
                .endpoint(minioProperties.getUrl())
                .build();
    }
}
