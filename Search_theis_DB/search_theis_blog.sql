

DROP TABLE IF EXISTS `blog`;

CREATE TABLE `blog` (
  `blogId` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `Title` text,
  `CreateDate` date DEFAULT NULL,
  `FixDate` date DEFAULT NULL,
  `Summary` text,
  `Content` text,
  `CategoryId` int DEFAULT NULL,
  PRIMARY KEY (`blogId`),
  KEY `user_id_fk_idx` (`user_id`),
  CONSTRAINT `user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
LOCK TABLES `blog` WRITE;
UNLOCK TABLES;


