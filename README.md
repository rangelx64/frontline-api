# Frontline

API backend do projeto Frontline.

## Cache

O projeto usa Redis para cache de leituras frequentes, com serialização JSON via
`GenericJackson2JsonRedisSerializer`.

Configure as variáveis de ambiente:

- `REDIS_HOST`
- `REDIS_PORT`
- `CACHE_EXPIRATION`

## Migrations

As migrations ficam em `src/main/resources/database/migrations` e são executadas
com Flyway.

