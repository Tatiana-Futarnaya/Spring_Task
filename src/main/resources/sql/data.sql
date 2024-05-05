DROP TABLE IF EXISTS position CASCADE;
DROP TABLE IF EXISTS departments CASCADE;
DROP TABLE IF EXISTS employee CASCADE;
DROP TABLE IF EXISTS employee_departments CASCADE;
DROP TABLE IF EXISTS phone CASCADE;

CREATE TABLE IF NOT EXISTS position
(
    position_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    position_title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS departments
(
    department_id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY ,
    department_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS employee
(
   id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employeeFirstName VARCHAR(255) NOT NULL,
    employeeLastName  VARCHAR(255) NOT NULL,
    position_id        BIGINT REFERENCES position (position_id) on delete cascade
);

CREATE TABLE IF NOT EXISTS employee_departments
(
    employee_department_id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    employee_id                BIGINT REFERENCES employee (employee_id) on delete cascade,
    department_id             BIGINT REFERENCES departments (department_id) on delete cascade,
    CONSTRAINT employee_department_id_key UNIQUE (employee_id, department_id)
);

CREATE TABLE IF NOT EXISTS phone
(
    phone_id     BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    phone_number VARCHAR(255) NOT NULL UNIQUE,
    employee_id  BIGINT REFERENCES employee (employee_id) on delete cascade
);


INSERT INTO position (position_title)
VALUES ('Администратор'),        -- 1
       ('Технический директор'), -- 2
       ('Программист Java'),     -- 3
       ('Программист React'),    -- 4
       ('HR'); -- 5

INSERT INTO departments (department_name)
VALUES ('Администрация'),       -- 1
       ('BackEnd разработка'),  -- 2
       ('Frontend разработка'), -- 3
       ('HR менеджмент'); -- 4

INSERT INTO employee (employee_firstName, employee_lastName, position_id)
VALUES ('Иван', 'Субботин', 1),      -- 1
       ('Петр', 'Понедельников', 2), -- 2
       ('Игнат', 'Вторников', 3),    -- 3
       ('Иван', 'Середец', 3),       -- 4
       ('Максим', 'Четверкин', 3),   -- 5
       ('Вера', 'Пятницкая', 4),     -- 6
       ('Ольга', 'Воскресенская', 5); -- 7

INSERT INTO employee_departments (employee_id, department_id)
VALUES (1, 1), -- 1
       (2, 1), -- 2
       (3, 2), -- 3
       (4, 2), -- 4
       (5, 2), -- 5
       (6, 1), -- 6
       (6, 3), -- 6
       (7, 4); -- 7


INSERT INTO phone (phone_number, employee_id)
VALUES ('+1(123)123 1111', 1), -- 1
       ('+1(123)123 2222', 1), -- 2
       ('+1(123)123 3333', 2), -- 3
       ('+1(123)123 4444', 2), -- 4
       ('+1(123)123 5555', 3), -- 5
       ('+1(123)123 6666', 4), -- 6
       ('+1(123)123 7777', 5), -- 7
       ('+1(123)123 8888', 6), -- 8
       ('+1(123)123 9995', 7); -- 9
