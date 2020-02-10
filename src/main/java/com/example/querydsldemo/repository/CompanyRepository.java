package com.example.querydsldemo.repository;

import com.example.querydsldemo.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,String> {
}
