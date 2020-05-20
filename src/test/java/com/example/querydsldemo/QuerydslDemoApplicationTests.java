package com.example.querydsldemo;

import com.example.querydsldemo.domain.dto.EmployeeDto2;
import com.example.querydsldemo.domain.dto.EmployeeDyo1;
import com.example.querydsldemo.domain.entity.Company;
import com.example.querydsldemo.domain.entity.Employee;
import com.example.querydsldemo.domain.entity.querydsl.QCompany;
import com.example.querydsldemo.domain.entity.querydsl.QEmployee;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import org.hibernate.criterion.Projection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class QuerydslDemoApplicationTests {

    @Autowired
    private JPQLQueryFactory jpaQueryFactory;

    private QCompany qCompany = QCompany.company;
    private QEmployee qEmployee = QEmployee.employee;

    @Test
    public void findAll(){
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee);
        List<Employee> employees = jpaQuery.fetch();
        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }

    @Test
    public void findById() {
        String id = "1";
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee).where(qEmployee.id.eq(id));
        //.selectFrom(entity) == .select(entity).from (entity)
        Employee employee = jpaQuery.fetchOne();
        if (employee ==null) {
            System.out.println("ID为" + id + "的员工不存在！");
        } else {
            System.out.println("员工信息："+employee.toString());
        }
    }

    @Test
    public void findByGender() {
        int gender = 1;//男
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee).where(qEmployee.gender.eq(gender)).distinct();
        List<Employee> employees = jpaQuery.fetch();

        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }

    @Test
    public void findByLikeName() {
        String name = "%刘%";//男
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee).where(qEmployee.name.like(name)).distinct();
        List<Employee> employees = jpaQuery.fetch();

        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }



    @Test
    public void findByGenderAndAgeGt() {
        int gender = 1;
        int age = 21;
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee).where(qEmployee.gender.eq(gender), qEmployee.age.gt(age));
        //.where(qEmployee.gender.eq(gender), qEmployee.age.gt(age)) = .where(qEmployee.gender.eq(gender).and(qEmployee.age.gt(age))
        List<Employee> employees = jpaQuery.fetch();
        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }

    @Test
    public void findByNameOrAgeGt() {
        String name="李白";
        int age = 25;
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee).where(qEmployee.name.eq(name).or(qEmployee.age.gt(age)));
        List<Employee> employees = jpaQuery.fetch();

        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }

    @Test
    public void findAllPaged(){
        //第2页，每页2条数据
        int page = 2;
        int size = 2;
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory.selectFrom(qEmployee).offset((page-1)*size).limit(size);
        List<Employee> employees = jpaQuery.fetch();
        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }


    @Test
    void findNameAndAge(){
        List<Tuple> tuples = jpaQueryFactory.select(qEmployee.name, qEmployee.age).from(qEmployee).fetch();
        System.out.println("员工信息如下：");
        for (Tuple tuple:tuples){
            System.out.println("name="+tuple.get(qEmployee.name)+",age="+tuple.get(qEmployee.age));
        }
    }

    /**
     * bean 映射
     */
    @Test
    void findEmployeeDyo1(){
        List<EmployeeDyo1> employeeDyo1s = jpaQueryFactory
                .select(
                Projections.bean(EmployeeDyo1.class,
                        qEmployee.id,
                        qEmployee.name,
                        qEmployee.age))
                .from(qEmployee).fetch();

        System.out.println("员工信息如下：");
        for (EmployeeDyo1 dyo1:employeeDyo1s){
            System.out.println("------分割线------");
            System.out.println(dyo1.toString());
        }
    }

    @Test
    void findEmployeeDyo2(){
        List<EmployeeDto2> EmployeeDto2s = jpaQueryFactory
                .select(
                Projections.bean(EmployeeDto2.class,
                        qEmployee.id,
                        qEmployee.name,
                        qEmployee.age))
                .from(qEmployee).fetch();
        System.out.println("员工信息如下：");
        for (EmployeeDto2 dto:EmployeeDto2s){
            System.out.println("------分割线------");
            System.out.println(dto.toString());
        }
    }

    @Test
    void findEmployeeDto3(){
        List<EmployeeDto2> EmployeeDto2s = jpaQueryFactory
                .select(
                        Projections.bean(EmployeeDto2.class,
                                qEmployee.id.as("code"),//指定as为我们需要的字段名称code
                                qEmployee.name.as("userName"), //指定as为我们需要的字段名称userName
                                qEmployee.age))
                .from(qEmployee).fetch();
        System.out.println("员工信息如下：");
        for (EmployeeDto2 dto:EmployeeDto2s){
            System.out.println("------分割线------");
            System.out.println(dto.toString());
        }
    }

    //EmployeeDto2(String code,String userName,int age)
    @Test
    void findEmployeeDto4(){
        List<EmployeeDto2> EmployeeDto2s = jpaQueryFactory
                .select(
                        Projections.constructor(EmployeeDto2.class,
                                qEmployee.id,
                                qEmployee.name,
                                qEmployee.age))
                .from(qEmployee).fetch();
        System.out.println("员工信息如下：");
        for (EmployeeDto2 dto:EmployeeDto2s){
            System.out.println("------分割线------");
            System.out.println(dto.toString());
        }
    }

    @Test
    public void findEmployeesByCompanyName() {
        String name = "三国";
        // JPQLQuery<Customer> jpaQuery = jpaQueryFactory.select(qEmployee).from(qCompany, qEmployee).where(qCompany.id.eq(qEmployee.companyId).and(qCompany.name.eq(name)));
        JPQLQuery<Employee> jpaQuery = jpaQueryFactory
                .select(qEmployee)
                .from(qEmployee)
                .rightJoin(qCompany)
                .on(qCompany.id.eq(qEmployee.companyId))
                .where(qCompany.name.eq(name));

        List<Employee> employees = jpaQuery.fetch();
        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }

    @Test
    public void findEmployeesInCompanyInShangHai() {
        String name = "三国";

        JPQLQuery<String> jpqlQuery = JPAExpressions
                .select(qCompany.id)
                .from(qCompany)
                .where(qCompany.city.eq("上海"));

        List<Employee> employees = jpaQueryFactory.selectFrom(qEmployee)
                .where(qEmployee.companyId.in(jpqlQuery)
                ).fetch();

        System.out.println("员工信息如下：");
        for (Employee employee : employees) {
            System.out.println("------分割线------");
            System.out.println(employee.toString());
        }
    }

    @Test
    @Transactional() //更新需加入事务，不然抛出异常 Executing an update/delete query
    public void updateEmployee(){
        long execute = jpaQueryFactory.update(qEmployee)
                .set(qEmployee.age, 100)
                .set(qEmployee.phone, "10009")
                .where(qEmployee.name.eq("李白"))
                .execute();
        System.out.println("更新成功，影响了"+execute+"条数据！");
    }


    @Test
    @Transactional() //删除需加入事务，不然抛出异常 Executing an update/delete query
    void deleteEmployee(){
        long result = jpaQueryFactory.delete(qEmployee).where(qEmployee.age.gt(30)).execute();
        System.out.println("删除更成功，影响了"+result+"条数据！");
    }



    @Test
    void analyzeEmployee(){
        long count = jpaQueryFactory
                .selectFrom(qEmployee)
                .fetchCount();
        //Long count = jpaQueryFactory.select(qEmployee.count()).from(qEmployee).fetchOne();
        System.out.println("用户表中共有"+count+"条数据！");

        Integer sum = jpaQueryFactory
                .select(qEmployee.age.sum())
                .from(qEmployee).fetchOne();
        System.out.println("员工总年龄是"+sum);

        Integer max = jpaQueryFactory.select(qEmployee.age.max()).from(qEmployee).fetchOne();
        System.out.println("员工最大年龄是"+max);

        Integer min = jpaQueryFactory.select(qEmployee.age.min()).from(qEmployee).fetchOne();
        System.out.println("员工最大年龄是"+min);

        Double avg = jpaQueryFactory.select(qEmployee.age.avg()).from(qEmployee).fetchOne();
        System.out.println("员工平均年龄是"+avg);
    }




    @Test
    void testGroupBy(){
        List<Tuple> tuples = jpaQueryFactory.select(qCompany.city, qEmployee.age.avg())
                .from(qCompany, qEmployee)
                .where(qEmployee.companyId.eq(qCompany.id))
                .groupBy(qCompany.city).fetch();

        System.out.println("查询结果：");
        for (Tuple tuple:tuples){
            String city = tuple.get(qCompany.city);
            Double avg = tuple.get(qEmployee.age.avg());
            System.out.println(city+"城市的平均员工年龄是："+avg);
        }
    }

}
