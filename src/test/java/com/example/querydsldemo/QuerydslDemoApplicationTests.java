package com.example.querydsldemo;

import com.example.querydsldemo.domain.dto.CustomerDto;
import com.example.querydsldemo.domain.entity.Company;
import com.example.querydsldemo.domain.entity.Customer;
import com.example.querydsldemo.domain.entity.querydsl.QCompany;
import com.example.querydsldemo.domain.entity.querydsl.QCustomer;
import com.example.querydsldemo.repository.CompanyRepository;
import com.example.querydsldemo.repository.CustomerRepository;
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
    private CustomerRepository customerRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Test
    void test1(){
        QCustomer customer = QCustomer.customer;
        JPAQuery<Customer> jpaQuery = queryFactory.selectFrom(customer).where(customer.sex.eq(1)).distinct();
        List<Customer> customers = jpaQuery.fetch();
        log.info(customers.toString());
    }

    @Test
    void test2(){
//        Customer customer = new Customer("李白",1,"1008611","111");
//        customerRepository.save(customer);
//        Company company = new Company("传奇","北京");
//        Company saveCompany = companyRepository.save(company);
//        Customer customer = new Customer("赵云",0,"1001011",saveCompany.getId());
//        customerRepository.save(customer);

        Company company = new Company("三国","北京");
        Company company2 = new Company("西游记","上海");
        companyRepository.save(company);
        companyRepository.save(company2);
//        Customer customer = new Customer("张飞",1,"1008611","");
//        Customer customer2 = new Customer("关羽",1,"1008611","");
//        Customer customer3 = new Customer("孙猴子",1,"1008611","");
//        Customer customer4 = new Customer("小乔",0,"1008611","");
//        customerRepository.save(customer);
//        customerRepository.save(customer2);
//        customerRepository.save(customer3);
//        customerRepository.save(customer4);


    }


    /**
     * 多条件查询
     */
    @Test
    void testMultiParams(){
        QCustomer qCustomer = QCustomer.customer;
        List<Customer> customers1 = queryFactory.selectFrom(qCustomer).where(qCustomer.sex.eq(1)).fetch();
        log.info(customers1.toString());
        List<Customer> customers2 = queryFactory.selectFrom(qCustomer).where(qCustomer.sex.eq(1).and(qCustomer.name.endsWith("羽"))).fetch();
        log.info(customers2.toString());
    }

    /**
     * 表连接查询，无需建立两个实体之间关系
     */
    @Test
    void testMultiTable(){
        QCustomer qCustomer = QCustomer.customer;
        QCompany qCompany = QCompany.company;
        JPAQuery<?> jpaQuery = queryFactory.select(qCustomer,qCompany).from(qCustomer,qCompany).where(qCustomer.companyId.eq(qCompany.id).and(qCompany.name.eq("三国")));
        List<?> objects = jpaQuery.fetch();
        log.info(objects.toString());
    }

    /**
     * 左连接查询，需要两个实体之间不需要进行关联
     */
    @Test
    void testLeftJoin(){
        QCustomer qCustomer = QCustomer.customer;
        QCompany qCompany = QCompany.company;
        JPAQuery<Tuple> query = queryFactory.select(qCompany, qCustomer).from(qCompany).leftJoin(qCustomer).on(qCompany.id.eq(qCustomer.companyId)).where(qCompany.name.eq("三国"));
        List<Tuple> tuples = query.fetch();
        System.out.println("----全部查询数据----");
        System.out.println(tuples);
        for (Tuple tuple:tuples){
            System.out.println("-----分界线-------");
            Company company = tuple.get(0, Company.class);
            System.out.println(company);
            Customer customer = tuple.get(1, Customer.class);
            System.out.println(customer);
        }
    }

    /**
     * 测试查询排序 orderBy
     */
    @Test
    void testOrderBy(){
        QCustomer qCustomer = QCustomer.customer;
        JPAQuery<Customer> query = queryFactory.selectFrom(qCustomer).orderBy(qCustomer.age.asc(), qCustomer.sex.asc());
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
        JPAQuery<Tuple> query = queryFactory.select(qCustomer.count(), qCustomer.sex).from(qCustomer).groupBy(qCustomer.sex);
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
        JPAQuery<CustomerDto> query = queryFactory.select(Projections.constructor(CustomerDto.class, qCustomer.id, qCustomer.name)).from(qCustomer);
        List<CustomerDto> dtos = query.fetch();
        dtos.forEach(dto->{
            System.out.println("---分割线---");
            System.out.println(dto.toString());
        });
    }

}
