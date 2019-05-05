package recherche;


import Gestion_admin.Database_Connection;

import java.sql.ResultSet;
import java.sql.SQLException;

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


    public recherche(Database_Connection database)
    {
        this.database = database;
    }


    /**
     * Permet de récupérer une valeur précise dans un ResultSet donné
     * @param resultat Resultat de la requête SQL
     * @param valeur Rang recherché dans la requête
     * @return la valeur recherchée, retourne null si non trouvée
     */
    protected String RETOURNER_RESULTAT(ResultSet resultat, String valeur)
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
