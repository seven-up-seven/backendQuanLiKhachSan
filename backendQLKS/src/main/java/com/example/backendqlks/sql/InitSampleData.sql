SET NAMES utf8mb4;

CREATE DATABASE IF NOT EXISTS `HotelManagement` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `HotelManagement`;



-- Drop the tables if existed
DROP TABLE IF EXISTS `USER_ROLE_PERMISSION`;
DROP TABLE IF EXISTS `USER_ROLE`;
DROP TABLE IF EXISTS `ACCOUNT`;
DROP TABLE IF EXISTS `STAFF`;
DROP TABLE IF EXISTS `GUEST`;
DROP TABLE IF EXISTS `POSITION`;
DROP TABLE IF EXISTS `PERMISSION`;
DROP TABLE IF EXISTS `ROOM`;
DROP TABLE IF EXISTS `FLOOR`;
DROP TABLE IF EXISTS `BLOCK`;
DROP TABLE IF EXISTS `ROOMTYPE`;
DROP TABLE IF EXISTS `BOOKING_CONFIRMATION_FORM`;
DROP TABLE IF EXISTS `INVOICE_DETAIL`;
DROP TABLE IF EXISTS `INVOICE`;
DROP TABLE IF EXISTS `RENTAL_EXTENSION_FORM`;
DROP TABLE IF EXISTS `RENTAL_FORM_DETAIL`;
DROP TABLE IF EXISTS `RENTAL_FORM`;
DROP TABLE IF EXISTS `REVENUE_REPORT_DETAIL`;
DROP TABLE IF EXISTS `REVENUE_REPORT`;
DROP TABLE IF EXISTS `HISTORY`;
DROP TABLE IF EXISTS `VARIABLE`;

-- Create tables
CREATE TABLE `USER_ROLE` (
                             `ID` INT AUTO_INCREMENT PRIMARY KEY,
                             `NAME` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `PERMISSION` (
                              `ID` INT AUTO_INCREMENT PRIMARY KEY,
                              `NAME` VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `USER_ROLE_PERMISSION` (
                                        `USER_ROLE_ID` INT,
                                        `PERMISSION_ID` INT,
                                        PRIMARY KEY (`USER_ROLE_ID`, `PERMISSION_ID`),
                                        FOREIGN KEY (`USER_ROLE_ID`) REFERENCES `USER_ROLE`(`ID`),
                                        FOREIGN KEY (`PERMISSION_ID`) REFERENCES `PERMISSION`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ACCOUNT` (
                           `ID` INT AUTO_INCREMENT PRIMARY KEY,
                           `USERNAME` VARCHAR(255) NOT NULL,
                           `PASSWORD` VARCHAR(255) NOT NULL,
                           `USER_ROLE_ID` INT,
                           FOREIGN KEY (`USER_ROLE_ID`) REFERENCES `USER_ROLE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `POSITION` (
                            `ID` INT AUTO_INCREMENT PRIMARY KEY,
                            `NAME` VARCHAR(255) NOT NULL,
                            `BASE_SALARY` DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `STAFF` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `FULL_NAME` VARCHAR(255) NOT NULL,
                         `AGE` INT NOT NULL,
                         `IDENTIFICATION_NUMBER` VARCHAR(20) NOT NULL,
                         `ADDRESS` VARCHAR(255) NOT NULL,
                         `SEX` VARCHAR(10),
                         `SALARY_MULTIPLIER` FLOAT,
                         `EMAIL` VARCHAR(100),
                         `POSITION_ID` INT,
                         `ACCOUNT_ID` INT,
                         FOREIGN KEY (`POSITION_ID`) REFERENCES `POSITION`(`ID`),
                         FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `GUEST` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `NAME` VARCHAR(255) NOT NULL,
                         `SEX` VARCHAR(10),
                         `AGE` SMALLINT,
                         `IDENTIFICATION_NUMBER` VARCHAR(20),
                         `PHONE_NUMBER` VARCHAR(20),
                         `EMAIL` VARCHAR(255),
                         `ACCOUNT_ID` INT,
                         FOREIGN KEY (`ACCOUNT_ID`) REFERENCES `ACCOUNT`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `BLOCK` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `NAME` VARCHAR(255) NOT NULL,
                         `POS_X` DOUBLE DEFAULT NULL,
                         `POS_Y` DOUBLE DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `FLOOR` (
                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                         `NAME` VARCHAR(255) NOT NULL,
                         `BLOCK_ID` INT,
                         FOREIGN KEY (`BLOCK_ID`) REFERENCES `BLOCK`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ROOMTYPE` (
                            `ID` INT AUTO_INCREMENT PRIMARY KEY,
                            `NAME` VARCHAR(255) NOT NULL,
                            `PRICE` DOUBLE NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `ROOM` (
                        `ID` INT AUTO_INCREMENT PRIMARY KEY,
                        `NAME` VARCHAR(255) NOT NULL,
                        `NOTE` TEXT,
                        `ROOM_STATE` VARCHAR(50),
                        `ROOMTYPE_ID` INT,
                        `FLOOR_ID` INT,
                        FOREIGN KEY (`ROOMTYPE_ID`) REFERENCES `ROOMTYPE`(`ID`),
                        FOREIGN KEY (`FLOOR_ID`) REFERENCES `FLOOR`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `BOOKING_CONFIRMATION_FORM` (
                                             `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                             `BOOKING_GUEST_ID` INT,
                                             `BOOKING_STATE` VARCHAR(20),
                                             `ROOM_ID` INT,
                                             `BOOKING_DATE` DATETIME,
                                             `RENTAL_DAYS` INT,
                                             `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                             FOREIGN KEY (`BOOKING_GUEST_ID`) REFERENCES `GUEST`(`ID`),
                                             FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `RENTAL_FORM` (
                               `ID` INT AUTO_INCREMENT PRIMARY KEY,
                               `ROOM_ID` INT,
                               `STAFF_ID` INT,
                               `RENTAL_DATE` DATETIME,
                               `IS_PAID_AT` DATETIME,
                               `NUMBER_OF_RENTAL_DAY` SMALLINT,
                               `NOTE` TEXT,
                               `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`),
                               FOREIGN KEY (`STAFF_ID`) REFERENCES `STAFF`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `RENTAL_FORM_DETAIL` (
                                      `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                      `RENTAL_FORM_ID` INT,
                                      `GUEST_ID` INT,
                                      FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`),
                                      FOREIGN KEY (`GUEST_ID`) REFERENCES `GUEST`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `RENTAL_EXTENSION_FORM` (
                                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                         `RENTAL_FORM_ID` INT,
                                         `NUMBER_OF_RENTAL_DAY` SMALLINT,
                                         `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                         `STAFF_ID` INT,
                                         FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`),
                                         FOREIGN KEY (`STAFF_ID`) REFERENCES `STAFF`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `INVOICE` (
                           `ID` INT AUTO_INCREMENT PRIMARY KEY,
                           `TOTAL_RESERVATION_COST` DOUBLE NOT NULL,
                           `PAYING_GUEST_ID` INT,
                           `STAFF_ID` INT,
                           `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           FOREIGN KEY (`PAYING_GUEST_ID`) REFERENCES `GUEST`(`ID`),
                           FOREIGN KEY (`STAFF_ID`) REFERENCES `STAFF`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `INVOICE_DETAIL` (
                                  `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                  `NUMBER_OF_RENTAL_DAYS` INT,
                                  `INVOICE_ID` INT,
                                  `RESERVATION_COST` DOUBLE,
                                  `RENTAL_FORM_ID` INT,
                                  FOREIGN KEY (`INVOICE_ID`) REFERENCES `INVOICE`(`ID`),
                                  FOREIGN KEY (`RENTAL_FORM_ID`) REFERENCES `RENTAL_FORM`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `REVENUE_REPORT` (
                                  `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                  `YEAR` SMALLINT,
                                  `MONTH` TINYINT,
                                  `TOTAL_MONTH_REVENUE` DOUBLE,
                                  `CREATED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `REVENUE_REPORT_DETAIL` (
                                         `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                         `TOTAL_ROOM_REVENUE` DOUBLE,
                                         `REVENUE_REPORT_ID` INT,
                                         `ROOMTYPE_ID` INT,
                                         FOREIGN KEY (`REVENUE_REPORT_ID`) REFERENCES `REVENUE_REPORT`(`ID`),
                                         FOREIGN KEY (`ROOMTYPE_ID`) REFERENCES `ROOMTYPE`(`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `HISTORY` (
                           `ID` INT AUTO_INCREMENT PRIMARY KEY,
                           `IMPACTOR` VARCHAR(255) NOT NULL,
                           `IMPACTOR_ID` INT NOT NULL,
                           `AFFECTED_OBJECT` VARCHAR(255) NOT NULL,
                           `AFFECTED_OBJECT_ID` INT NOT NULL,
                           `ACTION` VARCHAR(20) NOT NULL,
                           `EXECUTE_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                           `CONTENT` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `VARIABLE` (
                            `ID` INT AUTO_INCREMENT PRIMARY KEY,
                            `NAME` VARCHAR(255) NOT NULL,
                            `VALUE` DOUBLE NOT NULL,
                            `DESCRIPTION` TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;



-- Insert sample data into database
INSERT INTO `USER_ROLE` (`NAME`) VALUES
                                     ('SuperAdmin'),
                                     ('Receptionist'),
                                     ('Manager'),
                                     ('Cleaner'),
                                     ('Guest');

INSERT INTO `PERMISSION` (`NAME`) VALUES
                                      ('ADMIN'),
                                      ('RECEPTIONIST'),
                                      ('MANAGER'),
                                      ('GUEST'),
                                      ('ACCOUNTANT');

INSERT INTO `USER_ROLE_PERMISSION` (`USER_ROLE_ID`, `PERMISSION_ID`) VALUES
                                                                         (1, 1),
                                                                         (1, 2),
                                                                         (1, 3),
                                                                         (1, 4),
                                                                         (2, 1),
                                                                         (2, 2),
                                                                         (2, 4),
                                                                         (3, 3),
                                                                         (5, 5),
                                                                         (4, 1);

INSERT INTO `ACCOUNT` (`USERNAME`, `PASSWORD`, `USER_ROLE_ID`) VALUES
                                                                   ('admin1', 'admin123', 1),
                                                                   ('recept1', 'recept123', 2),
                                                                   ('manager1', 'manager123', 3),
                                                                   ('cleaner1', 'cleaner123', 4),
                                                                   ('guest1', 'guest123', 5);

INSERT INTO `POSITION` (`NAME`, `BASE_SALARY`) VALUES
                                                   ('Manager', 1200.0),
                                                   ('Receptionist', 800.0),
                                                   ('Cleaner', 600.0),
                                                   ('Security', 700.0),
                                                   ('Technician', 750.0),
                                                   ('Janitor', 500.0),
                                                   ('Concierge', 850.0),
                                                   ('Event Planner', 1000.0),
                                                   ('IT Support', 900.0),
                                                   ('Maintenance Lead', 950.0);

INSERT INTO `STAFF` (
    `FULL_NAME`, `AGE`, `IDENTIFICATION_NUMBER`, `ADDRESS`, `SEX`, `SALARY_MULTIPLIER`, `EMAIL`, `POSITION_ID`, `ACCOUNT_ID`
) VALUES
      ('John Doe', 30, '111111111111', '123 Street', 'MALE', 1.2, 'john.doe@example.com', 1, 1),
      ('Jane Smith', 25, '222222222222', '456 Avenue', 'FEMALE', 1.1, 'jane.smith@example.com', 2, 2),
      ('Mike Johnson', 40, '333333333333', '789 Boulevard', 'MALE', 1.0, 'mike.johnson@example.com', 3, 3),
      ('Emily Davis', 28, '444444444444', '321 Road', 'FEMALE', 1.0, 'emily.davis@example.com', 4, 4),
      ('Chris Brown', 35, '555555555555', '654 Lane', 'MALE', 1.3, 'chris.brown@example.com', 5, NULL),
      ('Sarah Lee', 31, '666666666777', '12 Hill Street', 'FEMALE', 1.2, 'sarah.lee@example.com', 6, NULL),
      ('Tom Hardy', 38, '777777777888', '17 River Ave', 'MALE', 1.0, 'tom.hardy@example.com', 7, NULL),
      ('Lara Croft', 27, '999999999000', '89 Tomb Rd', 'FEMALE', 1.1, 'lara.croft@example.com', 8, NULL),
      ('Bruce Wayne', 40, '101010101010', '100 Gotham City', 'MALE', 1.3, 'bruce.wayne@example.com', 9, NULL),
      ('Peter Parker', 26, '111222333444', '20 Spider Lane', 'MALE', 1.0, 'peter.parker@example.com', 10, NULL),
      ('Tony Stark', 45, '222333444555', '10880 Malibu Point', 'MALE', 1.5, 'tony.stark@example.com', 5, NULL),
      ('Diana Prince', 34, '333444555666', 'Themyscira Island', 'FEMALE', 1.3, 'diana.prince@example.com', 1, NULL),
      ('Natasha Romanoff', 33, '444555666777', 'Red Room', 'FEMALE', 1.2, 'natasha.romanoff@example.com', 3, NULL),
      ('Clark Kent', 37, '555666777888', 'Metropolis', 'MALE', 1.4, 'clark.kent@example.com', 2, NULL),
      ('Steve Rogers', 39, '666777888999', 'Brooklyn', 'MALE', 1.2, 'steve.rogers@example.com', 4, NULL);


INSERT INTO `GUEST` (`NAME`, `SEX`, `AGE`, `IDENTIFICATION_NUMBER`, `PHONE_NUMBER`, `EMAIL`, `ACCOUNT_ID`) VALUES
                                                                                                               ('Alice', 'FEMALE', 27, '666666666666', '12345678923', 'alice@example.com', 5),
                                                                                                               ('Bob', 'MALE', 32, '777777777777', '15467893412', 'bob@example.com', NULL),
                                                                                                               ('Charlie', 'MALE', 45, '888888888888', '10394581234', 'charlie@example.com', NULL),
                                                                                                               ('Diana', 'FEMALE', 29, '999999999999', '21345676545', 'diana@example.com', NULL),
                                                                                                               ('Eve', 'FEMALE', 24, '123456789123', '45674567431', 'eve@example.com', NULL),
                                                                                                               ('Frank Castle', 'MALE', 38, '100000000001', '1231231234', 'frank@example.com', NULL),
                                                                                                               ('Jessica Jones', 'FEMALE', 31, '100000000002', '2342342345', 'jessica@example.com', NULL),
                                                                                                               ('Luke Cage', 'MALE', 40, '100000000003', '3453453456', 'luke@example.com', NULL),
                                                                                                               ('Matt Murdock', 'MALE', 35, '100000000004', '4564564567', 'matt@example.com', NULL),
                                                                                                               ('Karen Page', 'FEMALE', 29, '100000000005', '5675675678', 'karen@example.com', NULL),
                                                                                                               ('Foggy Nelson', 'MALE', 32, '100000000006', '6786786789', 'foggy@example.com', NULL),
                                                                                                               ('Wanda Maximoff', 'FEMALE', 28, '100000000007', '7897897890', 'wanda@example.com', NULL),
                                                                                                               ('Vision', 'MALE', 33, '100000000008', '8908908901', 'vision@example.com', NULL),
                                                                                                               ('Stephen Strange', 'MALE', 42, '100000000009', '9019019012', 'strange@example.com', NULL),
                                                                                                               ('Pepper Potts', 'FEMALE', 37, '100000000010', '0120120123', 'pepper@example.com', NULL);

INSERT INTO `BLOCK` (`NAME`, `POS_X`, `POS_Y`) VALUES
                                 ('Block A', 100.0, 200.0),
                                 ('Block B', 300.0, 250.0),
                                 ('Block C', 500.0, 210.0),
                                 ('Block D', 700.0, 230.0),
                                 ('Block E', NULL, NULL);

INSERT INTO `FLOOR` (`NAME`, `BLOCK_ID`) VALUES
                                             ('Floor 1', 1),
                                             ('Floor 2', 1),
                                             ('Floor 1', 2),
                                             ('Floor 2', 2),
                                             ('Floor 1', 3),
                                             ('Floor 2', 3),
                                             ('Floor 3', 1),
                                             ('Floor 3', 2),
                                             ('Floor 3', 3),
                                             ('Floor 1', 4);

INSERT INTO `ROOMTYPE` (`NAME`, `PRICE`) VALUES
                                             ('Single', 500.0),
                                             ('Double', 700.0),
                                             ('Suite', 1000.0),
                                             ('Family', 1200.0),
                                             ('Presidential', 2000.0);

INSERT INTO `ROOM` (`NAME`, `NOTE`, `ROOM_STATE`, `ROOMTYPE_ID`, `FLOOR_ID`) VALUES
                                                                                 ('101', 'Sea view', 'READY_TO_SERVE', 1, 1),
                                                                                 ('102', 'City view', 'BOOKED', 2, 1),
                                                                                 ('201', 'Garden view', 'BEING_CLEANED', 3, 2),
                                                                                 ('202', 'Mountain view', 'UNDER_RENOVATION', 4, 2),
                                                                                 ('301', 'Lake view', 'BEING_RENTED', 5, 3),
                                                                                 ('302', 'Near elevator', 'READY_TO_SERVE', 1, 4),
                                                                                 ('303', 'Quiet corner', 'BOOKED', 2, 5),
                                                                                 ('304', 'Family pack', 'READY_TO_SERVE', 4, 6),
                                                                                 ('401', 'Mountain view', 'READY_TO_SERVE', 5, 7),
                                                                                 ('402', 'No window', 'BEING_CLEANED', 1, 8),
                                                                                 ('403', 'Long stay', 'READY_TO_SERVE', 2, 9),
                                                                                 ('404', 'Presidential', 'BOOKED', 5, 10),
                                                                                 ('405', 'Accessible', 'READY_TO_SERVE', 3, 3),
                                                                                 ('406', 'VIP area', 'READY_TO_SERVE', 4, 2),
                                                                                 ('407', 'Balcony room', 'READY_TO_SERVE', 3, 1);

INSERT INTO `BOOKING_CONFIRMATION_FORM`
(`BOOKING_GUEST_ID`, `BOOKING_STATE`, `ROOM_ID`, `BOOKING_DATE`, `RENTAL_DAYS`) VALUES
                                                                                    (1, 'PENDING', 1, '2025-06-20 10:00:00', 2),
                                                                                    (2, 'COMMITED', 2, '2025-06-21 14:30:00', 3),
                                                                                    (3, 'EXPIRED', 3, '2025-06-18 09:00:00', 1),
                                                                                    (4, 'CANCELLED', 4, '2025-06-15 12:15:00', 4),
                                                                                    (5, 'PENDING', 5, '2025-06-22 08:45:00', 2),
                                                                                    (6, 'PENDING', 6, '2025-06-22 10:00:00', 2),
                                                                                    (7, 'COMMITED', 7, '2025-06-23 14:30:00', 3),
                                                                                    (8, 'EXPIRED', 8, '2025-06-19 09:00:00', 1),
                                                                                    (9, 'CANCELLED', 9, '2025-06-16 12:15:00', 4),
                                                                                    (10, 'PENDING', 10, '2025-06-24 08:45:00', 2);

INSERT INTO `RENTAL_FORM` (`ROOM_ID`, `STAFF_ID`, `RENTAL_DATE`, `IS_PAID_AT`, `NUMBER_OF_RENTAL_DAY`, `NOTE`) VALUES
                                                                                                                   (1, 1, NOW(), NOW(), 3, 'No note'),
                                                                                                                   (2, 2, NOW(), NOW(), 2, 'VIP guest'),
                                                                                                                   (3, 3, NOW(), NOW(), 1, 'Needs late checkout'),
                                                                                                                   (4, 4, NOW(), NOW(), 5, 'Birthday booking'),
                                                                                                                   (5, 5, NOW(), NOW(), 7, 'Company event'),
                                                                                                                   (6, 6, NOW(), NOW(), 3, 'Extra towels'),
                                                                                                                   (7, 7, NOW(), NOW(), 2, 'Child seat required'),
                                                                                                                   (8, 8, NOW(), NOW(), 1, 'No special needs'),
                                                                                                                   (9, 9, NOW(), NOW(), 5, 'Group booking'),
                                                                                                                   (10, 10, NOW(), NOW(), 2, 'Business trip');

INSERT INTO `RENTAL_FORM_DETAIL` (`RENTAL_FORM_ID`, `GUEST_ID`) VALUES
                                                                    (1, 1),
                                                                    (2, 2),
                                                                    (3, 3),
                                                                    (4, 4),
                                                                    (5, 5),
                                                                    (6, 6),
                                                                    (7, 7),
                                                                    (8, 8),
                                                                    (9, 9),
                                                                    (10, 10);

INSERT INTO `RENTAL_EXTENSION_FORM` (`RENTAL_FORM_ID`, `NUMBER_OF_RENTAL_DAY`, `STAFF_ID`) VALUES
                                                                                               (1, 2, 1),
                                                                                               (2, 3, 2),
                                                                                               (3, 1, 3),
                                                                                               (4, 2, 4),
                                                                                               (5, 1, 5),
                                                                                               (6, 1, 6),
                                                                                               (7, 2, 7),
                                                                                               (8, 1, 8),
                                                                                               (9, 4, 9),
                                                                                               (10, 1, 10);

INSERT INTO `INVOICE` (`TOTAL_RESERVATION_COST`, `PAYING_GUEST_ID`, `STAFF_ID`) VALUES
                                                                                    (1500.0, 1, 1),
                                                                                    (1400.0, 2, 2),
                                                                                    (1200.0, 3, 3),
                                                                                    (2000.0, 4, 4),
                                                                                    (2500.0, 5, 5),
                                                                                    (1300.0, 6, 6),
                                                                                    (1100.0, 7, 7),
                                                                                    (1700.0, 8, 8),
                                                                                    (1900.0, 9, 9),
                                                                                    (2100.0, 10, 10);

INSERT INTO `INVOICE_DETAIL` (`NUMBER_OF_RENTAL_DAYS`, `INVOICE_ID`, `RESERVATION_COST`, `RENTAL_FORM_ID`) VALUES
                                                                                                               (3, 1, 1500.0, 1),
                                                                                                               (2, 2, 1400.0, 2),
                                                                                                               (1, 3, 1200.0, 3),
                                                                                                               (5, 4, 2000.0, 4),
                                                                                                               (7, 5, 2500.0, 5),
                                                                                                               (3, 6, 1300.0, 6),
                                                                                                               (2, 7, 1100.0, 7),
                                                                                                               (1, 8, 1700.0, 8),
                                                                                                               (5, 9, 1900.0, 9),
                                                                                                               (2, 10, 2100.0, 10);

INSERT INTO `REVENUE_REPORT` (`YEAR`, `MONTH`, `TOTAL_MONTH_REVENUE`) VALUES
                                                                          (2024, 1, 10000.0),
                                                                          (2024, 2, 20000.0),
                                                                          (2024, 3, 15000.0),
                                                                          (2024, 4, 22000.0),
                                                                          (2024, 5, 18000.0),
                                                                          (2024, 6, 25000.0),
                                                                          (2024, 7, 26000.0),
                                                                          (2024, 8, 27000.0),
                                                                          (2024, 9, 28000.0),
                                                                          (2024, 10, 29000.0);

INSERT INTO `REVENUE_REPORT_DETAIL` (`TOTAL_ROOM_REVENUE`, `REVENUE_REPORT_ID`, `ROOMTYPE_ID`) VALUES
                                                                                                   (3000.0, 1, 1),
                                                                                                   (4000.0, 2, 2),
                                                                                                   (3500.0, 3, 3),
                                                                                                   (5000.0, 4, 4),
                                                                                                   (4500.0, 5, 5),
                                                                                                   (5000.0, 6, 1),
                                                                                                   (5200.0, 7, 2),
                                                                                                   (5400.0, 8, 3),
                                                                                                   (5600.0, 9, 4),
                                                                                                   (5800.0, 10, 5);

INSERT INTO `VARIABLE` (`NAME`, `VALUE`, `DESCRIPTION`) VALUES
                                                            ('MAX_EXTENSION_DAY', 10, 'Số ngày gia hạn tối đa'),
                                                            ('MAX_BOOKING_CONFIRMATION_TTL', 0.001388 , 'Thời gian tồn tại tối đa của một phiếu đặt phòng chưa thanh toán');


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