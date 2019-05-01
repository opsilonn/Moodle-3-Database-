package GUI;


import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


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

    private String ID;

    private JPanel panel;
    private JButton buttonChercherEleve;
    private JLabel labelNom;
    private JLabel labelID;
    private JLabel labelMatiere;
    private JButton buttonModifier;

    /**
     * Création de l'interface pour un Eleve
     *
     * @param  database liaison à la base de données SQL
     * @param ID - ID de l'élève connecté
     */
    public GUI_Professeur(Database_Connection database, String ID)
    {
        super("Professeur", true, DIM_X, DIM_Y);
        this.database = database;
        this.ID = ID;


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }
}
