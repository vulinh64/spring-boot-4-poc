package com.vulinh.data.exception;

import module java.base;

public class EmployeeUnchangedException extends RuntimeException {

  @Serial private static final long serialVersionUID = -3996359080504301745L;

  public EmployeeUnchangedException(long id) {
    super("Employee with id %d remained unchanged".formatted(id));
  }
}
