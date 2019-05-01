package GUI;


import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Fenêtre dédiée à l'utilisation du logiciel par un Professeur
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_Professeur extends CustomJFrame
{
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;

    private String matricule;

    private JPanel panel;
    private JButton buttonChercherEleve;
    private JLabel labelNom;
    private JLabel labelMatricule;
    private JLabel labelMatiere;
    private JButton buttonModifier;

    /**
     * Création de l'interface pour un Eleve
     *
     * @param  database liaison à la base de données SQL
     * @param matricule - Matricule de l'élève connecté
     */
    public GUI_Professeur(Database_Connection database, String matricule)
    {
        super("Professeur", true, DIM_X, DIM_Y);
        this.database = database;
        this.matricule = matricule;


        try
        {
            String query =
                    "SELECT * " +
                    "FROM personne, professeur " +
                    "WHERE personne.ID = professeur.ID_Personne AND Matricule = " + matricule +  " ;";

            ResultSet resultat = database.run_Statement_READ(query);

            while ( resultat.next() )
            {
                labelNom.setText( resultat.getString("Prenom") + " " + resultat.getString("Nom").toUpperCase() );
                labelMatricule.setText(matricule);
                labelMatiere.setText( "I DONT KNOW");
            }
        }
        catch (SQLException e1)
        {
            e1.printStackTrace();
        }

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }
}
