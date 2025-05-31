SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `HotelManagement` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `HotelManagement`;



-- Drop the tables if existed
DROP TABLE IF EXISTS `USER_ROLE_PERMISSION`;
DROP TABLE IF EXISTS `ACCOUNT`;
DROP TABLE IF EXISTS `BLOCK`;
DROP TABLE IF EXISTS `FLOOR`;
DROP TABLE IF EXISTS `GUEST`;
DROP TABLE IF EXISTS `BOOKING_CONFIRMATION_FORM`;
DROP TABLE IF EXISTS `INVOICE_DETAIL`;
DROP TABLE IF EXISTS `INVOICE`;
DROP TABLE IF EXISTS `PERMISSION`;
DROP TABLE IF EXISTS `POSITION`;
DROP TABLE IF EXISTS `RENTAL_EXTENSION_FORM`;
DROP TABLE IF EXISTS `RENTAL_FORM_DETAIL`;
DROP TABLE IF EXISTS `RENTAL_FORM`;
DROP TABLE IF EXISTS `REVENUE_REPORT_DETAIL`;
DROP TABLE IF EXISTS `REVENUE_REPORT`;
DROP TABLE IF EXISTS `ROOM`;
DROP TABLE IF EXISTS `ROOMTYPE`;
DROP TABLE IF EXISTS `STAFF`;
DROP TABLE IF EXISTS `USER_ROLE`;



-- Create tables if not exist
-- `USER_ROLE`
CREATE TABLE `USER_ROLE` (
                             `ID` INT AUTO_INCREMENT PRIMARY KEY,
                             `NAME` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `PERMISSION`
CREATE TABLE `PERMISSION` (
                              `ID` INT AUTO_INCREMENT PRIMARY KEY,
                              `NAME` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `USER_ROLE_PERMISSION` (Composite Key)
CREATE TABLE `USER_ROLE_PERMISSION` (
                                        `USER_ROLE_ID` INT NOT NULL,
                                        `PERMISSION_ID` INT NOT NULL,
                                        PRIMARY KEY (`USER_ROLE_ID`, `PERMISSION_ID`),
                                        FOREIGN KEY (`USER_ROLE_ID`) REFERENCES `USER_ROLE`(`ID`),
                                        FOREIGN KEY (`PERMISSION_ID`) REFERENCES `PERMISSION`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `ACCOUNT`
CREATE TABLE `ACCOUNT` (
                           `ID` INT AUTO_INCREMENT PRIMARY KEY,
                           `USERNAME` VARCHAR(255) NOT NULL,
                           `PASSWORD` VARCHAR(255) NOT NULL,
                           `USER_ROLE_ID` INT,
                           FOREIGN KEY (`USER_ROLE_ID`) REFERENCES `USER_ROLE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `BLOCK`
CREATE TABLE `BLOCK` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `NAME` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `FLOOR`
CREATE TABLE `FLOOR` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `NAME` VARCHAR(255) NOT NULL,
                         `BLOCK_ID` INT,
                         FOREIGN KEY (`BLOCK_ID`) REFERENCES `BLOCK`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `ROOMTYPE`
CREATE TABLE `ROOMTYPE` (
                            `ID` INT AUTO_INCREMENT PRIMARY KEY,
                            `NAME` VARCHAR(255) NOT NULL,
                            `PRICE` DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `ROOM`
CREATE TABLE `ROOM` (
                        `ID` INT AUTO_INCREMENT PRIMARY KEY,
                        `NAME` VARCHAR(255) NOT NULL,
                        `NOTE` TEXT,
                        `ROOM_STATE` ENUM('READY_TO_SERVE', 'BOOKED', 'BEING_CLEANED', 'UNDER_RENOVATION') NOT NULL,
                        `ROOMTYPE_ID` INT,
                        `FLOOR_ID` INT,
                        FOREIGN KEY (`ROOMTYPE_ID`) REFERENCES `ROOMTYPE`(`ID`),
                        FOREIGN KEY (`FLOOR_ID`) REFERENCES `FLOOR`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `GUEST`
CREATE TABLE `GUEST` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `NAME` VARCHAR(255) NOT NULL,
                         `SEX` ENUM('MALE', 'FEMALE') NOT NULL,
                         `AGE` SMALLINT NOT NULL,
                         `IDENTIFICATION_NUMBER` VARCHAR(255),
                         `PHONE_NUMBER` VARCHAR(255),
                         `EMAIL` VARCHAR(255),
                         `ACCOUNT_ID` INT,
                         FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `BOOKING_CONFIRMATION_FORM`
CREATE TABLE `BOOKING_CONFIRMATION_FORM` (
                                             `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                             `BOOKING_GUEST_ID` INT,
                                             `BOOKING_STATE` ENUM('PENDING', 'COMMITED', 'EXPIRED', 'CANCELLED') NOT NULL,
                                             `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (`BOOKING_GUEST_ID`) REFERENCES `GUEST`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `POSITION`
CREATE TABLE `POSITION` (
                            `ID` INT AUTO_INCREMENT PRIMARY KEY,
                            `NAME` VARCHAR(255) NOT NULL,
                            `BASE_SALARY` DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `STAFF`
CREATE TABLE `STAFF` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `FULL_NAME` VARCHAR(255) NOT NULL,
                         `AGE` INT NOT NULL,
                         `IDENTIFICATION_NUMBER` VARCHAR(255) NOT NULL,
                         `ADDRESS` VARCHAR(255) NOT NULL,
                         `SEX` ENUM('MALE', 'FEMALE') NOT NULL,
                         `SALARY_MULTIPLIER` FLOAT NOT NULL,
                         `POSITION_ID` INT,
                         `ACCOUNT_ID` INT,
                         FOREIGN KEY (`POSITION_ID`) REFERENCES `POSITION`(`ID`),
                         FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `INVOICE`
CREATE TABLE `INVOICE` (
                           `ID` INT AUTO_INCREMENT PRIMARY KEY,
                           `TOTAL_RESERVATION_COST` DOUBLE NOT NULL,
                           `PAYING_GUEST_ID` INT,
                           `STAFF_ID` INT,
                           `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (`PAYING_GUEST_ID`) REFERENCES `GUEST`(`ID`),
                           FOREIGN KEY (`STAFF_ID`) REFERENCES `STAFF`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `RENTAL_FORM`
CREATE TABLE `RENTAL_FORM` (
                               `ID` INT AUTO_INCREMENT PRIMARY KEY,
                               `ROOM_ID` INT,
                               `STAFF_ID` INT,
                               `RENTAL_DATE` DATETIME NOT NULL,
                               `NUMBER_OF_RENTAL_DAY` SMALLINT NOT NULL,
                               `NOTE` TEXT,
                               `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`),
                               FOREIGN KEY (`STAFF_ID`) REFERENCES `STAFF`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `RENTAL_FORM_DETAIL`
CREATE TABLE `RENTAL_FORM_DETAIL` (
                                      `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                      `RENTAL_FORM_ID` INT,
                                      `GUEST_ID` INT,
                                      FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`),
                                      FOREIGN KEY (`GUEST_ID`) REFERENCES `GUEST`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `INVOICE_DETAIL`
CREATE TABLE `INVOICE_DETAIL` (
                                  `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                  `NUMBER_OF_RENTAL_DAYS` INT NOT NULL,
                                  `INVOICE_ID` INT,
                                  `RESERVATION_COST` DOUBLE NOT NULL,
                                  `RENTAL_FORM_ID` INT,
                                  FOREIGN KEY (`INVOICE_ID`) REFERENCES `INVOICE`(`ID`),
                                  FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `RENTAL_EXTENSION_FORM`
CREATE TABLE `RENTAL_EXTENSION_FORM` (
                                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                         `RENTAL_FORM_ID` INT,
                                         `NUMBER_OF_RENTAL_DAY` SMALLINT NOT NULL,
                                         `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `REVENUE_REPORT`
CREATE TABLE `REVENUE_REPORT` (
                                  `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                  `YEAR` SMALLINT NOT NULL,
                                  `MONTH` TINYINT NOT NULL,
                                  `TOTAL_MONTH_REVENUE` DOUBLE NOT NULL,
                                  `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- `REVENUE_REPORT_DETAIL`
CREATE TABLE `REVENUE_REPORT_DETAIL` (
                                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                         `TOTAL_ROOM_REVENUE` DOUBLE NOT NULL,
                                         `REVENUE_REPORT_ID` INT,
                                         `ROOMTYPE_ID` INT,
                                         FOREIGN KEY (`REVENUE_REPORT_ID`) REFERENCES `REVENUE_REPORT`(`ID`),
                                         FOREIGN KEY (`ROOMTYPE_ID`) REFERENCES `ROOMTYPE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- Insert sample data into database
-- Insert data into USER_ROLE
INSERT INTO `USER_ROLE` (`NAME`) VALUES
                                     ('Admin'),
                                     ('Manager'),
                                     ('Receptionist'),
                                     ('Guest');

-- Insert data into PERMISSION
INSERT INTO `PERMISSION` (`NAME`) VALUES
                                      ('READ'),
                                      ('WRITE'),
                                      ('UPDATE'),
                                      ('DELETE');

-- Insert data into USER_ROLE_PERMISSION
INSERT INTO `USER_ROLE_PERMISSION` (`USER_ROLE_ID`, `PERMISSION_ID`) VALUES
                                                                         (1, 1), (1, 2), (1, 3), (1, 4), -- Admin full quyền
                                                                         (2, 1), (2, 2), (2, 3),         -- Manager quyền đọc, ghi, sửa
                                                                         (3, 1), (3, 2),                 -- Receptionist quyền đọc, ghi
                                                                         (4, 1);                         -- Guest chỉ đọc

-- Insert data into ACCOUNT
INSERT INTO `ACCOUNT` (`USERNAME`, `PASSWORD`, `USER_ROLE_ID`) VALUES
                                                                   ('admin', 'admin123', 1),
                                                                   ('manager', 'manager123', 2),
                                                                   ('reception', 'reception123', 3),
                                                                   ('guest', 'guest123', 4);

-- Insert data into BLOCK
INSERT INTO `BLOCK` (`NAME`) VALUES
                                 ('Block A'),
                                 ('Block B'),
                                 ('Block C'),
                                 ('Block D');

-- Insert data into FLOOR
INSERT INTO `FLOOR` (`NAME`, `BLOCK_ID`) VALUES
                                             ('Floor 1', 1),
                                             ('Floor 2', 1),
                                             ('Floor 3', 2),
                                             ('Floor 4', 2);

-- Insert data into ROOMTYPE
INSERT INTO `ROOMTYPE` (`NAME`, `PRICE`) VALUES
                                             ('Single', 500000),
                                             ('Double', 700000),
                                             ('Suite', 1500000),
                                             ('Deluxe', 1000000);

-- Insert data into ROOM
INSERT INTO `ROOM` (`NAME`, `NOTE`, `ROOM_STATE`, `ROOMTYPE_ID`, `FLOOR_ID`) VALUES
                                                                                 ('101', 'Sea View', 'READY_TO_SERVE', 1, 1),
                                                                                 ('102', 'Mountain View', 'BOOKED', 2, 1),
                                                                                 ('201', 'City View', 'READY_TO_SERVE', 3, 2),
                                                                                 ('202', 'Garden View', 'UNDER_RENOVATION', 4, 2);

-- Insert data into GUEST
INSERT INTO `GUEST` (`NAME`, `SEX`, `AGE`, `IDENTIFICATION_NUMBER`, `PHONE_NUMBER`, `EMAIL`, `ACCOUNT_ID`) VALUES
                                                                                                               ('Nguyen Van A', 'MALE', 30, '123456789', '0123456789', 'vana@example.com', 4),
                                                                                                               ('Tran Thi B', 'FEMALE', 28, '987654321', '0987654321', 'thib@example.com', 4),
                                                                                                               ('Le Van C', 'MALE', 35, '456789123', '0345678912', 'vanc@example.com', 4),
                                                                                                               ('Pham Thi D', 'FEMALE', 22, '321654987', '0765432198', 'thid@example.com', 4);

-- Insert data into BOOKING_CONFIRMATION_FORM
INSERT INTO `BOOKING_CONFIRMATION_FORM` (`BOOKING_GUEST_ID`, `BOOKING_STATE`) VALUES
                                                                                  (1, 'PENDING'),
                                                                                  (2, 'COMMITED'),
                                                                                  (3, 'EXPIRED'),
                                                                                  (4, 'CANCELLED');

-- Insert data into POSITION
INSERT INTO `POSITION` (`NAME`, `BASE_SALARY`) VALUES
                                                   ('Manager', 10000000),
                                                   ('Receptionist', 7000000),
                                                   ('Cleaner', 5000000),
                                                   ('Security', 6000000);

-- Insert data into STAFF
INSERT INTO `STAFF` (`FULL_NAME`, `AGE`, `IDENTIFICATION_NUMBER`, `ADDRESS`, `SEX`, `SALARY_MULTIPLIER`, `POSITION_ID`, `ACCOUNT_ID`) VALUES
                                                                                                                                          ('Nguyen Van E', 35, '100000001', 'Hanoi', 'MALE', 1.2, 1, 2),
                                                                                                                                          ('Tran Thi F', 29, '100000002', 'HCM', 'FEMALE', 1.1, 2, 3),
                                                                                                                                          ('Le Van G', 32, '100000003', 'Da Nang', 'MALE', 1.0, 3, NULL),
                                                                                                                                          ('Pham Thi H', 26, '100000004', 'Hai Phong', 'FEMALE', 0.9, 4, NULL);

-- Insert data into INVOICE
INSERT INTO `INVOICE` (`TOTAL_RESERVATION_COST`, `PAYING_GUEST_ID`, `STAFF_ID`) VALUES
                                                                                    (1500000, 1, 1),
                                                                                    (2000000, 2, 2),
                                                                                    (500000, 3, 3),
                                                                                    (1200000, 4, 4);

-- Insert data into RENTAL_FORM
INSERT INTO `RENTAL_FORM` (`ROOM_ID`, `STAFF_ID`, `RENTAL_DATE`, `NUMBER_OF_RENTAL_DAY`, `NOTE`) VALUES
                                                                                                     (1, 1, '2025-06-01 12:00:00', 3, 'VIP guest'),
                                                                                                     (2, 2, '2025-06-02 14:00:00', 2, 'Early check-in'),
                                                                                                     (3, 3, '2025-06-03 16:00:00', 5, NULL),
                                                                                                     (4, 4, '2025-06-04 10:00:00', 1, 'Special request');

-- Insert data into RENTAL_FORM_DETAIL
INSERT INTO `RENTAL_FORM_DETAIL` (`RENTAL_FORM_ID`, `GUEST_ID`) VALUES
                                                                    (1, 1),
                                                                    (2, 2),
                                                                    (3, 3),
                                                                    (4, 4);

-- Insert data into INVOICE_DETAIL
INSERT INTO `INVOICE_DETAIL` (`NUMBER_OF_RENTAL_DAYS`, `INVOICE_ID`, `RESERVATION_COST`, `RENTAL_FORM_ID`) VALUES
                                                                                                               (3, 1, 1500000, 1),
                                                                                                               (2, 2, 2000000, 2),
                                                                                                               (5, 3, 500000, 3),
                                                                                                               (1, 4, 1200000, 4);

-- Insert data into RENTAL_EXTENSION_FORM
INSERT INTO `RENTAL_EXTENSION_FORM` (`RENTAL_FORM_ID`, `NUMBER_OF_RENTAL_DAY`) VALUES
                                                                                   (1, 1),
                                                                                   (2, 2),
                                                                                   (3, 1),
                                                                                   (4, 3);

-- Insert data into REVENUE_REPORT
INSERT INTO `REVENUE_REPORT` (`YEAR`, `MONTH`, `TOTAL_MONTH_REVENUE`) VALUES
                                                                          (2025, 1, 100000000),
                                                                          (2025, 2, 120000000),
                                                                          (2025, 3, 110000000),
                                                                          (2025, 4, 130000000);

-- Insert data into REVENUE_REPORT_DETAIL
INSERT INTO `REVENUE_REPORT_DETAIL` (`TOTAL_ROOM_REVENUE`, `REVENUE_REPORT_ID`, `ROOMTYPE_ID`) VALUES
                                                                                                   (25000000, 1, 1),
                                                                                                   (30000000, 2, 2),
                                                                                                   (28000000, 3, 3),
                                                                                                   (32000000, 4, 4);



-- QUARTZ TABLES IMPLEMENT
-- THE WAY IT IS IMPLEMENTED BELOW IS DEFAULT, WHICH WAS TAKEN FROM GITHUB
-- LINK TO THE IMPLEMENTATION: https://github.com/quartz-scheduler/quartz/blob/main/quartz/src/main/resources/org/quartz/impl/jdbcjobstore/tables_mysql_innodb.sql
DROP TABLE IF EXISTS QRTZ_FIRED_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_PAUSED_TRIGGER_GRPS;
DROP TABLE IF EXISTS QRTZ_SCHEDULER_STATE;
DROP TABLE IF EXISTS QRTZ_LOCKS;
DROP TABLE IF EXISTS QRTZ_SIMPLE_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_SIMPROP_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_CRON_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_BLOB_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_TRIGGERS;
DROP TABLE IF EXISTS QRTZ_JOB_DETAILS;
DROP TABLE IF EXISTS QRTZ_CALENDARS;

CREATE TABLE QRTZ_JOB_DETAILS(
                                 SCHED_NAME VARCHAR(120) NOT NULL,
                                 JOB_NAME VARCHAR(190) NOT NULL,
                                 JOB_GROUP VARCHAR(190) NOT NULL,
                                 DESCRIPTION VARCHAR(250) NULL,
                                 JOB_CLASS_NAME VARCHAR(250) NOT NULL,
                                 IS_DURABLE VARCHAR(1) NOT NULL,
                                 IS_NONCONCURRENT VARCHAR(1) NOT NULL,
                                 IS_UPDATE_DATA VARCHAR(1) NOT NULL,
                                 REQUESTS_RECOVERY VARCHAR(1) NOT NULL,
                                 JOB_DATA BLOB NULL,
                                 PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_TRIGGERS (
                               SCHED_NAME VARCHAR(120) NOT NULL,
                               TRIGGER_NAME VARCHAR(190) NOT NULL,
                               TRIGGER_GROUP VARCHAR(190) NOT NULL,
                               JOB_NAME VARCHAR(190) NOT NULL,
                               JOB_GROUP VARCHAR(190) NOT NULL,
                               DESCRIPTION VARCHAR(250) NULL,
                               NEXT_FIRE_TIME BIGINT(13) NULL,
                               PREV_FIRE_TIME BIGINT(13) NULL,
                               PRIORITY INTEGER NULL,
                               TRIGGER_STATE VARCHAR(16) NOT NULL,
                               TRIGGER_TYPE VARCHAR(8) NOT NULL,
                               START_TIME BIGINT(13) NOT NULL,
                               END_TIME BIGINT(13) NULL,
                               CALENDAR_NAME VARCHAR(190) NULL,
                               MISFIRE_INSTR SMALLINT(2) NULL,
                               JOB_DATA BLOB NULL,
                               PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
                               FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
                                   REFERENCES QRTZ_JOB_DETAILS(SCHED_NAME,JOB_NAME,JOB_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_SIMPLE_TRIGGERS (
                                      SCHED_NAME VARCHAR(120) NOT NULL,
                                      TRIGGER_NAME VARCHAR(190) NOT NULL,
                                      TRIGGER_GROUP VARCHAR(190) NOT NULL,
                                      REPEAT_COUNT BIGINT(7) NOT NULL,
                                      REPEAT_INTERVAL BIGINT(12) NOT NULL,
                                      TIMES_TRIGGERED BIGINT(10) NOT NULL,
                                      PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
                                      FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
                                          REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_CRON_TRIGGERS (
                                    SCHED_NAME VARCHAR(120) NOT NULL,
                                    TRIGGER_NAME VARCHAR(190) NOT NULL,
                                    TRIGGER_GROUP VARCHAR(190) NOT NULL,
                                    CRON_EXPRESSION VARCHAR(120) NOT NULL,
                                    TIME_ZONE_ID VARCHAR(80),
                                    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
                                    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
                                        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_SIMPROP_TRIGGERS
(
    SCHED_NAME VARCHAR(120) NOT NULL,
    TRIGGER_NAME VARCHAR(190) NOT NULL,
    TRIGGER_GROUP VARCHAR(190) NOT NULL,
    STR_PROP_1 VARCHAR(512) NULL,
    STR_PROP_2 VARCHAR(512) NULL,
    STR_PROP_3 VARCHAR(512) NULL,
    INT_PROP_1 INT NULL,
    INT_PROP_2 INT NULL,
    LONG_PROP_1 BIGINT NULL,
    LONG_PROP_2 BIGINT NULL,
    DEC_PROP_1 NUMERIC(13,4) NULL,
    DEC_PROP_2 NUMERIC(13,4) NULL,
    BOOL_PROP_1 VARCHAR(1) NULL,
    BOOL_PROP_2 VARCHAR(1) NULL,
    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_BLOB_TRIGGERS (
                                    SCHED_NAME VARCHAR(120) NOT NULL,
                                    TRIGGER_NAME VARCHAR(190) NOT NULL,
                                    TRIGGER_GROUP VARCHAR(190) NOT NULL,
                                    BLOB_DATA BLOB NULL,
                                    PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP),
                                    INDEX (SCHED_NAME,TRIGGER_NAME, TRIGGER_GROUP),
                                    FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
                                        REFERENCES QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_CALENDARS (
                                SCHED_NAME VARCHAR(120) NOT NULL,
                                CALENDAR_NAME VARCHAR(190) NOT NULL,
                                CALENDAR BLOB NOT NULL,
                                PRIMARY KEY (SCHED_NAME,CALENDAR_NAME))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_PAUSED_TRIGGER_GRPS (
                                          SCHED_NAME VARCHAR(120) NOT NULL,
                                          TRIGGER_GROUP VARCHAR(190) NOT NULL,
                                          PRIMARY KEY (SCHED_NAME,TRIGGER_GROUP))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_FIRED_TRIGGERS (
                                     SCHED_NAME VARCHAR(120) NOT NULL,
                                     ENTRY_ID VARCHAR(95) NOT NULL,
                                     TRIGGER_NAME VARCHAR(190) NOT NULL,
                                     TRIGGER_GROUP VARCHAR(190) NOT NULL,
                                     INSTANCE_NAME VARCHAR(190) NOT NULL,
                                     FIRED_TIME BIGINT(13) NOT NULL,
                                     SCHED_TIME BIGINT(13) NOT NULL,
                                     PRIORITY INTEGER NOT NULL,
                                     STATE VARCHAR(16) NOT NULL,
                                     JOB_NAME VARCHAR(190) NULL,
                                     JOB_GROUP VARCHAR(190) NULL,
                                     IS_NONCONCURRENT VARCHAR(1) NULL,
                                     REQUESTS_RECOVERY VARCHAR(1) NULL,
                                     PRIMARY KEY (SCHED_NAME,ENTRY_ID))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_SCHEDULER_STATE (
                                      SCHED_NAME VARCHAR(120) NOT NULL,
                                      INSTANCE_NAME VARCHAR(190) NOT NULL,
                                      LAST_CHECKIN_TIME BIGINT(13) NOT NULL,
                                      CHECKIN_INTERVAL BIGINT(13) NOT NULL,
                                      PRIMARY KEY (SCHED_NAME,INSTANCE_NAME))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE QRTZ_LOCKS (
                            SCHED_NAME VARCHAR(120) NOT NULL,
                            LOCK_NAME VARCHAR(40) NOT NULL,
                            PRIMARY KEY (SCHED_NAME,LOCK_NAME))
    ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX IDX_QRTZ_J_REQ_RECOVERY ON QRTZ_JOB_DETAILS(SCHED_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_J_GRP ON QRTZ_JOB_DETAILS(SCHED_NAME,JOB_GROUP);

CREATE INDEX IDX_QRTZ_T_J ON QRTZ_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_JG ON QRTZ_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_T_C ON QRTZ_TRIGGERS(SCHED_NAME,CALENDAR_NAME);
CREATE INDEX IDX_QRTZ_T_G ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_T_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_N_G_STATE ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_GROUP,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NEXT_FIRE_TIME ON QRTZ_TRIGGERS(SCHED_NAME,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST ON QRTZ_TRIGGERS(SCHED_NAME,TRIGGER_STATE,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_STATE);
CREATE INDEX IDX_QRTZ_T_NFT_ST_MISFIRE_GRP ON QRTZ_TRIGGERS(SCHED_NAME,MISFIRE_INSTR,NEXT_FIRE_TIME,TRIGGER_GROUP,TRIGGER_STATE);

CREATE INDEX IDX_QRTZ_FT_TRIG_INST_NAME ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME);
CREATE INDEX IDX_QRTZ_FT_INST_JOB_REQ_RCVRY ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,INSTANCE_NAME,REQUESTS_RECOVERY);
CREATE INDEX IDX_QRTZ_FT_J_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_JG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,JOB_GROUP);
CREATE INDEX IDX_QRTZ_FT_T_G ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
CREATE INDEX IDX_QRTZ_FT_TG ON QRTZ_FIRED_TRIGGERS(SCHED_NAME,TRIGGER_GROUP);

commit;