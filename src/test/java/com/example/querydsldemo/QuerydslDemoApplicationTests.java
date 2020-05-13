package com.example.querydsldemo;

import com.example.querydsldemo.domain.dto.Customer2Dto;
import com.example.querydsldemo.domain.dto.CustomerDto;
import com.example.querydsldemo.domain.entity.Company;
import com.example.querydsldemo.domain.entity.Customer;
import com.example.querydsldemo.domain.entity.querydsl.QCompany;
import com.example.querydsldemo.domain.entity.querydsl.QCustomer;
import com.example.querydsldemo.repository.CompanyRepository;
import com.example.querydsldemo.repository.CustomerRepository;
import com.example.querydsldemo.service.ICustomerService;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Slf4j
class QuerydslDemoApplicationTests {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Autowired
    private ICustomerService customerService;


    @Test
    void findOne(){
        String id = "1";
        CustomerDto customerDto = customerService.findById(id);
        if (customerDto==null) {
            log.info("the customer does not exist!");
        }else {
            log.info(customerDto.toString());
        }
    }

    @Test
    void findByGender(){
        int gender = 1;
        List<CustomerDto> dtos = customerService.findByGender(gender);
        log.info(dtos.toString());
    }

    @Test
    void findByGenderAndAgeGt(){
        int gender = 1;
        int age = 21;
        List<CustomerDto> dtos = customerService.findByGenderAndAgeGt(gender, age);
        log.info(dtos.toString());
    }

    @Test
    void findByNameOrAgeGt(){
        String name="李白";
        int age = 25;
        List<CustomerDto> dtos = customerService.findByNameOrAgeGt(name, age);
        log.info(dtos.toString());
    }

    /**
     * 表连接查询，无需建立两个实体之间关系
     */
    @Test
    void findCustomersByCompanyName(){
        String name="三国";
        List<CustomerDto> dtos = customerService.findByCompanyName(name);
        log.info(dtos.toString());
    }


    @Test
    void findByCompanyNameLeftJoin(){
        String name = "三国";
        List<CustomerDto> dtos = customerService.findByCompanyNameLeftJoin(name);
        log.info(dtos.toString());
    }

    @Test
    void findCompanyAndCustomerByCompanyName(){
        String name = "三国";
        List<Tuple> tuples = customerService.findCompanyAndCustomerByCompanyName(name);
        log.info(tuples.toString());
    }


    /**
     * 测试查询排序 orderBy
     */
    @Test
    void testOrderBy(){
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer> query = queryFactory.selectFrom(qCustomer).orderBy(qCustomer.age.asc(), qCustomer.gender.asc());
        List<Customer> customers = query.fetch();
        customers.forEach(customer -> {
            System.out.println("----分割线----");
            System.out.println(customer);
        });
    }

    /**
     * 测试分组查询 groupBy
     */
    @Test
    void testGroupBy(){
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Tuple> query = queryFactory.select(qCustomer.count(), qCustomer.gender).from(qCustomer).groupBy(qCustomer.gender);
        List<Tuple> tuples = query.fetch();
        System.out.println("----查询结果----");
        tuples.forEach(tuple -> {
            System.out.println("sex="+tuple.get(1,int.class)+"的客户有"+tuple.get(0,Long.class)+"人");
        });
    }

    /**
     * 测试删除
     * 删除需加入事务，不然抛出异常 Executing an update/delete query
     */
    @Test
    @Transactional()
    void testDelete(){
        QCustomer qCustomer = QCustomer.customer;
        //queryFactory.delete(qCustomer).execute();//删除全部
        long result = queryFactory.delete(qCustomer).where(qCustomer.age.gt(30)).execute();
        System.out.println("影响了"+result+"条数据");
    }

    /**
     * 测试数据更新
     * 删除需加入事务，不然抛出异常 Executing an update/delete query
     */
    @Test
    @Transactional
    void testUpdate(){
        QCustomer qCustomer = QCustomer.customer;
        long result = queryFactory.update(qCustomer).set(qCustomer.phone,"1001010086").set(qCustomer.age,22).where(qCustomer.age.gt(30)).execute();
        System.out.println("影响了"+result+"条数据");
    }

    /**
     * 测试子查询
     */
    @Test
    void testChildQuery(){
        QCustomer qCustomer = QCustomer.customer;
        QCompany qCompany = QCompany.company;
        JPAQuery<Customer> query = queryFactory.selectFrom(qCustomer).where(qCustomer.age.gt(20).and(qCustomer.companyId.in(
                JPAExpressions.select(qCompany.id).from(qCompany).where(qCompany.city.eq("北京"))
        )));
        List<Customer> customers = query.fetch();
        customers.forEach(customer -> {
            System.out.println("---分割线---");
            System.out.println(customer);
        });
    }

    @Test
    void testReflectDto(){
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer2Dto> query = queryFactory.select(Projections.constructor(Customer2Dto.class, qCustomer.id, qCustomer.name)).from(qCustomer);
        List<Customer2Dto> dtos = query.fetch();
        dtos.forEach(dto->{
            System.out.println("---分割线---");
            System.out.println(dto.toString());
        });
    }

}
