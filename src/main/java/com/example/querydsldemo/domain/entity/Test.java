package com.example.querydsldemo.domain.entity;

import com.example.querydsldemo.domain.entity.common.EntityBase;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "test")
@Data
public class Test extends EntityBase {
    private String name;
}
