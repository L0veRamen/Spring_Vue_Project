package com.jc666.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@MapperScan("com.jc666")
public class SpringbootApplication {


    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }


}
