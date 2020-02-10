package com.example.querydsldemo.domain.entity.common;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Data
public class EntityBase {
    @Id
    @GenericGenerator(name = "idGeneratorUUID",strategy = "uuid")
    @GeneratedValue(generator = "idGeneratorUUID")
    protected String id;
}
