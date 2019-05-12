package GUI;

import GUI_Components.*;
import GUI_Components.ButtonEditor.BEGEOT_BUNOUF_ButtonEditorAdresse;
import GUI_Components.ButtonEditor.BEGEOT_BUNOUF_ButtonEditorCours;
import GUI_Components.ButtonEditor.BEGEOT_BUNOUF_ButtonEditorTuteur;
import UsefulFunctions.BEGEOT_BUNOUF_CountRows_TableCell;
import UsefulFunctions.BEGEOT_BUNOUF_Database_Connection;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;

/**
 * Fenêtre dédiée à la fonction de recherche d'une Personne
 * Cette classe hérite de {@link BEGEOT_BUNOUF_CustomJFrame}
 */
public class BEGEOT_BUNOUF_GUI_chercherPersonne extends BEGEOT_BUNOUF_CustomJFrame {

    private static final int DIMX = 600;
    private static final int DIMY = 500;
    private final String table;

    private JPanel panel;
    private JPanel panelResultat;
    private JPanel panel_ID;

    private JTextField fieldID;
    private JButton buttonChercher;

    private JLabel labelErreur;
    private JLabel labelID;
    private JLabel photo;

    private JTable tableAdresses;
    private JButton buttonSave;

    private JTextField textNom;
    private JTextField textPrenom;
    private JTextField textCity;
    private DatePicker textDate;
    private JTextField textSexe;
    private JButton add_ID;
    private JButton addAddress;
    private JTextField textPays;
    private JScrollPane panelAddress;
    private JComboBox comboBoxGroupe;
    private JPanel panelGroupe;
    private JLabel labelNoGroup;
    private JTable tableCours;
    private JPanel panelProf;
    private JLabel labelNoCours;
    private JButton add_Cours;
    private JLabel labelErrorID;
    private JLabel labelErrorAdresse;

    private int ID_personne;
    private int IDinput;

    /**
     * Création des contraintes pour les fields de l'interface
     */
    private void createUIComponents() {
        fieldID = new BEGEOT_BUNOUF_CustomJTextField("NUMERIC", false, 8);
        textNom = new BEGEOT_BUNOUF_CustomJTextField("ALPHABET", false, 20);
        textPrenom = new BEGEOT_BUNOUF_CustomJTextField("ALPHABET", false, 20);
        textDate = new DatePicker();
        textCity = new BEGEOT_BUNOUF_CustomJTextField("ALPHABET", false, 20);
        textSexe = new BEGEOT_BUNOUF_CustomJTextField("SEXE", false, 1);
        textPays = new BEGEOT_BUNOUF_CustomJTextField("ALPHABET", false, 20);
    }

    /**
     * Constructeur de l'Interface d'affichage d'une personne
     *
     * @param table       table concernée par la recherche de personne :
     *                    etudiant
     *                    professeur
     *                    responsable
     * @param newPersonne ID de la personne si cette personne est nouvelle. -1 sinon
     */
    public BEGEOT_BUNOUF_GUI_chercherPersonne(String table, int newPersonne) {
        super("Chercher Personne", false, DIMX, DIMY);
        BEGEOT_BUNOUF_GUI_USER_Admin.WindowClosing(this);

        this.table = table;

        ID_personne = -1;

        textDate.setSettings(BEGEOT_BUNOUF_DateFunctions.customDates());
        LocalDate date = LocalDate.of(2000, Month.JANUARY, 1);
        textDate.setDate(date);

        labelErreur.setVisible(false);
        labelNoGroup.setVisible(false);

        panelGroupe.setVisible(false);
        panelResultat.setVisible(false);
        panel_ID.setVisible(false);
        panelAddress.setVisible(false);
        panelProf.setVisible(false);

        /*Cacher l'ajout d'ID si ce n'est pas un étudiant*/
        if (!table.equals("etudiant")) {
            add_ID.setVisible(false);
            labelErrorID.setVisible(false);
        }

        buttonSave.setVisible(false);

        buttonChercher.addActionListener(e -> chercherPersonne());
        buttonSave.addActionListener(e -> savePersonne());
        addAddress.addActionListener(e -> addAddress());
        add_ID.addActionListener(e -> addID());

        add(panel);
        pack();
        revalidate();
        setVisible(true);

        if (newPersonne != -1) {
            fieldID.setText(String.valueOf(newPersonne));
            chercherPersonne();
        }

        if (!table.equals("responsable")) {
            add_Cours.addActionListener(e -> addCours());
        }
    }


    /**
     * BEGEOT_BUNOUF_Recherche de la personne à l'aide de son matricule
     */
    private void chercherPersonne() {
        if (fieldID.getText().length() == 0) {
            labelErreur.setVisible(true);
            panelResultat.setVisible(false);
            buttonSave.setVisible(false);
        } else {
            IDinput = Integer.parseInt(fieldID.getText());
            int ID_result = find_Matricule(IDinput, table);

            if (ID_result == -1) {
                labelErreur.setVisible(true);
                panelResultat.setVisible(false);
                buttonSave.setVisible(false);
            } else {
                labelErreur.setVisible(false);
                panelResultat.setVisible(true);
                photo.setVisible(false);
                buttonSave.setVisible(true);
                ID_personne = ID_result;


                /*CREATE QUERY TO RETRIEVE ALL THE INFO CONCERNING THE PERSON*/
                BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
                String sql = "SELECT * FROM personne WHERE ID = " + ID_personne;
                ResultSet data = database.run_Statement_READ(sql);

                try {
                    if (data.next()) {
                        labelID.setText(String.valueOf(IDinput));
                        textNom.setText(data.getString("Nom").toUpperCase());
                        textPrenom.setText(data.getString("Prenom").toUpperCase());
                    }
                } catch (SQLException ignore) {
                }

                /*TROUVER L'IDENTITE SI ETUDIANT*/
                if (table.equals("etudiant")) {
                    displayGroup();
                    displayID();
                }

                if (table.equals("professeur")) {
                    displayCours();
                }

                if (table.equals("responsable")) {
                    modify4responsable();
                    displayEleves();
                }

                /*AFFICHER LES ADDRESSES DE LA PERSONNE S'IL Y A*/
                displayAddress();
            }
        }
    }

    /**
     * Chercher le matricule entré dans une table précise
     *
     * @param matricule matricule de la personne
     * @param table     nom de la table dans laquelle recherchée
     * @return retourne l'ID de la personne auquel le matricule correspond ou -1 si celui-ci n'est pas trouvé.
     */
    private int find_Matricule(int matricule, String table) {

        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        try {
            if (!table.equals("responsable")) {
                ResultSet data = database.run_Statement_READ("SELECT Matricule, ID_Personne FROM " + table);
                while (data.next()) {
                    Integer matriculeData = data.getInt("Matricule");
                    if (matriculeData.equals(matricule)) {
                        int ID_result = data.getInt("ID_Personne");
                        database.Database_Deconnection();
                        return ID_result;
                    }
                }
            } else {
                ResultSet data = database.run_Statement_READ("SELECT Numero, ID_Personne FROM " + table);
                while (data.next()) {
                    Integer matriculeData = data.getInt("Numero");
                    if (matriculeData.equals(matricule)) {
                        int ID_result = data.getInt("ID_Personne");
                        database.Database_Deconnection();
                        return ID_result;
                    }
                }
            }


            database.Database_Deconnection();
            return -1;
        } catch (SQLException ignore) {
            database.Database_Deconnection();
            return -1;
        }
    }

    /**
     * Affiche la ou les adresses de la personne
     */
    public void displayAddress() {

        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();

        String sql = "SELECT * FROM adresse WHERE ID_Personne = " + ID_personne;
        ResultSet data = database.run_Statement_READ(sql);
        int totalRows = BEGEOT_BUNOUF_CountRows_TableCell.getRows(data);

        try {
            if (totalRows > 0) {
                String[] columns = new String[]{"Rue", "Ville", "Code Postal", "Telephone", "Email", " X "};
                Object[][] adresses = new Object[totalRows][columns.length];

                int index = 0;
                while (data.next()) {
                    adresses[index][0] = data.getString("Street");
                    adresses[index][1] = data.getString("City");
                    adresses[index][2] = data.getString("ZIP_code");
                    adresses[index][3] = data.getString("Phone");
                    adresses[index][4] = data.getString("Email");

                    int id = data.getInt("Adresse_ID");
                    adresses[index][5] = id;
                    index++;
                }

                DefaultTableModel model = new DefaultTableModel(adresses, columns);
                tableAdresses.setModel(model);

                tableAdresses.getColumn(" X ").setCellRenderer(new BEGEOT_BUNOUF_ButtonRenderer());
                tableAdresses.getColumn(" X ").setCellEditor(new BEGEOT_BUNOUF_ButtonEditorAdresse(new JCheckBox(), this));

                panelAddress.setVisible(true);
                labelErrorAdresse.setVisible(false);
            } else {
                labelErrorAdresse.setVisible(true);
                panelAddress.setVisible(false);
            }
            database.Database_Deconnection();
        } catch (SQLException ignore) {
        }
    }

    /**
     * Affiche l'identité de l'étudiant
     */
    public void displayID() {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String sql = "SELECT * FROM identite WHERE ID_Personne = " + ID_personne;
        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (BEGEOT_BUNOUF_CountRows_TableCell.getRows(data) > 0) {
                data.next();
                panel_ID.setVisible(true);
                add_ID.setVisible(false);
                labelErrorID.setVisible(false);

                textCity.setText(data.getString("ville_naissance"));
                textDate.setDate(BEGEOT_BUNOUF_DateFunctions.convertDate(data.getString("date_naissance")));

                textSexe.setText(data.getString("sexe"));
                textPays.setText(data.getString("pays_naissance"));

            } else {
                add_ID.setVisible(true);
                labelErrorID.setVisible(true);
                panel_ID.setVisible(false);
            }
            database.Database_Deconnection();
        } catch (SQLException ignore) {
        }
    }

    /**
     * Ajout d'un identité si il s'agit d'un étudiant.
     */
    private void addID() {
        this.setVisible(false);
        new BEGEOT_BUNOUF_GUI_addID(ID_personne, this);
    }

    /**
     * Sauvegarde de l'identité de l'étudiant
     */
    private void saveID() {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String sql = "UPDATE identite SET date_naissance = '" + textDate.getDate().toString() +
                "', ville_naissance = '" + textCity.getText() +
                "', pays_naissance = '" + textPays.getText() +
                "', sexe = '" + textSexe.getText() +
                "' WHERE ID_Personne = " + ID_personne;
        //TODO: "'photo= " == ??

        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
    }

    /**
     * Sauvegarde des Adresses
     */
    private void saveAddress() {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();

        for (int i = 0; i < tableAdresses.getRowCount(); i++) {
            String sql = "UPDATE adresse SET Street = '" + tableAdresses.getValueAt(i, 0).toString() +
                    "', City = '" + tableAdresses.getValueAt(i, 1).toString() +
                    "', ZIP_code = '" + tableAdresses.getValueAt(i, 2).toString() +
                    "', Phone = " + tableAdresses.getValueAt(i, 3).toString() +
                    ", Email = '" + tableAdresses.getValueAt(i, 4).toString() +
                    "' WHERE ID_Personne = " + ID_personne +
                    " AND Adresse_ID = " + tableAdresses.getValueAt(i, 5).toString();
            database.run_Statement_WRITE(sql);
        }

        database.Database_Deconnection();
    }

    /**
     * Ajouter une adresse pour la personne
     */
    private void addAddress() {
        this.setVisible(false);
        new BEGEOT_BUNOUF_GUI_addAddress(ID_personne, this);
    }

    /**
     * Suppression d'une adresse pour la personne
     *
     * @param ID
     */
    public void deleteAddress(int ID) {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String sql = "DELETE FROM adresse WHERE Adresse_ID = " + ID;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayAddress();
        JOptionPane.showMessageDialog(this, "Adresse supprimée.", "Deleted", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * Sauvegarde de la personne
     */
    private void savePersonne() {

        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();

        if (textNom.getText().length() == 0 || textPrenom.getText().length() == 0) {

            JOptionPane.showMessageDialog(this, "Le nom ou prénom entré n'est pas valide.", "Warning", JOptionPane.WARNING_MESSAGE);

        } else {
            String sql = "UPDATE personne SET Nom = '" + textNom.getText() +
                    "', Prenom = '" + textPrenom.getText() +
                    "' WHERE ID = " + ID_personne;
            database.run_Statement_WRITE(sql);
        }
        database.Database_Deconnection();

        if (table.equals("etudiant")) {
            saveGroup();
            saveID();
        }

        saveAddress();

        JOptionPane.showMessageDialog(this, "Bien enregistré", "Saved", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Affichage du groupe d'appartenance de l'étudiant
     */
    private void displayGroup() {
        String sql = "SELECT * from groupe";
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        ResultSet groupNames = database.run_Statement_READ(sql);

        try {
            while (groupNames.next()) {
                comboBoxGroupe.addItem(groupNames.getString("Nom"));
            }

            sql = "SELECT groupe.Nom from etudiant INNER JOIN groupe WHERE ID_Personne = " + ID_personne;
            ResultSet currentGroup = database.run_Statement_READ(sql);
            if (currentGroup.next()) {
                comboBoxGroupe.setSelectedItem(currentGroup.getString("Nom"));
            }
            panelGroupe.setVisible(true);
            labelNoCours.setVisible(false);
        } catch (SQLException e) {
            labelNoGroup.setVisible(true);
        }
    }

    /**
     * Sauvegarde du groupe de l'étudiant
     */
    private void saveGroup() {
        String sql = "SELECT Groupe_ID from groupe WHERE Nom = '" + comboBoxGroupe.getSelectedItem() + "'";
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        ResultSet groupID = database.run_Statement_READ(sql);
        try {
            if (groupID.next()) {
                sql = "UPDATE etudiant SET Groupe_ID = " + groupID.getInt("Groupe_ID")
                        + " WHERE ID_Personne = " + ID_personne;
                database.run_Statement_WRITE(sql);
            }
        } catch (SQLException ignore) {
        }
        database.Database_Deconnection();
    }

    /**
     * Affichage du ou des cours enseignés par le professeur
     */
    public void displayCours() {
        panelProf.setVisible(true);
        labelNoCours.setVisible(false);

        String sql = "SELECT Nom, cours.Code FROM cours INNER JOIN enseigner on cours.Code = enseigner.Code" +
                " WHERE enseigner.Matricule_Prof = " + IDinput;
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        ResultSet cours = database.run_Statement_READ(sql);
        int totalRows = BEGEOT_BUNOUF_CountRows_TableCell.getRows(cours);
        try {
            if (totalRows > 0) {
                String[] columns = new String[]{"Nom", " X "};
                Object[][] courses = new Object[totalRows][columns.length];

                int index = 0;
                while (cours.next()) {
                    courses[index][0] = cours.getString("Nom");
                    courses[index][1] = cours.getInt("Code");
                    index++;
                }

                tableCours.setModel(BEGEOT_BUNOUF_CountRows_TableCell.createModel(courses, columns));
                tableCours.getColumn(" X ").setCellRenderer(new BEGEOT_BUNOUF_ButtonRenderer());
                tableCours.getColumn(" X ").setCellEditor(new BEGEOT_BUNOUF_ButtonEditorCours(new JCheckBox(), this));
                tableCours.setVisible(true);
                labelNoCours.setVisible(false);
            } else {
                tableCours.setVisible(false);
                labelNoCours.setVisible(true);
            }
        } catch (SQLException ignore) {
        }
    }

    /**
     * Suppression d'un cours enseigné par le professeur
     *
     * @param code code du cours à supprimer
     */
    public void deleteCours(int code) {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String sql = "DELETE FROM enseigner WHERE Matricule_Prof = " + IDinput
                + " AND Code = " + code;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayCours();
        JOptionPane.showMessageDialog(this, "Cours enlevé.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
    }


    /**
     * Ajouter un cours pour le professeur
     */
    private void addCours() {
        new BEGEOT_BUNOUF_GUI_addCours(IDinput, this, null);
    }

    public void modify4responsable() {
        labelNoCours.setText("Responsable d'aucun étudiant.");
        add_Cours.setText("Ajouter un étudiant");
        add_Cours.addActionListener(e -> new BEGEOT_BUNOUF_GUI_addEleve(IDinput, null, this));
    }

    /**
     * Affichage du ou des cours enseignés par le professeur
     */
    public void displayEleves() {
        panelProf.setVisible(true);
        labelNoCours.setVisible(false);

        String sql = "SELECT Matricule_Etudiant FROM tuteur WHERE Numero = " + IDinput;
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        ResultSet eleves = database.run_Statement_READ(sql);
        int totalRows = BEGEOT_BUNOUF_CountRows_TableCell.getRows(eleves);
        try {
            if (totalRows > 0) {
                String[] columns = new String[]{"Matricule", " X "};
                Object[][] courses = new Object[totalRows][columns.length];

                int index = 0;
                while (eleves.next()) {
                    courses[index][0] = eleves.getString("Matricule_Etudiant");
                    courses[index][1] = eleves.getString("Matricule_Etudiant");
                    index++;
                }

                tableCours.setModel(BEGEOT_BUNOUF_CountRows_TableCell.createModel(courses, columns));
                tableCours.getColumn(" X ").setCellRenderer(new BEGEOT_BUNOUF_ButtonRenderer());
                tableCours.getColumn(" X ").setCellEditor(new BEGEOT_BUNOUF_ButtonEditorTuteur(new JCheckBox(), this));
                tableCours.setVisible(true);
                labelNoCours.setVisible(false);
            } else {
                tableCours.setVisible(false);
                labelNoCours.setVisible(true);
            }
        } catch (SQLException ignore) {
        }
    }

    public void deleteEleve(int matricule) {
        BEGEOT_BUNOUF_Database_Connection database = new BEGEOT_BUNOUF_Database_Connection();
        String sql = "DELETE FROM tuteur WHERE Matricule_Etudiant = " + matricule
                + " AND Numero = " + IDinput;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayCours();
        JOptionPane.showMessageDialog(this, "Etudiant enlevé.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
    }

}

