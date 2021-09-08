CREATE SCHEMA IF NOT EXISTS `reportmanager`;
USE `reportmanager`;

CREATE TABLE IF NOT EXISTS `user` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(50) NOT NULL,
	`password` VARCHAR(50) NOT NULL,
    `is_active` BOOLEAN NOT NULL,
    `created_stamp` DATETIME
);

CREATE TABLE IF NOT EXISTS `role` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR (30) NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_roles` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS `report` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `file_name` NVARCHAR(100) NOT NULL,
    `content` TEXT NULL,
    `vector` TEXT NULL,
    `link` NVARCHAR(100) NOT NULL,
    `user_id` BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS `report_owner` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(30) NOT NULL,
	`type` INTEGER NOT NULL,
    `report_id` BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS `report_mark` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`mark` DOUBLE NOT NULL,
    `report_id` BIGINT NOT NULL
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES user (id); 
ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES role (id);   
ALTER TABLE report_owner
    ADD CONSTRAINT fk_report_owner_report FOREIGN KEY (report_id) REFERENCES report (id);
 ALTER TABLE report_mark
    ADD CONSTRAINT fk_report_mark_report FOREIGN KEY (report_id) REFERENCES report (id);
ALTER TABLE report
    ADD CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES user (id);   
    

CREATE SCHEMA IF NOT EXISTS `reportmanager`;
USE `reportmanager`;

CREATE TABLE IF NOT EXISTS `user` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(50) NOT NULL,
	`password` VARCHAR(50) NOT NULL,
    `is_active` BOOLEAN NOT NULL,
    `created_stamp` DATETIME
);

CREATE TABLE IF NOT EXISTS `role` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR (30) NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_roles` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS `report` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `file_name` NVARCHAR(100) NOT NULL,
    `content` TEXT NULL,
    `vector` TEXT NULL,
    `link` NVARCHAR(100) NOT NULL,
    `user_id` BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS `report_owner` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(30) NOT NULL,
	`type` INTEGER NOT NULL,
    `report_id` BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS `report_mark` (
	`id` BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`mark` DOUBLE NOT NULL,
    `report_id` BIGINT NOT NULL
);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES user (id); 
ALTER TABLE user_roles
    ADD CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES role (id);   
ALTER TABLE report_owner
    ADD CONSTRAINT fk_report_owner_report FOREIGN KEY (report_id) REFERENCES report (id);
 ALTER TABLE report_mark
    ADD CONSTRAINT fk_report_mark_report FOREIGN KEY (report_id) REFERENCES report (id);
ALTER TABLE report
    ADD CONSTRAINT fk_report_user FOREIGN KEY (user_id) REFERENCES user (id);   
    

