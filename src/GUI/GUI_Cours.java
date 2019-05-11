package GUI;

import GUI_Components.*;
import GUI_Components.ButtonEditor.ButtonEditorGroupe;
import GUI_Components.ButtonEditor.ButtonEditorProf;

import static UsefulFunctions.CountRows_TableCell.getRows;
import static UsefulFunctions.CountRows_TableCell.createModel;

import UsefulFunctions.Database_Connection;

import javax.swing.*;
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
    private JTextField textAnnee;
    private JTextField textNom;
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

    /**
     * Constructeur de l'interface de consultation d'un cours
     *
     * @param newCours Code du cours si celui-ci vient d'être créer
     */
    public GUI_Cours(int newCours) {
        super("Chercher Cours", false, DIM_X, DIM_Y);
        GUI_USER_Admin.WindowClosing(this);

        codeCours = -1;

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

        if (newCours != -1) {
            fieldID.setText(String.valueOf(newCours));
            searchCours();
        }
    }

    /**
     * Définition des contraintes pour les fields de l'interface
     */
    private void createUIComponents() {

        fieldID = new CustomJTextField("NUMERIC", false, 8);
        textNom = new CustomJTextField("ALPHABET", false, 20);
        textAnnee = new CustomJTextField("NUMERIC", false, 4);
        textDE = new CustomJTextField("DECIMAL", false, 5);
        textTP = new CustomJTextField("DECIMAL", false, 5);
        textProjet = new CustomJTextField("DECIMAL", false, 5);
        textCoeff = new CustomJTextField("DECIMAL", false, 5);
    }

    /**
     * Recherche du cours avec le code rentré par l'utilisateur
     */
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


            if (getRows(data) == 0) {
                /*Cours non trouvé*/
                labelErreur.setVisible(true);
                panelResultat.setVisible(false);
                buttonSave.setVisible(false);
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

    /**
     * Affichage des professeurs enseignant le cours
     */
    public void displayProf() {
        /*TROUVER LES PROFESSEURS QUI ENSEIGNENT*/
        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT Matricule_Prof FROM enseigner WHERE Code = " + codeCours;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Professeur", " X "};
                Object[][] profs = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    profs[index][0] = data.getString("Matricule_Prof");
                    profs[index][1] = data.getString("Matricule_Prof");
                }

                tableProf.setModel(createModel(profs, columns));

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

    /**
     * Affichage des Groupes suivant ce cours
     */
    public void displayGroup() {
        /*TROUVER LES GROUPES QUI SUIVENT*/
        Database_Connection database = new Database_Connection();
        try {
            String sql = "SELECT * FROM suivre INNER JOIN groupe on suivre.Groupe_ID = groupe.Groupe_ID WHERE Code = " + codeCours;
            ResultSet data = database.run_Statement_READ(sql);
            int totalRows = getRows(data);

            if (totalRows > 0) {
                String[] columns = new String[]{"Groupe", " X "};
                Object[][] groupes = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    groupes[index][0] = data.getString("Nom");
                    groupes[index][1] = data.getInt("Groupe_ID");
                    index++;
                }

                tableGroupe.setModel(createModel(groupes, columns));

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

    /**
     * Ajouter un professeur enseignant ce cours
     */
    private void addProf() {
        new GUI_addProfGroupe_toCours(codeCours, this, true);
    }

    /**
     * Ajouter un groupe suivant ce cours
     */
    private void addGroupe() {
        new GUI_addProfGroupe_toCours(codeCours, this, false);
    }

    /**
     * Enlever un groupe suivant ce cours
     *
     * @param Groupe_ID ID du groupe à enlever
     */
    public void deleteGroupe(int Groupe_ID) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM suivre WHERE Code = " + codeCours + " AND Groupe_ID = " + Groupe_ID;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayGroup();
    }


    /**
     * Enlever un professeur enseignant ce cours
     *
     * @param Mat_Prof Matricule du professeur à enlever
     */
    public void deleteProf(int Mat_Prof) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM enseigner WHERE Code = " + codeCours + " AND Matricule_Prof = " + Mat_Prof;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayProf();
    }

    /**
     * Sauvegarder les changements effectués sur le cours
     */
    private void saveChanges() {
        /*
         *Retrieve the infos ds des chiffres
         * si Dep+TP+PR != 100%
         * each can't be under 0%
         * */
        float DE = Float.parseFloat(textDE.getText());
        float Prj = Float.parseFloat(textProjet.getText());
        float TP = Float.parseFloat(textTP.getText());

        if (DE + Prj + TP != 100 || DE < 0 || Prj < 0 || TP < 0) {
            JOptionPane.showMessageDialog(panel, "Les valeurs entrées pour les pourcentages ne sont pas correctes",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            String sql = "UPDATE cours SET Nom ='" + textNom.getText() +
                    "', Description = '" + textDescription.getText() +
                    "', Annee = " + textAnnee.getText() +
                    ", Coefficient = " + textCoeff.getText() +
                    ", DE_pourcentage = " + textDE.getText() +
                    ", TP_pourcentage = " + textTP.getText() +
                    ", Projet_pourcentage = " + textProjet.getText();

            Database_Connection database = new Database_Connection();
            database.run_Statement_WRITE(sql);
            JOptionPane.showMessageDialog(this, "Bien enregistré", "Saved", JOptionPane.INFORMATION_MESSAGE);

        }

    }

}
