package com.example.querydsldemo.domain.dto;

import lombok.Data;

/**
 * @Desc
 * @Created By liukuihan
 * @date on 2020/5/13
 */
@Data
public class CustomerDto  {
    private String id;
    private String name;
    private Integer gender;
    private String phone;
    private String companyId;
    private Integer age;
}
