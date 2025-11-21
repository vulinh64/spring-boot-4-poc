package com.vulinh.data.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record EmployeeListResponse(long id, String name) {}
