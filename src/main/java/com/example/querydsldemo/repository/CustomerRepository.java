package com.example.querydsldemo.repository;

import com.example.querydsldemo.domain.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Employee,String> {
}
