package com.example.querydsldemo.repository;

import com.example.querydsldemo.domain.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,String> {
}
