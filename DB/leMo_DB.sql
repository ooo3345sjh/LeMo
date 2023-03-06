-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        8.0.30 - MySQL Community Server - GPL
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- lemo 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `lemo` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `lemo`;

-- 테이블 lemo. lemo_diary_comment 구조 내보내기
CREATE TABLE IF NOT EXISTS ` lemo_diary_comment` (
  `com_no` int NOT NULL AUTO_INCREMENT,
  `arti_no` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  `com_pno` int DEFAULT NULL,
  `com_comment` text NOT NULL,
  `com_regip` varchar(100) NOT NULL,
  `com_rdate` datetime NOT NULL,
  `com_udate` datetime DEFAULT NULL,
  PRIMARY KEY (`com_no`),
  KEY `fk_ lemo_diary_comment_lemo_member_userId1_idx` (`userId_id`),
  KEY `fk_ lemo_diary_comment_lemo_diary_article1_idx` (`arti_no`),
  CONSTRAINT `fk_ lemo_diary_comment_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`),
  CONSTRAINT `fk_ lemo_diary_comment_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo. lemo_diary_comment:~0 rows (대략적) 내보내기

-- 테이블 lemo. lemo_product_accommodationtype 구조 내보내기
CREATE TABLE IF NOT EXISTS ` lemo_product_accommodationtype` (
  `accType_no` int NOT NULL AUTO_INCREMENT,
  `accType_type` varchar(6) NOT NULL,
  PRIMARY KEY (`accType_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo. lemo_product_accommodationtype:~4 rows (대략적) 내보내기
INSERT INTO ` lemo_product_accommodationtype` (`accType_no`, `accType_type`) VALUES
	(1, '모텔'),
	(2, '호텔'),
	(3, '펜션'),
	(4, '게스트하우스'),
	(5, '캠핑·글램핑');

-- 테이블 lemo. lemo_product_reservation 구조 내보내기
CREATE TABLE IF NOT EXISTS ` lemo_product_reservation` (
  `res_no` bigint NOT NULL,
  `acc_id` int NOT NULL,
  `room_id` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  `res_price` int NOT NULL DEFAULT '0',
  `res_disPrice` int NOT NULL DEFAULT '0',
  `res_name` varchar(30) NOT NULL,
  `res_hp` char(13) NOT NULL,
  `res_payment` tinyint NOT NULL,
  `res_date` datetime NOT NULL,
  `res_checkIn` date NOT NULL,
  `res_checkOut` date NOT NULL,
  `res_state` tinyint(1) NOT NULL,
  `res_memo` text,
  PRIMARY KEY (`res_no`),
  KEY `fk_ lemo_product_reservation_lemo_member_userId1_idx` (`userId_id`),
  KEY `fk_ lemo_product_reservation_lemo_product_room1_idx` (`room_id`),
  KEY `fk_ lemo_product_reservation_lemo_product_accommodation1_idx` (`acc_id`),
  CONSTRAINT `fk_ lemo_product_reservation_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`),
  CONSTRAINT `fk_ lemo_product_reservation_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`),
  CONSTRAINT `fk_ lemo_product_reservation_lemo_product_room1` FOREIGN KEY (`room_id`) REFERENCES `lemo_product_room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo. lemo_product_reservation:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_cs 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_cs` (
  `cs_no` int NOT NULL AUTO_INCREMENT,
  `userId_id` varchar(50) NOT NULL,
  `cs_hp` char(13) DEFAULT NULL,
  `cs_email` varchar(20) DEFAULT NULL,
  `cs_cate` varchar(20) NOT NULL,
  `cs_type` varchar(20) NOT NULL,
  `cs_title` varchar(255) NOT NULL,
  `cs_content` text NOT NULL,
  `cs_regip` varchar(100) NOT NULL,
  `cs_rdate` datetime NOT NULL,
  `cs_reply` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `cs_eventbannerImg` varchar(300) DEFAULT NULL,
  `cs_eventViewImg` varchar(300) DEFAULT NULL,
  `cs_eventStart` date DEFAULT NULL,
  `cs_eventEnd` date DEFAULT NULL,
  PRIMARY KEY (`cs_no`),
  KEY `fk_lemo_cs_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_cs_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_cs:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_diary_article 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_diary_article` (
  `arti_no` int NOT NULL AUTO_INCREMENT,
  `res_no` bigint NOT NULL,
  `arti_title` varchar(255) NOT NULL,
  `arti_thumb` text NOT NULL,
  `arti_comment` int DEFAULT '0',
  `arti_hit` int DEFAULT '0',
  `arti_like` int DEFAULT '0',
  `arti_rdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `arti_udate` datetime NOT NULL,
  `arti_regip` varchar(100) NOT NULL,
  `arti_start` date NOT NULL,
  `arti_end` date NOT NULL,
  PRIMARY KEY (`arti_no`),
  KEY `fk_lemo_diary_article_ lemo_product_reservation1_idx` (`res_no`),
  CONSTRAINT `fk_lemo_diary_article_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_diary_article:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_diary_like 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_diary_like` (
  `arti_no` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  PRIMARY KEY (`arti_no`,`userId_id`),
  KEY `fk_lemo_diary_like_lemo_member_userId1_idx` (`userId_id`),
  KEY `fk_lemo_diary_like_lemo_diary_article1_idx` (`arti_no`),
  CONSTRAINT `fk_lemo_diary_like_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`),
  CONSTRAINT `fk_lemo_diary_like_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_diary_like:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_diary_spot 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_diary_spot` (
  `spot_no` int NOT NULL AUTO_INCREMENT,
  `arti_no` int NOT NULL,
  `spot_longtitude` decimal(20,15) NOT NULL,
  `spot_lattitude` decimal(20,15) NOT NULL,
  `spot_xy` point NOT NULL,
  `spot_content` text NOT NULL,
  `spot_thumb` varchar(100) NOT NULL,
  `province_name` varchar(10) NOT NULL,
  `spot_addr` varchar(255) NOT NULL,
  PRIMARY KEY (`spot_no`),
  KEY `fk_lemo_diary_spot_lemo_diary_article1_idx` (`arti_no`),
  CONSTRAINT `fk_lemo_diary_spot_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_diary_spot:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_businessinfo 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_businessinfo` (
  `userId_id` varchar(50) NOT NULL,
  `bis_company` varchar(20) NOT NULL,
  `bis_ceo` varchar(20) NOT NULL,
  `bis_openDate` date NOT NULL,
  `bis_bizRegNum` char(10) NOT NULL,
  `bis_tel` char(13) NOT NULL,
  `bis_zip` char(5) NOT NULL,
  `bis_addr` varchar(255) NOT NULL,
  `bis_addrDetail` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`userId_id`),
  UNIQUE KEY `bis_bizRegNum_UNIQUE` (`bis_bizRegNum`),
  UNIQUE KEY `bis_tel_UNIQUE` (`bis_tel`),
  KEY `fk_lemo_member_businessInfo_lemo_member_user1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_member_businessInfo_lemo_member_user1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_user` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_businessinfo:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_coupon 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_coupon` (
  `mcp_id` int NOT NULL AUTO_INCREMENT,
  `cp_id` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  `mcp_isUsed` tinyint(1) DEFAULT '0',
  `mcp_start` date DEFAULT NULL,
  `mcp_end` date DEFAULT NULL,
  `mcp_rdate` datetime DEFAULT NULL,
  PRIMARY KEY (`mcp_id`),
  KEY `fk_lemo_member_coupon_lemo__product_coupon1_idx` (`cp_id`),
  KEY `fk_lemo_member_coupon_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_member_coupon_lemo__product_coupon1` FOREIGN KEY (`cp_id`) REFERENCES `lemo_product_coupon` (`cp_id`),
  CONSTRAINT `fk_lemo_member_coupon_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_coupon:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_coupon_log 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_coupon_log` (
  `cl_id` int NOT NULL AUTO_INCREMENT,
  `mcp_id` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  `res_no` bigint NOT NULL,
  `dis_price` int NOT NULL,
  `cl_datetime` datetime NOT NULL,
  PRIMARY KEY (`cl_id`),
  KEY `fk_lemo_member_coupon_log_lemo_member_userId1_idx` (`userId_id`),
  KEY `fk_lemo_member_coupon_log_ lemo_product_reservation1_idx` (`res_no`),
  KEY `fk_lemo_member_coupon_log_lemo_member_coupon1_idx` (`mcp_id`),
  CONSTRAINT `fk_lemo_member_coupon_log_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`),
  CONSTRAINT `fk_lemo_member_coupon_log_lemo_member_coupon1` FOREIGN KEY (`mcp_id`) REFERENCES `lemo_member_coupon` (`mcp_id`),
  CONSTRAINT `fk_lemo_member_coupon_log_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_coupon_log:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_point_log 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_point_log` (
  `poi_id` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  `res_no` bigint DEFAULT NULL,
  `poi_content` varchar(50) NOT NULL,
  `poi_point` int NOT NULL,
  `poi_maximum` date NOT NULL,
  `poi_state` tinyint(1) NOT NULL,
  `poi_rdate` datetime NOT NULL,
  PRIMARY KEY (`poi_id`),
  KEY `fk_lemo_member_point_log_lemo_member_userId1_idx` (`userId_id`),
  KEY `fk_lemo_member_point_log_ lemo_product_reservation1_idx` (`res_no`),
  CONSTRAINT `fk_lemo_member_point_log_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`),
  CONSTRAINT `fk_lemo_member_point_log_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_point_log:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_social 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_social` (
  `userId_id` varchar(50) NOT NULL,
  `soci_typ` varchar(20) NOT NULL,
  `soci_email` varchar(20) DEFAULT NULL,
  `soci_photo` varchar(200) DEFAULT NULL,
  `soci_hp` char(13) NOT NULL,
  `soci_hp_certi` tinyint(1) NOT NULL DEFAULT '0',
  `soci_role` varchar(20) NOT NULL,
  `soci_level` tinyint(1) NOT NULL,
  `soci_point` int NOT NULL DEFAULT '0',
  `soci_regip` varchar(255) NOT NULL,
  `soci_udate` datetime DEFAULT NULL,
  `soci_rdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `soci_wdate` datetime DEFAULT NULL,
  `soci_isEnabled` tinyint NOT NULL,
  `soci_isLocked` tinyint NOT NULL,
  `soci_isPassNonExpired` tinyint NOT NULL,
  `soci_isNoticeEnabled` tinyint NOT NULL,
  `soci_isLocationEnabled` tinyint NOT NULL,
  `soci_isPrivacySelected` tinyint NOT NULL,
  `soci_memo` text,
  PRIMARY KEY (`userId_id`),
  UNIQUE KEY `soci_hp_UNIQUE` (`soci_hp`),
  KEY `fk_lemo_member_social_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_member_social_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_social:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_terms 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_terms` (
  `terms_no` int NOT NULL AUTO_INCREMENT,
  `termsType_no` int NOT NULL DEFAULT '0',
  `terms_title` varchar(50) NOT NULL,
  `terms_content` text NOT NULL,
  `terms_order` tinyint NOT NULL,
  PRIMARY KEY (`terms_no`),
  KEY `termsType_type_FK` (`termsType_no`),
  CONSTRAINT `termsType_type_FK` FOREIGN KEY (`termsType_no`) REFERENCES `lemo_member_termstype` (`termsType_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_terms:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_termstype 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_termstype` (
  `termsType_no` int NOT NULL AUTO_INCREMENT,
  `termsType_type` varchar(45) NOT NULL DEFAULT '0',
  PRIMARY KEY (`termsType_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_termstype:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_user` (
  `userId_id` varchar(50) NOT NULL,
  `user_pass` varchar(255) NOT NULL,
  `user_photo` varchar(200) DEFAULT NULL,
  `user_hp` char(13) NOT NULL,
  `user_hp_certi` tinyint(1) NOT NULL,
  `user_role` varchar(20) NOT NULL,
  `user_level` tinyint(1) NOT NULL,
  `user_point` int DEFAULT '0',
  `user_regip` varchar(100) DEFAULT NULL,
  `user_udate` datetime DEFAULT NULL,
  `user_rdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `user_wdate` datetime DEFAULT NULL,
  `user_isEnabled` tinyint(1) DEFAULT '1',
  `user_isLocked` tinyint(1) DEFAULT '1',
  `user_isPassNonExpired` tinyint(1) DEFAULT '1',
  `user_isNoticeEnabled` tinyint(1) DEFAULT '0',
  `user_isLocationEnabled` tinyint(1) DEFAULT '0',
  `user_isPrivacySelected` tinyint(1) DEFAULT '0',
  `user_memo` text,
  PRIMARY KEY (`userId_id`),
  UNIQUE KEY `user_hp_UNIQUE` (`user_hp`),
  KEY `fk_lemo_member_user_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_member_user_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_user:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_userid 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_userid` (
  `userId_id` varchar(50) NOT NULL,
  `userId_nick` varchar(30) NOT NULL,
  `userId_type` tinyint(1) NOT NULL,
  PRIMARY KEY (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_userid:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_accommodation 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_accommodation` (
  `acc_id` int NOT NULL AUTO_INCREMENT,
  `userId_id` varchar(50) NOT NULL,
  `acc_name` varchar(100) NOT NULL,
  `accType_no` int NOT NULL DEFAULT '0',
  `province_no` int NOT NULL DEFAULT '0',
  `acc_city` varchar(10) NOT NULL,
  `acc_zip` varchar(5) NOT NULL,
  `acc_addr` varchar(255) NOT NULL,
  `acc_addrDetail` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `acc_longtitude` decimal(20,15) NOT NULL,
  `acc_lattitude` decimal(20,15) NOT NULL,
  `acc_xy` point NOT NULL,
  `acc_info` text NOT NULL,
  `acc_comment` varchar(255) DEFAULT NULL,
  `acc_thumbs` text NOT NULL,
  `acc_rate` tinyint DEFAULT '1',
  `acc_review` int DEFAULT '0',
  `acc_discount` tinyint DEFAULT '0',
  `acc_checkIn` time NOT NULL,
  `acc_checkOut` time NOT NULL,
  PRIMARY KEY (`acc_id`),
  KEY `fk_lemo_product_accommodation_lemo_member_userId1_idx` (`userId_id`),
  KEY `fk_lemo_product_accommodation_lemo_product_province1_idx` (`province_no`) USING BTREE,
  KEY `fk_lemo_product_accommodation_ lemo_product_accommodationTy_idx` (`accType_no`) USING BTREE,
  CONSTRAINT `fk_lemo_product_accommodation_ lemo_product_accommodationType1` FOREIGN KEY (`accType_no`) REFERENCES ` lemo_product_accommodationtype` (`accType_no`),
  CONSTRAINT `fk_lemo_product_accommodation_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`),
  CONSTRAINT `fk_lemo_product_accommodation_lemo_product_province1` FOREIGN KEY (`province_no`) REFERENCES `lemo_product_province` (`province_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_accommodation:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_coupon 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_coupon` (
  `cp_id` int NOT NULL AUTO_INCREMENT,
  `userId_id` varchar(50) NOT NULL,
  `user_role` varchar(20) NOT NULL,
  `cp_subject` varchar(225) NOT NULL,
  `cp_group` varchar(10) NOT NULL,
  `cp_type` varchar(10) NOT NULL,
  `cp_rate` tinyint DEFAULT '0',
  `cp_price` int DEFAULT '0',
  `cp_disType` tinyint(1) NOT NULL,
  `cp_minimum` int NOT NULL,
  `cp_maximum` int NOT NULL,
  `cp_start` date NOT NULL,
  `cp_end` date NOT NULL,
  `cp_daysAvailable` tinyint NOT NULL,
  `cp_limitedIssuance` tinyint DEFAULT NULL,
  `cp_IssuedCnt` tinyint DEFAULT NULL,
  PRIMARY KEY (`cp_id`),
  KEY `fk_lemo__product_coupon_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo__product_coupon_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_coupon:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_pick 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_pick` (
  `arti_no` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  PRIMARY KEY (`arti_no`,`userId_id`),
  KEY `fk_lemo_product_pick_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_product_pick_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`),
  CONSTRAINT `fk_lemo_product_pick_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_pick:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_province 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_province` (
  `province_no` int NOT NULL AUTO_INCREMENT,
  `province_name` varchar(10) NOT NULL,
  PRIMARY KEY (`province_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_province:~16 rows (대략적) 내보내기
INSERT INTO `lemo_product_province` (`province_no`, `province_name`) VALUES
	(1, '강원도'),
	(2, '경기도'),
	(3, '경상남도'),
	(4, '경상북도'),
	(5, '광주광역시'),
	(6, '대구광역시'),
	(7, '대전광역시'),
	(8, '부산광역시'),
	(9, '서울특별시'),
	(10, '울산광역시'),
	(11, '인천광역시'),
	(12, '전라남도'),
	(13, '전라북도'),
	(14, '제주특별자치도'),
	(15, '충청남도'),
	(16, '충청북도'),
	(17, '알수없음');

-- 테이블 lemo.lemo_product_qna 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_qna` (
  `pQna_no` int NOT NULL AUTO_INCREMENT,
  `acc_id` int NOT NULL,
  `userId_id` varchar(50) NOT NULL,
  `pQna_title` varchar(255) NOT NULL,
  `pQna_content` text NOT NULL,
  `pQna_regip` varchar(100) NOT NULL,
  `pQna_rdate` datetime NOT NULL,
  `pQna_udate` datetime DEFAULT NULL,
  `pQna_reply` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pQna_no`),
  KEY `fk_lemo_product_qna_lemo_product_accommodation1_idx` (`acc_id`),
  KEY `fk_lemo_product_qna_lemo_member_userId1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_product_qna_lemo_member_userId1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_userid` (`userId_id`),
  CONSTRAINT `fk_lemo_product_qna_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_qna:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_ratepolicy 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_ratepolicy` (
  `rp_id` int NOT NULL AUTO_INCREMENT,
  `acc_id` int NOT NULL,
  `rp_offSeason_weekday` int NOT NULL,
  `rp_offSeason_weekend` int NOT NULL,
  `rp_peakSeason_weekday` int NOT NULL,
  `rp_peakSeason_weekend` int NOT NULL,
  PRIMARY KEY (`rp_id`),
  UNIQUE KEY `acc_id_UNIQUE` (`acc_id`),
  KEY `fk_lemo_product_ratePolicy_lemo_product_accommodation1_idx` (`acc_id`),
  CONSTRAINT `fk_lemo_product_ratePolicy_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_ratepolicy:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_reserved_room 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_reserved_room` (
  `rero_id` int NOT NULL AUTO_INCREMENT,
  `res_no` bigint NOT NULL,
  `room_id` int NOT NULL,
  `rero_checkIn_time` time NOT NULL,
  `rero_checkOut_time` time NOT NULL,
  `rero_state` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`rero_id`),
  KEY `fk_lemo_product_reserved_room_ lemo_product_reservation1_idx` (`res_no`),
  CONSTRAINT `fk_lemo_product_reserved_room_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_reserved_room:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_review 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_review` (
  `revi_id` int NOT NULL AUTO_INCREMENT,
  `res_no` bigint NOT NULL,
  `revi_rate` tinyint NOT NULL DEFAULT '1',
  `revi_title` varchar(255) NOT NULL,
  `revi_content` text NOT NULL,
  `revi_reply` varchar(255) DEFAULT NULL,
  `revi_regip` varchar(100) NOT NULL,
  `revi_rdate` datetime NOT NULL,
  `revi_thumb` text,
  PRIMARY KEY (`revi_id`,`res_no`),
  KEY `fk_lemo_product_review_ lemo_product_reservation1_idx` (`res_no`),
  CONSTRAINT `fk_lemo_product_review_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_review:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_room 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_room` (
  `room_id` int NOT NULL AUTO_INCREMENT,
  `acc_id` int NOT NULL,
  `room_name` varchar(100) NOT NULL,
  `room_stock` int NOT NULL,
  `room_defNum` tinyint DEFAULT '1',
  `room_maxNum` tinyint DEFAULT '1',
  `room_addPrice` int DEFAULT '0',
  `room_price` int DEFAULT '0',
  `room_info` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `room_thumb` text NOT NULL,
  `room_checkIn` time NOT NULL,
  `room_checkOut` time NOT NULL,
  PRIMARY KEY (`room_id`),
  KEY `fk_lemo_product_room_lemo_product_accommodation1_idx` (`acc_id`),
  CONSTRAINT `fk_lemo_product_room_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_room:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_servicecate 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_servicecate` (
  `sc_no` int NOT NULL AUTO_INCREMENT,
  `sc_name` varchar(30) NOT NULL,
  PRIMARY KEY (`sc_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_servicecate:~21 rows (대략적) 내보내기
INSERT INTO `lemo_product_servicecate` (`sc_no`, `sc_name`) VALUES
	(1, '피트니스'),
	(2, '와이파이'),
	(3, '레스토랑'),
	(4, 'TV'),
	(5, '에어컨'),
	(6, '냉장고'),
	(7, '드라이기'),
	(8, '장애인편의시설'),
	(9, '주차장'),
	(10, '무료주차'),
	(11, '금연'),
	(12, '카페'),
	(13, '수영장'),
	(14, '욕조'),
	(15, '스파'),
	(16, '엘리베이터'),
	(17, '욕실용품'),
	(18, '사우나'),
	(19, '짐보관'),
	(20, '세탁기'),
	(21, '건조기');

-- 테이블 lemo.lemo_product_servicereginfo 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_servicereginfo` (
  `srg_id` int NOT NULL AUTO_INCREMENT,
  `sc_no` int NOT NULL DEFAULT '0',
  `acc_id` int NOT NULL,
  PRIMARY KEY (`srg_id`),
  KEY `fk_lemo_product_serviceRegInfo_lemo_product_accommodation1_idx` (`acc_id`),
  KEY `fk_lemo_product_serviceRegInfo_lemo_product_serviceCate1_idx` (`sc_no`) USING BTREE,
  CONSTRAINT `fk_lemo_product_serviceRegInfo_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`),
  CONSTRAINT `fk_lemo_product_serviceRegInfo_lemo_product_serviceCate1` FOREIGN KEY (`sc_no`) REFERENCES `lemo_product_servicecate` (`sc_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_servicereginfo:~0 rows (대략적) 내보내기

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
