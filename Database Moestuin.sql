-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema moestuinbp1her
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema moestuinbp1her
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `moestuinbp1her` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `moestuinbp1her` ;

-- -----------------------------------------------------
-- Table `moestuinbp1her`.`adressen`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`adressen` (
  `adres` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`adres`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`bewoners`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`bewoners` (
  `bewonernaam` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`bewonernaam`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`taken`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`taken` (
  `taakomschrijving` VARCHAR(250) NOT NULL,
  PRIMARY KEY (`taakomschrijving`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`eigenaartaken`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`eigenaartaken` (
  `bewoners_bewonernaam` VARCHAR(50) NOT NULL,
  `taken_taakomschrijving` VARCHAR(250) NOT NULL,
  `datum` DATE NOT NULL,
  PRIMARY KEY (`bewoners_bewonernaam`, `taken_taakomschrijving`, `datum`),
  INDEX `fk_eigenaartaken_taken1_idx` (`taken_taakomschrijving` ASC) VISIBLE,
  CONSTRAINT `fk_eigenaartaken_bewoners1`
    FOREIGN KEY (`bewoners_bewonernaam`)
    REFERENCES `moestuinbp1her`.`bewoners` (`bewonernaam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_eigenaartaken_taken1`
    FOREIGN KEY (`taken_taakomschrijving`)
    REFERENCES `moestuinbp1her`.`taken` (`taakomschrijving`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`groenten`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`groenten` (
  `groente` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`groente`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`moestuinen`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`moestuinen` (
  `moestuinnaam` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`moestuinnaam`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`moestuineigenaren`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`moestuineigenaren` (
  `moestuinen_moestuinnaam` VARCHAR(10) NULL,
  `bewoners_bewonernaam` VARCHAR(50) NOT NULL,
  `adressen_adres` VARCHAR(20) NULL,
  INDEX `fk_moestuineigenaren_moestuinen1_idx` (`moestuinen_moestuinnaam` ASC) VISIBLE,
  INDEX `fk_moestuineigenaren_adressen1_idx` (`adressen_adres` ASC) VISIBLE,
  PRIMARY KEY (`bewoners_bewonernaam`),
  CONSTRAINT `fk_moestuineigenaren_moestuinen1`
    FOREIGN KEY (`moestuinen_moestuinnaam`)
    REFERENCES `moestuinbp1her`.`moestuinen` (`moestuinnaam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_moestuineigenaren_adressen1`
    FOREIGN KEY (`adressen_adres`)
    REFERENCES `moestuinbp1her`.`adressen` (`adres`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_moestuineigenaren_bewoners1`
    FOREIGN KEY (`bewoners_bewonernaam`)
    REFERENCES `moestuinbp1her`.`bewoners` (`bewonernaam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `moestuinbp1her`.`moestuinplanten`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `moestuinbp1her`.`moestuinplanten` (
  `groenten_groente` VARCHAR(50) NOT NULL,
  `moestuinen_moestuinnaam` VARCHAR(10) NOT NULL,
  `datum` DATE NOT NULL,
  PRIMARY KEY (`groenten_groente`, `moestuinen_moestuinnaam`),
  INDEX `fk_moestuinplanten_moestuinen1_idx` (`moestuinen_moestuinnaam` ASC) VISIBLE,
  CONSTRAINT `fk_moestuinplanten_groenten`
    FOREIGN KEY (`groenten_groente`)
    REFERENCES `moestuinbp1her`.`groenten` (`groente`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_moestuinplanten_moestuinen1`
    FOREIGN KEY (`moestuinen_moestuinnaam`)
    REFERENCES `moestuinbp1her`.`moestuinen` (`moestuinnaam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
