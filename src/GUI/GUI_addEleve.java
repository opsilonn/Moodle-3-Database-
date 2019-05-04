package GUI;

import Gestion_admin.Database_Connection;
import Gestion_admin.Display_ResultSet;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GUI_addEleve extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JPanel panel;
    private JComboBox comboBoxItem;
    private JButton buttonSave;
    private JLabel labelThingtoadd;
    private JLabel labelError;

    private GUI_Groupe gui;

    public GUI_addEleve(int matricule, GUI_Groupe gui) {
        super("Ajouter au Groupe", false, DIM_X, DIM_Y);
        this.gui = gui;

        buttonSave.addActionListener(e -> saveAddtoGroupe(matricule));
        putTheData();

        labelError.setVisible(false);

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void putTheData() {
        Database_Connection database = new Database_Connection();
        String sql = "SELECT Matricule FROM etudiant";

        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (Display_ResultSet.getRows(data) == 0) {
                labelError.setVisible(true);
                comboBoxItem.setVisible(false);
                buttonSave.setVisible(false);
                labelThingtoadd.setVisible(false);
            } else {
                labelError.setVisible(false);
                labelThingtoadd.setVisible(true);
                while (data.next()) {
                    comboBoxItem.addItem(data.getString("Matricule"));
                }

                comboBoxItem.setVisible(true);
                buttonSave.setVisible(true);
            }
        } catch (SQLException e) {
        }
    }

    private void saveAddtoGroupe(int matricule) {
        String sql = "";
        String elementToAdd = comboBoxItem.getSelectedItem().toString();
        String[] result = elementToAdd.split(": ");

        sql = "UPDATE etudiant SET Groupe_ID = " + matricule;

        Database_Connection database = new Database_Connection();
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();

        gui.displayEleves();

        dispose();
    }
}
