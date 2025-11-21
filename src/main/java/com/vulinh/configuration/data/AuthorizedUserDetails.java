package com.vulinh.configuration.data;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import lombok.Builder;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

// Customized UserDetails object
@Builder
public record AuthorizedUserDetails(
    UUID userId, String username, String email, Collection<GrantedAuthority> authorities)
    implements UserDetails {

  public AuthorizedUserDetails {
    authorities = authorities == null ? Collections.emptyList() : authorities;
  }

  @Override
  @NullMarked
  public Collection<GrantedAuthority> getAuthorities() {
    return authorities;
  }

  // No credentials expose
  @Override
  public String getPassword() {
    return null;
  }

  @Override
  @NullMarked
  public String getUsername() {
    return username;
  }
}
