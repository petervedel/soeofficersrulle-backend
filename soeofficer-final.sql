-- MySQL dump 10.13  Distrib 5.5.61, for Linux (x86_64)
--
-- Host: nordkerndk.csk1n1qcyjd7.eu-central-1.rds.amazonaws.com    Database: soeofficer
-- ------------------------------------------------------
-- Server version	5.6.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `DATABASECHANGELOG`
--

DROP TABLE IF EXISTS `DATABASECHANGELOG`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOG` (
  `ID` varchar(255) NOT NULL,
  `AUTHOR` varchar(255) NOT NULL,
  `FILENAME` varchar(255) NOT NULL,
  `DATEEXECUTED` datetime NOT NULL,
  `ORDEREXECUTED` int(11) NOT NULL,
  `EXECTYPE` varchar(10) NOT NULL,
  `MD5SUM` varchar(35) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `COMMENTS` varchar(255) DEFAULT NULL,
  `TAG` varchar(255) DEFAULT NULL,
  `LIQUIBASE` varchar(20) DEFAULT NULL,
  `CONTEXTS` varchar(255) DEFAULT NULL,
  `LABELS` varchar(255) DEFAULT NULL,
  `DEPLOYMENT_ID` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOG`
--

LOCK TABLES `DATABASECHANGELOG` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOG` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOG` VALUES ('createUserTable','morten','liquibase/user.xml','2018-06-17 15:50:12',1,'EXECUTED','7:b46a1bd8616bfa506409ca5962fda176','createTable tableName=user','',NULL,'3.5.3',NULL,NULL,'9243411937'),('createPersonTable','morten','liquibase/person.xml','2018-06-17 15:50:12',2,'EXECUTED','7:cf45e858fcdb072540654fc3e6153be5','createTable tableName=person','',NULL,'3.5.3',NULL,NULL,'9243411937'),('createIsParentTable','morten','liquibase/relation.xml','2018-06-17 15:50:12',3,'EXECUTED','7:55ea4266bdf4e67d4555cabe8b739029','createTable tableName=relation','',NULL,'3.5.3',NULL,NULL,'9243411937'),('createIsPromotedTable','morten','liquibase/is_promoted.xml','2018-06-17 15:50:12',4,'EXECUTED','7:417843876ecb64f056806f59bc35007e','createTable tableName=is_promoted','',NULL,'3.5.3',NULL,NULL,'9243411937'),('createRankTable','morten','liquibase/rank.xml','2018-06-17 15:50:12',5,'EXECUTED','7:1ddd05ac247a0d4c8fc9cae500f53c3d','createTable tableName=rank','',NULL,'3.5.3',NULL,NULL,'9243411937'),('createOfficerTable','morten','liquibase/officer.xml','2018-06-17 15:50:12',6,'EXECUTED','7:70ebca624a9713530e279c048568c9c8','createTable tableName=officer','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addUniqueConstraint-email','morten','liquibase/constraints.xml','2018-06-17 15:50:12',7,'EXECUTED','7:ad56ce5877ab1957e1ab6ef52e6b9454','addUniqueConstraint constraintName=const_email, tableName=user','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addUniqueConstraint-username','morten','liquibase/constraints.xml','2018-06-17 15:50:12',8,'EXECUTED','7:65f77f84cc4a66abb046f7469478d788','addUniqueConstraint constraintName=const_user, tableName=user','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addForeignKeyConstraint-person','morten','liquibase/constraints.xml','2018-06-17 15:50:12',9,'EXECUTED','7:01490281d165b6b7593a3e0c0989a0b2','addForeignKeyConstraint baseTableName=officer, constraintName=fk_officer_person, referencedTableName=person','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addForeignKeyConstraint-relation-parent','morten','liquibase/constraints.xml','2018-06-17 15:50:12',10,'EXECUTED','7:9a055b68a175b5d8fa3dfa99e7817f92','addForeignKeyConstraint baseTableName=relation, constraintName=fk_parent_person, referencedTableName=person','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addForeignKeyConstraint-relation-child','morten','liquibase/constraints.xml','2018-06-17 15:50:12',11,'EXECUTED','7:3171d93a42ec0f89e4c582e0afc66ab1','addForeignKeyConstraint baseTableName=relation, constraintName=fk_child_person, referencedTableName=person','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addForeignKeyConstraint-is-promoted','morten','liquibase/constraints.xml','2018-06-17 15:50:12',12,'EXECUTED','7:2775d2271099940245a28eae2d27bc73','addForeignKeyConstraint baseTableName=is_promoted, constraintName=fk_officer_promoted, referencedTableName=officer','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addForeignKeyConstraint-is-promoted2','morten','liquibase/constraints.xml','2018-06-17 15:50:12',13,'EXECUTED','7:719bd464701a51e5395c7086ae91056c','addForeignKeyConstraint baseTableName=is_promoted, constraintName=fk_promotion_rank, referencedTableName=rank','',NULL,'3.5.3',NULL,NULL,'9243411937'),('addUniqueConstraint-officer','morten','liquibase/constraints.xml','2018-06-17 15:50:12',14,'EXECUTED','7:3674d45f4e9f09c28c1e8a7b72a5556d','addUniqueConstraint constraintName=serial_number_constraint, tableName=officer','',NULL,'3.5.3',NULL,NULL,'9243411937'),('rows','morten','liquibase/inserts.xml','2018-06-17 15:50:12',15,'EXECUTED','7:f410df182425dfa4f943bdf3113805ed','insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=rank; insert tableName=...','',NULL,'3.5.3',NULL,NULL,'9243411937');
/*!40000 ALTER TABLE `DATABASECHANGELOG` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `DATABASECHANGELOGLOCK`
--

DROP TABLE IF EXISTS `DATABASECHANGELOGLOCK`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `DATABASECHANGELOGLOCK` (
  `ID` int(11) NOT NULL,
  `LOCKED` bit(1) NOT NULL,
  `LOCKGRANTED` datetime DEFAULT NULL,
  `LOCKEDBY` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `DATABASECHANGELOGLOCK`
--

LOCK TABLES `DATABASECHANGELOGLOCK` WRITE;
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` DISABLE KEYS */;
INSERT INTO `DATABASECHANGELOGLOCK` VALUES (1,'\0',NULL,NULL);
/*!40000 ALTER TABLE `DATABASECHANGELOGLOCK` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `is_promoted`
--

DROP TABLE IF EXISTS `is_promoted`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `is_promoted` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_of_promotion` date NOT NULL,
  `officer_id` int(11) NOT NULL,
  `rank_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_officer_promoted` (`officer_id`),
  KEY `fk_promotion_rank` (`rank_id`),
  CONSTRAINT `fk_officer_promoted` FOREIGN KEY (`officer_id`) REFERENCES `officer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_promotion_rank` FOREIGN KEY (`rank_id`) REFERENCES `rank` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `is_promoted`
--

LOCK TABLES `is_promoted` WRITE;
/*!40000 ALTER TABLE `is_promoted` DISABLE KEYS */;
INSERT INTO `is_promoted` VALUES (23,'1910-09-24',24,1),(24,'1911-04-15',24,3),(25,'2018-08-15',22,1),(26,'2018-08-15',22,2),(27,'2018-08-15',22,4),(28,'2018-08-15',22,5),(29,'1910-09-24',25,1),(30,'1911-04-15',25,3),(31,'1871-05-10',26,1),(33,'2018-08-20',10,1),(34,'2018-08-20',10,2),(35,'2018-08-20',10,4),(36,'2018-08-20',10,5),(37,'2018-08-20',10,7),(38,'2018-08-20',10,8),(39,'2018-08-20',10,9),(40,'2018-08-20',10,11),(41,'2018-08-20',10,13),(46,'1916-09-15',27,1),(47,'1917-10-01',27,3),(53,'1923-07-01',27,5),(54,'1932-11-01',27,7),(55,'1939-04-01',27,8),(56,'1941-07-25',27,11),(57,'1941-09-01',27,13),(58,'1920-05-01',24,6),(59,'1923-07-01',24,7),(68,'1945-05-01',27,14),(69,'1923-07-01',25,5),(76,'1900-02-01',23,1),(77,'1900-01-05',23,3),(78,'1900-01-01',23,6),(79,'1900-01-01',23,9),(80,'1900-01-01',23,13);
/*!40000 ALTER TABLE `is_promoted` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `officer`
--

DROP TABLE IF EXISTS `officer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `officer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serial_number1` int(11) DEFAULT NULL,
  `serial_number2` int(11) DEFAULT NULL,
  `appointed_date` date DEFAULT NULL,
  `appointed_until` date DEFAULT '9999-12-31',
  `termination_cause` enum('Terminated','Killed_in_action','Accidental_death','Transferred','Other') DEFAULT 'Terminated',
  `person_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serial_number_constraint` (`serial_number1`),
  KEY `fk_officer_person` (`person_id`),
  CONSTRAINT `fk_officer_person` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `officer`
--

LOCK TABLES `officer` WRITE;
/*!40000 ALTER TABLE `officer` DISABLE KEYS */;
INSERT INTO `officer` VALUES (10,235476783,23656845,'2018-08-20','9999-12-31',NULL,3),(22,123123,111222,'2018-08-15','9999-12-31',NULL,26),(23,1375,123123123,'1900-02-01','1932-06-30',NULL,37),(24,1376,NULL,'1910-09-24','9999-12-31',NULL,28),(25,1377,NULL,'1910-09-24','1948-02-29',NULL,31),(26,1179,NULL,'1871-05-10','1892-01-01',NULL,32),(27,1412,NULL,'1916-09-15','1958-05-31',NULL,34);
/*!40000 ALTER TABLE `officer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `person`
--

DROP TABLE IF EXISTS `person`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `person` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `given_name` varchar(40) NOT NULL,
  `surname` varchar(40) NOT NULL,
  `date_of_birth` date NOT NULL,
  `gender` enum('Male','Female','Unknown') NOT NULL,
  `date_of_death` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `person`
--

LOCK TABLES `person` WRITE;
/*!40000 ALTER TABLE `person` DISABLE KEYS */;
INSERT INTO `person` VALUES (1,'Anders','Karlsen','9999-01-01','Male',NULL),(2,'aawdCharlotte','Dottir','9999-01-01','Female',NULL),(3,'Ib','Hansenawd','1924-01-01','Male',NULL),(4,'Jens','Jensen','1940-05-02','Male',NULL),(5,'Jakob','Kristoffersen','1955-10-05','Male',NULL),(6,'Johanne','Sand Ramussen','1910-12-10','Female','1950-09-05'),(7,'Sven Aage','Albeck','1888-03-11','Male',NULL),(8,'Axel','Hoeck','1889-02-05','Male',NULL),(9,'Vilhelm Theodor','Albeck','1858-01-01','Male',NULL),(10,'Anna Augusta','Prætorius','1865-11-11','Female','1917-10-10'),(11,'Hans Grove','Bildsøe','1888-02-24','Male',NULL),(12,'Jens Arnold','Bildsøe','1849-07-24','Male',NULL),(13,'Jacobsen','Bente','1940-06-01','Female','2018-07-30'),(14,'qwlkmd','lkamwd','2018-08-05','Male',NULL),(15,'wqlkmd','alwkdm','2018-08-05','Male',NULL),(16,'wq,md','amwd','2018-08-05','Male',NULL),(17,'qwlkdm','lawmkd','2018-08-05','Male',NULL),(18,'lkmqwd','lkamwd','2018-08-05','Male',NULL),(19,'qlwkdm','lkamwd','2018-08-05','Male',NULL),(20,'qwlkdmq','lkamdlkawmd','2018-08-05','Male',NULL),(21,'lkmwqd','æamwdlka','2018-08-05','Male',NULL),(22,'wqldk','adawnd','2018-08-05','Male','2018-08-05'),(23,'Bente','hejsen','2018-08-05','Male',NULL),(24,'bjarne','bentebjarne','1900-01-01','Male',NULL),(26,'Frank','Morten','1987-01-01','Male',NULL),(27,'Sven Aage','Albeck','1888-03-11','Male','1969-07-25'),(28,'Axel','Hoeck','1989-02-05','Male','1915-07-27'),(30,'dsadas','dsads','2018-08-15','Male',NULL),(31,'Hans Grove','Bildsøe','1888-02-24','Male',NULL),(32,'Jens Arnold','Bildsøe','1849-07-24','Male',NULL),(34,'Aage Helgesen','Vedel','1894-09-01','Male','1981-02-09'),(35,'Helge','Vedel','1500-01-01','Male',NULL),(36,'Charlotte Serene','Braëm','1500-01-01','Female',NULL),(37,'Sven Aage','Albeck','1888-03-11','Male','1969-07-25');
/*!40000 ALTER TABLE `person` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rank`
--

DROP TABLE IF EXISTS `rank`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rank` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nato_code` int(11) NOT NULL,
  `rank_name` varchar(25) NOT NULL,
  `rank_name_en` varchar(25) NOT NULL,
  `rank_valid_from` date NOT NULL,
  `rank_valid_until` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rank`
--

LOCK TABLES `rank` WRITE;
/*!40000 ALTER TABLE `rank` DISABLE KEYS */;
INSERT INTO `rank` VALUES (1,1,'Sekondløjtnant','Sekondløjtnant','1868-01-02','1922-12-31'),(2,1,'Søløjtnant 2. grad','Søløjtnant 2. grad','1923-01-01','9999-12-31'),(3,2,'Premierløjtnant','Premierløjtnant','1868-01-02','1922-12-31'),(4,2,'Søløjtnant 1. grad','Søløjtnant 1. grad','1923-01-01','9999-12-31'),(5,3,'Kaptajnløjtnant','Kaptajnløjtnant','1922-01-02','9999-12-31'),(6,4,'Kaptajn','Kaptajn','1868-01-02','1922-12-31'),(7,4,'Orlogskaptajn','Orlogskaptajn','1923-01-01','9999-12-31'),(8,5,'Kommandørkaptajn','Kommandørkaptajn','1923-01-01','9999-12-31'),(9,6,'Kommandør','Kommandør','1868-01-02','9999-12-31'),(10,7,'Kontreadmiral','Kontreadmiral','1771-01-02','1868-12-31'),(11,7,'Kontreadmiral','Kontreadmiral','1923-01-02','9999-12-31'),(12,8,'Admiralens lieutenant','Admiralens lieutenant','1565-01-01','1566-07-01'),(13,8,'Viceadmiral','Viceadmiral','1566-07-02','9999-12-31'),(14,9,'Admiralen','Admiralen','1535-01-01','9999-12-31');
/*!40000 ALTER TABLE `rank` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `relation`
--

DROP TABLE IF EXISTS `relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `relation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `parent_id` int(11) NOT NULL,
  `child_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_parent_person` (`parent_id`),
  KEY `fk_child_person` (`child_id`),
  CONSTRAINT `fk_child_person` FOREIGN KEY (`child_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_parent_person` FOREIGN KEY (`parent_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `relation`
--

LOCK TABLES `relation` WRITE;
/*!40000 ALTER TABLE `relation` DISABLE KEYS */;
INSERT INTO `relation` VALUES (10,3,2),(11,9,7),(12,10,7),(13,12,11),(14,2,1),(17,35,34),(18,36,34);
/*!40000 ALTER TABLE `relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(25) NOT NULL,
  `password` varchar(250) NOT NULL,
  `email` varchar(50) NOT NULL,
  `from_date` date NOT NULL,
  `until_date` date NOT NULL,
  `role` enum('contributor','administrator','read','privileged_read') NOT NULL DEFAULT 'read',
  PRIMARY KEY (`id`),
  UNIQUE KEY `const_email` (`email`),
  UNIQUE KEY `const_user` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','6X7hSi7yjxFhsT4gsE2gSeOpaWILniFmvnEjDvD/o+M=$l8SLrgeAMypmE0zlhY14c66N7k5oiZuJR2VU8P/qpto=','kontakt@nordkern.dk','2018-03-20','9999-12-31','administrator'),(2,'bente','N2My/Fnv6uW+5Y2laU+BNvhtPCPpMjrQOUbiBqJks2U=$pCufvxJ5SQJO3CW+rGoX3iDU+g7LYToLOVUyS8uYsjk=','test@test.com','2018-07-13','2099-07-13','contributor'),(3,'bi','57Cv8hPgM9sCZRqBk7/TYKD8aERbLSpps8zoaMWxaE4=$QQyIBoT6i8Ti3VCB7ewLpcI84JddxUroppdkoNB/cMs=','wqldm@qwdq.com','2018-08-05','2018-09-01','contributor'),(4,'bi2','AI0pW0LxmavKI9gAFfRbcnO5wWIoH+pF0FPB9qu6CWs=$wqOXWNnBqMz2WmYRmswfJd/kxbnrUI/YIVGfhnMgXAY=','wdqdq@wqdqd.com','2018-08-05','2018-08-30','contributor'),(5,'bi3wad','gifQ6Jr6OAE1cQP6NSrtS+O8+u19QNOCcelKeOuYNNI=$YBeDnvQ6/Hsftl1kwv4gZ1d3tZOOHvUDExgXgJZhD+o=','lkmdkam@d.com','2018-08-05','2018-08-30','contributor'),(8,'lkawmdka','08pjSoiliQkES1DBjPi6NOvfeACepeEVd3tAxLR9sjE=$sWGRGajUZhMjE08Dyt/unG0PWlNhg4wMIRFi+u3LRbc=','lkqwd@qwd.com','2018-08-05','2018-08-05','administrator'),(9,'lkanwd','DY5PlF50a7vsOCRm2BQSr/0E02k6Lsb/cVDk+VWXMks=$uaGg2ANe5JlnaP3NG3ehXGgdWROjDmCfZoP4GENdfCk=','lqwknd@qwdlnk.com','2018-08-05','2018-08-05','administrator'),(10,'morten','bjPi3jur8hin38gLr5iDTOnpq2g/nViD5FgAeRstE4g=$EKnBjJdpmAD7dKbUgaiqifE87yNwCh0cizH8q1IeBqU=','morten@nordkern.dk','2018-08-29','9999-12-31','privileged_read');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-11-05 18:37:55
