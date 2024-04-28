package com.microecommerce.utilitymodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class UtilityModuleApplication {
    public static void main(String[] args) {
        SpringApplication.run(UtilityModuleApplication.class, args);
    }
}
