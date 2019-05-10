package recherche;


import static UsefulFunctions.CountRows_TableCell.getRows;

import UsefulFunctions.Database_Connection;

import java.sql.ResultSet;
import java.util.ArrayList;


/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Professeur
 * *
 *
 * @author Hugues
 */
public class RechercheProfesseur extends Recherche
{
    /* COURS */


    /**
     * Permet de savoir combien de cours dispense un professeur
     *
     * @param matricule Matricule du professeur connecté
     * @return le nombre de cours dispensé
     */
    public static int nombreCoursProfesseur(String matricule)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM enseigner " +
                "WHERE enseigner.Matricule_Prof = " + matricule;

        ResultSet resultat = database.run_Statement_READ(query);

        int cpt = getRows(resultat);
        database.Database_Deconnection();
        return cpt;
    }

    /**
     * Permet de récupérer une valeur précise dans la table Cours
     *
     * @param coursCode Code du cours recherché
     * @param valeur    Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public static String getCours(String coursCode, String valeur)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM cours " +
                        "WHERE cours.Code = " + coursCode + " ;";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /**
     * Permet de récupérerla liste des cours qu'une professeur dispense
     *
     * @param matricule Matricule du professeur connecté
     * @return la liste des Codes des cours dispensés
     */
    public static ArrayList<String> getCoursArray(String matricule, String valeur)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM professeur, enseigner, cours " +
                        "WHERE professeur.Matricule = " + matricule + " " +
                        "AND professeur.Matricule = enseigner.Matricule_Prof " +
                        "AND enseigner.Code = cours.Code;";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_ARRAY(resultat, valeur);
    }


    /**
     * Permet de savoir combien de Groupes suivent un cours donné
     *
     * @param coursCode Code du cours donné
     * @return le nombre de Groupes suivant le cours
     */
    public static int nombreGroupeSuivantCours(String coursCode)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM cours, suivre, groupe " +
                        "WHERE cours.Code = " + coursCode + " " +
                        "AND cours.Code = suivre.Code " +
                        "AND suivre.Groupe_ID = groupe.Groupe_ID;";

        ResultSet resultat = database.run_Statement_READ(query);

        return getRows(resultat);
    }
}
