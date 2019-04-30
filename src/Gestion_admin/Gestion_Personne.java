package Gestion_admin;


import java.util.Scanner;

public class Gestion_Personne {

    public static void Find_Person() {
        Scanner userInput = new Scanner(System.in);
        Database_Connection database = new Database_Connection();
        //demander si recherche prof/ eleve ou respo
        //demander le matricule ou numero.
        //chercher la personne selon le matricule ou numero
        System.out.println("Do your want to find a prof(1), eleve(2) ou respo(3) ?");
        int choice = Integer.parseInt(userInput.nextLine());
        switch (choice) {
            case 1:
                //cherchcer dans prof et personne && adresse
                break;
            case 2: // chercher dans etudiant puis personne JOIN identite et adresse
                /* Création de l'objet gérant les requêtes */
                //System.out.println("HERE");
                String sql = "SELECT * FROM personne";

                Display_ResultSet.DisplayPersonne(database.run_Statement_READ(sql));

                break;
            case 3: // chercher dans responsable puis personne JOIN identite et adresse
                break;
        }
        database.Database_Deconnection();
    }

}
