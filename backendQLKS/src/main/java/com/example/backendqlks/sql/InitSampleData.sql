SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `HotelManagement` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `HotelManagement`;

-- Create database tables and insert values into these tables
-- Bảng USER_TYPE
CREATE TABLE USER_TYPE (
                           ID INT AUTO_INCREMENT PRIMARY KEY,
                           NAME VARCHAR(255) NOT NULL,
                           MINIMUM_WAGE DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng ROLE
CREATE TABLE ROLE (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      NAME VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng PERMISSION
CREATE TABLE PERMISSION (
                            ID INT AUTO_INCREMENT PRIMARY KEY,
                            NAME VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng ROLE_PERMISSION (composite key)
CREATE TABLE ROLE_PERMISSION (
                                 ROLE_ID INT NOT NULL,
                                 PERMISSION_ID INT NOT NULL,
                                 PRIMARY KEY (ROLE_ID, PERMISSION_ID),
                                 FOREIGN KEY (ROLE_ID) REFERENCES ROLE(ID),
                                 FOREIGN KEY (PERMISSION_ID) REFERENCES PERMISSION(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng ACCOUNT
CREATE TABLE ACCOUNT (
                         ID INT AUTO_INCREMENT PRIMARY KEY,
                         USERNAME VARCHAR(255) NOT NULL UNIQUE,
                         PASSWORD VARCHAR(255) NOT NULL,
                         ROLE_ID INT NOT NULL,
                         FOREIGN KEY (ROLE_ID) REFERENCES ROLE(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng STAFF
CREATE TABLE STAFF (
                       ID INT AUTO_INCREMENT PRIMARY KEY,
                       FULL_NAME VARCHAR(255) NOT NULL,
                       AGE INT NOT NULL,
                       CCCD VARCHAR(50) NOT NULL UNIQUE,
                       ADDRESS VARCHAR(255) NOT NULL,
                       GENDER BOOLEAN NOT NULL,
                       SALARY_MULTIPLIER DOUBLE NOT NULL,
                       USER_TYPE_ID INT NOT NULL,
                       ACCOUNT_ID INT NOT NULL,
                       FOREIGN KEY (USER_TYPE_ID) REFERENCES USER_TYPE(ID),
                       FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng BLOCK
CREATE TABLE BLOCK (
                       ID INT AUTO_INCREMENT PRIMARY KEY,
                       NAME VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng FLOOR
CREATE TABLE FLOOR (
                       ID INT AUTO_INCREMENT PRIMARY KEY,
                       NAME VARCHAR(255) NOT NULL,
                       BLOCK_ID INT NOT NULL,
                       FOREIGN KEY (BLOCK_ID) REFERENCES BLOCK(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng ROOMTYPE
CREATE TABLE ROOMTYPE (
                          ID INT AUTO_INCREMENT PRIMARY KEY,
                          NAME VARCHAR(255) NOT NULL,
                          PRICE DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng ROOM
CREATE TABLE ROOM (
                      ID INT AUTO_INCREMENT PRIMARY KEY,
                      NAME VARCHAR(255) NOT NULL,
                      PRICE DOUBLE NOT NULL,
                      NOTE VARCHAR(255),
                      ROOM_STATE ENUM('READY_TO_SERVE', 'BOOKED', 'BEING_CLEANED', 'UNDER_RENOVATION') NOT NULL,
                      ROOMTYPE_ID INT NOT NULL,
                      FLOOR_ID INT NOT NULL,
                      FOREIGN KEY (ROOMTYPE_ID) REFERENCES ROOMTYPE(ID),
                      FOREIGN KEY (FLOOR_ID) REFERENCES FLOOR(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng GUEST
CREATE TABLE GUEST (
                       ID INT AUTO_INCREMENT PRIMARY KEY,
                       NAME VARCHAR(255) NOT NULL,
                       SEX ENUM('MALE', 'FEMALE') NOT NULL,
                       AGE TINYINT NOT NULL,
                       IDENTIFICATION_NUMBER VARCHAR(50) NOT NULL,
                       ADDRESS VARCHAR(255),
                       NATIONALITY VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng SUPPLY
CREATE TABLE SUPPLY (
                        ID INT AUTO_INCREMENT PRIMARY KEY,
                        NAME VARCHAR(255) NOT NULL,
                        NUMBER INT NOT NULL,
                        PRICE DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng IMPORT_INVOICE
CREATE TABLE IMPORT_INVOICE (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                TOTAL_NUMBER INT NOT NULL,
                                TOTAL_COST DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng IMPORT_INVOICE_DETAIL
CREATE TABLE IMPORT_INVOICE_DETAIL (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       NUMBER INT NOT NULL,
                                       COST DOUBLE NOT NULL,
                                       SUPPLY_ID INT NOT NULL,
                                       IMPORT_INVOICE_ID INT NOT NULL,
                                       FOREIGN KEY (SUPPLY_ID) REFERENCES SUPPLY(ID),
                                       FOREIGN KEY (IMPORT_INVOICE_ID) REFERENCES IMPORT_INVOICE(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng ROOMS_SUPPLY (composite key)
CREATE TABLE ROOM_SUPPLY (
                             ROOM_ID INT NOT NULL,
                             SUPPLY_ID INT NOT NULL,
                             TOTAL_NUMBER INT NOT NULL,
                             DAMAGED_NUMBER INT NOT NULL,
                             PRIMARY KEY (ROOM_ID, SUPPLY_ID),
                             FOREIGN KEY (ROOM_ID) REFERENCES ROOM(ID),
                             FOREIGN KEY (SUPPLY_ID) REFERENCES SUPPLY(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng BOOKING_CONFIRMATION_FORM
CREATE TABLE BOOKING_CONFIRMATION_FORM (
                                           ID INT AUTO_INCREMENT PRIMARY KEY,
                                           BOOKING_PERSON_NAME VARCHAR(255) NOT NULL,
                                           IDENTIFICATION_NUMBER VARCHAR(50) NOT NULL,
                                           BOOKING_PERSON_AGE TINYINT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng RENTAL_FORM
CREATE TABLE RENTAL_FORM (
                             ID INT AUTO_INCREMENT PRIMARY KEY,
                             ROOM_ID INT NOT NULL,
                             RENTAL_DATE DATETIME NOT NULL,
                             NUMBER_OF_RENTAL_DAY INT NOT NULL,
                             NOTE VARCHAR(255),
                             FOREIGN KEY (ROOM_ID) REFERENCES ROOM(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng RENTAL_FORM_DETAIL
CREATE TABLE RENTAL_FORM_DETAIL (
                                    ID INT AUTO_INCREMENT PRIMARY KEY,
                                    RENTAL_FORM_ID INT NOT NULL,
                                    GUEST_ID INT NOT NULL,
                                    FOREIGN KEY (RENTAL_FORM_ID) REFERENCES RENTAL_FORM(ID),
                                    FOREIGN KEY (GUEST_ID) REFERENCES GUEST(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng RENTAL_EXTENSION_FORM
CREATE TABLE RENTAL_EXTENSION_FORM (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       RENTAL_FORM_ID INT NOT NULL,
                                       NUMBER_OF_RENTAL_DAY INT NOT NULL,
                                       FOREIGN KEY (RENTAL_FORM_ID) REFERENCES RENTAL_FORM(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng INVOICE
CREATE TABLE INVOICE (
                         ID INT AUTO_INCREMENT PRIMARY KEY,
                         TOTAL_INVOICES_VALUE DOUBLE NOT NULL,
                         PAYING_GUEST_ID INT NOT NULL,
                         STAFF_ID INT NOT NULL,
                         FOREIGN KEY (PAYING_GUEST_ID) REFERENCES GUEST(ID)
    -- FOREIGN KEY (STAFF_ID) REFERENCES STAFF(ID) -- Chưa hoàn chỉnh entity
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng INVOICE_DETAIL
CREATE TABLE INVOICE_DETAIL (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                NUMBER_OF_RENTAL_DAYS INT NOT NULL,
                                INVOICE_ID INT NOT NULL,
                                INVOICE_VALUE DOUBLE NOT NULL,
                                RENTAL_FORM_ID INT NOT NULL,
                                FOREIGN KEY (INVOICE_ID) REFERENCES INVOICE(ID),
                                FOREIGN KEY (RENTAL_FORM_ID) REFERENCES RENTAL_FORM(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng REVENUE_REPORT
CREATE TABLE REVENUE_REPORT (
                                ID INT AUTO_INCREMENT PRIMARY KEY,
                                YEAR TINYINT NOT NULL,
                                MONTH TINYINT NOT NULL,
                                TOTAL_MONTH_REVENUE DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng REVENUE_REPORT_DETAIL
CREATE TABLE REVENUE_REPORT_DETAIL (
                                       ID INT AUTO_INCREMENT PRIMARY KEY,
                                       TOTAL_ROOM_REVENUE DOUBLE NOT NULL,
                                       REVENUE_REPORT_ID INT NOT NULL,
                                       ROOMTYPE_ID INT NOT NULL,
                                       FOREIGN KEY (REVENUE_REPORT_ID) REFERENCES REVENUE_REPORT(ID),
                                       FOREIGN KEY (ROOMTYPE_ID) REFERENCES ROOMTYPE(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng FACILITY_COMPENSATION (composite key)
CREATE TABLE FACILITY_COMPENSATION (
                                       FACILITY_ID INT NOT NULL,
                                       INVOICE_DETAIL_ID INT NOT NULL,
                                       QUANTITY INT NOT NULL,
                                       PRIMARY KEY (FACILITY_ID, INVOICE_DETAIL_ID)
    -- Chưa đủ thông tin relationship
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng STOCK_REQUISITION_INVOICE
CREATE TABLE STOCK_REQUISITION_INVOICE (
                                           ID INT AUTO_INCREMENT PRIMARY KEY,
                                           NUMBER INT NOT NULL,
                                           ROOM_ID INT NOT NULL,
                                           FOREIGN KEY (ROOM_ID) REFERENCES ROOM(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Bảng STOCK_REQUISITION_INVOICE_DETAIL
CREATE TABLE STOCK_REQUISITION_DETAIL (
                                          ID INT AUTO_INCREMENT PRIMARY KEY,
                                          TOTAL_NUMBER INT NOT NULL,
                                          SUPPLY_ID INT NOT NULL,
                                          STOCK_REQUISITION_INVOICE_ID INT NOT NULL,
                                          FOREIGN KEY (SUPPLY_ID) REFERENCES SUPPLY(ID),
                                          FOREIGN KEY (STOCK_REQUISITION_INVOICE_ID) REFERENCES STOCK_REQUISITION_INVOICE(ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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