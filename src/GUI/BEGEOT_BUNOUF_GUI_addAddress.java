package GUI;

import GUI_Components.BEGEOT_BUNOUF_CustomJFrame;
import GUI_Components.BEGEOT_BUNOUF_CustomJTextField;
import UsefulFunctions.BEGEOT_BUNOUF_Database_Connection;

import javax.swing.*;

import static GUI.BEGEOT_BUNOUF_GUI_USER_Admin.WindowClosingVisible;

public class BEGEOT_BUNOUF_GUI_addAddress extends BEGEOT_BUNOUF_CustomJFrame {
    private JTextField street;
    private JTextField city;
    private JTextField codePostal;
    private JTextField phone;
    private JTextField email;
    private JButton buttonSave;
    private JPanel panel;

    private static final int DIMX = 400;
    private static final int DIMY = 300;

    private BEGEOT_BUNOUF_GUI_chercherPersonne gui;

    /**
     * Création des contraintes pour les FIELDS de l'interface
     */
    private void createUIComponents() {
        street = new BEGEOT_BUNOUF_CustomJTextField("ALL", false, 30);
        city = new BEGEOT_BUNOUF_CustomJTextField("ALPHABET", false, 20);
        codePostal = new BEGEOT_BUNOUF_CustomJTextField("NUMERIC", false, 20);
        phone = new BEGEOT_BUNOUF_CustomJTextField("NUMERIC", false, 10);
        email = new BEGEOT_BUNOUF_CustomJTextField("ALL", false, 20);
    }

    /**
     * Constructeur de l'interface
     *
     * @param ID_personne ID de la personne pour laquelle on entre la nouvelle adresse
     * @param gui
     */
    public BEGEOT_BUNOUF_GUI_addAddress(int ID_personne, BEGEOT_BUNOUF_GUI_chercherPersonne gui) {
        super("Ajouter une adresse", false, DIMX, DIMY);
        this.gui = gui;

        WindowClosingVisible(this, gui);

        buttonSave.addActionListener(e -> saveAddress(ID_personne));

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    /**
     * Sauvegarde la nouvelle adresse entrée
     *
     * @param ID_personne ID de la personne habitant à l'adresse donnée
     */
    private void saveAddress(int ID_personne) {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();

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
                setVisible(false);
                gui.displayAddress();
                gui.setVisible(true);
                dispose(); //Destroy the Frame object
            }
        }

    }

}
