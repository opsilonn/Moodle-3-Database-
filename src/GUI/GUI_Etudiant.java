package GUI;


import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;
import javax.swing.*;


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

    private String ID;

    private JPanel panel;

    private JLabel labelNom;
    private JLabel labelID;
    private JLabel labelPromotion;
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
     * @param ID - ID de l'élève connecté
     */
    public GUI_Etudiant(Database_Connection database, String ID)
    {
        super("Eleve", true, DIM_X, DIM_Y);
        this.database = database;
        this.ID = ID;


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }
}
