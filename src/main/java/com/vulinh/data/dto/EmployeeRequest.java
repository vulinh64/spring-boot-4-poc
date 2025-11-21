package com.vulinh.data.dto;

import module java.base;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.With;
import org.springframework.validation.annotation.Validated;

@Builder
@With
@Validated
public record EmployeeRequest(@NotEmpty String name, @NotNull LocalDate birthDate) {}
