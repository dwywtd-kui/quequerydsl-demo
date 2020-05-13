package com.example.querydsldemo.domain.entity;

import com.example.querydsldemo.domain.entity.common.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer extends EntityBase {
    private String name;
    private int gender;
    private String phone;
    private String companyId;
    private Integer age;
}
