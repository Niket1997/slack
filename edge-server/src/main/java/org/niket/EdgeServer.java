package org.niket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EdgeServer {
    public static void main(String[] args) {
        SpringApplication.run(EdgeServer.class, args);
    }
}