DROP TABLE IF EXISTS documentrequest;

CREATE TABLE `documentrequest` (
  `id` int(7) auto_increment NOT NULL,
  `bracnoStanje` varchar(255) NOT NULL,
  `brojPrebivalista` varchar(255) NOT NULL,
  `datumRodjenja` varchar(255) NOT NULL,
  `ime` varchar(255) NOT NULL,
  `imeMajke` varchar(255) NOT NULL,
  `imeOca` varchar(255) NOT NULL,
  `JMBG` varchar(255) NOT NULL,
  `nacionalnost` varchar(255) NOT NULL,
  `opstinaPrebivalista` varchar(255) NOT NULL,
  `pol` varchar(255) NOT NULL,
  `prezime` varchar(255) NOT NULL,
  `prezimeMajke` varchar(255) NOT NULL,
  `prezimeOca` varchar(255) NOT NULL,
  `profesija` varchar(255) NOT NULL,
  `status` varchar(255) NOT NULL,
  `ulicaPrebivalista` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;