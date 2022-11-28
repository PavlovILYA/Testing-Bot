CREATE TABLE IF NOT EXISTS users (
    chat_id BIGINT UNIQUE, -- from tg chat
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    PRIMARY KEY (chat_id)
);

CREATE INDEX IF NOT EXISTS users_username
    ON users (username);

-- DROP table users;