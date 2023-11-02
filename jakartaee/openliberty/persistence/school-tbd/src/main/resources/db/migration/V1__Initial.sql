CREATE TABLE professor
(
    id         BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL
);

CREATE TABLE course
(
    id           BIGSERIAL PRIMARY KEY,
    course_name  TEXT NOT NULL,
    professor_id BIGINT REFERENCES professor (id)
);

CREATE TABLE student
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name TEXT NOT NULL,
    last_name  TEXT NOT NULL,
    birth_date DATE NOT NULL
);

CREATE TABLE student_contact
(
    student_id       UUID PRIMARY KEY REFERENCES student (id),
    email            TEXT NOT NULL UNIQUE,
    address_street   TEXT,
    address_city     TEXT,
    address_zip_code TEXT,
    address_country  TEXT,
    phone_number     TEXT

);



CREATE TABLE enrollment
(
    student_id UUID REFERENCES student (id),
    course_id  BIGINT REFERENCES course (id),
    PRIMARY KEY (student_id, course_id)
);
