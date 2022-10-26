DELETE
FROM user_roles;
DELETE
FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO user_roles (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

DELETE
FROM meals;

ALTER SEQUENCE global_seq_meals RESTART WITH 1;

INSERT INTO meals (dateTime, description, calories)
VALUES ('2015-06-01 14:00:12.66', 'lunch', '510'),
('2015-06-01 21:00:12.66', 'dinner', '1500');