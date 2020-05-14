package com.example.querydsldemo.domain.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;

@Entity
@Table(name = "employee")
@Data
public class Employee {
    @Id
    @GenericGenerator(name = "idGeneratorUUID",strategy = "uuid")
    @GeneratedValue(generator = "idGeneratorUUID")
    private String id;
    /**员工名称*/
    private String name;
    /**员工性别*/
    private int gender;
    /**员工电话*/
    private String phone;
    /**所在公司ID*/
    private String companyId;
    /**员工年龄*/
    private Integer age;
}
