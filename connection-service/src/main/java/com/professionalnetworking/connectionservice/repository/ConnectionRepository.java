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

    @Query("MATCH (p:Person {userId: $userId}) RETURN p")
    Optional<Person> findByUserId(Long userId);

    @Query("MERGE (p:Person {userId: $userId}) SET p.name = $name RETURN p")
    void createOrUpdatePerson(Long userId, String name);

    @Query("MERGE (p:Person {userId: $userId}) SET p.name = COALESCE(p.name, 'User ' + toString($userId)) RETURN p")
    void createOrUpdatePersonWithDefault(Long userId);

    @Query("MATCH (p:Person {userId: $userId})-[:CONNECTED_TO]->(c:Person) RETURN c")
    List<Person> getFirstDegreeConnections(Long userId);

//    @Query("MATCH (p:Person {userId: $senderId})-[:SENT_CONNECTION_REQUEST]->(c:Person {userId: $receiverId}) RETURN c")
    @Query("MATCH (p1:Person)-[r:REQUESTED_TO]->(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean connectionRequestExists(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person)-[r:CONNECTED_TO]-(p2:Person) " +
            "WHERE p1.userId = $senderId AND p2.userId = $receiverId " +
            "RETURN count(r) > 0")
    boolean alreadyConnected(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId}), (p2:Person {userId: $receiverId}) " +
            "MERGE (p1)-[:REQUESTED_TO]->(p2)")
    void addConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId})-[r:REQUESTED_TO]->(p2:Person {userId: $receiverId}) " +
            "DELETE r " +
            "MERGE (p1)-[:CONNECTED_TO]->(p2) " +
            "MERGE (p2)-[:CONNECTED_TO]->(p1)")
    void acceptConnectionRequest(Long senderId, Long receiverId);

    @Query("MATCH (p1:Person {userId: $senderId})-[r:REQUESTED_TO]->(p2:Person {userId: $receiverId}) " +
            "DELETE r")
    void rejectConnectionRequest(Long senderId, Long receiverId);
}
