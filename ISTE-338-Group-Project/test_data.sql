-- ISTE-338-Group-Project
-- Authors-Anderson Cardenas,Andrew Apollo,Jacob Bier,Nick Berko,Lauren Carver


-- Insert data into Major
INSERT INTO Major (name) VALUES ('Computer Information Technologies');
INSERT INTO Major (name) VALUES ('Web and Mobile Computing');
INSERT INTO Major (name) VALUES ('Human-Centered Computing');

-- Insert data into Account for faculty, students, and outside organizations
INSERT INTO Account (email, password, type) VALUES ('jdoe@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Faculty');
INSERT INTO Account (email, password, type) VALUES ('asmith@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Student');
INSERT INTO Account (email, password, type) VALUES ('rjones@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Faculty');
INSERT INTO Account (email, password, type) VALUES ('mjane@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Student');
INSERT INTO Account (email, password, type) VALUES ('org1@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Outside');
INSERT INTO Account (email, password, type) VALUES ('org2@example.com', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'Outside');

-- Insert data into Faculty
INSERT INTO Faculty (firstName, lastName, phone, email, building, office) VALUES ('John', 'Doe', '1234567890', 'jdoe@example.com', 'RH4', '101');
INSERT INTO Faculty (firstName, lastName, phone, email, building, office) VALUES ('Rebecca', 'Jones', '2345678901', 'rjones@example.com', 'MB1', '102B');

-- Insert data into Student
INSERT INTO Student (firstName, lastName, phone, email, majorID) VALUES ('Alice', 'Smith', 1234567890, 'asmith@example.com', 1);
INSERT INTO Student (firstName, lastName, phone, email, majorID) VALUES ('Mary', 'Jane', 2345678901, 'mjane@example.com', 2);

-- Insert data into Outside Organization
INSERT INTO Outside (orgName, phone, email) VALUES ('Organization 1', 3456789012, 'org1@example.com');
INSERT INTO Outside (orgName, phone, email) VALUES ('Organization 2', 4567890123, 'org2@example.com');

-- Insert data into Interest
INSERT INTO Interest (name, interestDescription) VALUES ('Machine Learning', 'Study of computer algorithms that improve automatically through experience.');
INSERT INTO Interest (name, interestDescription) VALUES ('JavaScript', 'A multi-paradigm, dynamic language with types and operators, standard built-in objects, and methods.');
INSERT INTO Interest (name, interestDescription) VALUES ('React', 'A free and open-source front-end JavaScript library for building user interfaces based on components.');
INSERT INTO Interest (name, interestDescription) VALUES ('MySQL', 'A relational database management system (RDBMS) developed by Oracle that is based on structured query language (SQL).');
INSERT INTO Interest (name, interestDescription) VALUES ('Java', 'A multiplatform, object-oriented programming language that runs on billions of devices worldwide.');
INSERT INTO Interest (name, interestDescription) VALUES ('Python', 'An interpreted, object-oriented, high-level programming language with dynamic semantics.');
INSERT INTO Interest (name, interestDescription) VALUES ('C#', 'A cross-platform general purpose language that makes developers productive while writing highly performant code.');
INSERT INTO Interest (name, interestDescription) VALUES ('HTML', 'HTML defines the content and the structure of a webpage.');
INSERT INTO Interest (name, interestDescription) VALUES ('PHP', 'An open-source, server-side programming language that can be used to create websites, applications, customer relationship management systems and more.');
INSERT INTO Interest (name, interestDescription) VALUES ('C++', 'A high-level, general-purpose programming language created by Danish computer scientist Bjarne Stroustrup.');

-- Insert data into Abstract
INSERT INTO Abstract (abstractFile) VALUES ('This is a sample abstract text related to Machine Learning.');
INSERT INTO Abstract (abstractFile) VALUES ('This is a sample abstract text related to Literature.');

-- Link Faculty and Abstracts
INSERT INTO Faculty_Abstract (facultyID, abstractID) VALUES (1, 1);
INSERT INTO Faculty_Abstract (facultyID, abstractID) VALUES (2, 2);

-- Insert data into StudentInterest
INSERT INTO StudentInterest (interestID, studentID) VALUES (1, 1);
INSERT INTO StudentInterest (interestID, studentID) VALUES (9, 1);
INSERT INTO StudentInterest (interestID, studentID) VALUES (2, 2);
INSERT INTO StudentInterest (interestID, studentID) VALUES (7, 2);
INSERT INTO StudentInterest (interestID, studentID) VALUES (4, 2);

-- Insert data into FacultyInterest
INSERT INTO FacultyInterest (facultyID, interestID) VALUES (1, 5);
INSERT INTO FacultyInterest (facultyID, interestID) VALUES (1, 7);
INSERT INTO FacultyInterest (facultyID, interestID) VALUES (2, 6);
INSERT INTO FacultyInterest (facultyID, interestID) VALUES (2, 4);