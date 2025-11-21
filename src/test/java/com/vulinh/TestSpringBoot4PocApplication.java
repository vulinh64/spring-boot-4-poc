package com.vulinh;

import org.springframework.boot.SpringApplication;

public class TestSpringBoot4PocApplication {

  static void main(String[] args) {
    SpringApplication.from(SpringBoot4PocApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
