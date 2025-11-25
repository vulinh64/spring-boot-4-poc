package com.vulinh.configuration;

import com.vulinh.keycloak.KeycloakAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfiguration {

  @Bean
  public HttpServiceProxyFactory restClientAdapter() {
    return HttpServiceProxyFactory.builderFor(
            RestClientAdapter.create(RestClient.builder().build()))
        .build();
  }

  @Bean
  public KeycloakAuth keycloakAuth(HttpServiceProxyFactory restClient) {
    return restClient.createClient(KeycloakAuth.class);
  }
}
