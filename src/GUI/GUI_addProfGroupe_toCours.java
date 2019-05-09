package GUI;

import static UsefulFunctions.CountRows_TableCell.getRows;

import UsefulFunctions.Database_Connection;

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


    /**
     * Constructeur de l'interface d'ajout de professeur ou groupe au cours
     *
     * @param code Code du cours concerné
     * @param gui  Interface d'affichage du cours
     * @param mode si true = on ajoute un professeur
     *             si fals = on ajoute un groupe
     */
    public GUI_addProfGroupe_toCours(int code, GUI_Cours gui, boolean mode) {
        super("Ajouter au Cours", false, DIM_X, DIM_Y);
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


    /**
     * Ajout des données des professeurs ou groupes dans la drop-down box
     */
    private void putTheData() {
        Database_Connection database = new Database_Connection();
        String sql = "";
        if (mode) {
            sql = "SELECT Matricule FROM professeur";
        } else {
            sql = "SELECT Nom, Groupe_ID FROM groupe";
        }

        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (getRows(data) == 0) {
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
                        comboBoxItem.addItem(data.getInt("Groupe_ID") + ": " + data.getString("Nom"));
                    }
                }

                comboBoxItem.setVisible(true);
                buttonSave.setVisible(true);
            }
        } catch (SQLException e) {
        }
    }

    /**
     * Sauvegarde des informations pour le cours
     *
     * @param code code du cours concerné
     */
    private void saveAddtoCours(int code) {
        String sql = "";
        String elementToAdd = comboBoxItem.getSelectedItem().toString();

        if (mode) {
            sql = "INSERT INTO enseigner (Code, Matricule_Prof) VALUES (" +
                    code + ", " + Integer.parseInt(elementToAdd) + ")";
        } else {
            String[] result = elementToAdd.split(": ");

            sql = "INSERT INTO suivre (Code, Groupe_ID) VALUES (" +
                    code + ", " + Integer.parseInt(result[0]) + ")";
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
