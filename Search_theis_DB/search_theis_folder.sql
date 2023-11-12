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
-- Table structure for table `folder`
--

DROP TABLE IF EXISTS `folder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `folder` (
  `id_folder` int NOT NULL AUTO_INCREMENT,
  `Contributor_id` int DEFAULT NULL,
  `Title` varchar(45) NOT NULL,
  `Category_id_key` int DEFAULT NULL,
  `Publish_date` date DEFAULT NULL,
  PRIMARY KEY (`id_folder`),
  UNIQUE KEY `idFolder_UNIQUE` (`id_folder`),
  KEY `category_id_idx` (`Category_id_key`),
  CONSTRAINT `category_id_fk_key` FOREIGN KEY (`Category_id_key`) REFERENCES `category_document` (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `folder`
--

LOCK TABLES `folder` WRITE;
/*!40000 ALTER TABLE `folder` DISABLE KEYS */;
INSERT INTO `folder` VALUES (2,1,'Test',28,'2022-11-19'),(6,1,'Test1',8,'2022-11-21'),(7,1,'test2',4,'2022-11-23'),(8,1,'Test4',28,'2022-11-23'),(9,1,'test3',6,'2022-11-23'),(10,1,'check',28,'2022-11-23'),(11,1,'test',6,'2022-11-24'),(12,1,'phamdinhduy',8,'2022-11-24'),(13,1,'phamdinhduy2508',8,'2023-06-04'),(14,1,'67213',8,'2023-06-04'),(15,1,'phamdinh',8,'2023-06-04'),(16,1,'phamdinhsdsdsd',8,'2023-06-04'),(17,1,'okkasdasd',6,'2023-06-04'),(18,1,'okkasdasd',8,'2023-06-04'),(19,1,'okkasdasd123123123',8,'2023-06-04');
/*!40000 ALTER TABLE `folder` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-08  8:22:46
