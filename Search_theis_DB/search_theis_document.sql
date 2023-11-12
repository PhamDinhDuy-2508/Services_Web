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
-- Table structure for table `document`
--

DROP TABLE IF EXISTS `document`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `document` (
  `id_document` int NOT NULL AUTO_INCREMENT,
  `Title` varchar(256) NOT NULL,
  `Publish_date` date NOT NULL,
  `File` varchar(256) NOT NULL,
  `Author` varchar(45) DEFAULT NULL,
  `Id_folder` int DEFAULT NULL,
  `PATH` varchar(256) DEFAULT NULL,
  `id_document_drive` varchar(256) NOT NULL,
  `FolderId` int DEFAULT NULL,
  `folder_id` int DEFAULT NULL,
  PRIMARY KEY (`id_document`),
  UNIQUE KEY `idDocument_UNIQUE` (`id_document`),
  KEY `DocumentName` (`Title`,`Publish_date`,`Author`),
  KEY `folder_id_key_FK_idx` (`Id_folder`,`FolderId`),
  KEY `folder_ID_FK_idx` (`FolderId`),
  KEY `FKfgao4bvuaqv7qwlvv456p5xuh` (`folder_id`),
  CONSTRAINT `FKfgao4bvuaqv7qwlvv456p5xuh` FOREIGN KEY (`folder_id`) REFERENCES `folder` (`id_folder`),
  CONSTRAINT `folder_ID_FK` FOREIGN KEY (`FolderId`) REFERENCES `folder` (`id_folder`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `document`
--

LOCK TABLES `document` WRITE;
/*!40000 ALTER TABLE `document` DISABLE KEYS */;
INSERT INTO `document` VALUES (8,'electronics-09-00117.pdf','2022-11-21','1WwuVwpMys1PQQWckjhzlQExBUuFBmukn',NULL,6,'https://drive.google.com/file/d/1BpghowAtabDhUvtRAGem-h2wFmbSLyNh/view?usp=drivesdk','1BpghowAtabDhUvtRAGem-h2wFmbSLyNh',0,NULL),(10,'mang-may-tinh_pham-tran-vu_mmt1-lec10-11_application-layer.pdf','2022-11-23','1kaUe173DrRNbIQ0IJVJJxZ3znpQ33C-V',NULL,2,'https://drive.google.com/file/d/1hErw8XTiigUh1fGtLW8_pKpPNCnDBm2l/view?usp=drivesdk','1hErw8XTiigUh1fGtLW8_pKpPNCnDBm2l',0,NULL),(11,'mang-may-tinh_pham-tran-vu_mmt1-lec10-11_application-layer.pdf','2022-11-23','1yhFF1ugnRNBalbqJ44Ue4oU1CalZ--vz',NULL,8,'https://drive.google.com/file/d/18jxYz1JKa7mrOCX3z6vLzZg85aVCsR68/view?usp=drivesdk','18jxYz1JKa7mrOCX3z6vLzZg85aVCsR68',0,NULL),(12,'mang-may-tinh_pham-tran-vu_mmt1-lec10-11_application-layer.pdf','2022-11-23','1Mj0gK_lO_63DevVaUi_3m2b58I2D0IXv',NULL,7,'https://drive.google.com/file/d/1JAX9EumAmZb4z3P-fxiF1nnnK7tUWu5C/view?usp=drivesdk','1JAX9EumAmZb4z3P-fxiF1nnnK7tUWu5C',0,NULL),(13,'Sách ETS 1000 LC (2019).pdf','2022-11-23','1vZxki2_WkySgZTt-90FTojUat9_HM_Lq',NULL,9,'https://drive.google.com/file/d/1ipMsUbtyq6GqXhyCs__ebLQlqm5pfK6Z/view?usp=drivesdk','1ipMsUbtyq6GqXhyCs__ebLQlqm5pfK6Z',0,NULL),(14,'Shape_steel_C0.png','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1Dc3bQVFI5cVAod05Q98va0TMyD9MSqa-/view?usp=drivesdk','1Dc3bQVFI5cVAod05Q98va0TMyD9MSqa-',0,NULL),(15,'shapeC_45_thresh.png','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1BpEduaJAn7QVYAOE5_s37oN3mbESYtgY/view?usp=drivesdk','1BpEduaJAn7QVYAOE5_s37oN3mbESYtgY',0,NULL),(16,'shapeC_45_thresh_border.png','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1QH6MMOvY5Kdnlo_BGFQMYSa1AAjU1c-3/view?usp=drivesdk','1QH6MMOvY5Kdnlo_BGFQMYSa1AAjU1c-3',0,NULL),(17,'Shape_Steel_C_3.png','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1hUUu1d40NqK_xuy3sYtPXXELWWmwGAp5/view?usp=drivesdk','1hUUu1d40NqK_xuy3sYtPXXELWWmwGAp5',0,NULL),(18,'Sách ETS 1000 LC (2019).pdf','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1Ah0Wu4CScGLMRW-lWFHzL3tDKBnN1WiL/view?usp=drivesdk','1Ah0Wu4CScGLMRW-lWFHzL3tDKBnN1WiL',0,NULL),(19,'mang-may-tinh_pham-tran-vu_mmt1-lec10-11_application-layer.pdf','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1hu95nV0CboVnSn83Tmi5POP06hpjoai8/view?usp=drivesdk','1hu95nV0CboVnSn83Tmi5POP06hpjoai8',0,NULL),(20,'2021_MT_KHM_KHMT (2).pdf','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1swfTP95pJEpT8FkD8VBSF1GV_zSY63QT/view?usp=drivesdk','1swfTP95pJEpT8FkD8VBSF1GV_zSY63QT',0,NULL),(21,'mang-may-tinh_pham-tran-vu_mmt1-lec10-11_application-layer.pdf','2022-11-23','1Xu9MvyhAp5OLKrG0yoodlAjaI5A53wJW',NULL,9,'https://drive.google.com/file/d/1iV1J3OAqDeFe2V72pVGQT10hyRjYNOZO/view?usp=drivesdk','1iV1J3OAqDeFe2V72pVGQT10hyRjYNOZO',0,NULL),(22,'2021_MT_KHM_KHMT (2).pdf','2022-11-23','1k_6DE8bpreCGrUMe-YOagKddJAm3ZTw2',NULL,9,'https://drive.google.com/file/d/1hWFcacOORBrxiNSMvDYmss2_-X6nC3pJ/view?usp=drivesdk','1hWFcacOORBrxiNSMvDYmss2_-X6nC3pJ',0,NULL),(23,'mang-may-tinh_pham-tran-vu_mmt1-lec10-11_application-layer.pdf','2022-11-23','1k_6DE8bpreCGrUMe-YOagKddJAm3ZTw2',NULL,9,'https://drive.google.com/file/d/1JqudGXMJ_8iUpZ7JU9vAOpgQZWXvinir/view?usp=drivesdk','1JqudGXMJ_8iUpZ7JU9vAOpgQZWXvinir',0,NULL),(24,'electronics-09-00117.pdf','2022-11-23','1kaUe173DrRNbIQ0IJVJJxZ3znpQ33C-V',NULL,2,'https://drive.google.com/file/d/1U3xZj6JlgNXkbkuJX6TtiwRqRPq_3cwh/view?usp=drivesdk','1U3xZj6JlgNXkbkuJX6TtiwRqRPq_3cwh',0,NULL),(25,'2021_MT_KHM_KHMT (2).pdf','2022-11-23','1t8xpGTtxjMaYb0xHn_AMdC3SeTzU8Xej',NULL,10,'https://drive.google.com/file/d/1buZpLX7sQFW4DbzlqRqTcTX7MmsR_cWM/view?usp=drivesdk','1buZpLX7sQFW4DbzlqRqTcTX7MmsR_cWM',0,NULL),(26,'Sách ETS 1000 LC (2019).pdf','2022-11-23','1sSCH7c8lWUGiSfb-EapbXv4wcvGlVCEm',NULL,10,'https://drive.google.com/file/d/1qSPzByU3eGFlBnioyuDk4tedjej35gF4/view?usp=drivesdk','1qSPzByU3eGFlBnioyuDk4tedjej35gF4',0,NULL),(27,'2021_MT_KHM_KHMT (2).pdf','2022-11-24','1kaUe173DrRNbIQ0IJVJJxZ3znpQ33C-V',NULL,2,'https://drive.google.com/file/d/1pYdFBt48I9cMKCetohqrbmEdWynEwhM7/view?usp=drivesdk','1pYdFBt48I9cMKCetohqrbmEdWynEwhM7',0,NULL),(29,'mang-may-tinh_pham-tran-vu_mmt1-lec4_networking-technologies-(cont) - [cuuduongthancong.com] (4).pdf','2022-11-24','1xtanOtiVKvDXX0OdVpNKV6atP2oFKp9X',NULL,12,'https://drive.google.com/file/d/1tW2xm4HRoN2QJXPqzcpc8Vs4CdHAtgZl/view?usp=drivesdk','1tW2xm4HRoN2QJXPqzcpc8Vs4CdHAtgZl',0,NULL);
/*!40000 ALTER TABLE `document` ENABLE KEYS */;
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
