#------------------------------------------------------------
#        Script MySQL.
#------------------------------------------------------------


#------------------------------------------------------------
# Table: Cours
#------------------------------------------------------------

CREATE TABLE Cours(
        Code               Int  Auto_increment  NOT NULL ,
        Nom                Varchar (50) NOT NULL ,
        Description        Text NOT NULL ,
        Annee              Year NOT NULL ,
        Coefficient        Float NOT NULL ,
        DE_pourcentage     Float NOT NULL ,
        TP_pourcentage     Float NOT NULL ,
        Projet_pourcentage Float NOT NULL
	,CONSTRAINT Cours_PK PRIMARY KEY (Code)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Personne
#------------------------------------------------------------

CREATE TABLE Personne(
        ID       Int Auto_increment NOT NULL ,
        Nom      Varchar (50) NOT NULL ,
        Prenom   Varchar (50) NOT NULL
	,CONSTRAINT Personne_PK PRIMARY KEY (ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Adresse
#------------------------------------------------------------

CREATE TABLE Adresse(
        Adresse_ID Int Auto_increment NOT NULL ,
        Street     Varchar (50) NOT NULL ,
        City       Varchar (50) NOT NULL ,
        ZIP_code   Varchar (50) NOT NULL ,
        Phone      Int NOT NULL ,
        Email      Varchar (50) NOT NULL ,
        ID_Personne         Int NOT NULL
	,CONSTRAINT Adresse_PK PRIMARY KEY (Adresse_ID)
	,CONSTRAINT Adresse_Personne_FK FOREIGN KEY (ID_Personne) REFERENCES Personne(ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Identite
#------------------------------------------------------------

CREATE TABLE Identite(
        Identite_ID     Int Auto_increment NOT NULL,
        date_naissance  Date NOT NULL,
        ville_naissance Varchar (50) NOT NULL,
        pays_naissance  Varchar (50) NOT NULL,
        sexe            Char (5) NOT NULL,
        photo           Text,
        ID_Personne     Int NOT NULL,
        UNIQUE (ID_Personne)
	,CONSTRAINT Identite_PK PRIMARY KEY (Identite_ID)
	,CONSTRAINT Identite_Personne_FK FOREIGN KEY (ID_Personne) REFERENCES Personne(ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Groupe
#------------------------------------------------------------

CREATE TABLE Groupe(
        Groupe_ID Int  Auto_increment  NOT NULL ,
        Nom       Varchar (50) NOT NULL,
        Bulletin  Boolean NOT NULL
        UNIQUE (Nom)
	,CONSTRAINT Groupe_PK PRIMARY KEY (Groupe_ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Professeur
#------------------------------------------------------------

CREATE TABLE Professeur(
		ID_Personne  Int NOT NULL,
        Matricule 	Int  NOT NULL,
        Password Varchar (50) NOT NULL,
        UNIQUE (ID_Personne)
	,CONSTRAINT Professeur_PK PRIMARY KEY (Matricule)
	,CONSTRAINT Professeur_Personne_FK FOREIGN KEY (ID_Personne) REFERENCES Personne(ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Etudiant
#------------------------------------------------------------

CREATE TABLE Etudiant(
        Matricule Int NOT NULL,
        Groupe_ID Int,
        ID_Personne Int NOT NULL,
        Password Varchar (50) NOT NULL,
        UNIQUE (ID_Personne)
	,CONSTRAINT Etudiant_PK PRIMARY KEY (Matricule)
	,CONSTRAINT Etudiant_Personne_FK FOREIGN KEY (ID_Personne) REFERENCES Personne(ID)
	,CONSTRAINT Etudiant_Groupe_FK FOREIGN KEY (Groupe_ID) REFERENCES Groupe(Groupe_ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Responsable
#------------------------------------------------------------

CREATE TABLE Responsable(
		ID_Personne  Int NOT NULL,
        Numero	  Int  Auto_increment  NOT NULL,
        UNIQUE (ID_Personne)
	,CONSTRAINT Responsable_PK PRIMARY KEY (Numero)
	,CONSTRAINT Responsable_Personne_FK FOREIGN KEY (ID_Personne) REFERENCES Personne(ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Administration
#------------------------------------------------------------

CREATE TABLE Administration(
		ID_Personne        Int NOT NULL,
        Matricule Int NOT NULL,
        Password Varchar (50) NOT NULL,
        UNIQUE (ID_Personne)
	,CONSTRAINT Administration_PK PRIMARY KEY (Matricule)
	,CONSTRAINT Administration_Personne_FK FOREIGN KEY (ID_Personne) REFERENCES Personne(ID)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: Note
#------------------------------------------------------------

CREATE TABLE Note(
        ID        Int  Auto_increment  NOT NULL ,
        Valeur    Float NOT NULL,
        Type	  Varchar (10) NOT NULL,
        Date_exam Date NOT NULL,
        Code      Int NOT NULL,
        Matricule_Etudiant Int NOT NULL
	,CONSTRAINT Note_PK PRIMARY KEY (ID)
	,CONSTRAINT Note_Cours_FK FOREIGN KEY (Code) REFERENCES Cours(Code)
	,CONSTRAINT Note_Etudiant0_FK FOREIGN KEY (Matricule_Etudiant) REFERENCES Etudiant(Matricule)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: suivre
#------------------------------------------------------------

CREATE TABLE suivre(
        Groupe_ID Int NOT NULL ,
        Code      Int NOT NULL
	,CONSTRAINT suivre_PK PRIMARY KEY (Groupe_ID,Code)
	,CONSTRAINT suivre_Groupe_FK FOREIGN KEY (Groupe_ID) REFERENCES Groupe(Groupe_ID)
	,CONSTRAINT suivre_Cours0_FK FOREIGN KEY (Code) REFERENCES Cours(Code)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: tuteur
#------------------------------------------------------------

CREATE TABLE tuteur(
        Numero    Int NOT NULL ,
        Matricule_Etudiant Int NOT NULL
	,CONSTRAINT tuteur_PK PRIMARY KEY (Numero,Matricule_Etudiant)
	,CONSTRAINT tuteur_Responsable_FK FOREIGN KEY (Numero) REFERENCES Responsable(Numero)
	,CONSTRAINT tuteur_Etudiant0_FK FOREIGN KEY (Matricule_Etudiant) REFERENCES Etudiant(Matricule)
)ENGINE=InnoDB;


#------------------------------------------------------------
# Table: enseigner
#------------------------------------------------------------

CREATE TABLE enseigner(
        Code      Int NOT NULL ,
        Matricule_Prof Int NOT NULL
	,CONSTRAINT enseigner_PK PRIMARY KEY (Code,Matricule_Prof)
	,CONSTRAINT enseigner_Cours_FK FOREIGN KEY (Code) REFERENCES Cours(Code)
	,CONSTRAINT enseigner_Professeur0_FK FOREIGN KEY (Matricule_Prof) REFERENCES Professeur(Matricule)
)ENGINE=InnoDB;
