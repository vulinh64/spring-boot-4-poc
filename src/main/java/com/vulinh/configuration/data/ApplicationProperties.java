package com.vulinh.configuration.data;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "application-properties")
public record ApplicationProperties(String realmName, String clientName, List<String> noAuthUrls) {}
