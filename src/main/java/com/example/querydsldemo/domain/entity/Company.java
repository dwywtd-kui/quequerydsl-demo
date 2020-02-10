package com.example.querydsldemo.domain.entity;

import com.example.querydsldemo.domain.entity.common.EntityBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "company")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Company extends EntityBase {
    private String name;
    private String city;
}
