package recherche;


import Gestion_admin.Database_Connection;
import java.util.ArrayList;


/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Professeur
 **
 * @author Hugues
 */
public class RechercheProfesseur extends Recherche
{
    /* PERSONNE */


    /**
     * Permet de récupérer une valeur précise dans la table Personne en fonction d'un professeur
     * @param matricule Matricule du professeur connecté
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public String getPersonne(String matricule, String valeur)
    {
        database = new Database_Connection();
        query =
                "SELECT * " +
                "FROM personne, professeur " +
                "WHERE personne.ID = professeur.ID_Personne " +
                "AND Matricule = " + matricule +  " ;";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* COURS */


    /**
     * Permet de savoir combien de cours dispense un professeur
     * @param matricule Matricule du professeur connecté
     * @return le nombre de cours dispensé
     */
    public int nombreCours(String matricule)
    {
        database = new Database_Connection();
        query =
                "SELECT * " +
                "FROM professeur, enseigner" +
                "WHERE professeur.Matricule = " + matricule + " " +
                "AND professeur.Matricule = enseigner.Matricule_Prof;";

        resultat = database.run_Statement_READ(query);

        return Database_Connection.getRows(resultat);
    }

    /**
     * Permet de récupérer une valeur précise dans la table Cours
     * @param coursCode Code du cours recherché
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public String getCours(String coursCode, String valeur)
    {
        database = new Database_Connection();
        query =
                "SELECT * " +
                "FROM cours " +
                "WHERE cours.Code = " + coursCode + " ;";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /**
     * Permet de récupérerla liste des cours qu'une professeur dispense
     * @param matricule Matricule du professeur connecté
     * @return la liste des Codes des cours dispensés
     */
    public ArrayList<String> getCoursArray(String matricule, String valeur)
    {
        database = new Database_Connection();
        query =
                "SELECT * " +
                "FROM professeur, enseigner, cours " +
                "WHERE professeur.Matricule = " + matricule + " " +
                "AND professeur.Matricule = enseigner.Matricule_Prof " +
                "AND enseigner.Code = cours.Code;";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_ARRAY(resultat, valeur);
    }


    /**
     * Permet de savoir combien de Groupes suivent un cours donné
     * @param coursCode Code du cours donné
     * @return le nombre de Groupes suivant le cours
     */
    public int nombreGroupeSuivantCours(String coursCode)
    {
        database = new Database_Connection();
        query =
                "SELECT * " +
                "FROM cours, suivre, groupe " +
                "WHERE cours.Code = " + coursCode + " " +
                "AND cours.Code = suivre.Code " +
                "AND suivre.Groupe_ID = groupe.Groupe_ID;";

        resultat = database.run_Statement_READ(query);

        return Database_Connection.getRows(resultat);
    }
}
