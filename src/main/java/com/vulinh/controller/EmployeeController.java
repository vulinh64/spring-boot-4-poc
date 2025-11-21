package com.vulinh.controller;

import com.vulinh.data.dto.EmployeeListResponse;
import com.vulinh.data.dto.EmployeeRequest;
import com.vulinh.data.dto.EmployeeResponse;
import com.vulinh.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

  private final EmployeeService employeeService;

  @GetMapping
  public Page<@NonNull EmployeeListResponse> getEmployees(Pageable pageable) {
    return employeeService.findAllEmployees(pageable);
  }

  @GetMapping("/{id}")
  public EmployeeResponse getEmployee(@PathVariable Long id) {
    return employeeService.findSpecificEmployee(id);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createEmployee(@Valid @RequestBody EmployeeRequest employeeRequest) {
    employeeService.createEmployee(employeeRequest);
  }

  @PutMapping("/{id}")
  public void editEmployee(
      @PathVariable Long id, @Valid @RequestBody EmployeeRequest employeeRequest) {
    employeeService.editEmployee(id, employeeRequest);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<@NonNull Object> deleteEmployee(@PathVariable Long id) {
    return employeeService.deleteEmployee(id)
        ? ResponseEntity.ok().build()
        : ResponseEntity.noContent().build();
  }
}
