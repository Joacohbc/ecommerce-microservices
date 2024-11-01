package com.microecommerce.usersauthservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.microecommerce.utilitymodule.models.users")
public class UsersAuthServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UsersAuthServiceApplication.class, args);
    }
}
