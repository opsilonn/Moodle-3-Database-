package recherche;


import Gestion_admin.Database_Connection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Professeur
 **
 * @author Hugues
 */
public abstract class recherche
{
    protected Database_Connection database;
    protected String query;
    protected ResultSet resultat;


    /**
     * Permet de récupérer une valeur précise dans un ResultSet donné
     * @param resultat Resultat de la requête SQL
     * @param valeur Rang recherché dans la requête
     * @return la valeur recherchée, retourne null si non trouvée
     */
    protected String RETOURNER_RESULTAT(ResultSet resultat, String valeur)
    {
        String result = null;

        try
        {
            if(resultat.next() )
                result = resultat.getString( valeur );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        database.Database_Deconnection();
        return result;
    }


    /**
     * Permet d'obtenir la taille d'une requête SQL
     * @param resultat Resultat de la requête SQL
     * @return la taille de la requête SQL
     */
    protected int RETOURNER_COMPTEUR(ResultSet resultat)
    {
        int cpt = 0;

        try
        {
            while (resultat.next() )
                cpt++;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        database.Database_Deconnection();
        return cpt;
    }


    /**
     * Permet de récupérer la liste d'un élément précis sur une requête donnée
     * @param resultat Resultat de la requête SQL
     * @param valeur Rang recherché dans la requête
     * @return la liste contenant toutes les valeurs cherchées dans la requête donnée
     */
    protected ArrayList<String> RETOURNER_ARRAY(ResultSet resultat, String valeur)
    {
        ArrayList<String> liste = new ArrayList<>();

        try
        {
            while (resultat.next() )
                liste.add( resultat.getString( valeur ));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        database.Database_Deconnection();
        return liste;
    }
}
