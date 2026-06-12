package com.airport.viplounge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.airport.viplounge.mapper")
public class VipLoungeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VipLoungeApplication.class, args);
    }
}
