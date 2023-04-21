package org.vrr.simplecloudservice.integration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("application-test")
@TestPropertySource({"classpath:/application-test.yaml", "classpath:/minio-test.properties" })
@Transactional
@ContextConfiguration(initializers = AbstractCloudStorageServiceIntegrationTest.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractCloudStorageServiceIntegrationTest {

    protected static final String ORIGIN_HEADER_VALUE = "http://localhost:5555/";

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private static final String POSTGRES_DOCKER_IMAGE_NAME = "postgres:15";

        private static final String MINIO_DOCKER_IMAGE_NAME = "bitnami/minio:2023.3.24";


        private static PostgreSQLContainer postgres = new PostgreSQLContainer(POSTGRES_DOCKER_IMAGE_NAME) {
            {
                withDatabaseName("cloud_service");
                withUsername("user");
                withPassword("password");
            }
        };

        private static GenericContainer minio = new GenericContainer(MINIO_DOCKER_IMAGE_NAME){
            {
                Map<String, String> props = Map.of(
                        "MINIO_ROOT_USER", "minio_user",
                        "MINIO_ROOT_PASSWORD", "minio_password"
                );
                withExposedPorts(9000, 9001);
                withEnv(props);
            }
        };

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            postgres.start();
            minio.start();
            System.setProperty("SPRING_DATASOURCE_URL", postgres.getJdbcUrl());
            System.setProperty("SPRING_DATASOURCE_NAME)", postgres.getUsername());
            System.setProperty("SPRING_DATASOURCE_PASSWORD", postgres.getPassword());
            System.setProperty("MINIO_USER", "minio_user");
            System.setProperty("MINIO_PASSWORD", "minio_password");
            System.setProperty("CORS_ORIGINS", ORIGIN_HEADER_VALUE);
            System.setProperty("MINIO_URL", "http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
        }

        public static void close(){
            postgres.close();
            minio.close();
        }
    }

    @AfterAll
    static void afterAll(){
        Initializer.close();
    }
}
