CREATE INDEX IF NOT EXISTS idx_access_logs_created_at
    ON access_logs (created_at DESC);

CREATE INDEX IF NOT EXISTS idx_access_logs_user_created_at
    ON access_logs (user_id, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_access_logs_ip_created_at
    ON access_logs (ip_address, created_at DESC);

CREATE INDEX IF NOT EXISTS idx_users_active_deleted_email
    ON users (active, deleted_at, email);

CREATE INDEX IF NOT EXISTS idx_users_active_deleted_username
    ON users (active, deleted_at, username);

CREATE INDEX IF NOT EXISTS idx_users_active_deleted_bohemia_guid
    ON users (active, deleted_at, bohemia_guid);

