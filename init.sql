CREATE DATABASE IF NOT EXISTS lda;

USE lda;

CREATE TABLE IF NOT EXISTS
    user(
        userName VARCHAR(255) NOT NULL PRIMARY KEY,
        password VARCHAR(255) NOT NULL,
        name VARCHAR(255),
        role VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS
    book (
        title VARCHAR(255) NOT NULL PRIMARY KEY,
        author VARCHAR(255) NOT NULL,
        publisher VARCHAR(255),
        oldprice DECIMAL(10, 2),
        newprice DECIMAL(10, 2),
        href VARCHAR(255)
    );

INSERT INTO
    user
VALUES
    ('admin', 'admin', 'admin', 'admin');