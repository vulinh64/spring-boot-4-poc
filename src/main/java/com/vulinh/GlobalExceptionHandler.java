package com.vulinh;

import com.vulinh.data.dto.ErrorResponse;
import com.vulinh.data.exception.EmployeeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EmployeeNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handleEmployeeNotFoundException(EmployeeNotFoundException ex) {
    log.info(ex.getMessage());

    return new ErrorResponse("Employee not found");
  }
}
