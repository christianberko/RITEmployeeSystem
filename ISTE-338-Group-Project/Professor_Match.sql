-- ISTE-338-Group-Project
-- Authors-Anderson Cardenas,Andrew Apollo,Jacob Bier,Nick Berko,Lauren Carver

DROP DATABASE IF EXISTS Professor_Match;
CREATE DATABASE Professor_Match;
USE Professor_Match; 

CREATE TABLE Major (
    majorID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(45),
    CONSTRAINT PK_major PRIMARY KEY (majorID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Account (
    accountID INT AUTO_INCREMENT,
    email VARCHAR(55) UNIQUE,
    password VARCHAR(100),
    type ENUM('Faculty', 'Student', 'Outside'),
    PRIMARY KEY (accountID) 
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty (
    facultyID INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(30),
    lastName VARCHAR(30),
    phone VARCHAR(10), 
    email VARCHAR(55),
    building VARCHAR(4),
    office VARCHAR(30),
    CONSTRAINT faculty_email_fk FOREIGN KEY (email) REFERENCES Account(email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Outside (
    orgID INT AUTO_INCREMENT PRIMARY KEY,
    orgName VARCHAR(30),
    phone VARCHAR(10),
    email VARCHAR(55),
    CONSTRAINT pk_outsideID PRIMARY KEY (orgID),
    CONSTRAINT org_email_fk FOREIGN KEY (email) REFERENCES Account (email)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Student(
    studentID INT AUTO_INCREMENT,
    firstName VARCHAR(30),
    lastName VARCHAR(30),
    phone VARCHAR(10),
    email VARCHAR(55),
    majorID INT,
    CONSTRAINT pk_studentID PRIMARY KEY (studentID),
    CONSTRAINT student_email_fk FOREIGN KEY (email) REFERENCES Account(email),
    CONSTRAINT fk_majorID FOREIGN KEY (majorID) REFERENCES Major(majorID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE Interest (
    interestID INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(55),
    interestDescription MEDIUMTEXT,
    CONSTRAINT PK_interest PRIMARY KEY (interestID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE Abstract(
    abstractID INT NOT NULL AUTO_INCREMENT,
    abstractFile MEDIUMTEXT,
    CONSTRAINT pk_abstractID PRIMARY KEY(abstractID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE Faculty_Abstract(
    facultyID INT NOT NULL ,
    abstractID INT NOT NULL ,
    CONSTRAINT pk_faculty_abstractID PRIMARY KEY(facultyID,abstractID),
    CONSTRAINT abstract_ibfk_1 FOREIGN KEY (facultyID) REFERENCES Faculty (facultyID),
    CONSTRAINT abstract_ibfk_2 FOREIGN KEY (abstractID) REFERENCES Abstract (abstractID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE StudentInterest(
interestID int NOT NULL ,
studentID int NOT NULL ,
PRIMARY KEY(interestID, studentID),
CONSTRAINT student_interest_ibfk_1 FOREIGN KEY (interestID) REFERENCES Interest (interestID),
CONSTRAINT student_interest_ibfk_2 FOREIGN KEY (studentID) REFERENCES Student (studentID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE FacultyInterest (
    facultyID INT,
    interestID INT,
    CONSTRAINT faculty_interest_ibfk_1 FOREIGN KEY (facultyID) REFERENCES Faculty(facultyID),
    CONSTRAINT faculty_interst_ibfk_2 FOREIGN KEY (interestID) REFERENCES Interest(interestID)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;