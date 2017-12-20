package com.xiaoyu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApplicationActivemqServer {

    public static void main(String args[]) {
        SpringApplication.run(ApplicationActivemqServer.class, args);
    }

}
