## Neo4j Commands

### Start Neo4j
```bash
docker run \
  --name neo4j \
  -p 7474:7474 -p 7687:7687 \
  -d neo4j:latest
```
### Stop Neo4j
```bash
docker stop neo4j
```
### Remove Neo4j
```bash
docker rm neo4j
```

### Neo4j Commands
```cypher
// Create a node
CREATE (n:Person {name: 'John Doe', age: 30})

// Create a relationship
MATCH (a:Person {name: 'John Doe'}), (b:Person {name: 'Jane Doe'})
CREATE (a)-[r:KNOWS]->(b)

// Query for nodes
MATCH (n:Person) RETURN n

// Query for relationships
MATCH (a:Person)-[r:KNOWS]->(b:Person)
RETURN a, r, b
```

### More Neo4j Commands
```cypher
// Delete a node
MATCH (n:Person {name: 'John Doe'}) DELETE n

// Delete a relationship
MATCH (a:Person)-[r:KNOWS]->(b:Person)
DELETE r

// Update a node
MATCH (n:Person {name: 'John Doe'}) SET n.age = 31

// Update a relationship
MATCH (a:Person)-[r:KNOWS]->(b:Person)
SET r.since = 2010

// Query for nodes with a label
MATCH (n:Person) RETURN n

// Query for relationships with a type
MATCH (a:Person)-[r:KNOWS]->(b:Person)
RETURN a, r, b

// Query for nodes with a property
MATCH (n:Person {name: 'John Doe'}) RETURN n

// Query for relationships with a property
MATCH (a:Person)-[r:KNOWS]->(b:Person)
WHERE r.since = 2010
RETURN a, r, b

// Query for nodes with a label and a property
MATCH (n:Person {name: 'John Doe'}) RETURN n

// Get 2nd degree connections
MATCH (a:Person)-[:KNOWS]->(b:Person)-[:KNOWS]->(c:Person)
RETURN a, b, c

MATCH (p:Person {name :"Eve"}) - [a:CONNECTED_TO*2] -> (r:Person)
RETURN r

```
