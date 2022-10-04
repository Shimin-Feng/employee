-- MySQL dump 10.13  Distrib 8.0.28, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: employee_management
-- ------------------------------------------------------
-- Server version	8.0.28

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `search_record`
--

DROP TABLE IF EXISTS `search_record`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `search_record`
(
    `record_id`       varchar(36) NOT NULL COMMENT '搜索记录 id',
    `search_group_by` varchar(45) NOT NULL COMMENT '搜索记录根据',
    `record_name`     varchar(45) NOT NULL COMMENT '搜索记录名称',
    `username`        varchar(45) NOT NULL COMMENT '搜索记录创建者',
    `created_date`    datetime    NOT NULL COMMENT '搜索记录生成时间',
    PRIMARY KEY (`record_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='搜索记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `search_record`
--

LOCK TABLES `search_record` WRITE;
/*!40000 ALTER TABLE `search_record`
    DISABLE KEYS */;
INSERT INTO `search_record`
VALUES ('019003a4-c4ce-498d-868f-17816fa5e83c', 'employeeAge', '25', 'admin3', '2022-05-06 00:11:46');
INSERT INTO `search_record`
VALUES ('0250c67f-f9a8-4499-9834-ea949b5290ce', 'employeeAge', '25', 'admin1', '2022-05-10 16:29:51');
INSERT INTO `search_record`
VALUES ('0364ce8e-c421-4ad1-ac89-3654a8685f43', 'employeeName', 'JA', 'admin3', '2022-05-10 22:06:03');
INSERT INTO `search_record`
VALUES ('0b68a3b1-6468-4b7a-a372-6fa48f81001c', 'employeeAge', '32', 'admin1', '2022-04-30 20:05:02');
INSERT INTO `search_record`
VALUES ('0f81fc76-1ff9-43cc-9c27-3024af2f6aea', 'employeeAge', '32', 'admin1', '2022-05-05 04:49:10');
INSERT INTO `search_record`
VALUES ('1862c962-b9e9-4353-a1fe-1019af5da02d', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:10');
INSERT INTO `search_record`
VALUES ('1867faab-1a69-41bc-842f-647af8eb0806', 'employeeName', 'JASON', 'admin3', '2022-05-13 17:08:12');
INSERT INTO `search_record`
VALUES ('189b12a4-8934-4cef-a52c-f41b39a68312', 'employeeName', 'JASON', 'admin3', '2022-04-27 00:05:23');
INSERT INTO `search_record`
VALUES ('199a9c22-6d2a-467c-b88d-74dbc6b539a3', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:44');
INSERT INTO `search_record`
VALUES ('1b98e54b-c6a0-4ca1-8c63-dfb5f4bca417', 'employeeAge', '32', 'admin1', '2022-05-05 04:52:41');
INSERT INTO `search_record`
VALUES ('23e48657-accf-4e8f-80ac-8ff9d2f9bcf0', 'employeeAge', '25', 'admin1', '2022-05-10 16:29:55');
INSERT INTO `search_record`
VALUES ('2458678f-c6c4-490e-a8c4-c99376d8a3b6', 'employeeAge', '25', 'admin1', '2022-05-05 06:22:14');
INSERT INTO `search_record`
VALUES ('2a50c098-3778-45d0-9972-3776b8a1102b', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:55');
INSERT INTO `search_record`
VALUES ('2b8751c6-f5b9-46ea-ab9c-4a62fc31d466', 'employeeAge', '32', 'admin1', '2022-04-29 14:15:47');
INSERT INTO `search_record`
VALUES ('2c5f7873-3803-4c3a-aaab-5d8b0d514f98', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:30');
INSERT INTO `search_record`
VALUES ('2ff05a18-7c1e-4bfc-a316-4c784e774858', 'employeeName', 'kk', 'admin3', '2022-05-06 02:29:13');
INSERT INTO `search_record`
VALUES ('323ba683-0954-4c38-a00d-da09d0bd12e7', 'employeeAge', '25', 'admin1', '2022-05-04 16:52:13');
INSERT INTO `search_record`
VALUES ('325d8cfb-08d3-4768-8eae-f43f437c3cd6', 'employeeName', 'JA', 'admin3', '2022-04-27 00:05:41');
INSERT INTO `search_record`
VALUES ('39e747cc-dfc9-49d7-9367-23df2ec518bb', 'employeeName', 'kj', 'admin3', '2022-04-27 00:28:52');
INSERT INTO `search_record`
VALUES ('3f143461-ad7b-47ca-84ed-a816aa0e94c1', 'employeeAge', '5', 'admin1', '2022-05-28 04:04:35');
INSERT INTO `search_record`
VALUES ('4398dad6-bf4f-4263-9878-c841e9fe2100', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:46');
INSERT INTO `search_record`
VALUES ('439d2a84-60a2-4443-945c-90fc6423ddfd', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:25');
INSERT INTO `search_record`
VALUES ('48496283-880a-44d3-b050-58815a9bf486', 'employeeAge', '32', 'admin1', '2022-05-05 04:51:59');
INSERT INTO `search_record`
VALUES ('4a50013f-0572-4a94-93f3-2026dc94b6d8', 'employeeAge', '32', 'admin1', '2022-05-05 04:49:11');
INSERT INTO `search_record`
VALUES ('50de7e5f-6789-4e93-8892-567eeafd2b3a', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:49');
INSERT INTO `search_record`
VALUES ('5af2ead6-23db-4592-9b0d-d8db87cf5441', 'employeePhoneNumber', '150', 'admin1', '2022-04-26 12:57:52');
INSERT INTO `search_record`
VALUES ('5f317243-1fd7-441a-9e6d-3ec6b17f5c4a', 'employeeAge', '25', 'admin1', '2022-05-10 16:29:45');
INSERT INTO `search_record`
VALUES ('605342c5-cfa2-418a-98ae-12a088b08b80', 'employeeName', 'JA', 'admin3', '2022-04-27 00:05:47');
INSERT INTO `search_record`
VALUES ('61c8051f-5a3c-49f3-8690-45f2e8efdc37', 'employeeAge', '25', 'admin1', '2022-04-26 13:03:50');
INSERT INTO `search_record`
VALUES ('62d0e2f9-f0e2-48cb-9953-d91798ab7e14', 'employeeAge', '32', 'admin1', '2022-05-05 04:52:02');
INSERT INTO `search_record`
VALUES ('6430211c-c614-44a9-9fac-84d7a94a7705', 'employeeAge', '25', 'admin1', '2022-04-30 02:04:26');
INSERT INTO `search_record`
VALUES ('64e3c9f8-1a30-4d8f-ad18-ec9b6ca5cc76', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:19');
INSERT INTO `search_record`
VALUES ('6f6ed4a6-3888-470b-9295-88323f0bdc3f', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:49');
INSERT INTO `search_record`
VALUES ('70617220-362b-49c1-aca5-6f07e0b8a9bf', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:32');
INSERT INTO `search_record`
VALUES ('74e72c5e-7342-41e0-a6f2-431c7d40fc14', 'employeeAge', '25', 'admin1', '2022-04-29 14:28:38');
INSERT INTO `search_record`
VALUES ('7696ae8b-0618-4f06-b36f-6697a2fa5552', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:50');
INSERT INTO `search_record`
VALUES ('7a468290-b9b6-495a-8215-cbd8cd99f547', 'employeeSex', '1', 'admin3', '2022-05-15 15:53:08');
INSERT INTO `search_record`
VALUES ('82c322c9-85ab-476c-8541-b28e92a5d03b', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:47');
INSERT INTO `search_record`
VALUES ('8304850f-4403-48be-b45d-c790076626e1', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:55');
INSERT INTO `search_record`
VALUES ('84f27906-9f75-455a-8de1-f95f55f04be7', 'employeeAge', '25', 'admin1', '2022-04-30 20:07:38');
INSERT INTO `search_record`
VALUES ('85143d31-5001-4395-8edb-278aca57541b', 'employeeIdCard', 'x', 'admin1', '2022-04-30 21:10:50');
INSERT INTO `search_record`
VALUES ('90458dac-3acc-4381-b8ed-e42ec478be02', 'employeeAge', '5', 'admin1', '2022-05-28 04:03:30');
INSERT INTO `search_record`
VALUES ('9533a295-183e-414c-8462-6bd4f89ba2d6', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:28');
INSERT INTO `search_record`
VALUES ('9cd51f78-4c6c-4491-994a-b48f02a9f9bb', 'employeeIdCard', '1997', 'admin1', '2022-04-30 21:10:59');
INSERT INTO `search_record`
VALUES ('9dd8ee2a-808e-4dad-991b-9eca0d3e8234', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:41');
INSERT INTO `search_record`
VALUES ('a14ae9d4-7b7c-438a-bfc8-28863e7a71a1', 'employeeAge', '32', 'admin1', '2022-04-30 20:05:04');
INSERT INTO `search_record`
VALUES ('a2cb2dd8-7149-4ed2-9f75-cef70616999a', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:53');
INSERT INTO `search_record`
VALUES ('a6ab65a9-23bb-4f7e-9b82-f0cd8e162a7a', 'employeePhoneNumber', '15', 'admin1', '2022-04-30 20:56:18');
INSERT INTO `search_record`
VALUES ('aa3aae6b-9112-4aff-8538-a5de2bc559ef', 'employeeAge', '5', 'admin1', '2022-05-28 04:03:25');
INSERT INTO `search_record`
VALUES ('aacd078c-9eaf-4b08-a735-cf0096dd8ce0', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:36');
INSERT INTO `search_record`
VALUES ('acf871e2-af48-4b9a-a1e2-ebbfad74a9ad', 'employeeSex', '男', 'admin3', '2022-05-15 15:53:05');
INSERT INTO `search_record`
VALUES ('af834d03-d65d-447f-ac89-2a3b505626f5', 'employeeAge', '25', 'admin1', '2022-05-10 15:32:03');
INSERT INTO `search_record`
VALUES ('b48c3e93-2c49-4b6f-b841-3edd7f0a93f1', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:31');
INSERT INTO `search_record`
VALUES ('be594e10-b6df-4252-9889-eef4b98259ac', 'employeeAge', '25', 'admin1', '2022-05-10 15:31:58');
INSERT INTO `search_record`
VALUES ('c0803317-882f-4d77-b403-7eea7e83b6d4', 'employeeIdCard', '370', 'admin3', '2022-04-26 13:05:45');
INSERT INTO `search_record`
VALUES ('c6a3fe13-8a88-4bdd-a57a-bae4eeb938e4', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:40');
INSERT INTO `search_record`
VALUES ('c8983a99-262e-4ccf-913a-5871e53fbaf2', 'employeeAge', '32', 'admin1', '2022-05-05 04:52:02');
INSERT INTO `search_record`
VALUES ('c96b5182-e125-4df5-b403-cd2223dc6e62', 'employeeAge', '32', 'admin1', '2022-04-28 13:25:01');
INSERT INTO `search_record`
VALUES ('cc804cf9-1a6c-46ad-b0ff-dfe861449226', 'employeeAge', '25', 'admin1', '2022-05-10 16:29:47');
INSERT INTO `search_record`
VALUES ('cd535b1a-eabb-4aea-8d14-c03e845e39d2', 'employeeName', 'JA', 'admin3', '2022-04-27 00:05:44');
INSERT INTO `search_record`
VALUES ('d0294a04-cadc-45b5-a2d7-407797b34ffe', 'employeeName', '特斯拉', 'admin1', '2022-05-22 18:03:45');
INSERT INTO `search_record`
VALUES ('d170a079-8ca0-4b11-963a-5cac501c1e95', 'employeeAge', '25', 'admin1', '2022-04-30 20:06:34');
INSERT INTO `search_record`
VALUES ('d1ac361b-becf-406c-9f1f-ced9ae73e685', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:21');
INSERT INTO `search_record`
VALUES ('d40ce414-2554-41fd-a9fe-9b859f661617', 'employeePhoneNumber', '15', 'admin1', '2022-04-30 20:56:11');
INSERT INTO `search_record`
VALUES ('d4ac45e1-9de8-475a-a998-65b1707fd40e', 'employeeAge', '25', 'admin1', '2022-04-29 14:15:52');
INSERT INTO `search_record`
VALUES ('d726496a-6244-44a8-be09-ee77ad9de45d', 'employeeAge', '32', 'admin1', '2022-05-05 04:51:58');
INSERT INTO `search_record`
VALUES ('d879d2c5-dc26-4290-991a-63443b530cf0', 'employeeAge', '25', 'admin1', '2022-05-10 16:29:57');
INSERT INTO `search_record`
VALUES ('dea75c62-254c-4db4-9e1f-310c2ddf5547', 'employeeAge', '32', 'admin1', '2022-05-05 04:52:42');
INSERT INTO `search_record`
VALUES ('deda8308-b461-490b-9732-5aa8b3867de7', 'employeePhoneNumber', '15', 'admin1', '2022-04-30 20:56:16');
INSERT INTO `search_record`
VALUES ('e078d6c6-7908-4a86-927a-facc0a5a1a4d', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:45');
INSERT INTO `search_record`
VALUES ('ec1764ba-964e-4f68-a3d3-1d4e135770cf', 'employeeAge', '32', 'admin1', '2022-05-05 04:51:50');
INSERT INTO `search_record`
VALUES ('f089d90b-75a5-4772-966a-cb1ed703fce1', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:25');
INSERT INTO `search_record`
VALUES ('f379604a-8025-41ef-9d36-38cfcb226ca8', 'employeeAge', '32', 'admin1', '2022-05-05 04:48:37');
INSERT INTO `search_record`
VALUES ('f432e036-34b0-4972-a5fb-edad8c5637af', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:18');
INSERT INTO `search_record`
VALUES ('f43d3dd3-6b36-4a63-a2d6-42bbe84fb5e8', 'employeeAge', '25', 'admin1', '2022-05-10 15:32:01');
INSERT INTO `search_record`
VALUES ('fcd5ba41-f049-452b-a636-5049b743250f', 'employeeAge', '25', 'admin3', '2022-05-06 00:16:40');
/*!40000 ALTER TABLE `search_record`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2022-06-06 15:41:46
