-- Insert data into Professor table
INSERT INTO professor (first_name, last_name)
VALUES
    ('John', 'Doe'),
    ('Jane', 'Smith'),
    ('Michael', 'Johnson');

-- Insert data into Course table
INSERT INTO course (course_name, professor_id)
VALUES
    ('Mathematics', 1),
    ('History', 2),
    ('Physics', 1),
    ('Literature', 3);

-- Insert data into Student table
INSERT INTO student (id, first_name, last_name, birth_date)
VALUES
    (gen_random_uuid(), 'Alice', 'Johnson', '2000-01-15'),
    (gen_random_uuid(), 'Bob', 'Williams', '2000-01-15'),
    (gen_random_uuid(), 'Eva', 'Martinez','2000-01-15');

-- Insert data into StudentContact table
INSERT INTO student_contact (student_id, email, address_street, address_city, address_zip_code, address_country, phone_number)
VALUES
    ((SELECT id FROM student WHERE first_name = 'Alice'), 'alice@example.com', '123 Main St', 'Cityville', '12345', 'USA', '123-456-7890'),
    ((SELECT id FROM student WHERE first_name = 'Bob'), 'bob@example.com', '456 Elm St', 'Towntown', '67890', 'USA', '987-654-3210'),
    ((SELECT id FROM student WHERE first_name = 'Eva'), 'eva@example.com', '789 Oak St', 'Villatown', '54321', 'USA', '555-555-5555');

-- Insert data into Enrollment table
INSERT INTO enrollment (student_id, course_id)
VALUES
    ((SELECT id FROM student WHERE first_name = 'Alice'), 1),
    ((SELECT id FROM student WHERE first_name = 'Alice'), 2),
    ((SELECT id FROM student WHERE first_name = 'Bob'), 2),
    ((SELECT id FROM student WHERE first_name = 'Eva'), 3),
    ((SELECT id FROM student WHERE first_name = 'Eva'), 4),
    ((SELECT id FROM student WHERE first_name = 'Bob'), 1),
    ((SELECT id FROM student WHERE first_name = 'Bob'), 3),
    ((SELECT id FROM student WHERE first_name = 'Alice'), 3);
