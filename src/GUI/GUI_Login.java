package GUI;


import GUI_Components.*;
import Gestion_admin.Database_Connection;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Fenêtre permettant de se connecter au logiciel (version 4).
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_Login extends CustomJFrame
{
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
     * @param  database liaison à la base de données SQL
     */
    public GUI_Login(Database_Connection database)
    {
        super("Login", true, DIM_X, DIM_Y);
        this.database = database;

        // Adds the logo image
        ImageIcon imageIcon = new ImageIcon(PATH_LOGO_FULL); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (DIM_X * 0.6), DIM_Y /3,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        labelLogo.setIcon(imageIcon);


        labelIncorrect.setVisible(false);
        buttonLogin.addActionListener( e -> loginVerifier() );


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents()
    {
        fieldMatricule = new CustomJTextField("NUMERIC",  false, 8);
        fieldPassword = new CustomJTextField("ALL",  true, 20);
    }


    /**
     * Lance les vérification du login en testant successivement les tables "etudiant" et "professeur"
     */
    private void loginVerifier()
    {
        CustomJFrame frame;
        boolean etudiant = loginTest("etudiant");
        boolean professeur = loginTest("professeur");

        if (etudiant)
            frame = new GUI_Etudiant(database, fieldMatricule.getText());

        if(professeur)
            frame = new GUI_Professeur(database, fieldMatricule.getText());

        if(etudiant || professeur)
            dispose();
        else
            labelIncorrect.setVisible(true);
    }


    /**
     * Vérifie si les valeurs entrées correspondent aux valeurs d'une table précise
     * @param  table Nom de la table SQL à vérifier
     * @return Retourne true si les valeurs correspondent, sinon retourne false
     */
    private boolean loginTest(String table)
    {
        String inputM = fieldMatricule.getText();
        String inputMDP = String.valueOf( fieldPassword.getPassword() );

        if(inputM.length() != 0)
        {
            try
            {
                String query =
                        "SELECT Matricule, Password " +
                                "FROM " + table + " " +
                                "WHERE Matricule = " + inputM + " " +
                                "AND Password = '" + inputMDP + "' ;";

                ResultSet resultat = database.run_Statement_READ(query);

                if ( resultat.next() )
                    return true;

            }
            catch (SQLException e1)
            {
                e1.printStackTrace();
            }
        }

        return false;
    }
}