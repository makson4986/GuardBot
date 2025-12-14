package org.makson.guardbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.makson.guardbot")
public class GuardBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(GuardBotApplication.class, args);
    }

}
