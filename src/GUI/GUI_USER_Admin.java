package GUI;

import UsefulFunctions.Database_Connection;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class GUI_USER_Admin extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JButton buttonSearch;
    private JButton buttonCreate;
    private JButton buttonConsult;
    private JButton etudiantButton;
    private JButton professeurButton;
    private JButton responsableButton;
    private JButton coursButton;
    private JPanel panelAdmin;
    private JPanel panelChoice;
    private JButton groupeButton;
    private JPanel panel;
    private JButton buttonModif;

    private boolean search;

    /**
     * Création de l'interface pour une personne de l'administration
     */
    public GUI_USER_Admin() {
        super("Bienvenue", true, DIM_X, DIM_Y);

        panelChoice.setVisible(false);

        buttonSearch.addActionListener(e -> action(true));
        buttonCreate.addActionListener(e -> action(false));
        buttonConsult.addActionListener(e -> consult());
        buttonModif.addActionListener(e -> new GUI_modifierNote(-1));

        etudiantButton.addActionListener(e -> action("etudiant", 1));
        professeurButton.addActionListener(e -> action("professeur", 2));
        responsableButton.addActionListener(e -> action("responsable", 3));

        coursButton.addActionListener(e -> cours());
        groupeButton.addActionListener(e -> groupe());

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    /**
     * Sauvegarde le choix de la personne pour définir le mode d'éxecution
     *
     * @param mode si mode = true on effectue une simple recherche avec possibilités de modifications
     *             si false on effectue un ajout d'un élément
     */
    private void action(boolean mode) {
        search = mode;
        panelAdmin.setVisible(false);
        panelChoice.setVisible(true);
    }

    /**
     * Lancement de l'interface de consultation des cours
     */
    private void consult() {
        new GUI_consulterCours();
    }

    /**
     * Fonction de lancer des interfaces en fonction du choix de l'utilisateur
     *
     * @param table  Instance choisi par l'utilisateur :
     *               ETUDIANT
     *               PROF
     *               RESPONSABLE
     * @param choice code du choix pour la table
     */
    private void action(String table, int choice) {
        if (!search) {
            int res = createInstance(choice);
            new GUI_chercherPersonne(table, res);
        } else {
            new GUI_chercherPersonne(table, -1);
        }
        dispose();
    }

    /**
     * Lancement de l'interface d'un cours
     */
    private void cours() {
        if (!search) {
            int res = createInstance(4);
            //TODO Gérer le -1 dans toute la classe//
            new GUI_Cours(res);
        } else {
            new GUI_Cours(-1);
        }
        dispose();
    }

    /**
     * Lancement de l'interface d'un groupe
     */
    private void groupe() {
        if (!search) {
            int res = createInstance(5);
            new GUI_Groupe(res);
        } else {
            new GUI_Groupe(-1);
        }
        dispose();
    }

    /**
     * Création de l'instance si l'utilisateur a décider d'ajouter quelqu'un ou quelque chose
     *
     * @param choice choix de la personne ou chose à ajouter :
     *               1. Etudiant
     *               2. Professeur
     *               3. Responsable
     *               4. Cours
     *               5. Groupe
     * @return l'ID de l'instance crée ou -1 si une erreur s'est produite
     */
    private int createInstance(int choice) {
        Database_Connection database = new Database_Connection();
        String sql = "";
        int IDPersonne = -1;
        int matricule = -1;

        if (choice < 4) {
            IDPersonne = createPersonne();
            matricule = createMatricule();
        }

        if (matricule != -1 && IDPersonne != -1) {
            switch (choice) {
                case 1: // Etudiant
                    sql = "INSERT INTO etudiant (Matricule, ID_Personne, Password) VALUES (" + matricule + ", " + IDPersonne + ",'eleve')";
                    break;
                case 2: // Professeur
                    sql = "INSERT INTO professeur (Matricule, ID_Personne, Password) VALUES (" + matricule + ", " + IDPersonne + ",'prof')";
                    break;
                case 3: //Responsable
                    sql = "INSERT INTO responsable (ID_Personne) VALUES ()";
                    database.run_Statement_WRITE(sql);
                    sql = "SELECT Max(Numero) as LastID from responsable";

                    break;
            }
        }
        switch (choice) {
            case 4:  //Cours
                sql = "INSERT INTO `cours`(`Nom`, `Description`, `Annee`, `Coefficient`, `DE_pourcentage`, `TP_pourcentage`, `Projet_pourcentage`) VALUES ('',0,'',2019,0,0,0)";
                database.run_Statement_WRITE(sql);
                sql = "SELECT Max(Code) as LastID from cours";
                break;

            case 5: //Groupe
                sql = "INSERT INTO `groupe`(`Nom`) VALUES ('')";
                database.run_Statement_WRITE(sql);
                sql = "SELECT Max(Groupe_ID) as LastID from groupe";
                break;
        }


        if (choice < 3) {
            database.run_Statement_WRITE(sql);
            database.Database_Deconnection();
            return matricule;
        }

        ResultSet result = database.run_Statement_READ(sql);
        try {
            result.next();
            return result.getInt("LastID");
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Création du matricule de l'étudiant ou du professeur.
     *
     * @return le matricule de la personne
     */
    private int createMatricule() {
        int maximum = 0;
        int year = Calendar.getInstance().get(Calendar.YEAR);

        maximum = findMaximum("etudiant", maximum, year);
        maximum = findMaximum("professeur", maximum, year);
        maximum = findMaximum("administration", maximum, year);

        return year * 10000 + maximum + 1;
    }

    /**
     * Trouve le nombre maximum présent sur un matricule pour une année précise
     *
     * @param table   table dans laquelle cherché le matricule maximum
     * @param maximum maximum actuel pour le matricule
     * @param year    année recherché du matricule
     * @return retourne le maximum trouvé incluant celui donné en paramètre
     */
    private int findMaximum(String table, int maximum, int year) {

        Database_Connection database = new Database_Connection();
        String sql = "SELECT Max(Matricule) from etudiant";
        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (data.next()) {
                String number = data.getString("Max(Matricule)");
                if (number.substring(0, 4).equals(String.valueOf(year)) &&
                        Integer.parseInt(number.substring(4, 8)) > maximum) {
                    maximum = Integer.parseInt(number.substring(4, 8));
                }
            }
        } catch (SQLException ignore) {
        }

        database.Database_Deconnection();
        return maximum;
    }

    /**
     * Création d'une instance dans la table Personne
     *
     * @return l'ID de la nouvelle personne crée
     */
    private int createPersonne() {
        Database_Connection database = new Database_Connection();
        String sql = "INSERT INTO personne (Nom, Prenom) VALUES ('','')";
        database.run_Statement_WRITE(sql);
        ResultSet data = database.run_Statement_READ("SELECT Max(ID) from personne");
        try {
            if (data.next()) {
                return data.getInt("Max(ID)");
            }
        } catch (SQLException igore) {
        }
        database.Database_Deconnection();
        return -1;
    }
}
