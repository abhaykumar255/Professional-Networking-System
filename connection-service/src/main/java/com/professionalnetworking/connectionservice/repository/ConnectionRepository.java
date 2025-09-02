package com.professionalnetworking.connectionservice.repository;

import com.professionalnetworking.connectionservice.entity.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> getByName(String name);

    @Query("MATCH (p:Person {userId: $userId})-[:CONNECTED_TO]->(c:Person) RETURN c")
    List<Person> getFirstDegreeConnections(Long userId);
}
