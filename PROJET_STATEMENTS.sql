INSERT INTO `personne`(`Nom`, `Prenom`) VALUES 
('Bunouf','Celia'),
('Begeot','Hugues'),
('admin','admin'),
('Bush', 'Georges'),
('Tran','Xi');

INSERT INTO `groupe`(`Nom`) VALUES 
('L3Int'),
('L2Bio'),
('Zoo');

INSERT INTO `professeur`(`ID_Personne`, `Matricule`, `Password`) VALUES 
(5,20090002,'prof');

INSERT INTO `responsable`(`ID_Personne`) VALUES 
(4);

INSERT INTO `administration`(`ID_Personne`, `Matricule`, `Password`) VALUES 
(3,00000000,'admin');

INSERT INTO `etudiant`(`Matricule`, `Groupe_ID`, `ID_Personne`, `Password`) VALUES 
(20160024,1,1,'bla'),
(20160124,2,2,'coucou');

INSERT INTO `identite`(`date_naissance`, `ville_naissance`, `pays_naissance`, `sexe`, `ID_Personne`) VALUES 
('08/08/1998','Sarreguemines','France','F',1);

INSERT INTO `adresse` (`Street`, `City`, `ZIP_code`, `Phone`, `Email`, `ID_Personne`) VALUES
('2 rue Broca','Gripari',1997224,2710300249,'lescomptes@oui.fr',1);


INSERT INTO `cours`(`Nom`, `Description`, `Annee`, `Coefficient`, `DE_pourcentage`, `TP_pourcentage`, `Projet_pourcentage`) VALUES 
('Network & Protocols','Base concernant le modèle OSI et son application TCP/IP',2019,2,60,20,20),
('Databases','Tout sur les bases de données de MongoDB à Hadoop en passant par Neo4j avec Cypher. Le tout en observant MapReduce et un incroyable choix de stocker les informations',2019,3,60,20,20),
('Communication','Les dissertations, les débats sont au programme.',2019,1,40,20,40);


INSERT INTO `suivre`(`Groupe_ID`, `Code`) VALUES 
(1,1),
(2,2),
(1,3);

INSERT INTO `enseigner`(`Code`, `Matricule_Prof`) VALUES (1,20090002);

INSERT INTO `tuteur`(`Numero`, `Matricule_Etudiant`) VALUES (1,20160024);

INSERT INTO `note` (`Valeur`, `Type`, `Date_exam`, `Code`, `Matricule_Etudiant`) VALUES
(12, 'TP', '2019-05-09', 1, 20160024),
(12, 'DE', '2019-05-26', 1, 20160024),
(13, 'DE', '2019-05-26', 3, 20160024),
(14, 'Projet', '2019-05-26', 1, 20160024),
(2, 'DE', '2019-05-09', 1, 20160124),
(5, 'Projet', '2019-05-09', 1, 20160124),
(17, 'TP', '2019-05-26', 3, 20160024),
(9, 'Projet', '2019-05-26', 3, 20160024),
(12, 'TP', '2019-05-26', 2, 20160124),
(4, 'DE', '2019-05-26', 2, 20160124);