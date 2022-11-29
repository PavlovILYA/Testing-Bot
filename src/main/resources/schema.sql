CREATE TABLE IF NOT EXISTS users (
    chat_id BIGINT UNIQUE, -- from tg chat
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    PRIMARY KEY (chat_id)
);

CREATE INDEX IF NOT EXISTS users_username
    ON users (username);

-- CREATE TABLE IF NOT EXISTS test_types (
--     type VARCHAR(255) UNIQUE,
--     PRIMARY KEY (type)
-- );

CREATE TABLE IF NOT EXISTS tests (
    unique_title VARCHAR(500) UNIQUE,
    creator_id BIGINT,
    title VARCHAR(500),
    file_id BIGINT, -- from tg
    max_questions_number INT,
    test_type VARCHAR(255),
    PRIMARY KEY (unique_title),
    FOREIGN KEY (creator_id) REFERENCES users(chat_id)
--     FOREIGN KEY (test_type) REFERENCES test_types(type)
);

-- CREATE TABLE IF NOT EXISTS question_types (
--     type VARCHAR(255) UNIQUE,
--     PRIMARY KEY (type)
-- );

CREATE TABLE IF NOT EXISTS variable_answers (
    text VARCHAR(30) UNIQUE,
    PRIMARY KEY (text)
);

CREATE TABLE IF NOT EXISTS variable_questions (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    test_unique_title VARCHAR(500),
    text VARCHAR(5000),
    image_id BIGINT, -- from tg
    audio_id BIGINT, -- from tg
    file_id BIGINT, -- from tg
    max_answer_number INT,
    correct_answer VARCHAR(30),
    PRIMARY KEY (id),
    FOREIGN KEY (correct_answer) REFERENCES variable_answers(text)
);

CREATE TABLE IF NOT EXISTS variable_questions_answers (
    question_id BIGINT,
    answer_text VARCHAR(30),
    PRIMARY KEY (question_id, answer_text),
    FOREIGN KEY (question_id) REFERENCES variable_questions(id),
    FOREIGN KEY (answer_text) REFERENCES variable_answers(text)
);

CREATE TABLE IF NOT EXISTS open_questions (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    test_unique_title VARCHAR(500),
    text VARCHAR(5000),
    image_id BIGINT, -- from tg
    audio_id BIGINT, -- from tg
    file_id BIGINT, -- from tg
    correct_answer VARCHAR(5000),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS open_answers (
    question_id BIGINT,
    user_id BIGINT,
    text VARCHAR(5000),
    image_id BIGINT, -- from tg
    audio_id BIGINT, -- from tg
    file_id BIGINT, -- from tg
    PRIMARY KEY (question_id, user_id),
    FOREIGN KEY (question_id) REFERENCES open_questions(id),
    FOREIGN KEY (user_id) REFERENCES users(chat_id)
);

-- todo нормализовать таблицы (сделать уникальные id везде); у юзера chat_id -> id

-- DROP table users;