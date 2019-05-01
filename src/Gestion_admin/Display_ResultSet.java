package Gestion_admin;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Display_ResultSet {

    public static void DisplayPersonne(ResultSet resultat) {
        try {
            while (resultat.next()) {
                String nom = resultat.getString("Nom");
                String prenom = resultat.getString("Prenom");
                System.out.println("Nom : " + nom + " Prenom: " + prenom);
            }
        } catch (SQLException ignore) {
        }
    }

    public static int getRows(ResultSet res){
        int totalRows = 0;
        try {
            res.last();
            totalRows = res.getRow();
            res.beforeFirst();
        }
        catch(Exception ex)  {
            return 0;
        }
        return totalRows ;
    }
}
