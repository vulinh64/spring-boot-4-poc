package com.vulinh.data.exception;

import module java.base;

public class EmployeeNotFoundException extends RuntimeException {

  @Serial private static final long serialVersionUID = 7993421485272254985L;

  public EmployeeNotFoundException(long employeeId) {
    super("Employee with id %d not found".formatted(employeeId));
  }
}
