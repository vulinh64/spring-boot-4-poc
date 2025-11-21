package com.vulinh.data.mapper;

import module java.base;

import com.vulinh.data.dto.EmployeeListResponse;
import com.vulinh.data.dto.EmployeeRequest;
import com.vulinh.data.dto.EmployeeResponse;
import com.vulinh.data.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeMapper {

  EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

  Employee toEmployee(EmployeeRequest employeeResponse);

  EmployeeResponse toEmployeeResponse(Employee employee);

  EmployeeListResponse toEmployeeListResponse(Employee employees);

  Employee merge(EmployeeRequest employeeRequest, @MappingTarget Employee employee);
}
