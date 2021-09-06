DROP SCHEMA IF EXISTS university ;
CREATE SCHEMA IF NOT EXISTS university
    DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci ;

USE university ;

-- ---------------- users table ------------------------
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(40) NOT NULL,
  `last_name` VARCHAR(40) NOT NULL,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(32) NOT NULL,
  `role` ENUM('user', 'admin') NOT NULL,
  `lang` ENUM('ru','en') NOT NULL DEFAULT 'ru',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`));

-- ---------------- applicants -------------------------------------
DROP TABLE IF EXISTS applicants;

CREATE TABLE IF NOT EXISTS applicants (
  `id` INT NOT NULL AUTO_INCREMENT,
  `city` VARCHAR(40) NOT NULL,
  `district` VARCHAR(40) NOT NULL,
  `school` VARCHAR(50) NOT NULL,
  `users_id` INT REFERENCES `users` (`id`) ON DELETE CASCADE,
  `isBlocked` TINYINT NOT NULL DEFAULT false,
  PRIMARY KEY (`id`) );

-- ------------ faculties -----------------------------------------
DROP TABLE IF EXISTS faculties;

CREATE TABLE IF NOT EXISTS faculties (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(100) NOT NULL,
  `name_en` VARCHAR(100) NOT NULL,
  `total_places`  INT UNSIGNED NOT NULL,
  `budget_places` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name_ru`),
  UNIQUE (`name_en`) );

-- ------------- subjects ----------------------------------------
DROP TABLE IF EXISTS subjects;

CREATE TABLE IF NOT EXISTS subjects (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_ru` VARCHAR(40) NOT NULL,
  `name_en` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE (`name_ru`),
  UNIQUE (`name_en`) );

-- ---------------- faculty applicants -------------------------------------
DROP TABLE IF EXISTS faculty_applicants ;

CREATE TABLE IF NOT EXISTS faculty_applicants (
  `id` INT NOT NULL AUTO_INCREMENT,
  `applicant_id` INT REFERENCES `applicants` (`id`) ON DELETE CASCADE,
  `faculty_id`   INT REFERENCES `faculties` (`id`) ON DELETE CASCADE,
  PRIMARY KEY (`id`, `applicant_id`, `faculty_id`),
  UNIQUE (`applicant_id`, `faculty_id`) );

-- -----------------------------------------------------
DROP TABLE IF EXISTS faculty_subjects;

CREATE TABLE IF NOT EXISTS faculty_subjects (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `faculty_id` INT REFERENCES `faculties` (`id`) ON DELETE CASCADE,
  `subject_id` INT REFERENCES `subjects` (`id`) ON DELETE CASCADE,
  PRIMARY KEY (`id`, `faculty_id`, `subject_id`),
  UNIQUE (`faculty_id`, `subject_id`) );

-- -------------- grades ---------------------------------
DROP TABLE IF EXISTS grades;

CREATE TABLE IF NOT EXISTS grades (
  `id` INT NOT NULL AUTO_INCREMENT,
  `applicant_id` INT REFERENCES `applicants` (`id`) ON DELETE CASCADE,
  `subject_id`   INT REFERENCES `subjects` (`id`) ON DELETE CASCADE,
  `grade` TINYINT UNSIGNED NOT NULL,
  `exam_type` ENUM('diploma','preliminary') NOT NULL,
  PRIMARY KEY (`id`, `applicant_id`, `subject_id`, `exam_type`) );

-- ---------------------------------------------------------


------------------ prepare users ----------------------------------
INSERT INTO `users`
    VALUES (1, 'admin_name', 'admin_lastname', 'admin@univer.com', 'admin', 'admin', 'en');
INSERT INTO `users`
    VALUES (2, 'Иван', 'Иванов', 'ivanov@gmail.com', '123', 'user', 'ru');
INSERT INTO `users`
    VALUES (3, 'John', 'Smith', 'j.smith@gmail.com', 'j123', 'user', 'en');

------------------- prepare applicants ----------------------------
INSERT INTO applicants
    VALUES (1, 'Харьков', 'Харьковская область', 'Школа 1', 2, DEFAULT);
INSERT INTO applicants
    VALUES (2, 'London', 'district', 'Ashbourne college', 3, DEFAULT);

-------------------- prepare faculties ------------------------------
INSERT INTO faculties
    VALUES (1, 'Географический', 'Geography', 20, 5);
INSERT INTO faculties
    VALUES (2, 'Экономический', 'Economics', 100, 50);
INSERT INTO faculties
    VALUES (3, 'Исторический', 'History', 20, 10);
INSERT INTO faculties
    VALUES (4, 'Механико-математический', 'Mechanics and Mathematics', 100, 50);
INSERT INTO faculties
    VALUES (5, 'Информационных технологий', 'Information technology', 200, 50);

-- экономический, исторический, механико-математический, информационных технологий
-- психологии, социологии

-- укр язык и лит, история У, математика, ин. язык, биология, химия, география,

--------------------- prepare subjects ------------------------
INSERT INTO subjects
    VALUES (1, 'Украинский язык и литература', 'Ukrainian language and literature');
INSERT INTO subjects
    VALUES (2, 'Математика', 'Math');
INSERT INTO subjects
    VALUES (3, 'История Украины', 'History of Ukraine');
INSERT INTO subjects
    VALUES (4, 'География', 'Geography');
INSERT INTO subjects
    VALUES (5, 'Английский язык', 'English');

---------------------- prepare faculty_subjects ----------------------
INSERT INTO faculty_subjects
    VALUES (1, 1, 4);
INSERT INTO faculty_subjects
    VALUES (2, 1, 1);

INSERT INTO faculty_subjects
    VALUES (3, 2, 2);
INSERT INTO faculty_subjects
    VALUES (4, 2, 5);

INSERT INTO faculty_subjects
    VALUES (5, 3, 1);
INSERT INTO faculty_subjects
    VALUES (6, 3, 3);

INSERT INTO faculty_subjects
    VALUES (7, 4, 1);
INSERT INTO faculty_subjects
    VALUES (8, 4, 2);

INSERT INTO faculty_subjects
    VALUES (7, 5, 5);
INSERT INTO faculty_subjects
    VALUES (8, 5, 2);

--------------------- prepare
