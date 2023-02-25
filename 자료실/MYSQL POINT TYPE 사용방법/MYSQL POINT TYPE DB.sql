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


-- test 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `test`;

-- 테이블 test.search_by_coordinates 구조 내보내기
CREATE TABLE IF NOT EXISTS `search_by_coordinates` (
  `pno` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `area` varchar(50) DEFAULT NULL,
  `LONGTITUDE` decimal(20,6) DEFAULT NULL,
  `LATITUDE` decimal(20,6) DEFAULT NULL,
  `xy` point DEFAULT NULL,
  PRIMARY KEY (`pno`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 테이블 데이터 test.search_by_coordinates:~8 rows (대략적) 내보내기
INSERT INTO `search_by_coordinates` (`pno`, `name`, `area`, `LONGTITUDE`, `LATITUDE`, `xy`) VALUES
	(4, '삼성역', '서울', 127.060955, 37.508865, _binary 0x000000000101000000c6e1ccafe6c35f401e8a027d22c14240),
	(5, '경성대/부경대역', '부산', 129.100783, 35.136679, _binary 0x000000000101000000b1f9809c392360405c3d37b27e914140),
	(6, '서면역', '부산', 129.059529, 35.157875, _binary 0x0000000001010000008bd885a8e721604026cce93d35944140),
	(7, '기장역', '부산', 129.218527, 35.244666, _binary 0x000000000101000000171a522dfe266040fa2bd73a519f4140),
	(8, '다대포해수욕장역', '부산', 128.964976, 35.048331, _binary 0x000000000101000000f3481616e11e60401d499eb22f864140),
	(9, '부산시민공원', '부산', 129.057114, 35.168244, _binary 0x000000000101000000fbc731e0d32160402cf9770789954140),
	(10, '노포역', '부산', 129.095050, 35.283956, _binary 0x00000000010100000065c3b9a60a2360407668cfa758a44140),
	(11, '김해시청', '김해', 128.889401, 35.228551, _binary 0x00000000010100000029f3aaf8751c6040b3e90127419d4140);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
