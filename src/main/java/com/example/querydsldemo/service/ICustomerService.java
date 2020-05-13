package com.example.querydsldemo.service;

import com.example.querydsldemo.domain.dto.CustomerDto;
import com.querydsl.core.Tuple;

import java.util.List;

/**
 * @Desc
 * @Created By liukuihan
 * @date on 2020/5/13
 */
public interface ICustomerService {

    CustomerDto findById(String id);
    List<CustomerDto> findByGender(int gender);
    List<CustomerDto> findByGenderAndAgeGt(int gender,int age);
    List<CustomerDto> findByNameOrAgeGt(String name,int age);

    List<CustomerDto> findByCompanyName(String name);

    List<CustomerDto> findByCompanyNameLeftJoin(String name);

    List<Tuple> findCompanyAndCustomerByCompanyName(String name);
}
