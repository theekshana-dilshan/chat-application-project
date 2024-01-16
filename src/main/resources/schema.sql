CREATE DATABASE IF NOT EXISTS chat_application;

USE chat_application;

CREATE TABLE user(
                     employeeId VARCHAR(10) PRIMARY KEY,
                     username VARCHAR(10) NOT NULL,
                     image LONGBLOB
);