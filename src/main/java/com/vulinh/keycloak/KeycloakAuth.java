package com.vulinh.keycloak;


import com.vulinh.data.exchange.KeycloakTokenExchange;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
public interface KeycloakAuth {

  @PostExchange(
      url = "/protocol/openid-connect/token",
      contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  KeycloakTokenExchange getToken(
      @RequestPart("grant_type") String grantType,
      @RequestPart("client_id") String clientId,
      @RequestPart String username,
      @RequestPart String password);

  default KeycloakTokenExchange getToken(String username, String password) {
    return getToken("password", "spring-client", username, password);
  }
}
