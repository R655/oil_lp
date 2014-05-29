DROP TABLE IF EXISTS "Good";
CREATE TABLE "Good" ("Id" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , "Name" VARCHAR NOT NULL , "Price" REAL NOT NULL );
INSERT INTO "Good" VALUES(1,'Нефть обессоленная',0);
INSERT INTO "Good" VALUES(2,'Бензин',0);
INSERT INTO "Good" VALUES(3,'Д/т "л" (S>0.2)',32000);
INSERT INTO "Good" VALUES(4,'Д/т "з"',32000);
INSERT INTO "Good" VALUES(5,'Вакуумный газойль',0);
INSERT INTO "Good" VALUES(6,'Рефлюкс',22000);
INSERT INTO "Good" VALUES(7,'Газ',0);
INSERT INTO "Good" VALUES(8,'Гудрон',12500);
INSERT INTO "Good" VALUES(9,'Бензин АИ92',30900);
INSERT INTO "Good" VALUES(10,'Головка стабилизации',22000);
INSERT INTO "Good" VALUES(11,'Лёгкий каталитический газойль',0);
INSERT INTO "Good" VALUES(12,'Тяжёлый каталитический газойль',0);
INSERT INTO "Good" VALUES(13,'Газ',0);
INSERT INTO "Good" VALUES(14,'Сухой газ',0);
INSERT INTO "Good" VALUES(15,'Рефлюкс',22000);
INSERT INTO "Good" VALUES(16,'Бензин АИ95',32200);
INSERT INTO "Good" VALUES(17,'Водородосодержащий газ',0);
INSERT INTO "Good" VALUES(18,'Д/т "л" (S<0.2)',31500);
INSERT INTO "Good" VALUES(19,'Газ',0);
INSERT INTO "Good" VALUES(20,'Сероводород',22000);
DROP TABLE IF EXISTS "GoodFromTool";
CREATE TABLE GoodFromTool (
  ToolId INTEGER NOT NULL,
  GoodId INTEGER NOT NULL,
  ReceiveNumber REAL NOT NULL,
  FOREIGN KEY (GoodId) REFERENCES Good(Id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  FOREIGN KEY (ToolId) REFERENCES Tool(Id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  PRIMARY KEY (ToolId, GoodId)
);
INSERT INTO "GoodFromTool" VALUES(1,1,1);
INSERT INTO "GoodFromTool" VALUES(2,2,0.13);
INSERT INTO "GoodFromTool" VALUES(2,3,0.235);
INSERT INTO "GoodFromTool" VALUES(2,4,0.145);
INSERT INTO "GoodFromTool" VALUES(2,5,0.15);
INSERT INTO "GoodFromTool" VALUES(2,6,0.01);
INSERT INTO "GoodFromTool" VALUES(2,7,0.02);
INSERT INTO "GoodFromTool" VALUES(2,8,0.305);
INSERT INTO "GoodFromTool" VALUES(3,9,0.27);
INSERT INTO "GoodFromTool" VALUES(3,10,0.07);
INSERT INTO "GoodFromTool" VALUES(3,11,0.38);
INSERT INTO "GoodFromTool" VALUES(3,12,0.2);
INSERT INTO "GoodFromTool" VALUES(3,13,0.02);
INSERT INTO "GoodFromTool" VALUES(4,14,0.113);
INSERT INTO "GoodFromTool" VALUES(4,15,0.03);
INSERT INTO "GoodFromTool" VALUES(4,16,0.81);
INSERT INTO "GoodFromTool" VALUES(4,17,0.03);
INSERT INTO "GoodFromTool" VALUES(5,18,2.606);
INSERT INTO "GoodFromTool" VALUES(5,19,0.015);
INSERT INTO "GoodFromTool" VALUES(5,20,0.027);
DROP TABLE IF EXISTS "NeedGoodForTool";
CREATE TABLE NeedGoodForTool (
  ToolId INTEGER NOT NULL,
  GoodId INTEGER NOT NULL,
  RequestNumber REAL NOT NULL,
  FOREIGN KEY (GoodId) REFERENCES Good(Id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  FOREIGN KEY (ToolId) REFERENCES Tool(Id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  PRIMARY KEY (ToolId, GoodId)
);
INSERT INTO "NeedGoodForTool" VALUES(2,1,1);
INSERT INTO "NeedGoodForTool" VALUES(3,5,1);
INSERT INTO "NeedGoodForTool" VALUES(4,2,1);
INSERT INTO "NeedGoodForTool" VALUES(5,3,1);
INSERT INTO "NeedGoodForTool" VALUES(5,11,1);
INSERT INTO "NeedGoodForTool" VALUES(5,17,1);
DROP TABLE IF EXISTS "NeedResourceForTool";
CREATE TABLE NeedResourceForTool (
  ToolId INTEGER NOT NULL,
  ResourceID INTEGER NOT NULL,
  RequestNumber REAL NOT NULL,
  Deviation REAL NOT NULL,
  FOREIGN KEY (ToolId) REFERENCES Tool(Id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  FOREIGN KEY (ResourceID) REFERENCES "Resource"(Id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,
  PRIMARY KEY (ToolId, ResourceID)
);
INSERT INTO "NeedResourceForTool" VALUES(1,1,0.998,0.09);
INSERT INTO "NeedResourceForTool" VALUES(1,2,0.045,0.004);
INSERT INTO "NeedResourceForTool" VALUES(1,3,0.012,0.001);
INSERT INTO "NeedResourceForTool" VALUES(1,4,1358,100);
INSERT INTO "NeedResourceForTool" VALUES(1,5,1.08,0.1);
INSERT INTO "NeedResourceForTool" VALUES(1,6,0.552,0.05);
INSERT INTO "NeedResourceForTool" VALUES(1,8,0.031,0.003);
INSERT INTO "NeedResourceForTool" VALUES(2,2,0.021,0.002);
INSERT INTO "NeedResourceForTool" VALUES(2,3,0.021,0.002);
INSERT INTO "NeedResourceForTool" VALUES(2,4,284.019,0.002);
INSERT INTO "NeedResourceForTool" VALUES(2,5,6.908,0.7);
INSERT INTO "NeedResourceForTool" VALUES(2,6,4.472,0.4);
INSERT INTO "NeedResourceForTool" VALUES(2,7,29.097,3.0);
INSERT INTO "NeedResourceForTool" VALUES(2,9,0.06,0.006);
INSERT INTO "NeedResourceForTool" VALUES(2,15,0.063,0.006);
INSERT INTO "NeedResourceForTool" VALUES(3,2,0.057,0.006);
INSERT INTO "NeedResourceForTool" VALUES(3,3,0.78,0.08);
INSERT INTO "NeedResourceForTool" VALUES(3,4,53.481,5.0);
INSERT INTO "NeedResourceForTool" VALUES(3,5,37.84,4.0);
INSERT INTO "NeedResourceForTool" VALUES(3,6,9.525,0.1);
INSERT INTO "NeedResourceForTool" VALUES(3,7,22.578,2.0);
INSERT INTO "NeedResourceForTool" VALUES(3,10,3.653,0.4);
INSERT INTO "NeedResourceForTool" VALUES(4,2,0.052,0.005);
INSERT INTO "NeedResourceForTool" VALUES(4,3,0.033,0.003);
INSERT INTO "NeedResourceForTool" VALUES(4,4,118.402,12.0);
INSERT INTO "NeedResourceForTool" VALUES(4,5,39.876,4.0);
INSERT INTO "NeedResourceForTool" VALUES(4,6,4.357,0.4);
INSERT INTO "NeedResourceForTool" VALUES(4,7,47.468,5.0);
INSERT INTO "NeedResourceForTool" VALUES(4,11,0.035,0.0035);
INSERT INTO "NeedResourceForTool" VALUES(4,12,1.7,0.17);
INSERT INTO "NeedResourceForTool" VALUES(4,13,0.033,0.0033);
INSERT INTO "NeedResourceForTool" VALUES(4,14,0.338,0.034);
INSERT INTO "NeedResourceForTool" VALUES(5,2,2.354,0.2);
INSERT INTO "NeedResourceForTool" VALUES(5,3,0.09,0.01);
INSERT INTO "NeedResourceForTool" VALUES(5,4,751.404,75.0);
INSERT INTO "NeedResourceForTool" VALUES(5,5,31.595,3.0);
INSERT INTO "NeedResourceForTool" VALUES(5,6,4.37,0.4);
INSERT INTO "NeedResourceForTool" VALUES(5,7,48.227,5.0);
INSERT INTO "NeedResourceForTool" VALUES(5,10,0.061,0.006);
INSERT INTO "NeedResourceForTool" VALUES(5,12,3.626,0.4);
INSERT INTO "NeedResourceForTool" VALUES(5,13,0.146,0.01);
DROP TABLE IF EXISTS "Resource";
CREATE TABLE "Resource" (
  Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  Name VARCHAR NOT NULL UNIQUE,
  Price REAL NOT NULL,
  TotalNumber REAL NOT NULL,
  Deviation REAL NOT NULL
);
INSERT INTO "Resource" VALUES(15,'Аммиак, кг', 10.0, 1,0.1);
INSERT INTO "Resource" VALUES(4,'Вода, м^3', 10.0,30000,3000);
INSERT INTO "Resource" VALUES(8,'Деэмульгатор, кг', 10.0,1,0.1);
INSERT INTO "Resource" VALUES(14,'Диэтиленгликоль, кг', 10.0,1,0.1);
INSERT INTO "Resource" VALUES(9,'Известь комовая кальценированая негашённая, кг', 10.0,1,0.1);
INSERT INTO "Resource" VALUES(12,'Инертный газ, кг', 10.0,4,0.4);
INSERT INTO "Resource" VALUES(11,'Катализатор АП-64, кг', 10.0,0.1,0.01);
INSERT INTO "Resource" VALUES(10,'Катализатор, кг', 10.0,1,0.1);
INSERT INTO "Resource" VALUES(13,'Моноэтаноламин, кг', 10.0,0.1,0.01);
INSERT INTO "Resource" VALUES(1,'Нефть, т', 10.0,17,1.7);
INSERT INTO "Resource" VALUES(3,'Пар, Гкал', 10.0,1,0.1);
INSERT INTO "Resource" VALUES(6,'Сжатый воздух, м^3', 10.0,100,10);
INSERT INTO "Resource" VALUES(2,'Сода каустическая, кг', 10.0,1,0.1);
INSERT INTO "Resource" VALUES(7,'Топливо, кг', 10.0,600,60);
INSERT INTO "Resource" VALUES(5,'Электроэнергия, кВт*ч', 10.0,200,20);
DROP TABLE IF EXISTS "Tool";
CREATE TABLE Tool (
  Id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  Name VARCHAR NOT NULL UNIQUE,
  Abbreviation VARCHAR,
  "Power" INTEGER NOT NULL
);
INSERT INTO "Tool" VALUES(1,'Электрообессоливающая установка','ЭЛОУ',7028);
INSERT INTO "Tool" VALUES(2,'Атмосферно-вакуумная трубчатка','АВТ',7000);
INSERT INTO "Tool" VALUES(3,'Каталитический крекинг','КК',950);
INSERT INTO "Tool" VALUES(4,'Каталитический риформинг','КР',910);
INSERT INTO "Tool" VALUES(5,'Гидроочистка','ГО',1450);
