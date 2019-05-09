package GUI;

import GUI_Components.CustomJTextField;
import UsefulFunctions.Database_Connection;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;

public class GUI_addAddress extends GUI_Components.CustomJFrame {
    private JTextField street;
    private JTextField city;
    private JTextField codePostal;
    private JFormattedTextField phone;
    private JTextField email;
    private JButton buttonSave;
    private JPanel panel;

    private static final int DIMX = 300;
    private static final int DIMY = 200;

    private GUI_chercherPersonne gui;

    /**
     * Création des contraintes pour les FIELDS de l'interface
     */
    private void createUIComponents() {
        street = new CustomJTextField("ALL", false, 30);
        city = new CustomJTextField("ALPHABET", false, 20);
        codePostal = new CustomJTextField("NUMERIC", false, 20);
        //TODO: contrainte TELEPHONE
        //phone = new CustomJTextField("NUMERIC", false, 10);
        email = new CustomJTextField("ALL", false, 20);


        try {
            phone = new JFormattedTextField(new MaskFormatter("##.##.##.##.##"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructeur de l'interface
     *
     * @param ID_personne ID de la personne pour laquelle on entre la nouvelle adresse
     * @param gui
     */
    public GUI_addAddress(int ID_personne, GUI_chercherPersonne gui) {
        super("Ajouter une adresse", false, DIMX, DIMY);
        this.gui = gui;
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
                setVisible(false);
                gui.displayAddress();
                dispose(); //Destroy the Frame object
            }
        }

    }

}
