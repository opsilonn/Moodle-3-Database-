package recherche;


import static UsefulFunctions.CountRows_TableCell.getRows;
import UsefulFunctions.Database_Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Etudiant
 **
 * @author Hugues
 */
public class RechercheEtudiant extends Recherche
{
    /* GROUPE */


    /**
     * Permet de savoir si l'étudiant connecté possède un groupe
     * @param matricule Matricule de l'élève connecté
     * @return True s'il possède un groupe, sinon retourne false
     */
    public static boolean possedeGroupe(String matricule)
    {
        boolean result = false;
        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                        "FROM etudiant, groupe " +
                        "WHERE etudiant.Matricule = " + matricule +  " " +
                        "AND etudiant.Groupe_ID = groupe.Groupe_ID ;";

        ResultSet resultat = database.run_Statement_READ(query);

        try
        {
            result = resultat.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        database.Database_Deconnection();
        return result;
    }


    /**
     * Permet de récupérer une valeur précise dans la table Groupe
     * @param matricule Matricule de l'élève connecté
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public static String getGroupe(String matricule, String valeur)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM etudiant, groupe " +
                "WHERE etudiant.Matricule = " + matricule +  " " +
                "AND etudiant.Groupe_ID = groupe.Groupe_ID ;";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* COURS */


    /**
     * Permet de savoir combien de cours suit un étudiant
     * @param matricule Matricule de l'élève connecté
     * @return le nombre de cours suivi
     */
    public static int nombreCoursEtudiant(String matricule)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM etudiant,suivre " +
                "WHERE etudiant.Matricule = " + matricule + " " +
                "AND etudiant.Groupe_ID = suivre.Groupe_ID";

        System.out.println(query);
        ResultSet resultat = database.run_Statement_READ(query);

        return getRows(resultat);
    }



    /**
     * Permet de récupérer une valeur précise dans la table Cours
     * @param coursCode ID du cours recherché
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public static String getCours(String coursCode, String valeur)
    {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM cours " +
                "WHERE cours.Code = " + coursCode +  ";";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* NOTE */

    /**
     * Retourne la moyenne de l'élève connecté dans un cours donné
     * @param matricule Matricule de l'élève connecté
     * @param coursCode ID du cours recherché
     * @return la moyenne dans le cours donné (retourne -1 si non calculable)
     */
    public static float moyenne(String matricule, String coursCode)
    {
        float MOYENNE = -1;

        float TP_note = -1;
        float DE_note = -1;
        float PROJET_note = -1;

        float TP_coef = Float.parseFloat(getCours(coursCode, "TP_pourcentage"));
        float DE_coef = Float.parseFloat(getCours(coursCode, "DE_pourcentage"));
        float PROJET_coef = Float.parseFloat(getCours(coursCode, "Projet_pourcentage"));

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM note " +
                "WHERE note.Code = " + coursCode + " " +
                "AND note.Matricule_Etudiant = " + matricule + " ;";

        ResultSet resultat = database.run_Statement_READ(query);

        try
        {
            while (resultat.next())
            {
                float note = Float.parseFloat(resultat.getString("note.Valeur"));

                switch ( resultat.getString("note.Type") )
                {
                    case "TP":
                        TP_note = note;
                        break;

                    case "DE":
                        DE_note = note;
                        break;

                    case "Projet":
                        PROJET_note = note;
                        break;

                    default:
                        System.out.println("That's weird");
                        break;
                }
            }

            if(TP_note != -1 && DE_note != -1 && PROJET_note != -1)
                MOYENNE = TP_note * TP_coef + DE_note * DE_coef + PROJET_note * PROJET_coef;
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        database.Database_Deconnection();
        return MOYENNE;
    }


    /**
     * Retourne la moyenne générale de l'élève connecté (toutes matières confondues)
     * @param matricule Matricule de l'élève connecté
     * @return la moyenne générale de l'élève connecté (retourne -1 si non calculable)
     */
    public static float moyenneGenerale(String matricule)
    {
        float MOYENNE_GENERALE = -1;

        float moyenneGenerale = 0;
        float coefficientGeneral = 0;

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM etudiant, cours, suivre " +
                "WHERE etudiant.Matricule = " + matricule + " " +
                "AND etudiant.Groupe_ID = suivre.Groupe_ID " +
                "AND suivre.Code = cours.Code;";

        ResultSet resultat = database.run_Statement_READ(query);

        try
        {
            while ( resultat.next() )
            {
                float coefficient = resultat.getFloat("cours.Coefficient");
                float moyenne = moyenne(matricule, resultat.getString("cours.Code"));

                if( moyenne == -1 )
                    return -1;
                else
                {
                    moyenneGenerale += coefficient * moyenne;
                    coefficientGeneral += coefficient;
                }
            }

            MOYENNE_GENERALE = moyenneGenerale / coefficientGeneral;
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        database.Database_Deconnection();
        return MOYENNE_GENERALE;
    }
}
