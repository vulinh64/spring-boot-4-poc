package com.vulinh;

import com.vulinh.configuration.data.ApplicationProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EnableConfigurationProperties(ApplicationProperties.class)
class SpringBoot4PocApplication {

  static void main(String[] args) {
    SpringApplication.run(SpringBoot4PocApplication.class, args);
  }
}
