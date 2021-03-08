package com.pony;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Myblog1Application {

    @Bean
    public ExitCodeGenerator exitCodeGenerator(){
        return () -> 99999999;
    }

    public static void main(String[] args) {
        SpringApplication.run(Myblog1Application.class, args);
    }

}
