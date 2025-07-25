-- phpMyAdmin SQL Dump
-- version 4.9.11
-- https://www.phpmyadmin.net/
--
-- Host: database-5018260537.webspace-host.com
-- Erstellungszeit: 25. Jul 2025 um 11:12
-- Server-Version: 8.0.36
-- PHP-Version: 7.4.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `dbs14481873`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `berater`
--

CREATE TABLE `berater` (
  `id` int NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `password_hash` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `berater`
--

INSERT INTO `berater` (`id`, `email`, `password_hash`, `created_at`) VALUES
(1, 'apollopro@aol.com', '$2y$10$zj2Q9TjmkGqZe300rAxNYOU2M8eP4IdL6j6uT/dBaDPBxame23bNy', '2025-07-23 07:28:05');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `formulare`
--

CREATE TABLE `formulare` (
  `id` int NOT NULL,
  `berater_id` int NOT NULL,
  `status` enum('entwurf','gesendet') COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'entwurf',
  `share_token` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `companyName` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `legalForm` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `formData` text COLLATE utf8mb4_general_ci NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Daten für Tabelle `formulare`
--

INSERT INTO `formulare` (`id`, `berater_id`, `status`, `share_token`, `companyName`, `legalForm`, `formData`, `created_at`) VALUES
(1, 1, 'gesendet', NULL, 'Müller Digital GmbH (Vollständig)', 'GmbH', '{\r\n      \"id\": 1,\r\n      \"berater_id\": 1,\r\n      \"status\": \"gesendet\",\r\n      \"companyName\": \"Müller Digital GmbH (Vollständig)\",\r\n      \"legalForm\": \"GmbH\",\r\n      \"foundationDate\": \"01012020\",\r\n      \"streetAndNumber\": \"Musterstraße 1\",\r\n      \"postalCode\": \"99084\",\r\n      \"city\": \"Erfurt\",\r\n      \"isVatDeductible\": true,\r\n      \"industrySector\": \"6201 Programmierungstätigkeiten\",\r\n      \"hasWebsite\": true,\r\n      \"websiteUrl\": \"https://www.mueller-digital.de\",\r\n      \"mainContact\": {\r\n        \"name\": \"Petra Müller\",\r\n        \"email\": \"p.mueller@example.com\",\r\n        \"phone\": \"0361 123456\"\r\n      },\r\n      \"beneficialOwners\": [\r\n        {\r\n          \"lastName\": \"Mustermann\",\r\n          \"firstName\": \"Max\",\r\n          \"birthDate\": \"15081980\",\r\n          \"taxId\": \"12345678901\"\r\n        }\r\n      ],\r\n      \"bankDetails\": {\r\n        \"institute\": \"Sparkasse Mittelthüringen\",\r\n        \"iban\": \"DE89820510000123456789\",\r\n        \"taxId\": \"DE123456789\"\r\n      },\r\n      \"smeClassification\": {\r\n        \"lastYear\": {\r\n          \"year\": \"2024\",\r\n          \"employees\": 15,\r\n          \"turnover\": \"1200000\",\r\n          \"balanceSheetTotal\": \"800000\"\r\n        },\r\n        \"penultimateYear\": {\r\n          \"year\": \"2023\",\r\n          \"employees\": 12,\r\n          \"turnover\": \"950000\",\r\n          \"balanceSheetTotal\": \"650000\"\r\n        }\r\n      },\r\n      \"consultationDetails\": {\r\n        \"focus\": \"Digitalisierung\",\r\n        \"scopeInDays\": 10,\r\n        \"dailyRate\": \"800\",\r\n        \"endDate\": \"31122025\",\r\n        \"initialSituation\": \"Das Unternehmen möchte seine internen Prozesse optimieren und eine neue E-Commerce-Plattform aufbauen.\",\r\n        \"consultationContent\": \"Analyse der bestehenden IT-Infrastruktur, Erstellung eines Anforderungskatalogs für die neue Plattform und Begleitung bei der Auswahl eines Dienstleisters.\"\r\n      },\r\n      \"hasChosenConsultant\": true,\r\n      \"consultingFirm\": \"Eigene Beraterfirma\",\r\n      \"consultants\": [\r\n        {\r\n          \"firstName\": \"Ihr\",\r\n          \"lastName\": \"Name\",\r\n          \"accreditationId\": \"RKW-123\",\r\n          \"email\": \"ihre.email@example.com\"\r\n        }\r\n      ],\r\n      \"hasAcknowledgedPublicationObligations\": true,\r\n      \"attachedDocuments\": []\r\n    }', '2025-07-20 07:00:00'),
(2, 1, 'entwurf', NULL, 'Schmidt Logistik AG', 'AG', '{\r\n  \"berater_id\": 1,\r\n  \"status\": \"entwurf\",\r\n  \"companyName\": \"Schmidt Logistik AG\",\r\n  \"legalForm\": \"AG\",\r\n  \"mainContact\": { \"name\": \"Klaus Schmidt\", \"email\": \"k.schmidt@example.com\", \"phone\": \"023456789\" },\r\n  \"consultationDetails\": { \"scopeInDays\": 15, \"dailyRate\": \"950\" },\r\n  \"updated_at\": \"2025-07-21 11:30:00\"\r\n}', '2025-07-21 09:00:00'),
(3, 1, 'gesendet', NULL, 'Weber & Co. KG', 'KG', '{\r\n  \"berater_id\": 1,\r\n  \"status\": \"gesendet\",\r\n  \"companyName\": \"Weber & Co. KG\",\r\n  \"legalForm\": \"KG\",\r\n  \"mainContact\": { \"name\": \"Susanne Weber\", \"email\": \"s.weber@example.com\", \"phone\": \"034567890\" },\r\n  \"consultationDetails\": { \"scopeInDays\": 8, \"dailyRate\": \"700\" },\r\n  \"updated_at\": \"2025-06-15 14:00:00\"\r\n}', '2025-06-15 10:00:00'),
(4, 1, 'entwurf', NULL, 'Schulz Bäckerei e.K.', 'e.K.', '{\r\n  \"berater_id\": 1,\r\n  \"status\": \"entwurf\",\r\n  \"companyName\": \"Schulz Bäckerei e.K.\",\r\n  \"legalForm\": \"e.K.\",\r\n  \"mainContact\": { \"name\": \"Martin Schulz\", \"email\": \"m.schulz@example.com\", \"phone\": \"045678901\" },\r\n  \"consultationDetails\": { \"scopeInDays\": 6, \"dailyRate\": \"650\" },\r\n  \"updated_at\": \"2025-07-22 08:45:00\"\r\n}', '2025-07-22 06:45:00'),
(5, 1, 'entwurf', NULL, 'Musterfirma', 'KG auf Aktien (KGaA)', '{\"id\":null,\"berater_id\":1,\"status\":\"entwurf\",\"companyName\":\"Musterfirma ERF2\",\"legalForm\":\"KG auf Aktien (KGaA)\",\"foundationDate\":\"02022022\",\"streetAndNumber\":\"Muster-Stra\\u00dfe 125b\",\"postalCode\":\"12358\",\"city\":\"Berlin\",\"isVatDeductible\":true,\"industrySector\":\"2594 Herstellung von Schrauben und Nieten\",\"hasWebsite\":false,\"websiteUrl\":\"\",\"mainContact\":{\"name\":\"Max Mustermann\",\"email\":\"apollopro@aol.com\",\"phone\":\"\"},\"beneficialOwners\":[{\"lastName\":\"Mustermann\",\"firstName\":\"Max\",\"birthDate\":\"13101978\",\"taxId\":\"\"}],\"bankDetails\":{\"institute\":\"Deutsche Bank\",\"iban\":\"\",\"taxId\":\"\"},\"smeClassification\":{\"lastYear\":{\"year\":\"\",\"employees\":0,\"turnover\":\"\",\"balanceSheetTotal\":\"\"},\"penultimateYear\":{\"year\":\"\",\"employees\":0,\"turnover\":\"\",\"balanceSheetTotal\":\"\"}},\"consultationDetails\":{\"focus\":\"Internationalisierung\",\"scopeInDays\":25,\"dailyRate\":\"830\",\"endDate\":\"31122025\",\"initialSituation\":\"\",\"consultationContent\":\"\"},\"hasChosenConsultant\":false,\"consultingFirm\":\"\",\"consultants\":[],\"hasAcknowledgedPublicationObligations\":true,\"attachedDocuments\":[]}', '2025-07-23 13:56:36'),
(6, 1, 'entwurf', '786516a69751ac49c718b5d9955d86480169206b87aaac9b8f191b819203f7ad', 'Musterfirma', 'KG auf Aktien (KGaA)', '{\n    \"id\": null,\n    \"berater_id\": 1,\n    \"status\": \"entwurf\",\n    \"companyName\": \"Musterfirma\",\n    \"legalForm\": \"KG auf Aktien (KGaA)\",\n    \"foundationDate\": \"02022022\",\n    \"streetAndNumber\": \"Muster-Straße 125b\",\n    \"postalCode\": \"12358\",\n    \"city\": \"Boom \",\n    \"isVatDeductible\": true,\n    \"industrySector\": \"2594 Herstellung von Schrauben und Nieten\",\n    \"hasWebsite\": false,\n    \"websiteUrl\": \"\",\n    \"mainContact\": {\n        \"name\": \"Max Mustermann\",\n        \"email\": \"apollopro@aol.com\",\n        \"phone\": \"\"\n    },\n    \"beneficialOwners\": [\n        {\n            \"lastName\": \"Mustermann\",\n            \"firstName\": \"Max\",\n            \"birthDate\": \"13101978\",\n            \"taxId\": \"\"\n        }\n    ],\n    \"bankDetails\": {\n        \"institute\": \"Deutsche Bank\",\n        \"iban\": \"\",\n        \"taxId\": \"\"\n    },\n    \"smeClassification\": {\n        \"lastYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        },\n        \"penultimateYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        }\n    },\n    \"consultationDetails\": {\n        \"focus\": \"Internationalisierung\",\n        \"scopeInDays\": 25,\n        \"dailyRate\": \"830\",\n        \"endDate\": \"31122025\",\n        \"initialSituation\": \"\",\n        \"consultationContent\": \"\"\n    },\n    \"hasChosenConsultant\": false,\n    \"consultingFirm\": \"\",\n    \"consultants\": [],\n    \"hasAcknowledgedPublicationObligations\": true,\n    \"attachedDocuments\": []\n}', '2025-07-23 13:57:00'),
(7, 1, 'entwurf', NULL, 'Tolkien Hummel', 'GmbH', '{\"id\":null,\"berater_id\":1,\"status\":\"entwurf\",\"companyName\":\"Tolkien Hummel\",\"legalForm\":\"GmbH\",\"foundationDate\":\"01021985\",\"streetAndNumber\":\"\",\"postalCode\":\"\",\"city\":\"\",\"isVatDeductible\":false,\"industrySector\":\"\",\"hasWebsite\":false,\"websiteUrl\":\"\",\"mainContact\":{\"name\":\"Hubert Staiger\",\"email\":\"apollopro@aol.com\",\"phone\":\"0123456776\"},\"beneficialOwners\":[],\"bankDetails\":{\"institute\":\"\",\"iban\":\"\",\"taxId\":\"\"},\"smeClassification\":{\"lastYear\":{\"year\":\"\",\"employees\":0,\"turnover\":\"\",\"balanceSheetTotal\":\"\"},\"penultimateYear\":{\"year\":\"\",\"employees\":0,\"turnover\":\"\",\"balanceSheetTotal\":\"\"}},\"consultationDetails\":{\"focus\":\"Unternehmenswachstum und Wettbewerbsf\\u00e4higkeit\",\"scopeInDays\":17,\"dailyRate\":\"1200\",\"endDate\":\"\",\"initialSituation\":\"\",\"consultationContent\":\"\"},\"hasChosenConsultant\":false,\"consultingFirm\":\"\",\"consultants\":[],\"hasAcknowledgedPublicationObligations\":true,\"attachedDocuments\":[]}', '2025-07-23 19:02:32'),
(8, 1, 'gesendet', NULL, 'BB', 'GmbH & Co. KG', '{\n    \"id\": null,\n    \"berater_id\": 1,\n    \"status\": \"gesendet\",\n    \"companyName\": \"BB\",\n    \"legalForm\": \"GmbH & Co. KG\",\n    \"foundationDate\": \"08122020\",\n    \"streetAndNumber\": \"\",\n    \"postalCode\": \"\",\n    \"city\": \"\",\n    \"isVatDeductible\": false,\n    \"industrySector\": \"\",\n    \"hasWebsite\": false,\n    \"websiteUrl\": \"\",\n    \"mainContact\": {\n        \"name\": \"\",\n        \"email\": \"\",\n        \"phone\": \"\"\n    },\n    \"beneficialOwners\": [],\n    \"bankDetails\": {\n        \"institute\": \"\",\n        \"iban\": \"\",\n        \"taxId\": \"\"\n    },\n    \"smeClassification\": {\n        \"lastYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        },\n        \"penultimateYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        }\n    },\n    \"consultationDetails\": {\n        \"focus\": \"Personalmanagement/ Organisationsentwicklung\",\n        \"scopeInDays\": 9,\n        \"dailyRate\": \"800\",\n        \"endDate\": \"\",\n        \"initialSituation\": \"\",\n        \"consultationContent\": \"\"\n    },\n    \"hasChosenConsultant\": false,\n    \"consultingFirm\": \"\",\n    \"consultants\": [],\n    \"hasAcknowledgedPublicationObligations\": true,\n    \"attachedDocuments\": []\n}', '2025-07-23 19:21:33'),
(9, 1, 'entwurf', NULL, 'BB', 'GmbH & Co. KG', '{\n    \"id\": null,\n    \"berater_id\": 1,\n    \"status\": \"entwurf\",\n    \"companyName\": \"BB\",\n    \"legalForm\": \"GmbH & Co. KG\",\n    \"foundationDate\": \"08122020\",\n    \"streetAndNumber\": \"\",\n    \"postalCode\": \"\",\n    \"city\": \"\",\n    \"isVatDeductible\": false,\n    \"industrySector\": \"\",\n    \"hasWebsite\": false,\n    \"websiteUrl\": \"\",\n    \"mainContact\": {\n        \"name\": \"\",\n        \"email\": \"\",\n        \"phone\": \"\"\n    },\n    \"beneficialOwners\": [],\n    \"bankDetails\": {\n        \"institute\": \"\",\n        \"iban\": \"\",\n        \"taxId\": \"\"\n    },\n    \"smeClassification\": {\n        \"lastYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        },\n        \"penultimateYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        }\n    },\n    \"consultationDetails\": {\n        \"focus\": \"Personalmanagement/ Organisationsentwicklung\",\n        \"scopeInDays\": 9,\n        \"dailyRate\": \"800\",\n        \"endDate\": \"\",\n        \"initialSituation\": \"\",\n        \"consultationContent\": \"\"\n    },\n    \"hasChosenConsultant\": false,\n    \"consultingFirm\": \"\",\n    \"consultants\": [],\n    \"hasAcknowledgedPublicationObligations\": true,\n    \"attachedDocuments\": []\n}', '2025-07-23 19:21:38'),
(10, 1, 'entwurf', NULL, 'Mathe Haus', 'GmbH & Co. KG', '{\n    \"id\": null,\n    \"berater_id\": 1,\n    \"status\": \"entwurf\",\n    \"companyName\": \"Mathe Haus\",\n    \"legalForm\": \"GmbH & Co. KG\",\n    \"foundationDate\": \"\",\n    \"streetAndNumber\": \"\",\n    \"postalCode\": \"\",\n    \"city\": \"\",\n    \"isVatDeductible\": false,\n    \"industrySector\": \"\",\n    \"hasWebsite\": false,\n    \"websiteUrl\": \"\",\n    \"mainContact\": {\n        \"name\": \"\",\n        \"email\": \"\",\n        \"phone\": \"\"\n    },\n    \"beneficialOwners\": [],\n    \"bankDetails\": {\n        \"institute\": \"\",\n        \"iban\": \"\",\n        \"taxId\": \"\"\n    },\n    \"smeClassification\": {\n        \"lastYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        },\n        \"penultimateYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        }\n    },\n    \"consultationDetails\": {\n        \"focus\": \"\",\n        \"scopeInDays\": 0,\n        \"dailyRate\": \"\",\n        \"endDate\": \"\",\n        \"initialSituation\": \"\",\n        \"consultationContent\": \"\"\n    },\n    \"hasChosenConsultant\": false,\n    \"consultingFirm\": \"\",\n    \"consultants\": [],\n    \"hasAcknowledgedPublicationObligations\": true,\n    \"attachedDocuments\": []\n}', '2025-07-24 08:11:39'),
(11, 1, 'entwurf', NULL, 'Mathe Haus', 'GmbH & Co. KG', '{\n    \"id\": null,\n    \"berater_id\": 1,\n    \"status\": \"entwurf\",\n    \"companyName\": \"Mathe Haus\",\n    \"legalForm\": \"GmbH & Co. KG\",\n    \"foundationDate\": \"\",\n    \"streetAndNumber\": \"\",\n    \"postalCode\": \"\",\n    \"city\": \"\",\n    \"isVatDeductible\": false,\n    \"industrySector\": \"\",\n    \"hasWebsite\": false,\n    \"websiteUrl\": \"\",\n    \"mainContact\": {\n        \"name\": \"\",\n        \"email\": \"\",\n        \"phone\": \"\"\n    },\n    \"beneficialOwners\": [],\n    \"bankDetails\": {\n        \"institute\": \"\",\n        \"iban\": \"\",\n        \"taxId\": \"\"\n    },\n    \"smeClassification\": {\n        \"lastYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        },\n        \"penultimateYear\": {\n            \"year\": \"\",\n            \"employees\": 0,\n            \"turnover\": \"\",\n            \"balanceSheetTotal\": \"\"\n        }\n    },\n    \"consultationDetails\": {\n        \"focus\": \"Internationalisierung\",\n        \"scopeInDays\": 24,\n        \"dailyRate\": \"950\",\n        \"endDate\": \"\",\n        \"initialSituation\": \"\",\n        \"consultationContent\": \"\"\n    },\n    \"hasChosenConsultant\": false,\n    \"consultingFirm\": \"\",\n    \"consultants\": [],\n    \"hasAcknowledgedPublicationObligations\": true,\n    \"attachedDocuments\": []\n}', '2025-07-24 08:12:01');

--
-- Indizes der exportierten Tabellen
--

--
-- Indizes für die Tabelle `berater`
--
ALTER TABLE `berater`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `email` (`email`);

--
-- Indizes für die Tabelle `formulare`
--
ALTER TABLE `formulare`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `share_token_unique` (`share_token`),
  ADD KEY `berater_id` (`berater_id`);

--
-- AUTO_INCREMENT für exportierte Tabellen
--

--
-- AUTO_INCREMENT für Tabelle `berater`
--
ALTER TABLE `berater`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT für Tabelle `formulare`
--
ALTER TABLE `formulare`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `formulare`
--
ALTER TABLE `formulare`
  ADD CONSTRAINT `formulare_ibfk_1` FOREIGN KEY (`berater_id`) REFERENCES `berater` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
