package com.vulinh;

import com.vulinh.configuration.data.ApplicationProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;

@SpringBootApplication
@EnableJpaAuditing
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
@EnableConfigurationProperties(ApplicationProperties.class)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class SpringBoot4PocApplication {

  static void main(String[] args) {
    SpringApplication.run(SpringBoot4PocApplication.class, args);
  }
}
