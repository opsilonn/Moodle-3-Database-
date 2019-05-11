package GUI;

import GUI_Components.*;
import GUI_Components.ButtonEditor.ButtonEditorCours4Groupe;
import GUI_Components.ButtonEditor.ButtonEditorEleve;
import UsefulFunctions.Database_Connection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import static UsefulFunctions.CountRows_TableCell.createModel;
import static UsefulFunctions.CountRows_TableCell.getRows;

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
    private JButton buttonEditBulletin;
    private JLabel labelBulletin;

    private int codeGroupe;

    /**
     * Constructeur de l'interface d'affichage d'un groupe
     *
     * @param newGroupe Code du groupe si celui-ci créer sinon contient -1.
     */
    public GUI_Groupe(int newGroupe) {
        super("Chercher Groupe", false, DIM_X, DIM_Y);
        GUI_USER_Admin.WindowClosing(this);

        codeGroupe = -1;

        panelResultat.setVisible(false);
        labelErreur.setVisible(false);
        buttonSave.setVisible(false);


        buttonChercher.addActionListener(e -> searchGroupe());
        buttonAddEleve.addActionListener(e -> addEleve());
        buttonAddCours.addActionListener(e -> addCours());
        buttonSave.addActionListener(e -> saveChanges());
        buttonEditBulletin.addActionListener(e -> editBulletin());

        add(panel);
        pack();
        revalidate();
        setVisible(true);

        if (newGroupe != -1) {
            fieldID.setText(String.valueOf(newGroupe));
            searchGroupe();
        }
    }

    /**
     * Création des contraintes pour les fields de l'interface
     */
    private void createUIComponents() {

        fieldID = new CustomJTextField("NUMERIC", false, 8);
        textNom = new CustomJTextField("ALPHABET", false, 20);
    }

    /**
     * Chercher le groupe possédant le code renseigné
     */
    private void searchGroupe() {
        if (fieldID.getText().length() == 0) {
            labelErreur.setVisible(true);
            panelResultat.setVisible(false);
            buttonSave.setVisible(false);
        } else {
            codeGroupe = Integer.parseInt(fieldID.getText());
            labelID.setText(fieldID.getText());


            /*CHERCHER LE GROUPE DANS LA BDD*/
            Database_Connection database = new Database_Connection();
            ResultSet data = database.run_Statement_READ("SELECT * FROM groupe WHERE Groupe_ID = " + codeGroupe);


            if (getRows(data) == 0) {
                /*Groupe non trouvé*/
                labelErreur.setVisible(true);
                panelResultat.setVisible(false);
                buttonSave.setVisible(false);
            } else {
                labelErreur.setVisible(false);

                /*Groupe trouvé*/
                try {
                    data.next();
                    textNom.setText(data.getString("Nom"));
                    if (data.getBoolean("bulletin")) {
                        labelBulletin.setText("Les bulletins ont été édités.");
                        buttonEditBulletin.setVisible(false);
                    }
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


    /**
     * Affichage des cours suivis par le groupe
     */
    public void displayCours() {
        /*TROUVER LES COURS SUIVIS*/
        tableCours.removeAll();

        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT cours.Code, Nom FROM suivre INNER JOIN cours on cours.Code = suivre.Code WHERE Groupe_ID = " + codeGroupe;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Cours", " X "};
                Object[][] cours = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    cours[index][0] = data.getString("Nom");
                    cours[index][1] = data.getString("Code");
                    index++;
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

    /**
     * Suppression d'un cours suivi
     *
     * @param code code du cours à supprimer
     */
    public void deleteCours(int code) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM suivre WHERE Groupe_ID = " + codeGroupe + " AND Code = " + code;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayCours();
    }

    /**
     * Lancement de l'interface pour l'ajout d'un cours
     */
    public void addCours() {
        new GUI_addCours(codeGroupe, null, this);
    }

    /**
     * Lancement de l'interface pour l'ajout d'un élève dans le groupe
     */
    public void addEleve() {
        new GUI_addEleve(codeGroupe, this, null);
    }

    /**
     * Afficher les étudiants appartenant au groupe
     */
    public void displayEleves() {
        /*TROUVER LES ELEVES QUI FONT PARTIS DU GROUPE*/
        tableEleve.removeAll();

        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT Groupe_ID, Matricule FROM etudiant WHERE Groupe_ID = " + codeGroupe;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Eleves", " X "};
                Object[][] eleves = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    eleves[index][0] = data.getString("Matricule");
                    eleves[index][1] = data.getString("Matricule");
                    index++;
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

    /**
     * Enlever un étudiant du groupe
     *
     * @param matricule matricule de l'étudiant à enlever
     */
    public void deleteEleves(int matricule) {
        Database_Connection database = new Database_Connection();
        String sql = "UPDATE etudiant SET Groupe_ID = NULL WHERE Matricule = " + matricule;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();

        DefaultTableModel model = (DefaultTableModel) tableEleve.getModel();

        // get selected row index
        try {
            int SelectedRowIndex = tableEleve.getSelectedRow();
            if (tableEleve.getRowCount() == 1) {
                scrollEleve.setVisible(false);
                labelErrorEleve.setVisible(true);
            } else {
                model.removeRow(SelectedRowIndex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("Remove DONE");
        //displayEleves();
    }

    /**
     * Sauvegarde des changements effectués sur le groupe.
     */
    private void saveChanges() {
        Database_Connection database = new Database_Connection();
        String sql = "UPDATE groupe SET Nom = '" + textNom.getText() + "'WHERE Groupe_ID = " + codeGroupe;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        JOptionPane.showMessageDialog(this, "Bien enregistré", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Edition des bulletins pour les étudiants du groupe
     */
    private void editBulletin() {
        Database_Connection database = new Database_Connection();
        String sql = "SELECT MAX(CPT) AS max, MIN(CPT) AS min FROM " +
                "(SELECT COUNT(ID) AS CPT FROM note INNER JOIN etudiant WHERE Matricule = Matricule_Etudiant AND Groupe_ID = " + codeGroupe
                + " GROUP BY Matricule_Etudiant) AS T";
        ResultSet res = database.run_Statement_READ(sql);

        try {
            if (res.next() && res.getInt("max") == 3 && res.getInt("min") == 3) {
                sql = "UPDATE groupe SET bulletin = 1 WHERE Groupe_ID = " + codeGroupe;
                database.run_Statement_WRITE(sql);
                database.Database_Deconnection();
                JOptionPane.showMessageDialog(this, "Les bulletins ont été édités.", "Saved", JOptionPane.INFORMATION_MESSAGE);
                searchGroupe();
            } else {
                JOptionPane.showMessageDialog(this, "Tous les étudiants non pas toutes leurs notes. Les bulletins ne peuvent donc pas être édités.", "Saved", JOptionPane.WARNING_MESSAGE);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
