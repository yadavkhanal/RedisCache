# Redis Cache with Spring Boot

A sample Spring Boot application demonstrating how to integrate **Redis** as a caching layer using Spring Cache abstraction.

## Overview

This project shows how to improve application performance by caching frequently accessed data in Redis, reducing repeated database hits and improving response times.

It demonstrates:

- Spring Boot + Redis integration
- Cache-aside pattern using Spring Cache abstraction
- `@Cacheable`, `@CachePut`, `@CacheEvict`
- Redis configuration with `RedisTemplate`
- Cache TTL configuration
- Cache invalidation strategies
- Dockerized Redis setup

## Tech Stack

- Java 17+ (update if different)
- Spring Boot
- Spring Data Redis
- Spring Cache
- Maven
- Docker / Docker Compose
- Redis

---

## Architecture

```text
Client Request
    |
    v
Controller
    |
    v
Service Layer
    |
    |-- Check Redis Cache
    |      |
    |      |-- Cache Hit --> Return cached data
    |      |
    |      |-- Cache Miss
    |
    v
Database
    |
Store result in Redis
    |
Return response
```

## Caching Flow

### First Request (Cache Miss)
- Request comes in
- Data not found in Redis
- Application fetches from DB
- Result stored in Redis
- Response returned

### Subsequent Requests (Cache Hit)
- Request comes in
- Data served directly from Redis
- Database bypassed

---

# Features

## `@Cacheable`
Caches method results automatically.

```java
@Cacheable(value = "products", key = "#id")
public Product getProduct(Long id) {
    return repository.findById(id).orElseThrow();
}
```

---

## `@CachePut`
Updates both DB and cache.

```java
@CachePut(value = "products", key = "#product.id")
public Product update(Product product) {
    return repository.save(product);
}
```

---

## `@CacheEvict`
Removes stale data from cache.

```java
@CacheEvict(value = "products", key = "#id")
public void delete(Long id) {
    repository.deleteById(id);
}
```

---

## Redis Configuration

Example Redis configuration:

```java
@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisTemplate<String,Object> redisTemplate(
        RedisConnectionFactory connectionFactory) {

        RedisTemplate<String,Object> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
```

Example TTL configuration:

```yaml
spring:
  cache:
    type: redis
  data:
    redis:
      host: localhost
      port: 6379
```

---

# Running the Project

## Prerequisites

Install:

- Java
- Maven
- Docker (optional but recommended)
- Redis

---

## Start Redis with Docker

```bash
docker run -p 6379:6379 redis
```

Or with docker-compose:

```yaml
version: '3'
services:
  redis:
    image: redis
    ports:
      - "6379:6379"
```

Run:

```bash
docker-compose up
```

---

## Start Application

```bash
mvn clean install
mvn spring-boot:run
```

---

## Example Endpoints

(Replace these with your actual endpoints)

```http
GET /products/{id}
POST /products
PUT /products/{id}
DELETE /products/{id}
```

---

## Testing Cache

Call same endpoint twice:

```http
GET /products/1
```

Expected:

### First call
- DB hit
- Cache populated

### Second call
- Served from Redis

You can verify via:

```bash
redis-cli
keys *
get products::1
```

---

# Project Structure

```text
src/main/java
├── controller
├── service
├── repository
├── config
└── model
```

---

## Cache Strategies Demonstrated

This project can be used to learn:

- Cache-aside pattern
- Read-through caching
- Write-through concepts
- TTL based expiration
- Cache invalidation

---

## Why Redis Cache?

Benefits:

- Faster reads
- Reduced DB load
- Lower latency
- Better scalability
- Distributed caching support

---

## Common Pitfalls Covered

- Serialization issues
- Stale cache data
- TTL tuning
- Cache key design
- Cache stampede prevention (optional extension)

---

## Future Improvements

Possible enhancements:

- Redis Pub/Sub
- Distributed locks
- Cache warming
- Rate limiting with Redis
- Redis Streams
- Redis Cluster / Sentinel support
- Monitoring with Redis Insight

---

## Learning Goals

This project is useful for understanding:

- Spring Cache abstraction
- Redis fundamentals
- Production caching patterns
- Real-world cache invalidation

---

## Build

```bash
mvn clean package
```

Run jar:

```bash
java -jar target/app.jar
```

---

## References

- Spring Data Redis
- Redis Documentation
- Spring Cache Abstraction

---

## Contributing

PRs and suggestions welcome.

```bash
git clone https://github.com/yadavkhanal/RedisCache.git
```

---

## License

MIT
