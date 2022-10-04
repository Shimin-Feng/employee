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
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee`
(
    `employee_id`           varchar(36) NOT NULL COMMENT '员工 id',
    `employee_name`         varchar(45) NOT NULL COMMENT '员工姓名',
    `employee_sex`          varchar(1)  NOT NULL COMMENT '员工性别',
    `employee_age`          varchar(2)  NOT NULL COMMENT '员工年龄',
    `employee_id_card`      varchar(18) NOT NULL COMMENT '员工身份证号码',
    `employee_address`      varchar(45) NOT NULL COMMENT '员工住址',
    `employee_phone_number` varchar(11) NOT NULL COMMENT '员工电话号码',
    `created_by`            varchar(45) NOT NULL COMMENT '创建者',
    `created_date`          varchar(19) NOT NULL COMMENT '创建时间',
    `last_modified_date`    varchar(19) NOT NULL COMMENT '最后更改时间',
    PRIMARY KEY (`employee_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='员工信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee`
    DISABLE KEYS */;
INSERT INTO `employee`
VALUES ('05e69c33-4c71-4691-948e-7667cf5e08d1', '达芬奇', '1', '34', '371321198712237719', '美国芝加哥', '17799546679',
        'admin3', '2022/05/07 00:52:56', '2022/05/07 00:52:56');
INSERT INTO `employee`
VALUES ('08f214e2-5feb-43e8-a799-3dbc02b2d9a6', '盖茨', '0', '32', '370281198907212626', '中国香港', '15624221111',
        'admin1', '2022/05/25 21:32:34', '2022/05/25 21:32:34');
INSERT INTO `employee`
VALUES ('0fa507c8-72bd-4b21-9fc3-cc59af1fa35f', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin1', '2022/05/10 16:29:13', '2022/05/10 16:29:13');
INSERT INTO `employee`
VALUES ('121421ae-7d50-49d7-aad8-d376a2c1d2f1', '乔夫', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:10:55', '2022/05/18 13:10:55');
INSERT INTO `employee`
VALUES ('167ce87b-a843-4e8c-a71b-2e1bc4b3c3a6', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin3', '2022/05/07 03:13:19', '2022/05/07 03:13:19');
INSERT INTO `employee`
VALUES ('1ab76a07-9668-43e7-91ad-7253b07ab362', '乔纳德', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:27:21', '2022/05/17 12:27:21');
INSERT INTO `employee`
VALUES ('2070f3e5-1990-402f-b034-aa7ddffefff2', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660348433',
        'admin3', '2022/05/13 17:00:35', '2022/05/13 17:00:35');
INSERT INTO `employee`
VALUES ('27a77344-bd90-46c2-80b9-6cbdff35a7bb', '乔丹', '0', '32', '370281198907212626', '中国上海', '15646576344',
        'admin1', '2022/05/10 16:28:53', '2022/05/10 16:28:53');
INSERT INTO `employee`
VALUES ('373b47fb-2ff1-4d38-8445-5eb392cf983d', '乔森', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:09:52', '2022/05/18 13:09:52');
INSERT INTO `employee`
VALUES ('39187e70-b30d-4e9f-827b-26da18db735f', '大乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/17 20:22:49', '2022/05/17 20:22:49');
INSERT INTO `employee`
VALUES ('39f267e9-fd41-43d2-aed5-66537ca599ba', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890900',
        'admin3', '2022/05/12 17:41:51', '2022/05/12 17:41:51');
INSERT INTO `employee`
VALUES ('3bc9e199-86b8-4b8a-a130-00e596c1d1e9', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890999',
        'admin3', '2022/05/07 00:55:06', '2022/05/07 00:55:06');
INSERT INTO `employee`
VALUES ('3e51fbdd-b2d4-4476-9ba9-eda7389ab3d0', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin1', '2022/05/10 16:29:14', '2022/05/10 16:29:14');
INSERT INTO `employee`
VALUES ('3fb2b908-c5fa-4389-a7c6-0c133c12c877', '小乔希娜', '0', '32', '370281198907212626', '美国芝加哥',
        '15647767523', 'admin1', '2022/05/18 13:11:30', '2022/05/18 13:14:57');
INSERT INTO `employee`
VALUES ('42e4a03e-ab12-4647-b3e3-5751031de56a', '乔娜', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:27:05', '2022/05/17 12:27:05');
INSERT INTO `employee`
VALUES ('47273577-63ad-4872-9f0f-0ed39da14805', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890999',
        'admin3', '2022/05/06 00:16:36', '2022/05/06 00:16:36');
INSERT INTO `employee`
VALUES ('4b4bfb71-8b74-4afa-b5dd-234e26eade22', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin3', '2022/05/07 03:13:16', '2022/05/07 03:13:16');
INSERT INTO `employee`
VALUES ('56badd47-8c07-4876-8b9c-511ea6864326', '特斯拉', '1', '35', '371325198702130059', '中国上海', '15667778889',
        'admin1', '2022/05/05 13:26:55', '2022/05/05 13:26:55');
INSERT INTO `employee`
VALUES ('59963a5d-33a6-45b0-a968-e07349157b8c', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin1', '2022/05/10 16:29:19', '2022/05/10 16:29:19');
INSERT INTO `employee`
VALUES ('5f413ee5-029f-4879-a1d9-f3a6964b587c', '特斯拉', '1', '35', '371325198702130059', '中国上海', '15667778889',
        'admin1', '2022/05/05 16:45:17', '2022/05/05 16:45:17');
INSERT INTO `employee`
VALUES ('60a8f1c4-6e2a-4dff-aa40-09df20254d77', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890900',
        'admin3', '2022/05/12 17:41:50', '2022/05/12 17:41:50');
INSERT INTO `employee`
VALUES ('64e516db-9913-40de-8f32-a60e4d1b94ec', '特斯拉', '1', '25', '51152919970104501X', '中国四川成都',
        '15668770909', 'admin3', '2022/05/06 00:16:31', '2022/05/06 00:16:31');
INSERT INTO `employee`
VALUES ('664847e9-be31-44ad-b9ac-1e3cd25668f9', '小乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/17 20:22:57', '2022/05/17 20:22:57');
INSERT INTO `employee`
VALUES ('6810822c-d1ea-4604-a0c9-52287848697e', '盖茨', '0', '32', '370281198907212626', '中国香港', '15624221111',
        'admin1', '2022/04/29 01:44:48', '2022/05/10 11:46:47');
INSERT INTO `employee`
VALUES ('68ace81f-3362-4a8b-b917-12d81bc0ed2c', '乔治', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:09:45', '2022/05/18 13:09:45');
INSERT INTO `employee`
VALUES ('6a5bbf92-e1e2-476d-b875-0c2f2b808262', '乔娜', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:27:08', '2022/05/17 12:27:08');
INSERT INTO `employee`
VALUES ('6e867684-ad2c-4f96-b7c4-a049cac99160', '哈勃', '0', '32', '370281198907212626', '中国香港', '15660965449',
        'admin1', '2022/05/05 07:21:12', '2022/05/05 07:21:12');
INSERT INTO `employee`
VALUES ('752d4d3b-754b-4729-95e0-5bda6bf7398d', '乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647980944',
        'admin1', '2022/05/10 15:50:56', '2022/05/10 15:50:56');
INSERT INTO `employee`
VALUES ('7d97af18-f82e-450b-a9b6-2bf9a95253ca', '乔丹', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/05 07:28:30', '2022/05/05 07:28:30');
INSERT INTO `employee`
VALUES ('7de62a3f-e8f2-4992-a371-e5521737d6e8', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin1', '2022/05/10 16:29:18', '2022/05/10 16:29:18');
INSERT INTO `employee`
VALUES ('8aff1e20-2d32-4b64-a1df-e122dd7a6327', '乔布什', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:26:42', '2022/05/17 12:26:42');
INSERT INTO `employee`
VALUES ('8b47558d-bdb6-4fd0-8949-b07bc216781c', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660444645',
        'admin1', '2022/05/29 14:53:44', '2022/05/29 14:53:44');
INSERT INTO `employee`
VALUES ('8b89cf21-ec20-4ba8-9ea2-86c6d9ab346e', '乔丹', '0', '32', '370281198907212626', '中国上海', '15646576232',
        'admin3', '2022/05/10 22:06:24', '2022/05/10 22:06:24');
INSERT INTO `employee`
VALUES ('8ee2b85d-50b1-4958-80df-524904d91141', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890900',
        'admin3', '2022/05/12 17:42:06', '2022/05/12 17:42:06');
INSERT INTO `employee`
VALUES ('92d131c7-d476-4f0c-bae7-3d26b32f413b', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin1', '2022/05/10 16:29:11', '2022/05/10 16:29:11');
INSERT INTO `employee`
VALUES ('95c1fae7-40c2-486d-9fb7-4f28a1bffdf9', '洛克菲勒', '1', '35', '371325198702130059', '中国上海', '15667778884',
        'admin1', '2022/05/05 04:47:47', '2022/05/30 00:56:30');
INSERT INTO `employee`
VALUES ('98419c2a-df33-4d00-a59b-7a5971d7c4cd', '盖茨', '0', '32', '370281198907212626', '中国香港', '15624221111',
        'admin1', '2022/05/25 21:34:20', '2022/05/25 21:34:20');
INSERT INTO `employee`
VALUES ('994b45da-98ba-4c64-a7c8-ce484894e5c6', '乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647980944',
        'admin1', '2022/05/10 15:51:00', '2022/05/10 15:51:00');
INSERT INTO `employee`
VALUES ('996ae20d-8b96-4b6a-b366-88cba43ec61e', '特斯拉', '1', '25', '51152919970104501X', '中国四川成都',
        '15668770909', 'admin3', '2022/05/06 00:16:30', '2022/05/06 00:16:30');
INSERT INTO `employee`
VALUES ('9a901ed3-cde3-42ec-9429-7aa8d1b9bacb', '乔克', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:11:02', '2022/05/18 13:11:02');
INSERT INTO `employee`
VALUES ('a695ecb0-2512-44c5-9c3b-91e30da75e28', '盖茨', '0', '32', '370281198907212626', '中国香港', '15624221111',
        'admin1', '2022/05/10 11:47:01', '2022/05/10 11:47:01');
INSERT INTO `employee`
VALUES ('a74753b9-40d8-4103-803c-48a818283942', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15669869773',
        'admin3', '2022/06/03 17:55:29', '2022/06/03 17:55:29');
INSERT INTO `employee`
VALUES ('ac8e0060-f8ec-4c52-baef-d9e081bdb906', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660961111',
        'admin1', '2022/05/10 16:10:32', '2022/05/10 16:10:32');
INSERT INTO `employee`
VALUES ('ae5a15b4-6ffc-406c-916f-51ae09556c1c', '大乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/17 20:22:46', '2022/05/17 20:22:46');
INSERT INTO `employee`
VALUES ('b4bb69f9-1d00-4573-83bb-79f1718ab1a1', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660443535',
        'admin3', '2022/06/03 16:48:33', '2022/06/03 16:48:33');
INSERT INTO `employee`
VALUES ('b8930ba9-5d38-4aef-aa7d-63645bb8939c', '特斯拉', '1', '25', '51152919970104501X', '中国四川成都',
        '15668770909', 'admin3', '2022/05/10 21:19:48', '2022/05/10 21:19:48');
INSERT INTO `employee`
VALUES ('b96fc042-8a08-45a0-a41b-bcb8fb05d891', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890343',
        'admin3', '2022/05/10 22:05:56', '2022/05/10 22:05:56');
INSERT INTO `employee`
VALUES ('bc5a387a-5418-40fd-ad72-862ad17881d7', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890343',
        'admin3', '2022/05/07 00:54:42', '2022/05/10 22:05:51');
INSERT INTO `employee`
VALUES ('c2daa662-0377-4199-a77e-282b4c709dc6', '乔布什', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:26:46', '2022/05/17 12:26:46');
INSERT INTO `employee`
VALUES ('c5d49e4f-d38b-46e4-a92b-43531238fa4e', '乔纳德', '0', '32', '370281198907212626', '中国上海', '15646576834',
        'admin3', '2022/06/02 16:14:07', '2022/06/02 16:14:07');
INSERT INTO `employee`
VALUES ('c631f9a1-5522-4a6c-82f9-d153def1ea71', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799548989',
        'admin1', '2022/05/10 16:29:16', '2022/05/10 16:29:16');
INSERT INTO `employee`
VALUES ('c9deeb48-c121-4830-919d-38006bbb1d51', '哈勃', '0', '32', '370281198907212626', '中国香港', '15660965340',
        'admin1', '2022/05/03 22:36:58', '2022/06/03 17:44:09');
INSERT INTO `employee`
VALUES ('cc1950f0-10a4-4dd6-8c37-b6aaa805042c', '乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647980990',
        'admin1', '2022/05/05 17:22:15', '2022/05/05 17:22:15');
INSERT INTO `employee`
VALUES ('cc31f194-85ef-4758-9457-24778490c749', '乔纳德', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:27:19', '2022/05/17 12:27:19');
INSERT INTO `employee`
VALUES ('cc4ea1e9-4674-415a-a274-f28afb977762', '特斯拉', '1', '25', '51152919970104501X', '中国四川成都',
        '15668770909', 'admin3', '2022/05/06 00:16:27', '2022/05/06 00:16:27');
INSERT INTO `employee`
VALUES ('cef96746-bc36-43ef-b52c-5d3125dafa4e', '哥伦布', '1', '38', '371323198311016715', '美国芝加哥', '17799546666',
        'admin1', '2022/05/09 13:33:35', '2022/05/09 13:33:35');
INSERT INTO `employee`
VALUES ('d29f0b33-cfb2-4301-8f0c-25f94180a9c8', '乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647767576',
        'admin3', '2022/05/12 17:42:21', '2022/05/12 17:42:21');
INSERT INTO `employee`
VALUES ('d2bfc0d7-fc83-4292-acaa-31ab10f9fec0', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660334343',
        'admin3', '2022/05/13 16:46:53', '2022/05/29 15:00:39');
INSERT INTO `employee`
VALUES ('d7c2f2a0-1a4f-48db-a0aa-30bc89ada44a', '麦克斯韦', '0', '42', '620102197910181185', '中国台湾', '15675711542',
        'admin1', '2022/05/22 11:07:10', '2022/05/22 11:07:10');
INSERT INTO `employee`
VALUES ('d9448216-92ee-4a5a-aa9d-e2c23be8efe4', '大乔尼', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:12:19', '2022/05/18 13:14:29');
INSERT INTO `employee`
VALUES ('da40e764-d0c7-4e80-b461-757656de1218', '大乔希', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:11:35', '2022/05/18 13:14:39');
INSERT INTO `employee`
VALUES ('db590650-bcf7-42e9-8d64-a62d712cbcaa', '特斯拉', '1', '35', '371325198702130059', '中国上海', '15667778121',
        'admin1', '2022/05/05 16:45:08', '2022/05/10 21:15:43');
INSERT INTO `employee`
VALUES ('dbb33eb5-ca05-4686-8cf8-2cd064726327', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660334343',
        'admin1', '2022/05/29 15:00:47', '2022/05/29 15:00:47');
INSERT INTO `employee`
VALUES ('dd2c9f6b-13dc-4f1f-b73f-3b2587b834cb', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15669869770',
        'admin1', '2022/05/03 22:35:30', '2022/06/03 18:03:06');
INSERT INTO `employee`
VALUES ('e411967d-600b-45c1-9cee-e1988f51c18b', '大乔达', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:11:52', '2022/05/18 13:14:34');
INSERT INTO `employee`
VALUES ('e58dd285-c85c-4454-98e8-56e7c0820e33', '特斯拉', '1', '25', '51152919970104501X', '中国四川成都',
        '15668770909', 'admin3', '2022/05/06 00:16:24', '2022/05/06 00:16:24');
INSERT INTO `employee`
VALUES ('e611f404-740b-412f-9d86-ced5e1df04a0', '麦克斯韦', '0', '42', '620102197910181185', '中国台湾', '15675714646',
        'admin1', '2022/04/30 17:25:36', '2022/06/03 17:33:34');
INSERT INTO `employee`
VALUES ('e61cdc0b-34dc-44a4-bb5c-8cd17998d3dc', '乔纳德', '0', '32', '370281198907212626', '中国上海', '15646576800',
        'admin1', '2022/05/17 12:27:22', '2022/05/17 12:27:22');
INSERT INTO `employee`
VALUES ('ec3f1976-a6bc-4f01-ab1e-4d3c5c692223', '乔森', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:09:54', '2022/05/18 13:09:54');
INSERT INTO `employee`
VALUES ('f19a80c5-1232-42a8-a8fb-04714a8fb566', '乔纳德', '0', '32', '370281198907212626', '中国上海', '15646576834',
        'admin1', '2022/05/17 12:27:17', '2022/06/02 16:14:03');
INSERT INTO `employee`
VALUES ('f2a0702a-d487-409e-be86-5b7a506af628', '大乔安', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:12:14', '2022/05/18 13:14:22');
INSERT INTO `employee`
VALUES ('f56eea0b-78af-4ff8-8b5c-439d1f6c889a', '特斯拉', '0', '32', '370281198907212626', '中国香港', '15660965000',
        'admin1', '2022/05/05 04:48:39', '2022/05/05 04:48:39');
INSERT INTO `employee`
VALUES ('f7f2926b-bb4a-4892-9435-bcee37f20429', 'Jordan', '1', '25', '51152919970104501X', '中国上海', '15663890900',
        'admin3', '2022/05/12 17:41:43', '2022/05/12 17:41:43');
INSERT INTO `employee`
VALUES ('f9c5538b-29f5-4ce7-bf05-e8b30b1bc241', '小乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/17 20:22:56', '2022/05/17 20:22:56');
INSERT INTO `employee`
VALUES ('fd4ce52f-a420-4ebb-96cc-6500edb01f4d', '乔治', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin1', '2022/05/18 13:09:47', '2022/05/18 13:09:47');
INSERT INTO `employee`
VALUES ('fdd4fc09-0874-4f91-9b8b-ac40dde85904', '乔丹', '0', '32', '370281198907212626', '美国芝加哥', '15647767523',
        'admin3', '2022/05/15 16:23:12', '2022/05/15 16:23:12');
/*!40000 ALTER TABLE `employee`
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

-- Dump completed on 2022-06-06 15:38:58
