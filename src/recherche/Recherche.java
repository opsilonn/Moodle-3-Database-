package recherche;


import static UsefulFunctions.CountRows_TableCell.getRows;
import UsefulFunctions.Database_Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Professeur
 * *
 *
 * @author Hugues
 */
public class Recherche
{
    /**
     * Permet de récupérer une valeur précise dans la table Personne en fonction d'un élève
     * @param matricule Matricule de l'élève connecté
     * @param tablePersonne table de la personne connectée, soit professeur ou etudiant
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public static String getPersonne(int matricule, String tablePersonne, String valeur)
    {
        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                        "FROM personne, " + tablePersonne + " " +
                        "WHERE personne.ID = " + tablePersonne + ".ID_Personne " +
                        "AND Matricule = " + matricule +  " ;";

        ResultSet resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }



    /**
     * Permet de récupérer une valeur précise dans un ResultSet donné
     *
     * @param resultat Resultat de la requête SQL
     * @param valeur   Rang recherché dans la requête
     * @return la valeur recherchée, retourne null si non trouvée
     */
    protected static String RETOURNER_RESULTAT(ResultSet resultat, String valeur)
    {
        String result = null;

        try
        {
            if (resultat.next())
                result = resultat.getString(valeur);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }


    /**
     * Permet de récupérer la liste d'un élément précis sur une requête donnée
     *
     * @param resultat Resultat de la requête SQL
     * @param valeur   Rang recherché dans la requête
     * @return la liste contenant toutes les valeurs cherchées dans la requête donnée
     */
    protected static ArrayList<String> RETOURNER_ARRAY(ResultSet resultat, String valeur) {
        ArrayList<String> liste = new ArrayList<>();

        try {
            while (resultat.next())
                liste.add(resultat.getString(valeur));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return liste;
    }


    /**
     * Permet de récupérer le nombre de ligne compris dans une table
     *
     * @param table Rang recherché dans la requête
     * @return la taille de la table recherchée
     */
    public static int TAILLE_TABLE(String table)
    {
        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                        "FROM " + table + " ;";

        ResultSet resultat = database.run_Statement_READ(query);
        database.Database_Deconnection();

        return getRows(resultat);
    }
}

