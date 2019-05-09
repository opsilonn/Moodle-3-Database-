package GUI;

import UsefulFunctions.CountRows_TableCell;
import UsefulFunctions.Database_Connection;

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


    /**
     * Constructeur de l'interface
     * @param code Matricule du professeur ou code du groupe concerné
     * @param gui Interface chercherPersonne
     * @param guiGroupe Interface d'un groupe
     */
    public GUI_addCours(int code, GUI_chercherPersonne gui, GUI_Groupe guiGroupe) {
        super("Ajouter un Cours", false, DIM_X, DIM_Y);
        this.gui = gui;
        this.guiGroupe = guiGroupe;

        buttonSave.addActionListener(e -> saveAddtoGroupe(code));
        putTheData();

        labelError.setVisible(false);

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    /**
     * Fonction de remplissage de la drop-down box
     */
    private void putTheData() {
        Database_Connection database = new Database_Connection();
        String sql = "SELECT * FROM cours";

        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (CountRows_TableCell.getRows(data) == 0) {
                labelError.setVisible(true);
                comboBoxItem.setVisible(false);
                buttonSave.setVisible(false);
                labelThingtoadd.setVisible(false);
            } else {
                labelError.setVisible(false);
                labelThingtoadd.setVisible(true);
                while (data.next()) {

                    //Met le code et le nom du Cours dans la drop-down Box
                    comboBoxItem.addItem(data.getString("Code") + ": " + data.getString("Nom"));
                }

                comboBoxItem.setVisible(true);
                buttonSave.setVisible(true);
            }
        } catch (SQLException e) {
        }
    }


    /**
     * Sauvegarde de l'ajout du cours au professeur ou au groupe dans la table enseigner ou suivre.
     * @param code Matricule du professeur ou Code du groupe.
     */
    private void saveAddtoGroupe(int code) {
        String sql = "";
        String elementToAdd = comboBoxItem.getSelectedItem().toString();
        String[] result = elementToAdd.split(": ");


        if (gui != null) {
            sql = "INSERT INTO enseigner (Code, Matricule_Prof) VALUES (" +
                    Integer.parseInt(result[0]) + ", " + code + ")";
        } else {
            sql = "INSERT INTO suivre (Code, Groupe_ID) VALUES (" +
                    Integer.parseInt(result[0]) + ", " + code + ")";
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
