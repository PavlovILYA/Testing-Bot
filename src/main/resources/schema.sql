-- DROP TABLE course_participation;
-- DROP TABLE current_data;
-- DROP TABLE solving;
-- DROP TABLE users_tests;
-- DROP TABLE open_answers;
-- DROP TABLE open_questions;
-- DROP TABLE variable_questions_answers;
-- DROP TABLE variable_questions;
-- DROP TABLE variable_answers;
-- DROP TABLE tests;
-- DROP TABLE courses;
-- DROP TABLE users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT UNIQUE, -- from tg chat
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    username VARCHAR(255) UNIQUE,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS users_username
    ON users(username);

CREATE TABLE IF NOT EXISTS courses (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    title varchar(500) UNIQUE,
    creator_id BIGINT,
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS courses_title
    ON courses(title);

CREATE TABLE IF NOT EXISTS tests (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    unique_title VARCHAR(50) UNIQUE, -- 30
    creator_id BIGINT,
    title VARCHAR(500),
    file_id BIGINT, -- from tg
    max_questions_number INT,
    test_type VARCHAR(255),
    course_id BIGINT,
    PRIMARY KEY (unique_title),
    FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS variable_answers (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    text VARCHAR(30) UNIQUE,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS variable_answer_text
    ON variable_answers(text);

CREATE TABLE IF NOT EXISTS variable_questions (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    test_id BIGINT,
    text VARCHAR(5000),
    image_id BIGINT, -- from tg
    audio_id BIGINT, -- from tg
    file_id BIGINT,  -- from tg
    max_answer_number INT,
    correct_answer_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE,
    FOREIGN KEY (correct_answer_id) REFERENCES variable_answers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS variable_questions_answers (
    question_id BIGINT,
    answer_id BIGINT,
    PRIMARY KEY (question_id, answer_id),
    FOREIGN KEY (question_id) REFERENCES variable_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (answer_id) REFERENCES variable_answers(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS open_questions (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    test_id BIGINT,
    text VARCHAR(5000),
    image_id BIGINT, -- from tg
    audio_id BIGINT, -- from tg
    file_id BIGINT,  -- from tg
    correct_answer VARCHAR(5000),
    PRIMARY KEY (id),
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS open_answers (
    question_id BIGINT,
    user_id BIGINT,
    text VARCHAR(5000),
    image_id BIGINT, -- from tg
    audio_id BIGINT, -- from tg
    file_id BIGINT,  -- from tg
    PRIMARY KEY (question_id, user_id),
    FOREIGN KEY (question_id) REFERENCES open_questions(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS users_tests ( -- сохраненные тесты,
    user_id BIGINT,                      -- созданные другими
    test_id BIGINT,                      -- пользователями
    PRIMARY KEY (user_id, test_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS solving (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id BIGINT UNIQUE,           -- эту таблицу потом можно будет вынести в отдельную БД
    test_id BIGINT,                  -- и создать отдельный микросервис для работы со статистикой
    open_question_ids VARCHAR(1000), -- (который хранит информацию из других источников, например!)
    open_answer_ids VARCHAR(1000),
    variable_question_ids VARCHAR(1000),
    variable_answer_ids VARCHAR(1000),
    variable_answer_results VARCHAR(1000),
    started_at TIMESTAMP,
    type VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS solving_user_id
    ON solving(user_id);

CREATE TABLE IF NOT EXISTS current_data (
    id BIGINT GENERATED ALWAYS AS IDENTITY UNIQUE,
    user_id BIGINT UNIQUE,
    state VARCHAR(500),
    next_phase VARCHAR(500),
    test_id BIGINT,
    course_id BIGINT,
    need_check BOOLEAN,
    open_question_id BIGINT,
    variable_question_id BIGINT,
    previous_question_type VARCHAR(10),
    last_message_id BIGINT,
    menu_message_id BIGINT,
    clear_reply_message_id BIGINT,
    search_key_words VARCHAR(3000),
    student_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (test_id) REFERENCES tests(id) ON DELETE SET NULL,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
    FOREIGN KEY (open_question_id) REFERENCES open_questions(id) ON DELETE SET NULL,
    FOREIGN KEY (variable_question_id) REFERENCES variable_questions(id) ON DELETE SET NULL,
    FOREIGN KEY (student_id) REFERENCES users(id) ON DELETE SET NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS course_participation (
    user_id BIGINT,
    course_id BIGINT,
    approved BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (user_id, course_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS current_data_user_id
    ON current_data(user_id);

CREATE OR REPLACE FUNCTION regexp_match(varchar, varchar) RETURNS boolean
    AS 'select $1 ~* $2;'
    LANGUAGE SQL
    IMMUTABLE;