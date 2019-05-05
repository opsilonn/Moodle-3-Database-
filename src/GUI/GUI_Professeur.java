package GUI;


import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;
import recherche.rechercheProfesseur;
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

    private rechercheProfesseur PROFESSEUR;

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
        super("Professeur", true, database, DIM_X, DIM_Y);
        this.matricule = matricule;
        PROFESSEUR = new rechercheProfesseur(database);


        remplirInformations();


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    /**
     * Remplissage des champs sur l'information du professeur connecté
     */
    private void remplirInformations()
    {
        labelNom.setText(
                PROFESSEUR.getPersonne(matricule, "Prenom") + " " +
                PROFESSEUR.getPersonne(matricule, "Nom").toUpperCase() );
        labelMatricule.setText( matricule );
        labelMatiere.setText( "I DONT KNOW");
    }
}
