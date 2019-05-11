package GUI;

import static UsefulFunctions.CountRows_TableCell.getRows;

import UsefulFunctions.Database_Connection;

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
    private GUI_chercherPersonne gui2;

    /**
     * Constructeur de l'interface
     *
     * @param code code du groupe concerné
     * @param gui  GUI_Groupe sur lequel on travaille
     */
    public GUI_addEleve(int code, GUI_Groupe gui, GUI_chercherPersonne gui2) {
        super("Ajouter au Groupe", false, DIM_X, DIM_Y);
        this.gui = gui;
        this.gui2 = gui2;

        if (gui == null) {
            GUI_USER_Admin.WindowClosingVisible(this, gui2);
        } else {
            GUI_USER_Admin.WindowClosingVisible(this, gui);
        }

        buttonSave.addActionListener(e -> saveAddtoGroupe(code));
        putTheData(code);


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    /**
     * Mise des informations étudiantes dans la drop-down box.
     */
    private void putTheData(int code) {
        Database_Connection database = new Database_Connection();
        String sql = "SELECT Matricule FROM etudiant";
        String query = "";
        if (gui != null) {
            query = "SELECT Matricule FROM etudiant WHERE Groupe_ID = " + code;
        } else {
            //Un etudiant ne peut pas avoir plus d'un responsable
            query = "SELECT Matricule_Etudiant AS Matricule FROM tuteur";
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
                        if (alreadyAdded.getString("Matricule").equals(data.getString("Matricule"))) {
                            alreadyThere = true;
                        }
                    }
                    if (!alreadyThere) {
                        //Met le code et le nom du Cours dans la drop-down Box
                        comboBoxItem.addItem(data.getString("Matricule"));
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
            comboBoxItem.setVisible(false);
            buttonSave.setVisible(false);
            labelThingtoadd.setVisible(false);
            labelError.setVisible(true);
        } else {
            comboBoxItem.setVisible(true);
            buttonSave.setVisible(true);
            labelError.setVisible(false);
            labelThingtoadd.setVisible(true);
        }
    }

    /**
     * Ajout de l'étudiant dans le groupe associé
     *
     * @param code code du groupe concerné
     */
    private void saveAddtoGroupe(int code) {
        String sql = "";
        String elementToAdd = comboBoxItem.getSelectedItem().toString();

        if (gui2 == null) {
            sql = "UPDATE etudiant SET Groupe_ID = " + code + " WHERE Matricule = " + elementToAdd;
        } else {
            sql = "INSERT INTO tuteur (Matricule_Etudiant, Numero) VALUES (" + elementToAdd + ", " + code + ");";
        }

        Database_Connection database = new Database_Connection();
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();

        if (gui2 == null) {
            gui.displayEleves();
        } else {
            gui2.displayEleves();
        }

        dispose();
    }
}
