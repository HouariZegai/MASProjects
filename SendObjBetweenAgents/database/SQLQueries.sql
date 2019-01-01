
-- Delete database if exists
DROP DATABASE IF EXISTS persons_db;

-- Create new database
CREATE DATABASE persons_db;

-- Select datasabe
USE persons_db;

-- Structure of table person
CREATE TABLE person (
	id INT(11) PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(250),
	gender BOOLEAN,
	date_of_birth DATE
);

-- Data of table person

INSERT INTO person (name, gender, date_of_birth) VALUES ('Houari', true, '1996-11-17');
INSERT INTO person (name, gender, date_of_birth) VALUES ('Omar', true, '1966-04-03');
INSERT INTO person (name, gender, date_of_birth) VALUES ('Fatima', false, '1971-04-23');