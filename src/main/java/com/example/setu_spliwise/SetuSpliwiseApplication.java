package com.example.setu_spliwise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.example.setu_spliwise")
@ConfigurationPropertiesScan
public class SetuSpliwiseApplication {

  public static void main(String[] args) {
    SpringApplication.run(SetuSpliwiseApplication.class, args);
  }
}
