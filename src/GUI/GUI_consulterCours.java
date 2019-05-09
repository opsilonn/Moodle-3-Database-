package GUI;


import GUI_Components.CustomJFrame;
import UsefulFunctions.Database_Connection;
import recherche.Recherche;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static UsefulFunctions.CountRows_TableCell.createModel;
import static UsefulFunctions.CountRows_TableCell.getRows;


/**
 * Fenêtre dédiée à l'affichage de tous les cours disponibles
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_consulterCours extends CustomJFrame
{
    private static final int DIM_X = 700;
    private static final int DIM_Y = 400;

    private Recherche recherche = new Recherche();


    private JPanel panel;
    private JTable coursTable;


    public GUI_consulterCours()
    {
        super("Liste des Cours", false, DIM_X, DIM_Y);

        setCours();

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    /**
     * Rempli le tableau de la liste des cours (Code + Nom + Description)
     */
    private void setCours()
    {
        String[] columns = new String[]{ "Code", "Cours", "Description" };

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM cours ;";

        ResultSet resultat = database.run_Statement_READ(query);

        Object[][] DATA = new Object[getRows(resultat)][columns.length];
        try
        {
            int index = 0;
            while ( resultat.next() )
            {
                DATA[index][0] = resultat.getString("Code");
                DATA[index][1] = resultat.getString("Nom");
                DATA[index][2] = resultat.getString("Description");
                index++;
            }
        }
        catch (SQLException ignore)
        {}
        database.Database_Deconnection();

        coursTable.setModel(createModel(DATA, columns));
        centrerJTable(coursTable);
    }
}
