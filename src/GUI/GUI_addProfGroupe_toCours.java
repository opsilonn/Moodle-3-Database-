package GUI;

import static UsefulFunctions.CountRows_TableCell.getRows;
import static GUI.GUI_USER_Admin.WindowClosingVisible;

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
     *             si false = on ajoute un groupe
     */
    public GUI_addProfGroupe_toCours(int code, GUI_Cours gui, boolean mode) {
        super("Ajouter au Cours", false, DIM_X, DIM_Y);
        this.gui = gui;
        this.mode = mode;

        WindowClosingVisible(this, gui);

        buttonSave.addActionListener(e -> saveAddtoCours(code));
        putTheData(code);

        if (!mode) {
            labelError.setText("Pas de Groupe à ajouter.");
        }

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    /**
     * Ajout des données des professeurs ou groupes dans la drop-down box
     */
    private void putTheData(int code) {
        Database_Connection database = new Database_Connection();
        String sql = "";
        String query = "";
        if (mode) {
            sql = "SELECT Matricule FROM professeur";
            query = "SELECT Matricule_Prof FROM enseigner WHERE Code = " + code;
        } else {
            sql = "SELECT Nom, Groupe_ID FROM groupe";
            query = "SELECT Groupe_ID FROM suivre WHERE Code = " + code;
        }

        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (getRows(data) == 0) {
                ErrorDisplay(true);
            } else {

                while (data.next()) {
                    ResultSet alreadyAdded = database.run_Statement_READ(query);
                    boolean alreadyThere = false;

                    while (alreadyAdded.next()) {
                        //Condition pour eviter une ViolationPrimaryConstraint
                        if (mode && alreadyAdded.getString("Matricule_Prof").equals(data.getString("Matricule"))) {
                            alreadyThere = true;
                        } else if (!mode && alreadyAdded.getString("Groupe_ID").equals(data.getString("Groupe_ID"))) {
                            alreadyThere = true;
                        }
                    }

                    if (mode && !alreadyThere) {
                        comboBoxItem.addItem(data.getInt("Matricule"));
                    } else if (!mode && !alreadyThere) {
                        comboBoxItem.addItem(data.getInt("Groupe_ID") + ": " + data.getString("Nom"));
                    }
                }

                if (comboBoxItem.getItemCount() == 0) {
                    ErrorDisplay(true);
                } else {
                    ErrorDisplay(false);
                }
            }
        } catch (SQLException e) {
        }
    }

    private void ErrorDisplay(boolean mode) {
        if (mode) {
            labelError.setVisible(true);
            comboBoxItem.setVisible(false);
            buttonSave.setVisible(false);
            labelThingtoadd.setVisible(false);
        } else {
            comboBoxItem.setVisible(true);
            buttonSave.setVisible(true);
            labelError.setVisible(false);
            labelThingtoadd.setVisible(true);
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

        gui.setVisible(true);
        dispose();
    }


}
