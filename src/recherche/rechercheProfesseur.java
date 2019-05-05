package recherche;


import Gestion_admin.Database_Connection;

import java.sql.ResultSet;

/**
 * Classe permettant de faciliter les requêtes SQL relatives à un Professeur
 **
 * @author Hugues
 */
public class rechercheProfesseur extends recherche
{
    public rechercheProfesseur(Database_Connection database)
    {
        super(database);
    }


    /**
     * Permet de récupérer une valeur précise dans la table Personne en fonction d'un professeur
     * @param matricule Matricule du professeur connecté
     * @param valeur Rang recherché dans la table
     * @return la valeur recherchée, retourne null si non trouvée
     */
    public String getPersonne(String matricule, String valeur)
    {
        query =
                "SELECT * " +
                "FROM personne, professeur " +
                "WHERE personne.ID = professeur.ID_Personne " +
                "AND Matricule = " + matricule +  " ;";

        resultat = database.run_Statement_READ(query);

        return RETOURNER_RESULTAT(resultat, valeur);
    }
}
