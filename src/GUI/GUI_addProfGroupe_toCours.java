package GUI;

import Gestion_admin.Database_Connection;
import Gestion_admin.Display_ResultSet;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GUI_addProfGroupe_toCours extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JComboBox comboBoxItem;
    private JButton buttonSave;
    private JLabel labelThingtoadd;
    private JPanel panel;
    private JLabel labelError;

    private GUI_Cours gui;
    private boolean mode; /*SI TRUE : add prof --- SI FALSE : add group */

    public GUI_addProfGroupe_toCours(int code, GUI_Cours gui, boolean mode) {
        super("Ajouter une adresse", false, DIM_X, DIM_Y);
        this.gui = gui;
        this.mode = mode;

        buttonSave.addActionListener(e -> saveAddtoCours(code));
        putTheData();

        labelError.setVisible(false);

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void putTheData() {
        Database_Connection database = new Database_Connection();
        String sql = "";
        if (mode) {
            sql = "SELECT Matricule FROM professeur";
        } else {
            sql = "SELECT Nom FROM groupe";
        }

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
                    if (mode) {
                        comboBoxItem.addItem(data.getInt("Matricule"));
                    } else {
                        comboBoxItem.addItem(data.getString("Nom"));
                    }
                }

                comboBoxItem.setVisible(true);
                buttonSave.setVisible(true);
            }
        } catch (SQLException e) {
        }
    }

    private void saveAddtoCours(int code) {
        String sql = "";
        String elementToAdd = comboBoxItem.getSelectedItem().toString();

        if (mode) {
            sql = "INSERT INTO enseigner (Code, Matricule_Prof) VALUES (" +
                    code + ", " + Integer.parseInt(elementToAdd) + ")";
        } else {
            try {
                sql = "SELECT Groupe_ID from groupe WHERE Nom = '" + elementToAdd + "'";
                Database_Connection database = new Database_Connection();
                ResultSet data = database.run_Statement_READ(sql);

                if (data.next()) {
                    sql = "INSERT INTO suivre (Code, Groupe_ID) VALUES (" +
                            code + ", " + data.getInt("Groupe_ID") + ")";
                }
            } catch (SQLException ignore) {
            }
        }

        Database_Connection database = new Database_Connection();
        database.run_Statement_WRITE(sql);

        if (mode) {
            gui.displayProf();
        } else {
            gui.displayGroup();
        }

        dispose();
    }


}
