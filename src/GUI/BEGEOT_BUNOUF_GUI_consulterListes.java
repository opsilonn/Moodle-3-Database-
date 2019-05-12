package GUI;


import GUI_Components.BEGEOT_BUNOUF_CustomJFrame;
import UsefulFunctions.BEGEOT_BUNOUF_Database_Connection;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static UsefulFunctions.BEGEOT_BUNOUF_CountRows_TableCell.createModel;
import static UsefulFunctions.BEGEOT_BUNOUF_CountRows_TableCell.getRows;
import static GUI.BEGEOT_BUNOUF_GUI_USER_Admin.WindowClosingVisible;


/**
 * Fenêtre dédiée à l'affichage de tous les cours disponibles
 *
 * Cette classe hérite de {@link BEGEOT_BUNOUF_CustomJFrame}
 *
 * @author Hugues
 */
public class BEGEOT_BUNOUF_GUI_consulterListes extends BEGEOT_BUNOUF_CustomJFrame
{
    private static final int DIM_X = 700;
    private static final int DIM_Y = 400;

    private JPanel panel;
    private JTable coursTable;
    private JTable groupeTable;


    public BEGEOT_BUNOUF_GUI_consulterListes(Window win)
    {
        super("Liste des Cours", false, DIM_X, DIM_Y);
        WindowClosingVisible(this, win);

        setCours();
        setGroupe();

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

        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String query =
                "SELECT * FROM cours ;";

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

    private void setGroupe()
    {
        String[] columns = new String[]{ "Nom"};

        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String query = "SELECT Nom FROM groupe ;";

        ResultSet resultat = database.run_Statement_READ(query);

        Object[][] DATA = new Object[getRows(resultat)][columns.length];
        try
        {
            int index = 0;
            while ( resultat.next() )
            {
                DATA[index][0] = resultat.getString("Nom");
                index++;
            }
        }
        catch (SQLException ignore)
        {}
        database.Database_Deconnection();

        groupeTable.setModel(createModel(DATA, columns));
        centrerJTable(groupeTable);
    }
}
