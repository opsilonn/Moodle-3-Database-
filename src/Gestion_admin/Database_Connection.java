package Gestion_admin;

import java.sql.*;

public class Database_Connection {
    private static final String url = "jdbc:mysql://localhost:3306/projet_database";
    private static final String user = "root";
    private static final String password = "";
    Connection connexion;

    public Database_Connection() {
        try {
            connexion = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Impossible de se connecter à la base de données");
            connexion = null;
        }
    }

    public ResultSet run_Statement_READ(String query) {

        try {
            Statement statement = connexion.createStatement();
            ResultSet resultat = statement.executeQuery(query);
            return resultat;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int run_Statement_WRITE(String query) {
        try {
            Statement statement = connexion.createStatement();
            return statement.executeUpdate(query);

        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void Database_Deconnection() {
        //System.out.println("Objet nettoyé de la mémoire");
        if (connexion != null)
            try {
                //Fermeture de la connexion
                connexion.close();
            } catch (SQLException ignore) {
                //Si une erreur survient lors de la fermeture, il suffit de l'ignorer.
            }
    }

}
