package GUI;


import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Fenêtre dédiée à l'utilisation du logiciel par un Eleve
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_Etudiant extends CustomJFrame
{
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;

    private String matricule;

    private JPanel panel;

    private JLabel labelNom;
    private JLabel labelMatricule;
    private JLabel labelGroupe;
    private JLabel labelMoyenne;

    private JButton buttonCorrecteur;
    private JButton buttonPromotion;

    private JLabel labelErreur;

    private JScrollPane bulletin;
    private JTable bulletinValeurs;
    private JScrollPane bulletinPromo;
    private JTable bulletinPromoValeurs;

    /**
     * Création de l'interface pour un Eleve
     *
     * @param  database liaison à la base de données SQL
     * @param matricule - Matricule de l'élève connecté
     */
    public GUI_Etudiant(Database_Connection database, String matricule)
    {
        super("Etudiant", true, DIM_X, DIM_Y);
        this.database = database;
        this.matricule = matricule;

        try
        {
            String query =
                    "SELECT * " +
                    "FROM personne, etudiant " +
                    "WHERE personne.ID = etudiant.ID_Personne AND Matricule = " + matricule +  " ;";

            ResultSet resultat = database.run_Statement_READ(query);

            if ( resultat.next() )
            {
                labelNom.setText( resultat.getString("Prenom") + " " + resultat.getString("Nom").toUpperCase() );
                labelMatricule.setText( matricule );
                labelGroupe.setText( resultat.getString( "Groupe_ID" ));
                // labelMatiere.setText( "I DONT KNOW");
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
