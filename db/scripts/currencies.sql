CREATE TABLE currencies
(
    id       INTEGER PRIMARY KEY AUTOINCREMENT,
    code     VARCHAR NOT NULL UNIQUE,
    fullname VARCHAR,
    sign     VARCHAR
);

INSERT INTO currencies (code, fullname, sign)
VALUES ('USD', 'United States Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('JPY', 'Japanese Yen', '¥');