SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `HotelManagement` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `HotelManagement`;

-- Drop the tables if existed
DROP TABLE IF EXISTS `ROLE_PERMISSION`;
DROP TABLE IF EXISTS `USER_ROLE`;
DROP TABLE IF EXISTS `STAFF`;
DROP TABLE IF EXISTS `ACCOUNT`;
DROP TABLE IF EXISTS `POSITION`;
DROP TABLE IF EXISTS `PERMISSION`;
DROP TABLE IF EXISTS `ROOM_SUPPLY`;
DROP TABLE IF EXISTS `ROOM`;
DROP TABLE IF EXISTS `ROOMTYPE`;
DROP TABLE IF EXISTS `FLOOR`;
DROP TABLE IF EXISTS `BLOCK`;
DROP TABLE IF EXISTS `GUEST`;
DROP TABLE IF EXISTS `BOOKING_CONFIRMATION_FORM`;
DROP TABLE IF EXISTS `INVOICE_DETAIL`;
DROP TABLE IF EXISTS `INVOICE`;
DROP TABLE IF EXISTS `RENTAL_FORM_DETAIL`;
DROP TABLE IF EXISTS `RENTAL_FORM`;
DROP TABLE IF EXISTS `RENTAL_EXTENSION_FORM`;
DROP TABLE IF EXISTS `FACILITY_COMPENSATION`;
DROP TABLE IF EXISTS `FACILITY`;
DROP TABLE IF EXISTS `IMPORT_INVOICE_DETAIL`;
DROP TABLE IF EXISTS `IMPORT_INVOICE`;
DROP TABLE IF EXISTS `STOCK_REQUISITION_DETAIL`;
DROP TABLE IF EXISTS `STOCK_REQUISITION_INVOICE`;
DROP TABLE IF EXISTS `REVENUE_REPORT_DETAIL`;
DROP TABLE IF EXISTS `REVENUE_REPORT`;

-- Create tables for database
CREATE TABLE `USER_ROLE` (
                             `ID` INT PRIMARY KEY AUTO_INCREMENT,
                             `NAME` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ACCOUNT` (
                           `ID` INT PRIMARY KEY AUTO_INCREMENT,
                           `USERNAME` VARCHAR(100) NOT NULL UNIQUE,
                           `PASSWORD` VARCHAR(255) NOT NULL,
                           `USER_ROLE_ID` INT,
                           FOREIGN KEY (`USER_ROLE_ID`) REFERENCES `USER_ROLE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `POSITION` (
                            `ID` INT PRIMARY KEY AUTO_INCREMENT,
                            `NAME` VARCHAR(100) NOT NULL,
                            `BASE_SALARY` DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `STAFF` (
                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                         `FULL_NAME` VARCHAR(100) NOT NULL,
                         `AGE` INT,
                         `IDENTIFICATION_NUMBER` VARCHAR(20) UNIQUE,
                         `ADDRESS` VARCHAR(255),
                         `SEX` ENUM('MALE', 'FEMALE'),
                         `SALARY_MULTIPLIER` FLOAT,
                         `POSITION_ID` INT,
                         `ACCOUNT_ID` INT,
                         FOREIGN KEY (`POSITION_ID`) REFERENCES `POSITION`(`ID`),
                         FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `PERMISSION` (
                              `ID` INT PRIMARY KEY AUTO_INCREMENT,
                              `NAME` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ROLE_PERMISSION` (
                                   `USER_ROLE_ID` INT,
                                   `PERMISSION_ID` INT,
                                   PRIMARY KEY (`USER_ROLE_ID`, `PERMISSION_ID`),
                                   FOREIGN KEY (`USER_ROLE_ID`) REFERENCES `USER_ROLE`(`ID`),
                                   FOREIGN KEY (`PERMISSION_ID`) REFERENCES `PERMISSION`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `BLOCK` (
                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                         `NAME` VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `FLOOR` (
                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                         `NAME` VARCHAR(100) NOT NULL,
                         `BLOCK_ID` INT,
                         FOREIGN KEY (`BLOCK_ID`) REFERENCES `BLOCK`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ROOMTYPE` (
                            `ID` INT PRIMARY KEY AUTO_INCREMENT,
                            `NAME` VARCHAR(100) NOT NULL,
                            `PRICE` DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ROOM` (
                        `ID` INT PRIMARY KEY AUTO_INCREMENT,
                        `NAME` VARCHAR(100) NOT NULL,
                        `PRICE` DOUBLE NOT NULL,
                        `NOTE` TEXT,
                        `ROOM_STATE` ENUM('READY_TO_SERVE', 'BOOKED', 'BEING_CLEANED', 'UNDER_RENOVATION'),
                        `ROOMTYPE_ID` INT,
                        `FLOOR_ID` INT,
                        FOREIGN KEY (`ROOMTYPE_ID`) REFERENCES `ROOMTYPE`(`ID`),
                        FOREIGN KEY (`FLOOR_ID`) REFERENCES `FLOOR`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `GUEST` (
                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                         `NAME` VARCHAR(100) NOT NULL,
                         `SEX` ENUM('MALE', 'FEMALE'),
                         `AGE` SMALLINT,
                         `IDENTIFICATION_NUMBER` VARCHAR(20),
                         `ADDRESS` VARCHAR(255),
                         `PHONE_NUMBER` VARCHAR(20),
                         `NATIONALITY` VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `BOOKING_CONFIRMATION_FORM` (
                                             `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                             `BOOKING_PERSON_NAME` VARCHAR(100) NOT NULL,
                                             `IDENTIFICATION_NUMBER` VARCHAR(20) NOT NULL,
                                             `BOOKING_PERSON_AGE` TINYINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `RENTAL_FORM` (
                               `ID` INT PRIMARY KEY AUTO_INCREMENT,
                               `ROOM_ID` INT,
                               `RENTAL_DATE` DATETIME,
                               `NUMBER_OF_RENTAL_DAY` SMALLINT,
                               `NOTE` TEXT,
                               FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `RENTAL_FORM_DETAIL` (
                                      `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                      `RENTAL_FORM_ID` INT,
                                      `GUEST_ID` INT,
                                      FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`),
                                      FOREIGN KEY (`GUEST_ID`) REFERENCES `GUEST`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `RENTAL_EXTENSION_FORM` (
                                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                         `RENTAL_FORM_ID` INT,
                                         `NUMBER_OF_RENTAL_DAY` SMALLINT,
                                         FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `INVOICE` (
                           `ID` INT PRIMARY KEY AUTO_INCREMENT,
                           `TOTAL_RESERVATION_COST` DOUBLE,
                           `PAYING_GUEST_ID` INT,
                           `STAFF_ID` INT,
                           FOREIGN KEY (`PAYING_GUEST_ID`) REFERENCES `GUEST`(`ID`),
                           FOREIGN KEY (`STAFF_ID`) REFERENCES `STAFF`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `INVOICE_DETAIL` (
                                  `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                  `NUMBER_OF_RENTAL_DAYS` INT,
                                  `INVOICE_ID` INT,
                                  `RESERVATION_COST` DOUBLE,
                                  `RENTAL_FORM_ID` INT,
                                  FOREIGN KEY (`INVOICE_ID`) REFERENCES `INVOICE`(`ID`),
                                  FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `FACILITY` (
                            `ID` INT PRIMARY KEY AUTO_INCREMENT,
                            `NAME` VARCHAR(100) NOT NULL,
                            `QUANTITY` SMALLINT,
                            `PRICE` DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `FACILITY_COMPENSATION` (
                                         `FACILITY_ID` INT,
                                         `INVOICE_DETAIL_ID` INT,
                                         `QUANTITY` SMALLINT,
                                         PRIMARY KEY (`FACILITY_ID`, `INVOICE_DETAIL_ID`),
                                         FOREIGN KEY (`FACILITY_ID`) REFERENCES `FACILITY`(`ID`),
                                         FOREIGN KEY (`INVOICE_DETAIL_ID`) REFERENCES `INVOICE_DETAIL`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `IMPORT_INVOICE` (
                                  `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                  `TOTAL_QUANTITY` SMALLINT,
                                  `TOTAL_COST` DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `IMPORT_INVOICE_DETAIL` (
                                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                         `QUANTITY` SMALLINT,
                                         `COST` DOUBLE,
                                         `FACILITY_ID` INT,
                                         `IMPORT_INVOICE_ID` INT,
                                         FOREIGN KEY (`FACILITY_ID`) REFERENCES `FACILITY`(`ID`),
                                         FOREIGN KEY (`IMPORT_INVOICE_ID`) REFERENCES `IMPORT_INVOICE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `STOCK_REQUISITION_INVOICE` (
                                             `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                             `QUANTITY` SMALLINT,
                                             `ROOM_ID` INT,
                                             FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `STOCK_REQUISITION_DETAIL` (
                                            `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                            `TOTAL_QUANTITY` SMALLINT,
                                            `FACILITY_ID` INT,
                                            `STOCK_REQUISITION_INVOICE_ID` INT,
                                            FOREIGN KEY (`FACILITY_ID`) REFERENCES `FACILITY`(`ID`),
                                            FOREIGN KEY (`STOCK_REQUISITION_INVOICE_ID`) REFERENCES `STOCK_REQUISITION_INVOICE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ROOM_SUPPLY` (
                               `ROOM_ID` INT,
                               `FACILITY_ID` INT,
                               `TOTAL_QUANTITY` TINYINT,
                               `DAMAGED_QUANTITY` TINYINT,
                               PRIMARY KEY (`ROOM_ID`, `FACILITY_ID`),
                               FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`),
                               FOREIGN KEY (`FACILITY_ID`) REFERENCES `FACILITY`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `REVENUE_REPORT` (
                                  `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                  `YEAR` SMALLINT,
                                  `MONTH` TINYINT,
                                  `TOTAL_MONTH_REVENUE` DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `REVENUE_REPORT_DETAIL` (
                                         `ID` INT PRIMARY KEY AUTO_INCREMENT,
                                         `TOTAL_ROOM_REVENUE` DOUBLE,
                                         `REVENUE_REPORT_ID` INT,
                                         `ROOMTYPE_ID` INT,
                                         FOREIGN KEY (`REVENUE_REPORT_ID`) REFERENCES `REVENUE_REPORT`(`ID`),
                                         FOREIGN KEY (`ROOMTYPE_ID`) REFERENCES `ROOMTYPE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data
INSERT INTO `USER_ROLE` (`NAME`) VALUES ('Admin'), ('Receptionist');

INSERT INTO `ACCOUNT` (`USERNAME`, `PASSWORD`, `USER_ROLE_ID`) VALUES
                                                                   ('admin', 'admin123', 1),
                                                                   ('reception', 'reception123', 2);

INSERT INTO `POSITION` (`NAME`, `BASE_SALARY`) VALUES
                                                   ('Manager', 1000.00),
                                                   ('Cleaner', 500.00);

INSERT INTO `STAFF` (`FULL_NAME`, `AGE`, `IDENTIFICATION_NUMBER`, `ADDRESS`, `SEX`, `SALARY_MULTIPLIER`, `POSITION_ID`, `ACCOUNT_ID`) VALUES
                                                                                                                                          ('John Doe', 30, 'ID123456', '123 Main St', 'MALE', 1.5, 1, 1),
                                                                                                                                          ('Jane Smith', 25, 'ID654321', '456 Another St', 'FEMALE', 1.2, 2, 2);

INSERT INTO `PERMISSION` (`NAME`) VALUES
                                      ('View Room'),
                                      ('Manage Room'),
                                      ('Manage Booking');

INSERT INTO `ROLE_PERMISSION` (`USER_ROLE_ID`, `PERMISSION_ID`) VALUES
                                                                    (1, 1),
                                                                    (1, 2),
                                                                    (1, 3),
                                                                    (2, 1),
                                                                    (2, 3);

INSERT INTO `BLOCK` (`NAME`) VALUES
                                 ('A Block'), ('B Block');

INSERT INTO `FLOOR` (`NAME`, `BLOCK_ID`) VALUES
                                             ('1st Floor', 1),
                                             ('2nd Floor', 1),
                                             ('1st Floor', 2);

INSERT INTO `ROOMTYPE` (`NAME`, `PRICE`) VALUES
                                             ('Single', 100.00),
                                             ('Double', 150.00);

INSERT INTO `ROOM` (`NAME`, `PRICE`, `NOTE`, `ROOM_STATE`, `ROOMTYPE_ID`, `FLOOR_ID`) VALUES
                                                                                          ('101', 100.00, 'Near Elevator', 'READY_TO_SERVE', 1, 1),
                                                                                          ('102', 150.00, 'Sea View', 'BOOKED', 2, 2);

INSERT INTO `GUEST` (`NAME`, `SEX`, `AGE`, `IDENTIFICATION_NUMBER`, `ADDRESS`, `PHONE_NUMBER`, `NATIONALITY`) VALUES
    ('Alice', 'FEMALE', 28, 'ID987654', '789 Road', '123456789', 'USA');

INSERT INTO `BOOKING_CONFIRMATION_FORM` (`BOOKING_PERSON_NAME`, `IDENTIFICATION_NUMBER`, `BOOKING_PERSON_AGE`) VALUES
    ('Bob', 'ID111222', 35);

INSERT INTO `RENTAL_FORM` (`ROOM_ID`, `RENTAL_DATE`, `NUMBER_OF_RENTAL_DAY`, `NOTE`) VALUES
    (1, NOW(), 3, 'Business Trip');

INSERT INTO `RENTAL_FORM_DETAIL` (`RENTAL_FORM_ID`, `GUEST_ID`) VALUES
    (1, 1);

INSERT INTO `RENTAL_EXTENSION_FORM` (`RENTAL_FORM_ID`, `NUMBER_OF_RENTAL_DAY`) VALUES
    (1, 2);

INSERT INTO `INVOICE` (`TOTAL_RESERVATION_COST`, `PAYING_GUEST_ID`, `STAFF_ID`) VALUES
    (300.00, 1, 1);

INSERT INTO `INVOICE_DETAIL` (`NUMBER_OF_RENTAL_DAYS`, `INVOICE_ID`, `RESERVATION_COST`, `RENTAL_FORM_ID`) VALUES
    (3, 1, 300.00, 1);

INSERT INTO `FACILITY` (`NAME`, `QUANTITY`, `PRICE`) VALUES
                                                         ('Bed', 50, 500.00),
                                                         ('TV', 20, 300.00);

INSERT INTO `FACILITY_COMPENSATION` (`FACILITY_ID`, `INVOICE_DETAIL_ID`, `QUANTITY`) VALUES
    (1, 1, 1);

INSERT INTO `IMPORT_INVOICE` (`TOTAL_QUANTITY`, `TOTAL_COST`) VALUES
    (100, 10000.00);

INSERT INTO `IMPORT_INVOICE_DETAIL` (`QUANTITY`, `COST`, `FACILITY_ID`, `IMPORT_INVOICE_ID`) VALUES
    (50, 5000.00, 1, 1);

INSERT INTO `STOCK_REQUISITION_INVOICE` (`QUANTITY`, `ROOM_ID`) VALUES
    (10, 1);

INSERT INTO `STOCK_REQUISITION_DETAIL` (`TOTAL_QUANTITY`, `FACILITY_ID`, `STOCK_REQUISITION_INVOICE_ID`) VALUES
    (5, 1, 1);

INSERT INTO `ROOM_SUPPLY` (`ROOM_ID`, `FACILITY_ID`, `TOTAL_QUANTITY`, `DAMAGED_QUANTITY`) VALUES
    (1, 1, 2, 0);

INSERT INTO `REVENUE_REPORT` (`YEAR`, `MONTH`, `TOTAL_MONTH_REVENUE`) VALUES
    (2025, 5, 20000.00);

INSERT INTO `REVENUE_REPORT_DETAIL` (`TOTAL_ROOM_REVENUE`, `REVENUE_REPORT_ID`, `ROOMTYPE_ID`) VALUES
    (15000.00, 1, 1);



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