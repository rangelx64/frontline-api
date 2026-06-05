# Frontline API

Frontline é a API backend de um website de estatísticas de jogo. Ela expõe
endpoints REST autenticados para usuários, clãs, membros de clãs, servidores,
rounds, leaderboards, estatísticas de jogadores e logs de acesso, integrando-se
ao serviço externo GameStats para dados de gameplay em tempo real.

O projeto é pensado para uso em produção: PostgreSQL armazena os dados da
aplicação, Redis cacheia leituras custosas, Flyway gerencia as mudanças de
schema e Docker Compose fornece toda a infraestrutura local.

[Read in English](README.md)

## O Que Este Projeto Faz

Frontline é a camada de API entre o website e o serviço GameStats. Ela gerencia
dados locais como usuários, clãs, membros, logs de acesso e autenticação,
enquanto faz proxy e cacheia dados externos como jogadores, rounds, servidores e
leaderboards.

Principais capacidades:

- Autenticação com JWT.
- Registro, login, soft delete e regra de reativação de usuários.
- Gerenciamento de clãs e membros.
- Endpoints de servidores, rounds, scoreboards, estatísticas de jogadores e
  leaderboards.
- Registro e consulta de logs de acesso.
- Cache com Redis para endpoints de leitura intensa.
- Versionamento de banco com Flyway.

## Melhorias Implementadas

- Renomeação do serviço para `frontline` nos metadados e configuração da
  aplicação.
- Restauração do Maven Wrapper para build reproduzível sem Maven global.
- Troca da serialização Java nativa no Redis por JSON com
  `GenericJackson2JsonRedisSerializer`.
- Cache Redis com chaves por página e preenchimento sincronizado para chamadas
  externas mais caras.
- Reaproveitamento do service cacheado de estatísticas de jogadores, evitando
  chamadas HTTP duplicadas.
- Paginação nos endpoints de logs de acesso, clãs e membros de clãs.
- Limite máximo de tamanho de página para evitar respostas grandes demais.
- Buscas autenticadas considerando apenas usuários ativos e não deletados.
- Correção do fluxo de login para reativar conta deletada somente após senha
  válida.
- Correção do mapeamento de membros de clã para a tabela real `clans_members`.
- Adição do Flyway e configuração das migrations.
- Adição de índices para consultas de logs e usuários ativos.
- Desativação do SQL verbose por padrão, mantendo controle via variáveis de
  ambiente.

## Por Que Ele É Bom

Este projeto saiu do nível de uma API básica e passou a ter características de
um backend mais sólido:

- Leituras repetidas mais rápidas com Redis.
- Menos risco de sobrecarga com paginação e limites de resposta.
- Consultas mais eficientes com índices direcionados.
- Evolução de schema mais previsível com Flyway.
- Separação mais limpa entre dados locais e dados vindos da GameStats API.
- Build mais confiável com Maven Wrapper.
- Autenticação mais segura para usuários deletados ou inativos.

Na prática, isso torna a API mais preparada para uso real, mais fácil de
manter e menos propensa a gargalos óbvios de performance.

## Stack

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

## Variáveis de Ambiente

Obrigatórias:

- `POSTGRESQL_URL`
- `POSTGRESQL_USERNAME`
- `POSTGRESQL_PASSWORD`
- `POSTGRESQL_DATABASE`
- `JWT_SECRET`
- `GAMESTATS_API_URL`
- `GAMESTATS_API_SECRET_KEY`

Opcionais:

- `SERVER_PORT`, padrão `8080`
- `REDIS_HOST`, padrão `localhost`
- `REDIS_PORT`, padrão `6379`
- `CACHE_EXPIRATION`, padrão `10` minutos
- `JPA_SHOW_SQL`, padrão `false`
- `JPA_FORMAT_SQL`, padrão `false`

## Como Rodar

Com Docker Compose:

```bash
docker compose up --build
```

Com Maven:

```bash
MAVEN_USER_HOME=.m2 ./mvnw test
MAVEN_USER_HOME=.m2 ./mvnw spring-boot:run
```

## Migrations

As migrations ficam em:

```text
src/main/resources/database/migrations
```

O Flyway está configurado com:

```properties
spring.flyway.locations=classpath:database/migrations
spring.flyway.baseline-on-migrate=true
```
