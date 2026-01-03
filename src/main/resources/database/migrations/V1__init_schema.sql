CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    username VARCHAR(50) NOT NULL UNIQUE,
    bohemia_guid VARCHAR(40) NOT NULL UNIQUE,
    email VARCHAR(50) NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    accept_terms BOOLEAN DEFAULT FALSE,
    photo VARCHAR(255) DEFAULT '',
    banner_photo VARCHAR(255) DEFAULT '',
    description VARCHAR(255) DEFAULT '',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP DEFAULT NULL
);

-- Índices de performance
CREATE INDEX IF NOT EXISTS idx_users_username ON users (username);
CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
CREATE INDEX IF NOT EXISTS idx_users_bohemia_guid ON users (bohemia_guid);
CREATE INDEX IF NOT EXISTS idx_users_deleted_at ON users (deleted_at);

CREATE TABLE IF NOT EXISTS clans (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE,
    tag VARCHAR(10) NOT NULL UNIQUE,
    photo VARCHAR(255) NOT NULL DEFAULT '',
    description VARCHAR(255) NOT NULL DEFAULT '',
    created_at TIMESTAMP DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS clans_members (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    clan_id UUID NOT NULL REFERENCES clans(id) ON DELETE CASCADE,
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_owner BOOLEAN DEFAULT FALSE,
    joined_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (clan_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_clans_members_clan_id ON clans_members (clan_id);
CREATE INDEX IF NOT EXISTS idx_clans_members_user_id ON clans_members (user_id);
CREATE INDEX IF NOT EXISTS idx_clans_members_is_owner ON clans_members (is_owner);

CREATE TABLE access_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id UUID,
    ip_address INET NOT NULL,
    user_agent TEXT,
    method VARCHAR(10),
    endpoint TEXT,
    status_code INT,
    response_time_ms INT,
    created_at TIMESTAMP DEFAULT NOW()
);
