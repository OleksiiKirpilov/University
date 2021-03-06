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
    `password` CHAR(64) NOT NULL,
    `salt` VARCHAR(16) DEFAULT NULL,
    `role` ENUM('user', 'admin') NOT NULL,
    `lang` ENUM('ru','en') NOT NULL DEFAULT 'en',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC));

-- ---------------- applicants -------------------------------------
DROP TABLE IF EXISTS applicants;

CREATE TABLE IF NOT EXISTS applicants (
    `id` INT NOT NULL AUTO_INCREMENT,
    `city` VARCHAR(40) NOT NULL,
    `district` VARCHAR(40) NOT NULL,
    `school` VARCHAR(50) NOT NULL,
    `users_id` INT NOT NULL,
    `isBlocked` TINYINT NOT NULL DEFAULT false,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk_applicants_users_idx` (`users_id` ASC),
    CONSTRAINT `fk_applicant_users`
        FOREIGN KEY (`users_id`) REFERENCES `users` (`id`)
        ON DELETE CASCADE
    );

-- ------------ faculties -----------------------------------------
DROP TABLE IF EXISTS faculties;

CREATE TABLE IF NOT EXISTS faculties (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name_ru` VARCHAR(100) NOT NULL,
    `name_en` VARCHAR(100) NOT NULL,
    `total_places`  INT UNSIGNED NOT NULL,
    `budget_places` INT UNSIGNED NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_ru_UNIQUE`(`name_ru` ASC),
    UNIQUE INDEX `name_en_UNIQUE`(`name_en` ASC) );

-- ------------- subjects ----------------------------------------
DROP TABLE IF EXISTS subjects;

CREATE TABLE IF NOT EXISTS subjects (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name_ru` VARCHAR(40) NOT NULL,
    `name_en` VARCHAR(40) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_ru_UNIQUE`(`name_ru` ASC),
    UNIQUE INDEX `name_en_UNIQUE`(`name_en` ASC) );

-- ---------------- faculty applicants -------------------------------------
DROP TABLE IF EXISTS faculty_applicants ;

CREATE TABLE IF NOT EXISTS faculty_applicants (
    `id` INT NOT NULL AUTO_INCREMENT,
    `applicant_id` INT NOT NULL,
    `faculty_id`   INT NOT NULL,
    PRIMARY KEY (`id`), 
    INDEX `fk_applicants_has_faculty_faculty1_idx` (`faculty_id` ASC),
    INDEX `fk_applicants_has_faculty_applicant1_idx` (`applicant_id` ASC),
    UNIQUE INDEX `id_faculty_applicants_UNIQUE` (`id` ASC),
    CONSTRAINT `fk_applicants_has_faculty_applicant1`
		FOREIGN KEY (`applicant_id`)
		REFERENCES `applicants` (`id`)
			ON DELETE CASCADE
			ON UPDATE NO ACTION,
    CONSTRAINT `fk_applicants_has_faculty_faculty1`
		FOREIGN KEY (`faculty_id`)
		REFERENCES `faculties` (`id`)
			ON DELETE CASCADE
			ON UPDATE NO ACTION,
    UNIQUE (`applicant_id`, `faculty_id`) );

-- -----------------------------------------------------
DROP TABLE IF EXISTS faculty_subjects;

CREATE TABLE IF NOT EXISTS faculty_subjects (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `faculty_id` INT NOT NULL,
    `subject_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    INDEX `fk_faculty_has_subject_subject1_idx` (`subject_id` ASC),
    INDEX `fk_faculty_has_subject_faculty1_idx` (`faculty_id` ASC),
    UNIQUE INDEX `id_faculty_subjects_UNIQUE` (`id` ASC),
    CONSTRAINT `fk_faculty_has_subject_faculty1`
    FOREIGN KEY (`faculty_id`)
    REFERENCES `faculties` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_faculty_has_subject_subject1`
    FOREIGN KEY (`subject_id`)
    REFERENCES `subjects` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
    UNIQUE (`faculty_id`, `subject_id`) );

-- -------------- grades ---------------------------------
DROP TABLE IF EXISTS grades;

CREATE TABLE IF NOT EXISTS grades (
    `id` INT NOT NULL AUTO_INCREMENT,
    `applicant_id` INT NOT NULL,
    `subject_id`   INT NOT NULL,
    `grade` TINYINT UNSIGNED NOT NULL,
    `exam_type` ENUM('diploma','preliminary') NOT NULL,
    `confirmed` TINYINT NOT NULL DEFAULT FALSE,
    PRIMARY KEY (`id`),
    INDEX `fk_applicant_has_subjects_subject1_idx` (`subject_id` ASC),
    INDEX `fk_applicant_has_subjects_applicant1_idx` (`applicant_id` ASC),
    UNIQUE INDEX `id_grades_UNIQUE` (`id` ASC),
    CONSTRAINT `fk_applicants_has_subject_applicant1`
    FOREIGN KEY (`applicant_id`)
    REFERENCES `applicants` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_applicants_has_subject_subject1`
    FOREIGN KEY (`subject_id`)
    REFERENCES `subjects` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
	UNIQUE (`applicant_id`, `subject_id`, `exam_type`)
    );

-- -----------------------------------------------------
-- Placeholder table for view `faculties_report_sheet`
-- -----------------------------------------------------
DROP TABLE IF EXISTS report_sheet;

CREATE TABLE IF NOT EXISTS report_sheet
(`id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
`faculty_id` INT,
`first_name` VARCHAR(40) NOT NULL,
`last_name` VARCHAR(40) NOT NULL,
`email` VARCHAR(100) NOT NULL,
`isBlocked` TINYINT NOT NULL DEFAULT false,
`preliminary_sum` INT,
`diploma_sum` INT,
`total_sum` INT,
`entered` TINYINT NOT NULL DEFAULT false,
`entered_on_budget` TINYINT NOT NULL DEFAULT false,
PRIMARY KEY (`id`)	
 );

-- -------------- View `faculties_report_sheet` -------------------------
CREATE OR REPLACE VIEW faculties_report_sheet AS
SELECT
    faculty_id,
    first_name,
    last_name,
    users.email,
    applicants.isBlocked,
    preliminary_sum,
    diploma_sum,
    preliminary_sum + diploma_sum AS total_sum
FROM
    applicants_grades_sum
        INNER JOIN
    faculties ON applicants_grades_sum.faculty_id = faculties.id
        INNER JOIN
    applicants ON applicant_id = applicants.id
        INNER JOIN
    users ON applicants.users_id = users.id
ORDER BY isBlocked ASC , `total_sum` DESC;

-- --------------- View applicants_grades_sum --------------
CREATE OR REPLACE VIEW applicants_grades_sum AS
SELECT
    faculty_applicants.faculty_id AS `faculty_id`,
    grades.applicant_id AS `applicant_id`,
    SUM(CASE `exam_type`
            WHEN 'preliminary' THEN grades.grade
            ELSE 0
        END) AS `preliminary_sum`,
    SUM(CASE `exam_type`
            WHEN 'diploma' THEN grades.grade
            ELSE 0
        END) AS `diploma_sum`
FROM
    faculty_applicants INNER JOIN grades
                                  ON faculty_applicants.applicant_id = grades.applicant_id
WHERE grades.confirmed = true
GROUP BY faculty_applicants.faculty_id, applicant_id;

-- ---------------- prepare users ----------------------------------
INSERT INTO `users`
VALUES (1, 'adminname', 'adminlastname', 'admin@univer.com', 'admin', NULL, 'admin', 'en');
INSERT INTO `users`
VALUES (2, '????????', '????????????', 'ivanov@gmail.com', '1111', NULL, 'user', 'ru');
INSERT INTO `users`
VALUES (3, '????????????', '??????????????', 'sergeev@gmail.com', '1111', NULL, 'user', 'ru');
INSERT INTO `users`
VALUES (4, '????????', '????????????', 'petrov@gmail.com', '1111', NULL, 'user', 'ru');
INSERT INTO `users`
VALUES (5, '??????????????????', '??????????????????????', 'aleksandrov@gmail.com', '1111', NULL, 'user', 'ru');
INSERT INTO `users`
VALUES (6, 'John', 'Smith', 'j.smith@gmail.com', 'j123', NULL, 'user', 'en');
INSERT INTO `users`
VALUES (7, 'Dale', 'Cooper', 'd.cooper@gmail.com', '2222', NULL, 'user', 'en');
INSERT INTO `users`
VALUES (8, 'James', 'Brown', 'j.brown@gmail.com', '2222', NULL, 'user', 'en');
INSERT INTO `users`
VALUES (9, 'Darth', 'Vader', 'd.vader@gmail.com', '2222', NULL, 'user', 'en');

-- ----------------- prepare applicants ----------------------------
INSERT INTO applicants
VALUES (1, '??????????????', '?????????????????????? ??????????????', '?????????? 1', 2, DEFAULT);
INSERT INTO applicants
VALUES (2, '??????????????', '?????????????????????? ??????????????', '?????????? 2', 3, DEFAULT);
INSERT INTO applicants
VALUES (3, '????????', '????????', '?????????? 1', 4, DEFAULT);
INSERT INTO applicants
VALUES (4, '????????????', '????????????????', '?????????? 6', 5, DEFAULT);
INSERT INTO applicants
VALUES (5, 'London', 'London', 'Ashbourne college', 6, DEFAULT);
INSERT INTO applicants
VALUES (6, 'Chicago', 'Illinois', 'Columbia college', 7, DEFAULT);
INSERT INTO applicants
VALUES (7, 'Houston', 'Texas', 'Houston Community College', 8, DEFAULT);
INSERT INTO applicants
VALUES (8, 'Tatooine', 'no data', 'home education', 9, DEFAULT);

-- ------------------ prepare faculties ------------------------------
INSERT INTO faculties
VALUES (1, '????????????????????????????', 'Geography', 5, 2);
INSERT INTO faculties
VALUES (2, '??????????????????????????', 'Economics', 10, 2);
INSERT INTO faculties
VALUES (3, '????????????????????????', 'History', 5, 1);
INSERT INTO faculties
VALUES (4, '????????????????-????????????????????????????', 'Mechanics and Mathematics', 5, 2);
INSERT INTO faculties
VALUES (5, '???????????????????????????? ????????????????????', 'Information technology', 5, 2);
INSERT INTO faculties
VALUES (6, '????????????????????', 'Psychology', 6, 3);

-- ------------------- prepare subjects ------------------------
INSERT INTO subjects
VALUES (1, '???????????????????? ???????? ?? ????????????????????', 'Ukrainian language and literature');
INSERT INTO subjects
VALUES (2, '????????????????????', 'Math');
INSERT INTO subjects
VALUES (3, '?????????????? ??????????????', 'History of Ukraine');
INSERT INTO subjects
VALUES (4, '??????????????????', 'Geography');
INSERT INTO subjects
VALUES (5, '???????????????????? ????????', 'English');

-- -------------------- prepare faculty_subjects ----------------------
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
VALUES (9, 5, 5);
INSERT INTO faculty_subjects
VALUES (10, 5, 2);

-- ------------------- prepare grades ---------------------------------------
INSERT INTO grades
VALUES (1, 1, 1, 7, 'diploma', 0);
INSERT INTO grades
VALUES (2, 1, 2, 8, 'diploma', 0);
INSERT INTO grades
VALUES (3, 1, 3, 9, 'diploma', 0);
INSERT INTO grades
VALUES (4, 1, 4, 10, 'diploma', 0);
INSERT INTO grades
VALUES (5, 1, 5, 11, 'diploma', 0);
INSERT INTO grades
VALUES (6, 1, 1, 9, 'preliminary', 0);
INSERT INTO grades
VALUES (7, 1, 3, 12, 'preliminary', 0);

-- ------------------- prepare faculty applicants----------------------------
INSERT INTO faculty_applicants
VALUES (1, 1, 3);