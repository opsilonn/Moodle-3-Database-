package GUI;

import GUI_Components.*;
import Gestion_admin.Database_Connection;
import Gestion_admin.Display_ResultSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GUI_Cours extends CustomJFrame {

    private static final int DIM_X = 600;
    private static final int DIM_Y = 500;

    private JTextField fieldID;
    private JPanel panel;
    private JPanel panelResultat;
    private JLabel labelErreur;
    private JButton buttonChercher;
    private JLabel labelID;
    private JFormattedTextField textAnnee;
    private JFormattedTextField textNom;
    private JButton buttonSave;
    private JTextArea textDescription;
    private JTextField textDE;
    private JTextField textTP;
    private JTextField textProjet;
    private JTextField textCoeff;
    private JButton buttonAddProf;
    private JButton buttonAddGroup;
    private JTable tableProf;
    private JTable tableGroupe;
    private JLabel labelErrorProf;
    private JLabel labelErrorGroupe;
    private JScrollPane scrollProf;
    private JScrollPane scrollGroupe;

    private int codeCours;

    public GUI_Cours() {
        super("Chercher Cours", true, DIM_X, DIM_Y);

        codeCours = -1;

        /*TODO enlever*/
        fieldID.setText("1");
        panelResultat.setVisible(false);
        labelErreur.setVisible(false);
        buttonSave.setVisible(false);


        buttonChercher.addActionListener(e -> searchCours());
        buttonAddProf.addActionListener(e -> addProf());
        buttonAddGroup.addActionListener(e -> addGroupe());

        buttonSave.addActionListener(e -> saveChanges());

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void createUIComponents() {

        fieldID = new CustomJTextField("NUMERIC", false, 8);
    }


    private void searchCours() {
        if (fieldID.getText().length() == 0) {
            labelErreur.setVisible(true);
            panelResultat.setVisible(false);
            buttonSave.setVisible(false);
        } else {
            codeCours = Integer.parseInt(fieldID.getText());
            labelID.setText(fieldID.getText());


            /*CHERCHER LE COURS DANS LA BDD*/
            Database_Connection database = new Database_Connection();
            ResultSet data = database.run_Statement_READ("SELECT * FROM cours WHERE Code = " + codeCours);


            if (Display_ResultSet.getRows(data) == 0) {
                /*Cours non trouvé*/
                labelErreur.setVisible(true);
                panelResultat.setVisible(false);
            } else {
                labelErreur.setVisible(false);

                /*Cours trouvé*/
                try {
                    data.next();
                    textCoeff.setText(Float.toString(data.getFloat("Coefficient")));
                    textDE.setText(Float.toString(data.getFloat("DE_pourcentage")));
                    textTP.setText(Float.toString(data.getFloat("TP_pourcentage")));
                    textProjet.setText(Float.toString(data.getFloat("Projet_pourcentage")));
                    textAnnee.setText(Integer.toString(data.getInt("Annee")));
                    textDescription.setText(data.getString("Description"));
                    textNom.setText(data.getString("Nom"));
                } catch (SQLException e) {
                }

                database.Database_Deconnection();
                displayProf();
                displayGroup();

                buttonSave.setVisible(true);
                panelResultat.setVisible(true);
            }


        }
    }

    public void displayProf() {
        /*TROUVER LES PROFESSEURS QUI ENSEIGNENT*/
        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT Matricule_Prof FROM enseigner WHERE Code = " + codeCours;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = Display_ResultSet.getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Professeur", " X "};
                Object[][] adresses = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    adresses[index][0] = data.getString("Matricule_Prof");
                    adresses[index][1] = data.getString("Matricule_Prof");
                }

                tableProf.setModel(createModel(adresses, columns));

                tableProf.getColumn(" X ").setCellRenderer(new ButtonRenderer());
                tableProf.getColumn(" X ").setCellEditor(new ButtonEditorProf(new JCheckBox(), this));

                scrollProf.setVisible(true);
                labelErrorProf.setVisible(false);
            } else {
                scrollProf.setVisible(false);
                labelErrorProf.setVisible(true);
            }
        } catch (SQLException ignore) {
        }
        database.Database_Deconnection();
    }

    public void displayGroup() {
        /*TROUVER LES GROUPES QUI SUIVENT*/
        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT * FROM suivre INNER JOIN groupe on suivre.Groupe_ID = groupe.Groupe_ID WHERE Code = " + codeCours;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = Display_ResultSet.getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Groupe", " X "};
                Object[][] adresses = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    adresses[index][0] = data.getString("Nom");
                    adresses[index][1] = data.getInt("Groupe_ID");
                }

                tableGroupe.setModel(createModel(adresses, columns));

                tableGroupe.getColumn(" X ").setCellRenderer(new ButtonRenderer());
                tableGroupe.getColumn(" X ").setCellEditor(new ButtonEditorGroupe(new JCheckBox(), this));

                scrollGroupe.setVisible(true);
                labelErrorGroupe.setVisible(false);
            } else {
                scrollGroupe.setVisible(false);
                labelErrorGroupe.setVisible(true);
            }
        } catch (SQLException ignore) {
        }
        database.Database_Deconnection();

    }

    private void addProf() {
        new GUI_addProfGroupe_toCours(codeCours, this, true);
    }

    private void addGroupe() {
        new GUI_addProfGroupe_toCours(codeCours, this, false);
    }

    public void deleteGroupe(int Groupe_ID) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM suivre WHERE Code = " + codeCours + " AND Groupe_ID = " + Groupe_ID;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayGroup();
    }

    public void deleteProf(int Mat_Prof) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM enseigner WHERE Code = " + codeCours + " AND Matricule_Prof = " + Mat_Prof;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayProf();
    }

    private void saveChanges() {
        /*TODO : CONSTRAINT */
        String sql = "UPDATE cours SET Nom ='" + textNom.getText() +
                "', Description = '" + textDescription.getText() +
                "', Annee = " + textAnnee.getText() +
                ", Coefficient = " + textCoeff.getText() +
                ", DE_pourcentage = " + textDE.getText() +
                ", TP_pourcentage = " + textTP.getText() +
                ", Projet_pourcentage = " + textProjet.getText();

        Database_Connection database = new Database_Connection();
        database.run_Statement_WRITE(sql);
        JOptionPane.showMessageDialog(this,"Bien enregistré");
    }

    private TableModel createModel(Object[][] adresses, String[] columns) {
        return new DefaultTableModel(adresses, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 1) {
                    return true;
                }
                return false;
            }
        };
    }
}
