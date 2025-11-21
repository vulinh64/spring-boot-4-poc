package com.vulinh.data.repository;

import com.vulinh.data.entity.Employee;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.ListQuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository
    extends JpaRepository<@NonNull Employee, @NonNull Long>,
        JpaSpecificationExecutor<@NonNull Employee>,
        ListQuerydslPredicateExecutor<@NonNull Employee> {}
