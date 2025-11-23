package com.vulinh.service;

import module java.base;

import com.vulinh.data.dto.EmployeeListResponse;
import com.vulinh.data.dto.EmployeeRequest;
import com.vulinh.data.dto.EmployeeResponse;
import com.vulinh.data.exception.EmployeeNotFoundException;
import com.vulinh.data.exception.EmployeeUnchangedException;
import com.vulinh.data.mapper.EmployeeMapper;
import com.vulinh.data.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Strings;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

  static final EmployeeMapper EMPLOYEE_MAPPER = EmployeeMapper.INSTANCE;

  final EmployeeRepository employeeRepository;

  public Page<@NonNull EmployeeListResponse> findAllEmployees(Pageable pageable) {
    return employeeRepository.findAll(pageable).map(EMPLOYEE_MAPPER::toEmployeeListResponse);
  }

  public EmployeeResponse findSpecificEmployee(@NonNull Long employeeId) {
    return EMPLOYEE_MAPPER.toEmployeeResponse(
        employeeRepository
            .findById(employeeId)
            .orElseThrow(() -> new EmployeeNotFoundException(employeeId)));
  }

  public void createEmployee(@NonNull EmployeeRequest employeeRequest) {
    employeeRepository.save(EMPLOYEE_MAPPER.toEmployee(employeeRequest));
  }

  public void editEmployee(long id, @NonNull EmployeeRequest employeeRequest) {
    var existingEmployee =
        employeeRepository.findById(id).orElseThrow(() -> new EmployeeNotFoundException(id));

    if (Strings.CI.equals(existingEmployee.getName(), employeeRequest.name())
        && Objects.equals(employeeRequest.birthDate(), existingEmployee.getBirthDate())) {
      throw new EmployeeUnchangedException(id);
    }

    var updatedEmployee = EMPLOYEE_MAPPER.merge(employeeRequest, existingEmployee);

    employeeRepository.save(updatedEmployee);
  }

  public boolean deleteEmployee(@NonNull Long employeeId) {
    return employeeRepository
        .findById(employeeId)
        .map(
            employee -> {
              employeeRepository.delete(employee);
              return true;
            })
        .orElse(Boolean.FALSE);
  }
}
