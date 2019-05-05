package recherche;

import Gestion_admin.Database_Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

public class rechercheEtudiant
{
    private Database_Connection database;
    private String query;
    private ResultSet resultat;


    public rechercheEtudiant(Database_Connection database)
    {
        this.database = database;
    }


    /* PERSONNE */


    /**
     * Permet de récupérer une valeur précise dans la table Personne en fonction d'un élève
     * @param matricule Matricule de l'élève connecté
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public String getPersonne(String matricule, String valeur)
    {
        query =
                "SELECT * " +
                "FROM personne, etudiant " +
                "WHERE personne.ID = etudiant.ID_Personne " +
                "AND Matricule = " + matricule +  " ;";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* GROUPE */


    /**
     * Permet de savoir si l'étudiant connecté possède un groupe
     * @param matricule Matricule de l'élève connecté
     * @return True s'il possède un groupe, sinon retourne false
     */
    public boolean possedeGroupe(String matricule)
    {
        query =
                "SELECT * " +
                        "FROM etudiant, groupe " +
                        "WHERE Matricule = " + matricule +  " " +
                        "AND groupe.Groupe_ID = etudiant.Groupe_ID ;";

        resultat = database.run_Statement_READ(query);

        try
        {
            return resultat.next();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Permet de récupérer une valeur précise dans la table Groupe
     * @param matricule Matricule de l'élève connecté
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public String getGroupe(String matricule, String valeur)
    {
        query =
                "SELECT * " +
                "FROM etudiant, groupe " +
                "WHERE Matricule = " + matricule +  " " +
                "AND groupe.Groupe_ID = etudiant.Groupe_ID ;";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* COURS */


    /**
     * Permet de savoir combien de cours suit un étudiant
     * @param matricule Matricule de l'élève connecté
     * @return le nombre de cours suivi
     */
    public int nombreCours(String matricule)
    {
        int cpt = 0;

        query =
                "SELECT * " +
                "FROM etudiant, groupe, cours, suivre " +
                "WHERE etudiant.Matricule = " + matricule + " " +
                "AND etudiant.Groupe_ID = groupe.Groupe_ID " +
                "AND groupe.Groupe_ID = suivre.Groupe_ID " +
                "AND suivre.Code = cours.Code;";

        resultat = database.run_Statement_READ(query);

        try
        {
            while( resultat.next() )
                cpt++;
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        return cpt;
    }



    /**
     * Permet de récupérer une valeur précise dans la table Cours
     * @param coursCode ID du cours recherché
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public String getCours(String coursCode, String valeur)
    {
        query =
                "SELECT * " +
                "FROM cours " +
                "WHERE cours.Code = " + coursCode +  ";";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }


    /* NOTE */

    /**
     * Retourne la moyenne de l'élève connecté dans un cours donné
     * @param matricule Matricule de l'élève connecté
     * @param coursCode ID du cours recherché
     * @return la moyenne dans le cours donné (retourne -1 si non calculable)
     */
    public float moyenne(String matricule, String coursCode)
    {
        float TP_note = -1;
        float DE_note = -1;
        float PROJET_note = -1;

        float TP_coef = Float.parseFloat(getCours(coursCode, "TP_pourcentage"));
        float DE_coef = Float.parseFloat(getCours(coursCode, "DE_pourcentage"));
        float PROJET_coef = Float.parseFloat(getCours(coursCode, "Projet_pourcentage"));

        String query =
                "SELECT * " +
                "FROM cours, note " +
                "WHERE cours.Code = " + coursCode + " " +
                "AND cours.Code = note.Code " +
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
                return TP_note * TP_coef + DE_note * DE_coef + PROJET_note * PROJET_coef;
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        return -1;
    }


    /**
     * Retourne la moyenne générale de l'élève connecté (toutes matières confondues)
     * @param matricule Matricule de l'élève connecté
     * @return la moyenne générale de l'élève connecté (retourne -1 si non calculable)
     */
    public float moyenneGenerale(String matricule)
    {
        float moyenneGenerale = 0;
        float coefficientGeneral = 0;

        query =
                "SELECT * " +
                "FROM etudiant, groupe, cours, suivre " +
                "WHERE etudiant.Matricule = " + matricule + " " +
                "AND etudiant.Groupe_ID = groupe.Groupe_ID " +
                "AND groupe.Groupe_ID = suivre.Groupe_ID " +
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

            return moyenneGenerale / coefficientGeneral;
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
            return -1;
        }
    }


    /* BONUS */



    /**
     * Permet de récupérer une valeur précise dans un ResultSet donné
     * @param resultat Resultat de la requête SQL
     * @param valeur Rang recherché dans la requête
     * @return la valeur recherchée, retourne null si non trouvée
     */
    private String RETOURNER_RESULTAT(ResultSet resultat, String valeur)
    {
        try
        {
            if(resultat.next() )
                return resultat.getString( valeur );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
