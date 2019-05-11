package GUI;


import GUI_Components.CustomJTextField;
import UsefulFunctions.Database_Connection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Fenêtre permettant de se connecter au logiciel (version 4).
 * <p>
 * Cette classe hérite de {@link GUI_Components.CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_Login extends GUI_Components.CustomJFrame {
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;

    private JPanel panel;

    private JLabel labelLogo;

    public JTextField fieldMatricule;
    public JPasswordField fieldPassword;

    public JButton buttonLogin;
    public JLabel labelIncorrect;

    /**
     * Création de l'interface de login
     * <<<<<<< Updated upstream
     */

    public GUI_Login() {
        super("Login", true, DIM_X, DIM_Y);

        // Adds the logo image
        ImageIcon imageIcon = new ImageIcon(PATH_LOGO_FULL); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (DIM_X * 0.6), DIM_Y / 3, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        labelLogo.setIcon(imageIcon);


        labelIncorrect.setVisible(false);
        buttonLogin.addActionListener(e -> loginVerifier());


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents() {
        fieldMatricule = new CustomJTextField("NUMERIC", false, 8);
        fieldPassword = new CustomJTextField("ALL", true, 20);
    }


    /**
     * Lance les vérification du login en testant successivement les tables "etudiant" et "professeur"
     */
    private void loginVerifier() {
        boolean etudiant = loginTest("etudiant");
        boolean professeur = loginTest("professeur");
        boolean admin = loginTest("administration");


        if (etudiant)
            new GUI_USER_Etudiant(Integer.parseInt(fieldMatricule.getText()));
        else if (professeur)
            new GUI_USER_Professeur(Integer.parseInt(fieldMatricule.getText()));
        else if (admin)
            new GUI_USER_Admin();


        if (etudiant || professeur || admin)
            dispose();
        else
            labelIncorrect.setVisible(true);
    }


    /**
     * Vérifie si les valeurs entrées correspondent aux valeurs d'une table précise
     *
     * @param table Nom de la table SQL à vérifier
     * @return Retourne true si les valeurs correspondent, sinon retourne false
     */
    private boolean loginTest(String table) {
        boolean result = false;
        String inputM = fieldMatricule.getText();
        String inputMDP = String.valueOf(fieldPassword.getPassword());
        Database_Connection database = new Database_Connection();
        String query =
                "SELECT Matricule, Password " +
                        "FROM " + table + " " +
                        "WHERE Matricule = " + inputM + " " +
                        "AND Password = '" + inputMDP + "' ;";


        if (inputM.length() != 0) {
            try {
                ResultSet resultat = database.run_Statement_READ(query);

                if (resultat.next())
                    result = true;

            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
        database.Database_Deconnection();
        return result;
    }
}