# Frontline API

Frontline is a Spring Boot backend for a game statistics website. It exposes
authenticated REST endpoints for users, clans, clan members, servers, rounds,
leaderboards, player stats, and access logs while integrating with an external
GameStats API for live gameplay data.

The project is built to be practical in production: PostgreSQL stores the
application data, Redis caches expensive reads, Flyway manages schema changes,
and Docker Compose provides the full local infrastructure.

[Leia em Português](README.pt-br.md)

## What This Project Does

Frontline acts as the API layer between the website and the GameStats service.
It manages local domain data such as users, clans, memberships, access logs, and
authentication, while proxying and caching external data such as players,
rounds, servers, and leaderboards.

Core capabilities:

- JWT-based authentication.
- User registration, login, soft deletion, and reactivation rules.
- Clan and clan member management.
- Server, round, scoreboard, player stat, and leaderboard endpoints.
- Access log registration and lookup.
- Redis-backed cache for high-read endpoints.
- PostgreSQL schema versioning with Flyway.

## Improvements Implemented

- Renamed the service to `frontline` across project metadata and application
  configuration.
- Restored the Maven Wrapper so the project builds without requiring a global
  Maven installation.
- Replaced Java native Redis serialization with JSON serialization using
  `GenericJackson2JsonRedisSerializer`.
- Added Redis cache keys for paginated reads and synchronized cache population
  for expensive external API calls.
- Reused the cached player stats service instead of duplicating HTTP calls.
- Added pagination to collection endpoints for access logs, clans, and clan
  members.
- Added maximum page-size protection to avoid oversized API responses.
- Added active and non-deleted user lookup rules for authenticated operations.
- Fixed login reactivation so deleted accounts are reactivated only after a
  valid password check.
- Fixed the clan member entity mapping to match the actual `clans_members`
  table.
- Added Flyway dependencies and configured migration discovery.
- Added database indexes for access log lookup and active user queries.
- Disabled verbose SQL logging by default while keeping it configurable through
  environment variables.

## Why It Is Strong

This API is not just a CRUD backend. It now has the pieces expected from a
serious service:

- Fast repeated reads through Redis.
- Safer data access through pagination and bounded response sizes.
- Better database performance through targeted indexes.
- Predictable schema evolution with Flyway migrations.
- Cleaner separation between local data and external GameStats data.
- Build reproducibility through the Maven Wrapper.
- Safer authentication behavior for deleted and inactive users.

The result is a backend that is easier to deploy, easier to maintain, and better
prepared for real traffic than a basic unoptimized API.

## Tech Stack

- Java 17
- Spring Boot 3.5
- Spring Web
- Spring Security
- Spring Data JPA
- PostgreSQL
- Redis
- Flyway
- Docker Compose
- JWT

## Environment Variables

Required:

- `POSTGRESQL_URL`
- `POSTGRESQL_USERNAME`
- `POSTGRESQL_PASSWORD`
- `POSTGRESQL_DATABASE`
- `JWT_SECRET`
- `GAMESTATS_API_URL`
- `GAMESTATS_API_SECRET_KEY`

Optional:

- `SERVER_PORT` defaults to `8080`
- `REDIS_HOST` defaults to `localhost`
- `REDIS_PORT` defaults to `6379`
- `CACHE_EXPIRATION` defaults to `10` minutes
- `JPA_SHOW_SQL` defaults to `false`
- `JPA_FORMAT_SQL` defaults to `false`

## Running Locally

With Docker Compose:

```bash
docker compose up --build
```

With Maven:

```bash
MAVEN_USER_HOME=.m2 ./mvnw test
MAVEN_USER_HOME=.m2 ./mvnw spring-boot:run
```

## Database Migrations

Migrations are stored in:

```text
src/main/resources/database/migrations
```

Flyway is configured with:

```properties
spring.flyway.locations=classpath:database/migrations
spring.flyway.baseline-on-migrate=true
```

