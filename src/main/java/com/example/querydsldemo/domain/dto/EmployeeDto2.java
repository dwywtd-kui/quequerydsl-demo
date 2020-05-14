package com.example.querydsldemo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto2 {
    private String code;
    private String userName;
    private Integer age;
}
