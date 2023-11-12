-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: search_theis
-- ------------------------------------------------------
-- Server version	8.0.31

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `category_document`
--

DROP TABLE IF EXISTS `category_document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category_document` (
  `category_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `root_id` int NOT NULL,
  `code` varchar(45) NOT NULL,
  PRIMARY KEY (`category_id`,`code`),
  UNIQUE KEY `category_id_UNIQUE` (`category_id`),
  KEY `root_id` (`root_id`),
  KEY `root_id_FK` (`root_id`),
  KEY `root_id_FK123` (`root_id`),
  CONSTRAINT `root_CATEGORY` FOREIGN KEY (`root_id`) REFERENCES `root_folder` (`idroot_folder`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category_document`
--

LOCK TABLES `category_document` WRITE;
/*!40000 ALTER TABLE `category_document` DISABLE KEYS */;
INSERT INTO `category_document` VALUES (1,'Kỹ thuật lập trình cơ kỹ thuật	',2,'AS2003'),(2,'Cơ lý thuyết	',2,'AS1003'),(3,'Cơ lý thuyết nâng cao',2,'AS2005'),(4,'Lý thuyết đàn hồi	',2,'AS2011'),(5,'CAD ứng dụng',2,'AS2013'),(6,'Thực tập 1',2,'AS2015'),(7,'Phương pháp phần tử hữu hạn cơ kỹ thuật',2,'AS3015'),(8,'Thiết kế kỹ thuật	',2,'AS2021'),(9,'Cơ học vật rắn biến dạng	',2,'AS3083'),(10,'Vẽ Kỹ Thuật',1,'CI1003	'),(28,'Mô hình hóa động lực học cơ hệ',2,'AS3039'),(30,'Phạm Đình Duy',2,'AS3032');
/*!40000 ALTER TABLE `category_document` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-08  8:22:45
