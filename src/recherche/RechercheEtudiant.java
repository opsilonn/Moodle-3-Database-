package recherche;


import static UsefulFunctions.CountRows_TableCell.getRows;

import UsefulFunctions.Database_Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Etudiant
 * *
 *
 * @author Hugues
 */
public class RechercheEtudiant extends Recherche {
    /* GROUPE */


    /**
     * Permet de savoir si l'étudiant connecté possède un groupe
     *
     * @param matricule Matricule de l'élève connecté
     * @return True s'il possède un groupe, sinon retourne false
     */
    public static boolean possedeGroupe(int matricule) {
        boolean result = false;
        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                        "FROM etudiant, groupe " +
                        "WHERE etudiant.Matricule = " + matricule + " " +
                        "AND etudiant.Groupe_ID = groupe.Groupe_ID ;";

        ResultSet resultat = database.run_Statement_READ(query);

        try {
            result = resultat.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.Database_Deconnection();
        return result;
    }


    /**
     * Permet de récupérer une valeur précise dans la table Groupe
     *
     * @param matricule Matricule de l'élève connecté
     * @param valeur    Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public static String getGroupe(int matricule, String valeur) {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM etudiant, groupe " +
                        "WHERE etudiant.Matricule = " + matricule + " " +
                        "AND etudiant.Groupe_ID = groupe.Groupe_ID ;";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* COURS */


    /**
     * Permet de savoir combien de cours suit un étudiant
     *
     * @param matricule Matricule de l'élève connecté
     * @return le nombre de cours suivi
     */
    public static int nombreCoursEtudiant(int matricule) {
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
     *
     * @param coursCode ID du cours recherché
     * @param valeur    Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public static String getCours(int coursCode, String valeur) {
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM cours " +
                        "WHERE cours.Code = " + coursCode + ";";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* NOTE */

    /**
     * Retourne la moyenne de l'élève connecté dans un cours donné
     *
     * @param matricule Matricule de l'élève connecté
     * @param coursCode ID du cours recherché
     * @return la moyenne dans le cours donné (retourne -1 si non calculable)
     */
    public static float moyenne(int matricule, int coursCode) {
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
                        "WHERE Code = " + coursCode + " " +
                        "AND Matricule_Etudiant = " + matricule + " ;";

        ResultSet resultat = database.run_Statement_READ(query);

        try {
            while (resultat.next()) {
                float note = Float.parseFloat(resultat.getString("Valeur"));

                switch (resultat.getString("Type")) {
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

            if (TP_note != -1 && DE_note != -1 && PROJET_note != -1)
                MOYENNE = TP_note * TP_coef + DE_note * DE_coef + PROJET_note * PROJET_coef;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        database.Database_Deconnection();
        return MOYENNE / 100;
    }


    /**
     * Retourne la moyenne générale de l'élève connecté (toutes matières confondues)
     *
     * @param matricule Matricule de l'élève connecté
     * @return la moyenne générale de l'élève connecté (retourne -1 si non calculable)
     */
    public static float moyenneGenerale(int matricule) {
        float MOYENNE_GENERALE = -1;

        float moyenneGenerale = 0;
        float coefficientGeneral = 0;

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT cours.Code, cours.Coefficient " +
                        "FROM etudiant, cours, suivre " +
                        "WHERE etudiant.Matricule = " + matricule + " " +
                        "AND etudiant.Groupe_ID = suivre.Groupe_ID " +
                        "AND suivre.Code = cours.Code;";

        ResultSet resultat = database.run_Statement_READ(query);

        try {
            while (resultat.next()) {
                float coefficient = resultat.getFloat("cours.Coefficient");
                float moyenne = moyenne(matricule, resultat.getInt("cours.Code"));

                if (moyenne == -1)
                    return -1;
                else {
                    moyenneGenerale += coefficient * moyenne;
                    coefficientGeneral += coefficient;
                }
            }

            MOYENNE_GENERALE = moyenneGenerale / coefficientGeneral;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        database.Database_Deconnection();

        return MOYENNE_GENERALE;
    }

    public static ArrayList<Integer> eleveBelongingtoGroup(int groupID) {
        ArrayList<Integer> matricules = new ArrayList<Integer>();

        String sql = "SELECT Matricule from etudiant WHERE Groupe_ID = " + groupID;

        Database_Connection database = new Database_Connection();
        ResultSet resultat = database.run_Statement_READ(sql);
        if (getRows(resultat) == 0) {
            System.out.println("NO etudiant");
            return null;
        }
        try {
            while (resultat.next()) {
                matricules.add(resultat.getInt("Matricule"));
            }
        } catch (SQLException ignore) {
        }

        database.Database_Deconnection();
        return matricules;
    }

    public static ArrayList<Integer> CoursfollowedByGroup(int groupID) {
        ArrayList<Integer> codes = new ArrayList<Integer>();

        String sql = "SELECT Code from suivre WHERE Groupe_ID = " + groupID;

        Database_Connection database = new Database_Connection();
        ResultSet resultat = database.run_Statement_READ(sql);
        if (getRows(resultat) == 0) {
            return null;
        }


        try {
            while (resultat.next()) {
                codes.add(resultat.getInt("Code"));
            }
        } catch (SQLException ignore) {
            ignore.printStackTrace();
        }

        database.Database_Deconnection();
        return codes;
    }


    public static float moyenneMinMax(int groupeID, boolean mode, int cours) {
        float returnValue = -1;
        ArrayList<Integer> matricules = RechercheEtudiant.eleveBelongingtoGroup(groupeID);
        for (int i = 0; i < matricules.size(); i++) {
            float moy = moyenne(matricules.get(i), cours);
            if (returnValue == -1 ||
                    (moy != -1 && returnValue < moy && mode) ||
                    (moy != -1 && returnValue > moy && !mode)) {
                returnValue = moy;
            }
        }
        return returnValue;
    }

    public static float moyenneGeneraleMinMax(int groupeID, boolean mode) {
        float returnValue = -1;
        ArrayList<Integer> matricules = RechercheEtudiant.eleveBelongingtoGroup(groupeID);
        for (int i = 0; i < matricules.size(); i++) {
            float moy = moyenneGenerale(matricules.get(i));
            if (returnValue == -1 ||
                    (moy != -1 && returnValue < moy && mode) ||
                    (moy != -1 && returnValue > moy && !mode)) {
                returnValue = moy;
            }
        }
        return returnValue;
    }
}
