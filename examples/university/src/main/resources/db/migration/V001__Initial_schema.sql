CREATE TABLE professor
(
    id         BIGINT PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL
);

CREATE SEQUENCE professor_sequence INCREMENT BY 1 MINVALUE 1 START WITH 1;

CREATE TABLE course
(
    id           BIGINT PRIMARY KEY,
    course_name  TEXT   NOT NULL,
    professor_id BIGINT NOT NULL REFERENCES professor (id)
);

CREATE TABLE student
(
    id         UUID PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    birth_date DATE,
    type       TEXT,
    street     TEXT NOT NULL,
    city       TEXT NOT NULL,
    zip_code   TEXT NOT NULL,
    country    TEXT NOT NULL
);

CREATE TABLE enrollment
(
    student_id UUID   NOT NULL REFERENCES student (id),
    course_id  BIGINT NOT NULL REFERENCES course (id),
    PRIMARY KEY (student_id, course_id)
);
