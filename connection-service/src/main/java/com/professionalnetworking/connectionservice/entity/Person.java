package com.professionalnetworking.connectionservice.entity;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Data
@Node
public class Person {

    @Id
    @GeneratedValue
    private Long id; // Id related to the node

    private Long userId; // User id from user service
    private String name;
}
