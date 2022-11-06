CREATE TABLE IF NOT EXISTS users (
    chat_id BIGINT UNIQUE, -- from tg chat
    username VARCHAR(255) UNIQUE,
    PRIMARY KEY (chat_id)
);

CREATE INDEX IF NOT EXISTS users_username
    ON users (username);

CREATE TABLE IF NOT EXISTS roles (
    chat_id BIGINT UNIQUE, -- from tg chat
    username VARCHAR(255) UNIQUE,
    PRIMARY KEY (chat_id)
);