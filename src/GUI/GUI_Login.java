package GUI;


import GUI_Components.*;
import Gestion_admin.Database_Connection;
import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;


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

    public JTextField fieldID;
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
        fieldID = new CustomJTextField("NUMERIC",  false, 8);
        fieldPassword = new CustomJTextField("ALL",  true, 20);
    }





    private void loginVerifier()
    {
        String inputID = fieldID.getText();
        String inputMDP = String.valueOf( fieldPassword.getPassword() );

        try
        {
            ResultSet resultat;

            // REQUÊTE POUR ETUDIANT
            String queryETUDIANT =
                    "SELECT * " +
                            "FROM personne " +
                            "INNER JOIN etudiant " +
                            "ON personne.ID = etudiant.ID_Personne;";

            resultat = database.run_Statement_READ(queryETUDIANT);

            while ( resultat.next() )
            {
                if(Objects.equals( inputID, resultat.getString("personne.ID") )
                        && Objects.equals( inputMDP, resultat.getString("etudiant.Password") ))
                {
                    GUI_Etudiant prof = new GUI_Etudiant(database, inputID);
                    dispose();
                }
            }



            // REQUÊTE POUR PROFESSEUR
            String queryPROF =
                "SELECT * " +
                "FROM personne " +
                "INNER JOIN professeur " +
                "ON personne.ID = professeur.ID_Personne;";

            resultat = database.run_Statement_READ(queryPROF);

            while ( resultat.next() )
            {
                if(Objects.equals( inputID, resultat.getString("personne.ID") )
                    && Objects.equals( inputMDP, resultat.getString("professeur.Password") ))
                {
                    GUI_Professeur prof = new GUI_Professeur(database, inputID);
                    dispose();
                }
            }
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        labelIncorrect.setVisible(true);
    }
}