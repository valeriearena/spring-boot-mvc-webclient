package com.mh.careteaminterface;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CTClientApplication is a Spring Boot Launcher, which bootstraps the app and starts Spring by
 * creating an instance of the Spring container (application context) and launch the app from the main method.
 */
@SpringBootApplication // @Configuration, @EnableAutoConfiguration, and @ComponentScan.
public class CTClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(CTClientApplication.class, args);
  }

}
