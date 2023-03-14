-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        8.0.30 - MySQL Community Server - GPL
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.4.0.6659
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
  `user_id` varchar(50) NOT NULL,
  `com_pno` int DEFAULT NULL,
  `com_comment` text NOT NULL,
  `com_regip` varchar(100) NOT NULL,
  `com_rdate` datetime NOT NULL,
  `com_udate` datetime DEFAULT NULL,
  PRIMARY KEY (`com_no`),
  KEY `fk_ lemo_diary_comment_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_ lemo_diary_comment_lemo_diary_article1_idx` (`arti_no`),
  CONSTRAINT `fk_ lemo_diary_comment_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`),
  CONSTRAINT `fk_ lemo_diary_comment_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo. lemo_diary_comment:~0 rows (대략적) 내보내기

-- 테이블 lemo. lemo_product_accommodationtype 구조 내보내기
CREATE TABLE IF NOT EXISTS ` lemo_product_accommodationtype` (
  `accType_no` int NOT NULL AUTO_INCREMENT,
  `accType_type` varchar(6) NOT NULL,
  PRIMARY KEY (`accType_no`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo. lemo_product_accommodationtype:~0 rows (대략적) 내보내기
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
  `user_id` varchar(50) NOT NULL,
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
  KEY `fk_ lemo_product_reservation_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_ lemo_product_reservation_lemo_product_room1_idx` (`room_id`),
  KEY `fk_ lemo_product_reservation_lemo_product_accommodation1_idx` (`acc_id`),
  CONSTRAINT `fk_ lemo_product_reservation_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`),
  CONSTRAINT `fk_ lemo_product_reservation_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`),
  CONSTRAINT `fk_ lemo_product_reservation_lemo_product_room1` FOREIGN KEY (`room_id`) REFERENCES `lemo_product_room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo. lemo_product_reservation:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_cs 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_cs` (
  `cs_no` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `cs_hp` char(13) DEFAULT NULL,
  `cs_email` varchar(20) DEFAULT NULL,
  `cs_cate` varchar(20) NOT NULL,
  `cs_type` varchar(20) DEFAULT NULL,
  `cs_title` varchar(255) NOT NULL,
  `cs_content` text NOT NULL,
  `cs_regip` varchar(100) NOT NULL,
  `cs_rdate` datetime NOT NULL,
  `cs_reply` text,
  `cs_eventbannerImg` varchar(300) DEFAULT NULL,
  `cs_eventViewImg` varchar(300) DEFAULT NULL,
  `cs_eventStart` date DEFAULT NULL,
  `cs_eventEnd` date DEFAULT NULL,
  `cs_eventState` tinyint DEFAULT '1',
  PRIMARY KEY (`cs_no`),
  KEY `fk_lemo_cs_lemo_member_userId1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_cs_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_cs:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_diary_article 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_diary_article` (
  `arti_no` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
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
  KEY `fk_lemo_diary_article_lemo_member_userInfo1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_diary_article_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`),
  CONSTRAINT `fk_lemo_diary_article_lemo_member_userInfo1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_diary_article:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_diary_like 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_diary_like` (
  `arti_no` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  PRIMARY KEY (`arti_no`,`user_id`),
  KEY `fk_lemo_diary_like_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_lemo_diary_like_lemo_diary_article1_idx` (`arti_no`),
  CONSTRAINT `fk_lemo_diary_like_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`),
  CONSTRAINT `fk_lemo_diary_like_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_diary_like:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_diary_spot 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_diary_spot` (
  `spot_no` int NOT NULL AUTO_INCREMENT,
  `arti_no` int NOT NULL,
  `spot_title` varchar(50) NOT NULL,
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
  `bis_company` varchar(50) NOT NULL,
  `bis_ceo` varchar(20) NOT NULL,
  `bis_openDate` date NOT NULL,
  `bis_bizRegNum` char(10) NOT NULL,
  `bis_tel` char(13) NOT NULL,
  `bis_zip` char(5) NOT NULL,
  `bis_addr` varchar(255) NOT NULL,
  `bis_addrDetail` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`userId_id`),
  UNIQUE KEY `bis_bizRegNum_UNIQUE` (`bis_bizRegNum`),
  UNIQUE KEY `bis_tel_UNIQUE` (`bis_tel`),
  KEY `fk_lemo_member_businessInfo_lemo_member_user1_idx` (`userId_id`),
  CONSTRAINT `fk_lemo_member_businessInfo_lemo_member_user1` FOREIGN KEY (`userId_id`) REFERENCES `lemo_member_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_businessinfo:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_coupon 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_coupon` (
  `mcp_id` int NOT NULL AUTO_INCREMENT,
  `cp_id` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `mcp_isUsed` tinyint(1) DEFAULT '0',
  `mcp_start` date DEFAULT NULL,
  `mcp_end` date DEFAULT NULL,
  `mcp_rdate` datetime DEFAULT NULL,
  PRIMARY KEY (`mcp_id`),
  KEY `fk_lemo_member_coupon_lemo__product_coupon1_idx` (`cp_id`),
  KEY `fk_lemo_member_coupon_lemo_member_userId1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_member_coupon_lemo__product_coupon1` FOREIGN KEY (`cp_id`) REFERENCES `lemo_product_coupon` (`cp_id`),
  CONSTRAINT `fk_lemo_member_coupon_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_coupon:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_coupon_log 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_coupon_log` (
  `cl_id` int NOT NULL AUTO_INCREMENT,
  `mcp_id` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `res_no` bigint NOT NULL,
  `dis_price` int NOT NULL,
  `cl_datetime` datetime NOT NULL,
  PRIMARY KEY (`cl_id`),
  KEY `fk_lemo_member_coupon_log_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_lemo_member_coupon_log_ lemo_product_reservation1_idx` (`res_no`),
  KEY `fk_lemo_member_coupon_log_lemo_member_coupon1_idx` (`mcp_id`),
  CONSTRAINT `fk_lemo_member_coupon_log_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`),
  CONSTRAINT `fk_lemo_member_coupon_log_lemo_member_coupon1` FOREIGN KEY (`mcp_id`) REFERENCES `lemo_member_coupon` (`mcp_id`),
  CONSTRAINT `fk_lemo_member_coupon_log_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_coupon_log:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_point_log 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_point_log` (
  `poi_id` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `res_no` bigint DEFAULT NULL,
  `poi_content` varchar(50) NOT NULL,
  `poi_point` int NOT NULL,
  `poi_maximum` date NOT NULL,
  `poi_state` tinyint(1) NOT NULL,
  `poi_rdate` datetime NOT NULL,
  PRIMARY KEY (`poi_id`),
  KEY `fk_lemo_member_point_log_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_lemo_member_point_log_ lemo_product_reservation1_idx` (`res_no`),
  CONSTRAINT `fk_lemo_member_point_log_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`),
  CONSTRAINT `fk_lemo_member_point_log_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_point_log:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_social 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_social` (
  `user_id` varchar(50) NOT NULL,
  `soci_typ` varchar(20) NOT NULL,
  `soci_token` varchar(50) NOT NULL,
  `email` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_lemo_member_social_lemo_member_userId1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_member_social_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_social:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_terms 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_terms` (
  `terms_no` int NOT NULL AUTO_INCREMENT,
  `termsType_no` int NOT NULL,
  `terms_title` varchar(50) NOT NULL,
  `terms_content` text NOT NULL,
  `terms_order` tinyint NOT NULL,
  PRIMARY KEY (`terms_no`),
  KEY `fk_lemo_member_terms_lemo_member_termsType1_idx` (`termsType_no`),
  CONSTRAINT `fk_lemo_member_terms_lemo_member_termsType1` FOREIGN KEY (`termsType_no`) REFERENCES `lemo_member_termstype` (`termsType_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_terms:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_termstype 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_termstype` (
  `termsType_no` int NOT NULL AUTO_INCREMENT,
  `termsType_type_ko` varchar(45) NOT NULL,
  `termsType_type_en` varchar(45) NOT NULL,
  PRIMARY KEY (`termsType_no`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_termstype:~0 rows (대략적) 내보내기
INSERT INTO `lemo_member_termstype` (`termsType_no`, `termsType_type_ko`, `termsType_type_en`) VALUES
	(1, '이용약관', 'terms'),
	(2, '만14세 이상 확인', 'over14yearsOldAgree'),
	(3, '개인정보 수집 및 이용(필수)', 'privacyRequire'),
	(4, '개인정보 수집 및 이용(선택)', 'privacySelect'),
	(5, '마케팅 수신 동의(선택)', 'marketingAgree'),
	(6, '위치정보 서비스 이용약관', 'location');

-- 테이블 lemo.lemo_member_user 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_user` (
  `user_id` varchar(50) NOT NULL,
  `pass` varchar(255) NOT NULL,
  PRIMARY KEY (`user_id`),
  KEY `fk_lemo_member_user_lemo_member_userId1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_member_user_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_user:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_member_userinfo 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_member_userinfo` (
  `user_id` varchar(50) NOT NULL,
  `hp` char(13) NOT NULL,
  `nick` varchar(30) NOT NULL,
  `photo` varchar(200) DEFAULT NULL,
  `type` tinyint(1) NOT NULL,
  `role` varchar(20) NOT NULL,
  `level` tinyint(1) DEFAULT '1',
  `point` int DEFAULT '0',
  `regip` varchar(100) NOT NULL,
  `isEnabled` tinyint(1) DEFAULT '1',
  `isLocked` tinyint(1) DEFAULT '1',
  `isPassNonExpired` tinyint(1) DEFAULT '1',
  `isNoticeEnabled` tinyint(1) DEFAULT '0',
  `isLocationEnabled` tinyint(1) DEFAULT '0',
  `isPrivacySelected` tinyint(1) DEFAULT '0',
  `memo` text,
  `udate` datetime DEFAULT NULL,
  `rdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `wdate` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `userId_nick_UNIQUE` (`nick`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_member_userinfo:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_accommodation 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_accommodation` (
  `acc_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `acc_name` varchar(100) NOT NULL,
  `accType_no` int NOT NULL,
  `province_no` int NOT NULL,
  `acc_city` varchar(10) NOT NULL,
  `acc_zip` varchar(5) NOT NULL,
  `acc_addr` varchar(255) NOT NULL,
  `acc_addrDetail` varchar(255) DEFAULT NULL,
  `acc_longtitude` decimal(20,15) NOT NULL,
  `acc_lattitude` decimal(20,15) NOT NULL,
  `acc_xy` point NOT NULL,
  `acc_mainInfo` varchar(50) DEFAULT NULL,
  `acc_info` text NOT NULL,
  `acc_comment` varchar(255) DEFAULT NULL,
  `acc_thumbs` text NOT NULL,
  `acc_rate` tinyint DEFAULT '1',
  `acc_review` int DEFAULT '0',
  `acc_discount` tinyint DEFAULT '0',
  `acc_checkIn` time NOT NULL,
  `acc_checkOut` time DEFAULT NULL,
  PRIMARY KEY (`acc_id`),
  KEY `fk_lemo_product_accommodation_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_lemo_product_accommodation_lemo_product_province1_idx` (`province_no`),
  KEY `fk_lemo_product_accommodation_ lemo_product_accommodationTy_idx` (`accType_no`),
  CONSTRAINT `fk_lemo_product_accommodation_ lemo_product_accommodationType1` FOREIGN KEY (`accType_no`) REFERENCES ` lemo_product_accommodationtype` (`accType_no`),
  CONSTRAINT `fk_lemo_product_accommodation_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`),
  CONSTRAINT `fk_lemo_product_accommodation_lemo_product_province1` FOREIGN KEY (`province_no`) REFERENCES `lemo_product_province` (`province_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_accommodation:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_coupon 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_coupon` (
  `cp_id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) NOT NULL,
  `acc_id` int NOT NULL,
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
  KEY `fk_lemo__product_coupon_lemo_member_userId1_idx` (`user_id`),
  KEY `fk_lemo_product_coupon_lemo_product_accommodation1_idx` (`acc_id`),
  CONSTRAINT `fk_lemo__product_coupon_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`),
  CONSTRAINT `fk_lemo_product_coupon_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_coupon:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_pick 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_pick` (
  `arti_no` int NOT NULL,
  `user_id` varchar(50) NOT NULL,
  PRIMARY KEY (`arti_no`,`user_id`),
  KEY `fk_lemo_product_pick_lemo_member_userId1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_product_pick_lemo_diary_article1` FOREIGN KEY (`arti_no`) REFERENCES `lemo_diary_article` (`arti_no`),
  CONSTRAINT `fk_lemo_product_pick_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_pick:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_province 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_province` (
  `province_no` int NOT NULL AUTO_INCREMENT,
  `province_name` varchar(10) NOT NULL,
  PRIMARY KEY (`province_no`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_province:~17 rows (대략적) 내보내기
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
  `user_id` varchar(50) NOT NULL,
  `pQna_title` varchar(255) NOT NULL,
  `pQna_content` text NOT NULL,
  `pQna_regip` varchar(100) NOT NULL,
  `pQna_rdate` datetime NOT NULL,
  `pQna_udate` datetime DEFAULT NULL,
  `pQna_reply` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`pQna_no`),
  KEY `fk_lemo_product_qna_lemo_product_accommodation1_idx` (`acc_id`),
  KEY `fk_lemo_product_qna_lemo_member_userId1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_product_qna_lemo_member_userId1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`),
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
  `rero_checkIn_date` date NOT NULL,
  `rero_checkOut_date` date NOT NULL,
  PRIMARY KEY (`rero_id`),
  KEY `fk_lemo_product_reserved_room_ lemo_product_reservation1_idx` (`res_no`),
  CONSTRAINT `fk_lemo_product_reserved_room_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_reserved_room:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_review 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_review` (
  `revi_id` int NOT NULL AUTO_INCREMENT,
  `res_no` bigint NOT NULL,
  `user_id` varchar(50) NOT NULL,
  `revi_rate` tinyint NOT NULL DEFAULT '1',
  `revi_title` varchar(255) NOT NULL,
  `revi_content` text NOT NULL,
  `revi_reply` varchar(255) DEFAULT NULL,
  `revi_regip` varchar(100) NOT NULL,
  `revi_rdate` datetime NOT NULL,
  `revi_thumb` text,
  PRIMARY KEY (`revi_id`,`res_no`),
  KEY `fk_lemo_product_review_ lemo_product_reservation1_idx` (`res_no`),
  KEY `fk_lemo_product_review_lemo_member_userInfo1_idx` (`user_id`),
  CONSTRAINT `fk_lemo_product_review_ lemo_product_reservation1` FOREIGN KEY (`res_no`) REFERENCES ` lemo_product_reservation` (`res_no`),
  CONSTRAINT `fk_lemo_product_review_lemo_member_userInfo1` FOREIGN KEY (`user_id`) REFERENCES `lemo_member_userinfo` (`user_id`)
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
  `room_info` text NOT NULL,
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
  `lemo_product_serviceCatecol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sc_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_servicecate:~0 rows (대략적) 내보내기

-- 테이블 lemo.lemo_product_servicereginfo 구조 내보내기
CREATE TABLE IF NOT EXISTS `lemo_product_servicereginfo` (
  `srg_id` int NOT NULL AUTO_INCREMENT,
  `sc_no` int NOT NULL,
  `acc_id` int NOT NULL,
  PRIMARY KEY (`srg_id`),
  KEY `fk_lemo_product_serviceRegInfo_lemo_product_accommodation1_idx` (`acc_id`),
  KEY `fk_lemo_product_serviceRegInfo_lemo_product_serviceCate1_idx` (`sc_no`),
  CONSTRAINT `fk_lemo_product_serviceRegInfo_lemo_product_accommodation1` FOREIGN KEY (`acc_id`) REFERENCES `lemo_product_accommodation` (`acc_id`),
  CONSTRAINT `fk_lemo_product_serviceRegInfo_lemo_product_serviceCate1` FOREIGN KEY (`sc_no`) REFERENCES `lemo_product_servicecate` (`sc_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

-- 테이블 데이터 lemo.lemo_product_servicereginfo:~0 rows (대략적) 내보내기

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
