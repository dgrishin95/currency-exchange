CREATE TABLE currencies
(
    id   INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHAR NOT NULL UNIQUE,
    name VARCHAR,
    sign VARCHAR
);

INSERT INTO currencies (code, name, sign)
VALUES ('USD', 'United States Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('JPY', 'Japanese Yen', '¥');