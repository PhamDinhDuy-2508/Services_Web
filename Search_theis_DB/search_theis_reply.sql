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
-- Table structure for table `reply`
--

DROP TABLE IF EXISTS `reply`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reply` (
  `idreply` int NOT NULL AUTO_INCREMENT,
  `Date_create` date NOT NULL,
  `use_id` int DEFAULT NULL,
  `Question_id` int NOT NULL,
  `Content` text NOT NULL,
  `user_user_id` int DEFAULT NULL,
  `like` int DEFAULT NULL,
  `dislike` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idreply`),
  UNIQUE KEY `idreply_UNIQUE` (`idreply`),
  KEY `FKdegfm6syq4wn4syd0koe1oghe` (`user_user_id`),
  KEY `User_reply_FK_idx` (`use_id`),
  KEY `question_FK_idx` (`Question_id`),
  CONSTRAINT `FKdegfm6syq4wn4syd0koe1oghe` FOREIGN KEY (`user_user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `question_FK` FOREIGN KEY (`Question_id`) REFERENCES `question` (`id_question`),
  CONSTRAINT `User_reply_FK` FOREIGN KEY (`use_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reply`
--

LOCK TABLES `reply` WRITE;
/*!40000 ALTER TABLE `reply` DISABLE KEYS */;
INSERT INTO `reply` VALUES (1,'2022-11-11',NULL,1,'check',1,NULL,NULL),(2,'2022-11-11',NULL,1,'testasdasdasd',1,NULL,NULL),(3,'2022-11-12',NULL,2,'checkasdasd',1,NULL,NULL),(4,'2022-11-12',NULL,3,'testasd',1,NULL,NULL),(5,'2022-11-13',NULL,4,'check',1,NULL,NULL),(6,'2022-11-14',NULL,5,'hello',1,NULL,NULL),(7,'2022-11-15',NULL,9,'xheck',1,NULL,NULL),(8,'2022-11-24',NULL,25,'cgecjasdasdasd<div><br></div>',1,NULL,NULL);
/*!40000 ALTER TABLE `reply` ENABLE KEYS */;
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
