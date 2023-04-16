package org.vrr.simplecloudservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.vrr.simplecloudservice.repo.jpa.ClientProfileJpaRepository;

import java.awt.*;

@SpringBootApplication
public class SimpleCloudServiceApplication {

    public static void main(String[] args) {
       SpringApplication.run(SimpleCloudServiceApplication.class, args);
    }

}
