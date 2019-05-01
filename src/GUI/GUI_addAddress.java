package GUI;

import Gestion_admin.Database_Connection;

import javax.swing.*;

public class GUI_addAddress extends GUI_Components.CustomJFrame {
    private JTextField street;
    private JTextField city;
    private JTextField codePostal;
    private JFormattedTextField phone;
    private JFormattedTextField email;
    private JButton buttonSave;
    private JPanel panel;

    private static final int DIMX = 300;
    private static final int DIMY = 200;

    private GUI_chercherPersonne gui;


    public GUI_addAddress(int ID_personne, GUI_chercherPersonne gui) {
        super("Ajouter une adresse", false, DIMX, DIMY);
        this.gui = gui;
        buttonSave.addActionListener(e -> saveAddress(ID_personne));

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void saveAddress(int ID_personne) {
        Database_Connection database = new Database_Connection();

        if (street.getText().length() == 0 || city.getText().length() == 0 ||
                codePostal.getText().length() == 0 || phone.getText().length() == 0
                || email.getText().length() == 0) {
            System.out.println("ERROR");
        } else {
            if (ID_personne != -1) {
                String sql = "INSERT INTO adresse (Street, City, ZIP_code, Phone, Email, ID_Personne) VALUES (" +
                        "'" + street.getText() + "','" +
                        city.getText() + "','" +
                        codePostal.getText() + "'," +
                        phone.getText() + ",'" +
                        email.getText() + "'," +
                        ID_personne + ")";
                database.run_Statement_WRITE(sql);
                database.Database_Deconnection();
                System.out.println("Enregistré");
                setVisible(false);
                gui.displayAddress();
                dispose(); //Destroy the Frame object
            }
        }

    }
}
