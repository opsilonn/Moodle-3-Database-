package GUI;

import GUI_Components.CustomJTextField;
import GUI_Components.DateFunctions;
import UsefulFunctions.Database_Connection;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.time.LocalDate;
import java.time.Month;

public class GUI_addID extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JPanel panel_ID;
    private JTextField textCity;
    private DatePicker textDate;
    private JTextField textSexe;
    private JButton buttonSave;
    private JTextField textCountry;

    private GUI_chercherPersonne gui;

    public GUI_addID(int ID_personne, GUI_chercherPersonne gui) {
        super("Ajouter une identité", false, DIM_X, DIM_Y);
        this.gui = gui;
        buttonSave.addActionListener(e -> saveID(ID_personne));

        textDate.setSettings(DateFunctions.customDates());
        LocalDate date = LocalDate.of(2000, Month.JANUARY, 1);
        textDate.setDate(date);

        add(panel_ID);
        pack();
        revalidate();
        setVisible(true);
    }

    private void createUIComponents() {
        textDate = new DatePicker();
        textCity = new CustomJTextField("ALPHABET", false, 20);
        textSexe = new CustomJTextField("SEXE", false, 1);
    }


    private void saveID(int ID_personne) {
        Database_Connection database = new Database_Connection();

        if (textCity.getText().length() == 0 || textDate.getText().length() == 0 ||
                textSexe.getText().length() == 0) {
            System.out.println("ERROR");
        } else {
            if (ID_personne != -1) {
                String sql = "INSERT INTO identite (date_naissance, ville_naissance, pays_naissance, sexe, ID_Personne) VALUES (" +
                        "'" + textDate.getDate().toString() + "','" +
                        textCity.getText() + "','" +
                        textCountry.getText() + "','" +
                        textSexe.getText() + "'," +
                        ID_personne + ")";
                System.out.println(sql);
                database.run_Statement_WRITE(sql);
                database.Database_Deconnection();

                setVisible(false);
                gui.displayID();
                dispose(); //Destroy the Frame object
            }
        }

    }


}
