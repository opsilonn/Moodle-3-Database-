package GUI;

import Gestion_admin.Database_Connection;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GUI_addCours extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JPanel panel;
    private JComboBox comboBoxItem;
    private JButton buttonSave;
    private JLabel labelThingtoadd;
    private JLabel labelError;

    private GUI_chercherPersonne gui;
    private GUI_Groupe guiGroupe;

    public GUI_addCours(int matricule, GUI_chercherPersonne gui, GUI_Groupe guiGroupe) {
        super("Ajouter au Cours", false, DIM_X, DIM_Y);
        this.gui = gui;
        this.guiGroupe = guiGroupe;

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
        String sql = "SELECT * FROM cours";

        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (Database_Connection.getRows(data) == 0) {
                labelError.setVisible(true);
                comboBoxItem.setVisible(false);
                buttonSave.setVisible(false);
                labelThingtoadd.setVisible(false);
            } else {
                labelError.setVisible(false);
                labelThingtoadd.setVisible(true);
                while (data.next()) {
                    comboBoxItem.addItem(data.getString("Code") + ": " + data.getString("Nom"));
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


        if (gui != null) {
            sql = "INSERT INTO enseigner (Code, Matricule_Prof) VALUES (" +
                    Integer.parseInt(result[0]) + ", " + matricule + ")";
        } else {
            sql = "INSERT INTO suivre (Code, Groupe_ID) VALUES (" +
                    Integer.parseInt(result[0]) + ", " + matricule + ")";
        }
        Database_Connection database = new Database_Connection();

        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();

        if (gui != null) {
            gui.displayCours();
        } else {
            guiGroupe.displayCours();
        }

        dispose();
    }
}
