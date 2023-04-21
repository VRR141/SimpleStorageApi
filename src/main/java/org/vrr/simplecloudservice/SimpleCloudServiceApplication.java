package org.vrr.simplecloudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SimpleCloudServiceApplication {

    public static void main(String[] args) {
       SpringApplication.run(SimpleCloudServiceApplication.class, args);
    }

}
