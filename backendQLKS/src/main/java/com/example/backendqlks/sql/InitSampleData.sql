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
DROP TABLE IF EXISTS `IMAGE_ENTITY`;
DROP TABLE IF EXISTS `REVIEW`;
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

CREATE TABLE `IMAGE_ENTITY` (
                                `ID` INT AUTO_INCREMENT PRIMARY KEY,
                                `URL` VARCHAR(1000),
                                `FILE_NAME` VARCHAR(255),
                                `ROOM_ID` INT,
                                `UPLOADED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `REVIEW` (
                          `ID` INT AUTO_INCREMENT PRIMARY KEY,
                          `ROOM_ID` INT NOT NULL,
                          `GUEST_ID` INT NOT NULL, -- hoặc GUEST_ID nếu có bảng khách
                          `RATING` INT CHECK (RATING >= 1 AND RATING <= 5),
                          `COMMENT` TEXT,
                          `REVIEWED_AT` DATETIME DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (`ROOM_ID`) REFERENCES `ROOM`(`ID`) ON DELETE CASCADE,
                          FOREIGN KEY (`GUEST_ID`) REFERENCES `GUEST`(`ID`) ON DELETE CASCADE
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
                                     ('BigReceptionist'),
                                     ('Manager'),
                                     ('Cleaner'),
                                     ('Accountant'),
                                     ('NormalGuest'),
                                     ('Admin'),
                                     ('NormalReceptionist');

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
                                                                         (1, 5),
                                                                         (2, 1),
                                                                         (2, 2),
                                                                         (2, 4),
                                                                         (3, 3),
                                                                         (5, 5),
                                                                         (6, 4),
                                                                         (7, 1),
                                                                         (8, 2),
                                                                         (4, 1);

INSERT INTO `ACCOUNT` (`USERNAME`, `PASSWORD`, `USER_ROLE_ID`) VALUES
                                                                   ('admin1', 'admin123', 1),
                                                                   ('recept1', 'recept123', 2),
                                                                   ('manager1', 'manager123', 3),
                                                                   ('cleaner1', 'cleaner123', 4),
                                                                   ('guest1', 'guest123', 6);

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
                                                                                                               ('Alice', 'FEMALE', 27, '666666666666', '12345678923', 'nnguyenminhquang786@gmail.com', 5),
                                                                                                               ('Bob', 'MALE', 32, '777777777777', '15467893412', 'rinntrann05@gmail.com', NULL),
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

INSERT INTO `ROOM` (`NAME`,`NOTE`,`ROOM_STATE`,`ROOMTYPE_ID`,`FLOOR_ID`) VALUES
                                                                             ('101','Pet friendly','READY_TO_SERVE',1,1),
                                                                             ('102','City view','BOOKED',2,1),
                                                                             ('201','Garden view','BEING_CLEANED',3,2),
                                                                             ('202','Mountain view','UNDER_RENOVATION',4,2),
                                                                             ('301','Lake view','BEING_RENTED',5,3),
                                                                             ('302','Near elevator','READY_TO_SERVE',1,4),
                                                                             ('303','Quiet corner','BOOKED',2,5),
                                                                             ('304','Family pack','READY_TO_SERVE',4,6),
                                                                             ('401','Mountain view','READY_TO_SERVE',5,7),
                                                                             ('402','No window','BEING_CLEANED',1,8),
                                                                             ('403','Long stay','READY_TO_SERVE',2,9),
                                                                             ('404','Presidential','BOOKED',5,10),
                                                                             ('405','Accessible','READY_TO_SERVE',3,3),
                                                                             ('406','VIP area','READY_TO_SERVE',4,2),
                                                                             ('407','Balcony room','READY_TO_SERVE',3,1),
                                                                             ('408','Oceanfront','READY_TO_SERVE',2,1),
                                                                             ('409','Pool view','READY_TO_SERVE',3,2),
                                                                             ('501','Skyline view','READY_TO_SERVE',1,3),
                                                                             ('502','Corner suite','READY_TO_SERVE',4,4),
                                                                             ('503','Deluxe room','READY_TO_SERVE',5,5);

SET @payBaseMay = '2025-05-30 12:00:00';

INSERT INTO `BOOKING_CONFIRMATION_FORM`
(`BOOKING_GUEST_ID`,`BOOKING_STATE`,`ROOM_ID`,`BOOKING_DATE`,`RENTAL_DAYS`) VALUES
                                                                                #thang 6
                                                                                (1,'PENDING',1,'2025-06-20 10:00:00',2),
                                                                                (2,'COMMITED',2,'2025-06-21 14:30:00',3),
                                                                                (3,'EXPIRED',3,'2025-06-18 09:00:00',1),
                                                                                (4,'CANCELLED',4,'2025-06-15 12:15:00',4),
                                                                                (5,'PENDING',5,'2025-06-22 08:45:00',2),
                                                                                (6,'PENDING',6,'2025-06-22 10:00:00',2),
                                                                                (7,'COMMITED',7,'2025-06-23 14:30:00',3),
                                                                                (8,'EXPIRED',8,'2025-06-19 09:00:00',1),
                                                                                (9,'CANCELLED',9,'2025-06-16 12:15:00',4),
                                                                                (10,'PENDING',10,'2025-06-24 08:45:00',2),

                                                                                #thang 5
                                                                                (1,'PENDING',1,'2025-05-20 10:00:00',2),
                                                                                (2,'COMMITED',2,'2025-05-21 14:30:00',3),
                                                                                (3,'EXPIRED',3,'2025-05-18 09:00:00',1),
                                                                                (4,'CANCELLED',4,'2025-05-15 12:15:00',4),
                                                                                (5,'PENDING',5,'2025-05-22 08:45:00',2),
                                                                                (6,'PENDING',6,'2025-05-22 10:00:00',2),
                                                                                (7,'COMMITED',7,'2025-05-23 14:30:00',3),
                                                                                (8,'EXPIRED',8,'2025-05-19 09:00:00',1),
                                                                                (9,'CANCELLED',9,'2025-05-16 12:15:00',4),
                                                                                (10,'PENDING',10,'2025-05-24 08:45:00',2);

INSERT INTO `RENTAL_FORM`
(`ROOM_ID`,`STAFF_ID`,`RENTAL_DATE`,`IS_PAID_AT`,`NUMBER_OF_RENTAL_DAY`,`NOTE`) VALUES
                                                                                    #thang 6
                                                                                    (1,1,DATE_SUB(NOW(), INTERVAL 3 DAY),NOW(),3,'No note'),
                                                                                    (2,2,DATE_SUB(NOW(), INTERVAL 3 DAY),DATE_SUB(NOW(), INTERVAL 1 DAY),2,'VIP guest'),
                                                                                    (3,3,DATE_SUB(NOW(), INTERVAL 1 DAY),NOW(),1,'Needs late checkout'),
                                                                                    (4,4,DATE_SUB(NOW(), INTERVAL 7 DAY),DATE_SUB(NOW(), INTERVAL 2 DAY),5,'Birthday booking'),
                                                                                    (5,5,DATE_SUB(NOW(), INTERVAL 10 DAY),DATE_SUB(NOW(), INTERVAL 3 DAY),7,'Company event'),
                                                                                    (6,6,DATE_SUB(NOW(), INTERVAL 8 DAY),DATE_SUB(NOW(), INTERVAL 5 DAY),3,'Extra towels'),
                                                                                    (7,7,DATE_SUB(NOW(), INTERVAL 2 DAY),NOW(),2,'Child seat required'),
                                                                                    (8,8,DATE_SUB(NOW(), INTERVAL 4 DAY),DATE_SUB(NOW(), INTERVAL 3 DAY),1,'No special needs'),
                                                                                    (9,9,DATE_SUB(NOW(), INTERVAL 8 DAY),DATE_SUB(NOW(), INTERVAL 3 DAY),5,'Group booking'),
                                                                                    (10,10,DATE_SUB(NOW(), INTERVAL 2 DAY),NOW(),2,'Business trip'),

                                                                                    #thang 5
                                                                                    ( 1,  1, DATE_SUB(@payBaseMay, INTERVAL 3 DAY), @payBaseMay, 3, 'No note'),
                                                                                    ( 2,  2, DATE_SUB(@payBaseMay, INTERVAL 2 DAY), @payBaseMay, 2, 'VIP guest'),
                                                                                    ( 3,  3, DATE_SUB(@payBaseMay, INTERVAL 1 DAY), @payBaseMay, 1, 'Needs late checkout'),
                                                                                    ( 4,  4, DATE_SUB(@payBaseMay, INTERVAL 5 DAY), @payBaseMay, 5, 'Birthday booking'),
                                                                                    ( 5,  5, DATE_SUB(@payBaseMay, INTERVAL 7 DAY), @payBaseMay, 7, 'Company event'),
                                                                                    ( 6,  6, DATE_SUB(@payBaseMay, INTERVAL 3 DAY), @payBaseMay, 3, 'Extra towels'),
                                                                                    ( 7,  7, DATE_SUB(@payBaseMay, INTERVAL 2 DAY), @payBaseMay, 2, 'Child seat required'),
                                                                                    ( 8,  8, DATE_SUB(@payBaseMay, INTERVAL 1 DAY), @payBaseMay, 1, 'No special needs'),
                                                                                    ( 9,  9, DATE_SUB(@payBaseMay, INTERVAL 5 DAY), @payBaseMay, 5, 'Group booking'),
                                                                                    (10, 10, DATE_SUB(@payBaseMay, INTERVAL 2 DAY), @payBaseMay, 2, 'Business trip');

INSERT INTO `RENTAL_FORM_DETAIL` (`RENTAL_FORM_ID`,`GUEST_ID`) VALUES
                                                                   #thang 6
                                                                   (1,1),
                                                                   (2,2),
                                                                   (3,3),
                                                                   (4,4),
                                                                   (5,5),
                                                                   (6,6),
                                                                   (7,7),
                                                                   (8,8),
                                                                   (9,9),
                                                                   (10,10),

                                                                   #thang 5
                                                                   (11,1),
                                                                   (12,2),
                                                                   (13,3),
                                                                   (14,4),
                                                                   (15,5),
                                                                   (16,6),
                                                                   (17,7),
                                                                   (18,8),
                                                                   (19,9),
                                                                   (20,10);


INSERT INTO `RENTAL_EXTENSION_FORM` (`RENTAL_FORM_ID`,`NUMBER_OF_RENTAL_DAY`,`STAFF_ID`) VALUES
                                                                                             #thang 6
                                                                                             (1,2,1),
                                                                                             (2,3,2),
                                                                                             (3,1,3),
                                                                                             (4,2,4),
                                                                                             (5,1,5),
                                                                                             (6,1,6),
                                                                                             (7,2,7),
                                                                                             (8,1,8),
                                                                                             (9,4,9),
                                                                                             (10,1,10),

                                                                                             #thang 5
                                                                                             (11,2,1),
                                                                                             (12,3,2),
                                                                                             (13,1,3),
                                                                                             (14,2,4),
                                                                                             (15,1,5),
                                                                                             (16,1,6),
                                                                                             (17,2,7),
                                                                                             (18,1,8),
                                                                                             (19,4,9),
                                                                                             (20,1,10);

INSERT INTO `INVOICE` (`TOTAL_RESERVATION_COST`,`PAYING_GUEST_ID`,`STAFF_ID`) VALUES
                                                                                  #thang 6
                                                                                  (2500.0,1,1),
                                                                                  (3500.0,2,2),
                                                                                  (2000.0,3,3),
                                                                                  (8400.0,4,4),
                                                                                  (16000.0,5,5),
                                                                                  (2000.0,6,6),
                                                                                  (2800.0,7,7),
                                                                                  (2400.0,8,8),
                                                                                  (9000.0,9,9),
                                                                                  (6000.0,10,10),

                                                                                  #thang 5
                                                                                  (2500.0,1,1),
                                                                                  (3500.0,2,2),
                                                                                  (2000.0,3,3),
                                                                                  (8400.0,4,4),
                                                                                  (16000.0,5,5),
                                                                                  (2000.0,6,6),
                                                                                  (2800.0,7,7),
                                                                                  (2400.0,8,8),
                                                                                  (9000.0,9,9),
                                                                                  (6000.0,10,10);

INSERT INTO `INVOICE_DETAIL`
(`NUMBER_OF_RENTAL_DAYS`,`INVOICE_ID`,`RESERVATION_COST`,`RENTAL_FORM_ID`) VALUES
                                                                               #thang 6
                                                                               (5,1,2500.0,1),
                                                                               (5,2,3500.0,2),
                                                                               (2,3,2000.0,3),
                                                                               (7,4,8400.0,4),
                                                                               (8,5,16000.0,5),
                                                                               (4,6,2000.0,6),
                                                                               (4,7,2800.0,7),
                                                                               (2,8,2400.0,8),
                                                                               (9,9,9000.0,9),
                                                                               (3,10,6000.0,10),

                                                                               #thang 5
                                                                               (5,11,2500.0,11),
                                                                               (5,12,3500.0,12),
                                                                               (2,13,2000.0,13),
                                                                               (7,14,8400.0,14),
                                                                               (8,15,16000.0,15),
                                                                               (4,16,2000.0,16),
                                                                               (4,17,2800.0,17),
                                                                               (2,18,2400.0,18),
                                                                               (9,19,9000.0,19),
                                                                               (3,20,6000.0,20);

INSERT INTO `REVENUE_REPORT` (`YEAR`,`MONTH`,`TOTAL_MONTH_REVENUE`) VALUES
    (2025,6,54600.0),
    (2025,5,54600.0);

INSERT INTO `REVENUE_REPORT_DETAIL`
(`TOTAL_ROOM_REVENUE`,`REVENUE_REPORT_ID`,`ROOMTYPE_ID`) VALUES
                                                             #thang 6
                                                             (4500.0, (SELECT ID FROM `REVENUE_REPORT` WHERE YEAR=2025 AND MONTH=6), 1),
                                                             (6300.0, (SELECT ID FROM `REVENUE_REPORT` WHERE YEAR=2025 AND MONTH=6), 2),
                                                             (11000.0,(SELECT ID FROM `REVENUE_REPORT` WHERE YEAR=2025 AND MONTH=6), 3),
                                                             (10800.0,(SELECT ID FROM `REVENUE_REPORT` WHERE YEAR=2025 AND MONTH=6), 4),
                                                             (22000.0,(SELECT ID FROM `REVENUE_REPORT` WHERE YEAR=2025 AND MONTH=6), 5),

                                                             #thang 5
                                                             (4500.0, (SELECT `ID` FROM `REVENUE_REPORT` WHERE `YEAR`=2025 AND `MONTH`=5), 1),
                                                             (6300.0, (SELECT `ID` FROM `REVENUE_REPORT` WHERE `YEAR`=2025 AND `MONTH`=5), 2),
                                                             (11000.0,(SELECT `ID` FROM `REVENUE_REPORT` WHERE `YEAR`=2025 AND `MONTH`=5), 3),
                                                             (10800.0,(SELECT `ID` FROM `REVENUE_REPORT` WHERE `YEAR`=2025 AND `MONTH`=5), 4),
                                                             (22000.0,(SELECT `ID` FROM `REVENUE_REPORT` WHERE `YEAR`=2025 AND `MONTH`=5), 5);

INSERT INTO `VARIABLE` (`NAME`, `VALUE`, `DESCRIPTION`) VALUES
                                                            ('MAX_EXTENSION_DAY', 10, 'Số ngày gia hạn tối đa'),
                                                            ('MAX_BOOKING_CONFIRMATION_TTL', 0.001388 , 'Thời gian tồn tại tối đa của một phiếu đặt phòng chưa thanh toán');

INSERT INTO IMAGE_ENTITY (URL, FILE_NAME, ROOM_ID) VALUES
-- Room 1
('https://xuonggooccho.com/ckfinder/userfiles/files/anh-phong-ngu.jpg', 'anh-phong-ngu.jpg', 1),
('https://noithattrevietnam.com/uploaded/2019/12/1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', '1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', 1),
('https://acihome.vn/uploads/17/phong-ngu-khach-san-5-sao.jpg', 'phong-ngu-khach-san-5-sao.jpg', 1),
('https://www.vietnambooking.com/wp-content/uploads/2021/02/khach-san-ho-chi-minh-35.jpg', 'khach-san-ho-chi-minh-35.jpg', 1),
('https://ik.imagekit.io/tvlk/blog/2023/09/khach-san-view-bien-da-nang-1.jpg', 'khach-san-view-bien-da-nang-1.jpg', 1),
-- Room 2
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266362/qsj8vz0bptxfirwamtx5.png', 'qsj8vz0bptxfirwamtx5.png', 2),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qजरियाद्वार.png', 2),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 2),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 2),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 2),
-- Room 3
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 3),
('https://acihome.vn/uploads/15/mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 'mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 3),
('https://duopig.com/wp-content/uploads/2021/03/Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 'Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 3),
('https://images2.thanhnien.vn/528068263637045248/2023/9/11/biden-12-1694407393696398570440.jpg', 'biden-12-1694407393696398570440.jpg', 3),
('https://khachsandep.vn/storage/files/0%200%20%20bi%20quyet%20thiet%20ke%20homestay%20dep/0%20tieu%20chuan%20thiet%20ke%20phong%20tong%20thong/anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 'anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 3),
-- Room 4
('https://images2.thanhnien.vn/528068263637045248/2023/9/12/the-reverie-2-1694491501124271243870.jpg', 'the-reverie-2-1694491501124271243870.jpg', 4),
('https://xuonggooccho.com/ckfinder/userfiles/files/anh-phong-ngu.jpg', 'anh-phong-ngu.jpg', 4),
('https://noithattrevietnam.com/uploaded/2019/12/1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', '1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', 4),
('https://acihome.vn/uploads/17/phong-ngu-khach-san-5-sao.jpg', 'phong-ngu-khach-san-5-sao.jpg', 4),
('https://www.vietnambooking.com/wp-content/uploads/2021/02/khach-san-ho-chi-minh-35.jpg', 'khach-san-ho-chi-minh-35.jpg', 4),
-- Room 5
('https://ik.imagekit.io/tvlk/blog/2023/09/khach-san-view-bien-da-nang-1.jpg', 'khach-san-view-bien-da-nang-1.jpg', 5),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266362/qsj8vz0bptxfirwamtx5.png', 'qsj8vz0bptxfirwamtx5.png', 5),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 5),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 5),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 5),
-- Room 6
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 6),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 6),
('https://acihome.vn/uploads/15/mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 'mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 6),
('https://duopig.com/wp-content/uploads/2021/03/Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 'Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 6),
('https://images2.thanhnien.vn/528068263637045248/2023/9/11/biden-12-1694407393696398570440.jpg', 'biden-12-1694407393696398570440.jpg', 6),
-- Room 7
('https://khachsandep.vn/storage/files/0%200%20%20bi%20quyet%20thiet%20ke%20homestay%20dep/0%20tieu%20chuan%20thiet%20ke%20phong%20tong%20thong/anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 'anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 7),
('https://images2.thanhnien.vn/528068263637045248/2023/9/12/the-reverie-2-1694491501124271243870.jpg', 'the-reverie-2-1694491501124271243870.jpg', 7),
('https://xuonggooccho.com/ckfinder/userfiles/files/anh-phong-ngu.jpg', 'anh-phong-ngu.jpg', 7),
('https://noithattrevietnam.com/uploaded/2019/12/1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', '1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', 7),
('https://acihome.vn/uploads/17/phong-ngu-khach-san-5-sao.jpg', 'phong-ngu-khach-san-5-sao.jpg', 7),
-- Room 8
('https://www.vietnambooking.com/wp-content/uploads/2021/02/khach-san-ho-chi-minh-35.jpg', 'khach-san-ho-chi-minh-35.jpg', 8),
('https://ik.imagekit.io/tvlk/blog/2023/09/khach-san-view-bien-da-nang-1.jpg', 'khach-san-view-bien-da-nang-1.jpg', 8),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266362/qsj8vz0bptxfirwamtx5.png', 'qsj8vz0bptxfirwamtx5.png', 8),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 8),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 8),
-- Room 9
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 9),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 9),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 9),
('https://acihome.vn/uploads/15/mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 'mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 9),
('https://duopig.com/wp-content/uploads/2021/03/Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 'Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 9),
-- Room 10
('https://images2.thanhnien.vn/528068263637045248/2023/9/11/biden-12-1694407393696398570440.jpg', 'biden-12-1694407393696398570440.jpg', 10),
('https://khachsandep.vn/storage/files/0%200%20%20bi%20quyet%20thiet%20ke%20homestay%20dep/0%20tieu%20chuan%20thiet%20ke%20phong%20tong%20thong/anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 'anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 10),
('https://images2.thanhnien.vn/528068263637045248/2023/9/12/the-reverie-2-1694491501124271243870.jpg', 'the-reverie-2-1694491501124271243870.jpg', 10),
('https://xuonggooccho.com/ckfinder/userfiles/files/anh-phong-ngu.jpg', 'anh-phong-ngu.jpg', 10),
('https://noithattrevietnam.com/uploaded/2019/12/1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', '1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', 10),
-- Room 11
('https://acihome.vn/uploads/17/phong-ngu-khach-san-5-sao.jpg', 'phong-ngu-khach-san-5-sao.jpg', 11),
('https://www.vietnambooking.com/wp-content/uploads/2021/02/khach-san-ho-chi-minh-35.jpg', 'khach-san-ho-chi-minh-35.jpg', 11),
('https://ik.imagekit.io/tvlk/blog/2023/09/khach-san-view-bien-da-nang-1.jpg', 'khach-san-view-bien-da-nang-1.jpg', 11),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266362/qsj8vz0bptxfirwamtx5.png', 'qsj8vz0bptxfirwamtx5.png', 11),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 11),
-- Room 12
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 12),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 12),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 12),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 12),
('https://acihome.vn/uploads/15/mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 'mau-thiet-ke-noi-that-phong-2-giuong-don-ben-trong-khach-san-3-4-5-sao-4.jpg', 12),
-- Room 13
('https://duopig.com/wp-content/uploads/2021/03/Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 'Ch%E1%BB%A5p-%E1%BA%A3nh-resort-ch%E1%BB%A5p-%E1%BA%A3nh-nh%C3%A0-h%C3%A0ng-ch%E1%BB%A5p-%E1%BA%A3nh-kh%C3%A1ch-s%E1%BA%A1n-108-copy.jpg', 13),
('https://images2.thanhnien.vn/528068263637045248/2023/9/11/biden-12-1694407393696398570440.jpg', 'biden-12-1694407393696398570440.jpg', 13),
('https://khachsandep.vn/storage/files/0%200%20%20bi%20quyet%20thiet%20ke%20homestay%20dep/0%20tieu%20chuan%20thiet%20ke%20phong%20tong%20thong/anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 'anh-bia-tieu-chuan-thiet-ke-phong-tong-thong.jpg', 13),
('https://images2.thanhnien.vn/528068263637045248/2023/9/12/the-reverie-2-1694491501124271243870.jpg', 'the-reverie-2-1694491501124271243870.jpg', 13),
('https://xuonggooccho.com/ckfinder/userfiles/files/anh-phong-ngu.jpg', 'anh-phong-ngu.jpg', 13),
-- Room 14
('https://noithattrevietnam.com/uploaded/2019/12/1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', '1-thiet-ke-phong-ngu-khach-san%20%281%29.jpg', 14),
('https://acihome.vn/uploads/17/phong-ngu-khach-san-5-sao.jpg', 'phong-ngu-khach-san-5-sao.jpg', 14),
('https://www.vietnambooking.com/wp-content/uploads/2021/02/khach-san-ho-chi-minh-35.jpg', 'khach-san-ho-chi-minh-35.jpg', 14),
('https://ik.imagekit.io/tvlk/blog/2023/09/khach-san-view-bien-da-nang-1.jpg', 'khach-san-view-bien-da-nang-1.jpg', 14),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266362/qsj8vz0bptxfirwamtx5.png', 'qsj8vz0bptxfirwamtx5.png', 14),
-- Room 15
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 15),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 15),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 15),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 15),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 15),

('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 16),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 16),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 16),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 16),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 16),

('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 17),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 17),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 17),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 17),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 17),

('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 18),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 18),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 18),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 18),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 18),

('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 19),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 19),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 19),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 19),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 19),

('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266321/w05jzxrqfwb35qjg5p13.png', 'w05jzxrqfwb35qjg5p13.png', 20),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266245/erovkf0owfbai9h8jkzq.png', 'erovkf0owfbai9h8jkzq.png', 20),
('https://res.cloudinary.com/djbvf02yt/image/upload/v1744266199/s6xhgewuv9sf3c1jnlik.png', 's6xhgewuv9sf3c1jnlik.png', 20),
('https://watermark.lovepik.com/photo/20211130/large/lovepik-grand-bed-room-of-superior-hotel-picture_501221020.jpg', '501221020.jpg', 20),
('https://saigontourist.com.vn/files/images/luu-tru/luu-tru-mien-nam/hotel-grand-saigon-2.jpg', 'hotel-grand-saigon-2.jpg', 20);

INSERT INTO REVIEW (ROOM_ID, GUEST_ID, RATING, COMMENT) VALUES
(1, 1, 5, 'Phòng 101 có view biển tuyệt đẹp, không gian sạch sẽ và thoáng mát. Tiện nghi đầy đủ, giường ngủ êm ái, nhân viên phục vụ rất nhiệt tình. Tôi đặc biệt thích ban công nhỏ để ngắm bình minh. Trạng thái phòng READY_TO_SERVE đúng như kỳ vọng, mọi thứ đều sẵn sàng khi tôi check-in. Chắc chắn sẽ quay lại lần sau!'),
(2, 3, 4, 'Phòng 102 có view thành phố khá đẹp, đặc biệt vào ban đêm. Phòng được chuẩn bị kỹ lưỡng dù trạng thái là BOOKED. Nội thất hiện đại, wifi mạnh, nhưng điều hòa hơi ồn một chút. Nhân viên hỗ trợ nhanh chóng khi tôi yêu cầu thêm gối. Tổng thể là một trải nghiệm tốt, đáng giá tiền.'),
(3, 5, 3, 'Phòng 201 có view vườn dễ chịu, nhưng đang trong trạng thái BEING_CLEANED khi tôi đến nên phải chờ một chút. Nội thất hơi cũ, cần cải thiện. Điểm cộng là nhân viên rất thân thiện và xin lỗi vì sự bất tiện. Bình luận này dành cho những ai thích không gian yên tĩnh gần thiên nhiên.'),
(4, 7, 2, 'Phòng 202 đang UNDER_RENOVATION nên không phù hợp để ở thời điểm này. Mặc dù view núi rất đẹp, nhưng mùi sơn và tiếng ồn từ việc sửa chữa làm tôi khó chịu. Nhân viên đã cố gắng hỗ trợ, nhưng tôi nghĩ khách sạn nên thông báo rõ ràng hơn trước khi cho đặt phòng này.'),
(5, 9, 5, 'Phòng 301 có view hồ tuyệt vời, không gian sang trọng đúng chuẩn BEING_RENTED. Tiện nghi cao cấp, từ bồn tắm đến đồ dùng trong phòng đều hoàn hảo. Nhân viên phục vụ chu đáo, luôn hỏi thăm nhu cầu của tôi. Tôi rất hài lòng và sẽ giới thiệu cho bạn bè.'),
(6, 11, 4, 'Phòng 302 gần thang máy nên rất tiện, trạng thái READY_TO_SERVE đúng như mô tả. Phòng sạch sẽ, gọn gàng, nhưng cách âm hơi kém, nghe tiếng bước chân ngoài hành lang. View không đặc biệt, nhưng giá cả hợp lý. Nhân viên hỗ trợ nhanh khi tôi cần thêm nước uống.'),
(7, 13, 3, 'Phòng 303 ở góc yên tĩnh, đúng như mô tả "Quiet corner", nhưng trạng thái BOOKED nên tôi phải check-in muộn. Nội thất ổn, nhưng đèn ngủ bị hỏng, phải báo nhân viên sửa. Bình luận này dành cho ai thích không gian riêng tư, nhưng cần kiểm tra kỹ trước khi nhận phòng.'),
(8, 2, 5, 'Phòng 304 là lựa chọn hoàn hảo cho gia đình, đúng với tên "Family pack". Không gian rộng rãi, có nhiều tiện ích cho trẻ em. Trạng thái READY_TO_SERVE, mọi thứ đều sạch sẽ và sẵn sàng. Nhân viên rất thân thiện, còn tặng quà nhỏ cho con tôi. Rất đáng tiền!'),
(9, 4, 4, 'Phòng 401 có view núi đẹp, trạng thái READY_TO_SERVE đúng chuẩn. Nội thất hiện đại, giường lớn thoải mái, nhưng phòng tắm hơi nhỏ so với kỳ vọng. Nhân viên phục vụ tốt, hỗ trợ nhanh khi tôi cần thêm khăn. Tôi sẽ quay lại nếu có dịp.'),
(10, 6, 3, 'Phòng 402 không có cửa sổ nên hơi bí, đúng như mô tả "No window". Trạng thái BEING_CLEANED khi tôi đến, nhưng nhân viên dọn nhanh. Nội thất cơ bản, phù hợp với giá rẻ. Bình luận này dành cho ai tìm phòng giá thấp nhưng vẫn muốn sạch sẽ.');
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