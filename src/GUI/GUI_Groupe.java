package GUI;

import GUI_Components.*;
import GUI_Components.ButtonEditor.ButtonEditorCours4Groupe;
import GUI_Components.ButtonEditor.ButtonEditorEleve;
import Gestion_admin.Database_Connection;
import Gestion_admin.Display_ResultSet;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import static GUI.GUI_Cours.createModel;

public class GUI_Groupe extends CustomJFrame {

    private static final int DIM_X = 600;
    private static final int DIM_Y = 500;

    private JLabel labelErreur;
    private JTextField fieldID;
    private JButton buttonChercher;
    private JPanel panelResultat;
    private JLabel labelID;
    private JTextField textNom;
    private JScrollPane scrollEleve;
    private JTable tableEleve;
    private JButton buttonAddEleve;
    private JLabel labelErrorEleve;
    private JScrollPane scrollCours;
    private JTable tableCours;
    private JButton buttonAddCours;
    private JLabel labelErrorCours;
    private JButton buttonSave;
    private JPanel panel;

    private int codeGroupe;


    public GUI_Groupe(int newGroupe) {
        super("Chercher Cours", true, DIM_X, DIM_Y);

        codeGroupe = -1;

        /*TODO enlever*/
        fieldID.setText("1");

        panelResultat.setVisible(false);
        labelErreur.setVisible(false);
        buttonSave.setVisible(false);


        buttonChercher.addActionListener(e -> searchGroupe());
        buttonAddEleve.addActionListener(e -> addEleve());
        buttonAddCours.addActionListener(e -> addCours());

        //buttonSave.addActionListener(e -> saveChanges());

        add(panel);
        pack();
        revalidate();
        setVisible(true);

        if(newGroupe != -1){
            fieldID.setText(String.valueOf(newGroupe));
            searchGroupe();
        }
    }

    private void createUIComponents() {

        fieldID = new CustomJTextField("NUMERIC", false, 8);
        textNom = new CustomJTextField("ALPHABET", false, 20);
    }

    private void searchGroupe() {
        if (fieldID.getText().length() == 0) {
            labelErreur.setVisible(true);
            panelResultat.setVisible(false);
            buttonSave.setVisible(false);
        } else {
            codeGroupe = Integer.parseInt(fieldID.getText());
            labelID.setText(fieldID.getText());


            /*CHERCHER LE COURS DANS LA BDD*/
            Database_Connection database = new Database_Connection();
            ResultSet data = database.run_Statement_READ("SELECT * FROM cours WHERE Code = " + codeGroupe);


            if (Display_ResultSet.getRows(data) == 0) {
                /*Groupe non trouvé*/
                labelErreur.setVisible(true);
                panelResultat.setVisible(false);
            } else {
                labelErreur.setVisible(false);

                /*Groupe trouvé*/
                try {
                    data.next();
                    textNom.setText(data.getString("Nom"));
                } catch (SQLException e) {
                }

                database.Database_Deconnection();
                displayCours();
                displayEleves();

                buttonSave.setVisible(true);
                panelResultat.setVisible(true);
            }
        }
    }

    public void displayCours() {
        /*TROUVER LES COURS SUIVIS*/
        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT cours.Code, Nom FROM suivre INNER JOIN cours on cours.Code = suivre.Code WHERE Groupe_ID = " + codeGroupe;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = Display_ResultSet.getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Cours", " X "};
                Object[][] cours = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    cours[index][0] = data.getString("Nom");
                    cours[index][1] = data.getString("Code");
                    index ++;
                }

                tableCours.setModel(createModel(cours, columns));
                tableCours.getColumn(" X ").setCellRenderer(new ButtonRenderer());
                tableCours.getColumn(" X ").setCellEditor(new ButtonEditorCours4Groupe(new JCheckBox(), this));

                scrollCours.setVisible(true);
                labelErrorCours.setVisible(false);
            } else {
                scrollCours.setVisible(false);
                labelErrorCours.setVisible(true);
            }
        } catch (SQLException ignore) {
        }
        database.Database_Deconnection();
    }

    public void deleteCours(int code) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM suivre WHERE Groupe_ID = " + codeGroupe + " AND Code = " + code;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayCours();
    }

    public void addCours() {
        new GUI_addCours(codeGroupe, null, this);
    }

    public void addEleve() {
        new GUI_addEleve(codeGroupe, this);
    }

    public void displayEleves() {
        /*TROUVER LES ELEVES QUI FONT PARTIS DU GROUPE*/
        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT Groupe_ID, Matricule FROM etudiant WHERE Groupe_ID = " + codeGroupe;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = Display_ResultSet.getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Eleves", " X "};
                Object[][] eleves = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    eleves[index][0] = data.getString("Matricule");
                    eleves[index][1] = data.getString("Matricule");
                    index ++;
                }

                tableEleve.setModel(createModel(eleves, columns));
                tableEleve.getColumn(" X ").setCellRenderer(new ButtonRenderer());
                tableEleve.getColumn(" X ").setCellEditor(new ButtonEditorEleve(new JCheckBox(), this));

                scrollEleve.setVisible(true);
                labelErrorEleve.setVisible(false);
            } else {
                scrollEleve.setVisible(false);
                labelErrorEleve.setVisible(true);
            }
        } catch (SQLException ignore) {
        }
        database.Database_Deconnection();
    }

    public void deleteEleves(int matricule) {
        Database_Connection database = new Database_Connection();
        String sql = "UPDATE etudiant SET Groupe_ID = NULL WHERE Matricule = " + matricule;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayEleves();
    }
}
