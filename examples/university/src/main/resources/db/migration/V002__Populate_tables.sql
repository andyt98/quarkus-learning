-- Inserting professors
INSERT INTO professor (id, first_name, last_name)
VALUES (nextval('professor_sequence'), 'John', 'Doe'),
       (nextval('professor_sequence'), 'Jane', 'Smith'),
       (nextval('professor_sequence'), 'Emily', 'Taylor'),
       (nextval('professor_sequence'), 'Mark', 'Robinson');

-- Inserting courses
INSERT INTO course (id, course_name, professor_id)
VALUES (1, 'Computer Science 101', (SELECT id FROM professor WHERE first_name = 'John' AND last_name = 'Doe')),
       (2, 'Introduction to Physics', (SELECT id FROM professor WHERE first_name = 'Jane' AND last_name = 'Smith')),
       (3, 'Advanced Mathematics', (SELECT id FROM professor WHERE first_name = 'Emily' AND last_name = 'Taylor')),
       (4, 'History of Art', (SELECT id FROM professor WHERE first_name = 'Mark' AND last_name = 'Robinson'));

-- Inserting students
INSERT INTO student (id, first_name, last_name, birth_date, type, street, city, zip_code, country)
VALUES (gen_random_uuid(), 'Alice', 'Johnson', '2000-05-15', 'Full-time', '123 Maple Street', 'Springfield', '12345',
        'USA'),
       (gen_random_uuid(), 'Bob', 'Brown', '1999-07-20', 'Part-time', '456 Oak Avenue', 'Smalltown', '67890', 'USA'),
       (gen_random_uuid(), 'Charlie', 'Davis', '2001-09-10', 'Full-time', '789 Pine Road', 'Laketown', '10112', 'USA'),
       (gen_random_uuid(), 'Diana', 'Miller', '1998-03-30', 'Part-time', '321 Birch Lane', 'Hillcrest', '20221', 'USA');

-- Enrolling students in courses
INSERT INTO enrollment (student_id, course_id)
VALUES ((SELECT id FROM student WHERE first_name = 'Alice' AND last_name = 'Johnson'),
        (SELECT id FROM course WHERE course_name = 'Computer Science 101')),
       ((SELECT id FROM student WHERE first_name = 'Bob' AND last_name = 'Brown'),
        (SELECT id FROM course WHERE course_name = 'Introduction to Physics')),
       ((SELECT id FROM student WHERE first_name = 'Charlie' AND last_name = 'Davis'),
        (SELECT id FROM course WHERE course_name = 'Advanced Mathematics')),
       ((SELECT id FROM student WHERE first_name = 'Diana' AND last_name = 'Miller'),
        (SELECT id FROM course WHERE course_name = 'History of Art'));
