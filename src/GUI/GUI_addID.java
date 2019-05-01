package GUI;

import Gestion_admin.Database_Connection;

import javax.swing.*;

public class GUI_addID extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JPanel panel_ID;
    private JFormattedTextField textCity;
    private JFormattedTextField textDate;
    private JFormattedTextField textSexe;
    private JButton buttonSave;

    private GUI_chercherPersonne gui;

    public GUI_addID(int ID_personne, GUI_chercherPersonne gui) {
        super("Ajouter une adresse", false, DIM_X, DIM_Y);
        this.gui = gui;
        buttonSave.addActionListener(e -> saveID(ID_personne));

        add(panel_ID);
        pack();
        revalidate();
        setVisible(true);
    }


    private void saveID(int ID_personne) {
        Database_Connection database = new Database_Connection();

        if (textCity.getText().length() == 0 || textDate.getText().length() == 0 ||
                textSexe.getText().length() == 0) {
            System.out.println("ERROR");
        } else {
            if (ID_personne != -1) {
                String sql = "INSERT INTO identite (date_naissance, ville_naissance, pays_naissance, sexe, ID_Personne) VALUES (" +
                        "'" + textCity.getText() + "','" +
                        textDate.getText() + "','" +
                        textSexe.getText() + "'," +
                        ID_personne + ")";
                database.run_Statement_WRITE(sql);
                database.Database_Deconnection();

                setVisible(false);
                gui.displayAddress();
                dispose(); //Destroy the Frame object
            }
        }

    }


}
