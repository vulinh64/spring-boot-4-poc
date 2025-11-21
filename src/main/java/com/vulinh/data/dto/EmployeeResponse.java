package com.vulinh.data.dto;

import module java.base;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record EmployeeResponse(
    long id, String name, LocalDate birthDate, Instant createdAt, Instant updatedAt) {}
