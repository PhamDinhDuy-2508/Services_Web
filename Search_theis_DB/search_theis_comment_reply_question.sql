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
-- Table structure for table `comment_reply_question`
--

DROP TABLE IF EXISTS `comment_reply_question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comment_reply_question` (
  `idcomment_reply_question` int unsigned NOT NULL AUTO_INCREMENT,
  `content` text NOT NULL,
  `user_id` int NOT NULL,
  `reply_id` int NOT NULL,
  `Date` date DEFAULT NULL,
  PRIMARY KEY (`idcomment_reply_question`),
  KEY `replt_id_FK_idx` (`reply_id`),
  KEY `user_id_FK_idx` (`user_id`),
  CONSTRAINT `replt_id_FK` FOREIGN KEY (`reply_id`) REFERENCES `reply` (`idreply`),
  CONSTRAINT `user_id_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comment_reply_question`
--

LOCK TABLES `comment_reply_question` WRITE;
/*!40000 ALTER TABLE `comment_reply_question` DISABLE KEYS */;
INSERT INTO `comment_reply_question` VALUES (1,'check',1,1,'2022-11-11'),(2,'hello form duy',1,2,'2022-11-11'),(3,'phamdinhduy_test',1,2,'2022-11-11'),(4,'check',1,2,'2022-11-11'),(5,'check',1,2,'2022-11-11'),(6,'check',1,1,'2022-11-11'),(7,'testasdsd',1,3,'2022-11-12'),(8,'check',1,4,'2022-11-12'),(9,'test',1,5,'2022-11-13'),(10,'oke done !',1,8,'2022-11-24'),(11,'hello',1,5,'2023-05-05');
/*!40000 ALTER TABLE `comment_reply_question` ENABLE KEYS */;
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
