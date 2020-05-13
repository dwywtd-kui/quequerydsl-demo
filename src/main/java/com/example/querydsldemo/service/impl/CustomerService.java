package com.example.querydsldemo.service.impl;

import com.example.querydsldemo.domain.dto.CustomerDto;
import com.example.querydsldemo.domain.entity.Company;
import com.example.querydsldemo.domain.entity.Customer;
import com.example.querydsldemo.domain.entity.querydsl.QCompany;
import com.example.querydsldemo.domain.entity.querydsl.QCustomer;
import com.example.querydsldemo.service.ICustomerService;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @Created By liukuihan
 * @date on 2020/5/13
 */
@Service
public class CustomerService implements ICustomerService {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;


    @Override
    public CustomerDto findById(String id) {
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer> jpaQuery = jpaQueryFactory.selectFrom(qCustomer).where(qCustomer.id.eq(id));
        //.selectFrom(entity) == .select(entity).from (entity)
        Customer customer = jpaQuery.fetchOne();
        if (customer==null)
            return null;
        CustomerDto dto = new CustomerDto();
        BeanUtils.copyProperties(customer,dto);
        return dto;
    }

    @Override
    public List<CustomerDto> findByGender(int gender) {
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer> jpaQuery = jpaQueryFactory.selectFrom(qCustomer).where(qCustomer.gender.eq(1)).distinct();
        List<Customer> customers = jpaQuery.fetch();

        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer customer:customers) {
            CustomerDto dto = new CustomerDto();
            BeanUtils.copyProperties(customer,dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CustomerDto> findByGenderAndAgeGt(int gender, int age) {
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer> jpaQuery = jpaQueryFactory.selectFrom(qCustomer).where(qCustomer.gender.eq(gender), qCustomer.age.gt(age));
        //.where(qCustomer.gender.eq(gender), qCustomer.age.gt(age)) = .where(qCustomer.gender.eq(gender).and(qCustomer.age.gt(age))
        List<Customer> customers = jpaQuery.fetch();

        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer customer:customers) {
            CustomerDto dto = new CustomerDto();
            BeanUtils.copyProperties(customer,dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CustomerDto> findByNameOrAgeGt(String name, int age) {
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer> jpaQuery = jpaQueryFactory.selectFrom(qCustomer).where(qCustomer.name.eq(name).or(qCustomer.age.gt(age)));
        List<Customer> customers = jpaQuery.fetch();

        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer customer:customers) {
            CustomerDto dto = new CustomerDto();
            BeanUtils.copyProperties(customer,dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CustomerDto> findByCompanyName(String name) {
        QCustomer qCustomer = QCustomer.customer;
        QCompany qCompany = QCompany.company;
       // JPAQuery<Customer> jpaQuery = jpaQueryFactory.select(qCustomer).from(qCompany, qCustomer).where(qCompany.id.eq(qCustomer.companyId).and(qCompany.name.eq(name)));
        JPAQuery<Customer> jpaQuery = jpaQueryFactory.select(qCustomer).from(qCustomer).rightJoin(qCompany).on(qCompany.id.eq(qCustomer.companyId)).where(qCompany.name.eq(name));

        List<Customer> customers = jpaQuery.fetch();

        List<CustomerDto> dtos = new ArrayList<>();
        for (Customer customer:customers) {
            CustomerDto dto = new CustomerDto();
            BeanUtils.copyProperties(customer,dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<CustomerDto> findByCompanyNameLeftJoin(String name) {
        QCustomer qCustomer = QCustomer.customer;
        QCompany qCompany = QCompany.company;
        JPAQuery<Tuple> tupleJPAQuery = jpaQueryFactory.select(qCompany, qCustomer).from(qCustomer).rightJoin(qCompany).on(qCompany.id.eq(qCustomer.companyId)).where(qCompany.name.eq(name));
        List<Tuple> tuples = tupleJPAQuery.fetch();

        List<CustomerDto> dtos = new ArrayList<>();
        for (Tuple tuple:tuples){
            Customer customer = tuple.get(1, Customer.class);
            CustomerDto dto = new CustomerDto();
            BeanUtils.copyProperties(customer,dto);
            dtos.add(dto);
        }
        return dtos;
    }

    @Override
    public List<Tuple> findCompanyAndCustomerByCompanyName(String name) {
        QCustomer qCustomer = QCustomer.customer;
        QCompany qCompany = QCompany.company;
        //JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qCustomer,qCompany).from(qCustomer,qCompany).where(qCustomer.companyId.eq(qCompany.id).and(qCompany.name.eq("三国")));
        JPAQuery<Tuple> jpaQuery = jpaQueryFactory.select(qCompany, qCustomer).from(qCustomer).rightJoin(qCompany).on(qCompany.id.eq(qCustomer.companyId)).where(qCompany.name.eq(name));
        List<Tuple> tuples = jpaQuery.fetch();
        return tuples;
    }


}
