# website-backend

📝 Observação Importante:

Sobre DTOs Serializable: Aplicação está usando JdkSerializationRedisSerializer no RedisConfig. Isso pode funcionar, MAS
se tiver problemas de serialização, vai precisar fazer os DTOs implementarem Serializable:

public record ClanResponseDTO(...) implements Serializable {}

Se preferir evitar isso, trocar para GenericJackson2JsonRedisSerializer no RedisConfig:41-44.

Agora o cache está funcionando corretamente! Lembrar de configurar as variáveis de ambiente REDIS_HOST, REDIS_PORT
e CACHE_EXPIRATION no ambiente.
