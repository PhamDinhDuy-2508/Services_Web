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
-- Table structure for table `spring_session_attributes`
--

DROP TABLE IF EXISTS `spring_session_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `spring_session_attributes` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `spring_session` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `spring_session_attributes`
--

LOCK TABLES `spring_session_attributes` WRITE;
/*!40000 ALTER TABLE `spring_session_attributes` DISABLE KEYS */;
INSERT INTO `spring_session_attributes` VALUES ('90a04120-adbc-499b-86b6-85ec69abb997','jwt_code',_binary '¬\í\0t\0\äeyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoaWVwc2lydWE3NSIsInBhc3N3b3JkIjoie25vb3B9MjUwODIwMDAiLCJpZCI6MSwiZXhwIjoxNjg2MTg0ODUwLCJpYXQiOjE2ODYxNDg4NTB9.saHQWVlbvMTZ3uEt7V-9JXbPi21v3dPfQTXEKnh_z0-nqWDhWYIfz9jfu2VzEc2Pb0B16p0x0pXx7huZIKH0xQ'),('90a04120-adbc-499b-86b6-85ec69abb997','SPRING_SECURITY_SAVED_REQUEST',_binary '¬\í\0sr\0Aorg.springframework.security.web.savedrequest.DefaultSavedRequest\0\0\0\0\0\0:\0I\0\nserverPortL\0contextPatht\0Ljava/lang/String;L\0cookiest\0Ljava/util/ArrayList;L\0headerst\0Ljava/util/Map;L\0localesq\0~\0L\0methodq\0~\0L\0\nparametersq\0~\0L\0pathInfoq\0~\0L\0queryStringq\0~\0L\0\nrequestURIq\0~\0L\0\nrequestURLq\0~\0L\0schemeq\0~\0L\0\nserverNameq\0~\0L\0servletPathq\0~\0xp\0\0t\0\0sr\0java.util.ArrayListx\Ò™\Ça\0I\0sizexp\0\0\0w\0\0\0sr\09org.springframework.security.web.savedrequest.SavedCookie\0\0\0\0\0\0:\0I\0maxAgeZ\0secureI\0versionL\0commentq\0~\0L\0domainq\0~\0L\0nameq\0~\0L\0pathq\0~\0L\0valueq\0~\0xpÿÿÿÿ\0\0\0\0\0ppt\0\rIdea-76b2c85apt\0$7153fbd8-c0f1-435a-85e4-af3228643932sq\0~\0ÿÿÿÿ\0\0\0\0\0ppt\0SESSIONpt\00YWRhNDMwY2UtMmY1YS00YjljLWJkMmEtNjA4M2ZkYTgxN2Q2sq\0~\0ÿÿÿÿ\0\0\0\0\0ppt\0	login_jwtpt\0\äeyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoaWVwc2lydWE3NSIsInBhc3N3b3JkIjoie25vb3B9MjUwODIwMDAiLCJpZCI6MSwiZXhwIjoxNjg2MTg0ODUwLCJpYXQiOjE2ODYxNDg4NTB9.saHQWVlbvMTZ3uEt7V-9JXbPi21v3dPfQTXEKnh_z0-nqWDhWYIfz9jfu2VzEc2Pb0B16p0x0pXx7huZIKH0xQsq\0~\0ÿÿÿÿ\0\0\0\0\0ppt\0	save_passpt\00xsr\0java.util.TreeMapÁö>-%j\æ\0L\0\ncomparatort\0Ljava/util/Comparator;xpsr\0*java.lang.String$CaseInsensitiveComparatorw\\}\\P\å\Î\0\0xpw\0\0\0t\0acceptsq\0~\0\0\0\0w\0\0\0t\0*/*xt\0accept-encodingsq\0~\0\0\0\0w\0\0\0t\0gzip, deflate, brxt\0accept-languagesq\0~\0\0\0\0w\0\0\0t\08vi-VN,vi;q=0.9,fr-FR;q=0.8,fr;q=0.7,en-US;q=0.6,en;q=0.5xt\0\nconnectionsq\0~\0\0\0\0w\0\0\0t\0\nkeep-alivext\0cookiesq\0~\0\0\0\0w\0\0\0tiIdea-76b2c85a=7153fbd8-c0f1-435a-85e4-af3228643932; SESSION=YWRhNDMwY2UtMmY1YS00YjljLWJkMmEtNjA4M2ZkYTgxN2Q2; login_jwt=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoaWVwc2lydWE3NSIsInBhc3N3b3JkIjoie25vb3B9MjUwODIwMDAiLCJpZCI6MSwiZXhwIjoxNjg2MTg0ODUwLCJpYXQiOjE2ODYxNDg4NTB9.saHQWVlbvMTZ3uEt7V-9JXbPi21v3dPfQTXEKnh_z0-nqWDhWYIfz9jfu2VzEc2Pb0B16p0x0pXx7huZIKH0xQ; save_pass=0xt\0hostsq\0~\0\0\0\0w\0\0\0t\0localhost:8080xt\0referersq\0~\0\0\0\0w\0\0\0t\0Ghttp://localhost:8080/document_upload?code=AS2021&page=1&Filter=defaultxt\0	sec-ch-uasq\0~\0\0\0\0w\0\0\0t\0A\"Google Chrome\";v=\"113\", \"Chromium\";v=\"113\", \"Not-A.Brand\";v=\"24\"xt\0sec-ch-ua-mobilesq\0~\0\0\0\0w\0\0\0t\0?0xt\0sec-ch-ua-platformsq\0~\0\0\0\0w\0\0\0t\0	\"Windows\"xt\0sec-fetch-destsq\0~\0\0\0\0w\0\0\0t\0scriptxt\0sec-fetch-modesq\0~\0\0\0\0w\0\0\0t\0no-corsxt\0sec-fetch-sitesq\0~\0\0\0\0w\0\0\0t\0same-originxt\0\nuser-agentsq\0~\0\0\0\0w\0\0\0t\0oMozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36xxsq\0~\0\0\0\0w\0\0\0sr\0java.util.Locale~ø`œ0ù\ì\0I\0hashcodeL\0countryq\0~\0L\0\nextensionsq\0~\0L\0languageq\0~\0L\0scriptq\0~\0L\0variantq\0~\0xpÿÿÿÿt\0VNq\0~\0t\0viq\0~\0q\0~\0xsq\0~\0Eÿÿÿÿq\0~\0q\0~\0q\0~\0Hq\0~\0q\0~\0xsq\0~\0Eÿÿÿÿt\0FRq\0~\0t\0frq\0~\0q\0~\0xsq\0~\0Eÿÿÿÿq\0~\0q\0~\0q\0~\0Lq\0~\0q\0~\0xsq\0~\0Eÿÿÿÿt\0USq\0~\0t\0enq\0~\0q\0~\0xsq\0~\0Eÿÿÿÿq\0~\0q\0~\0q\0~\0Pq\0~\0q\0~\0xxt\0GETsq\0~\0pw\0\0\0\0xppt\0/vendor/jquery/jquery.min.jst\01http://localhost:8080/vendor/jquery/jquery.min.jst\0httpt\0	localhostt\0/vendor/jquery/jquery.min.js');
/*!40000 ALTER TABLE `spring_session_attributes` ENABLE KEYS */;
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
