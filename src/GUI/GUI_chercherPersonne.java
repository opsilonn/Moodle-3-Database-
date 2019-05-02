package GUI;

import GUI_Components.ButtonEditorAdresse;
import GUI_Components.ButtonRenderer;
import GUI_Components.CustomJFrame;
import GUI_Components.CustomJTextField;
import Gestion_admin.Database_Connection;
import Gestion_admin.Display_ResultSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Fenêtre dédiée à la fonction de recherche d'une Personne
 * <p>
 * Cette classe hérite de {@link CustomJFrame}
 */
public class GUI_chercherPersonne extends CustomJFrame {

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
    private JTextField textDate;
    private JTextField textSexe;
    private JButton add_ID;
    private JButton addAddress;
    private JTextField textPays;
    private JScrollPane panelAddress;
    private JComboBox comboBoxGroupe;
    private JPanel panelGroupe;
    private JLabel labelNoGroup;

    private int ID_personne;

    public GUI_chercherPersonne(String table) {
        super("Chercher Personne", true, DIMX, DIMY);
        this.table = table;

        ID_personne = -1;


        /*TODO : ENLEVER CA*/
        fieldID.setText("20160024");

        labelErreur.setVisible(false);
        labelNoGroup.setVisible(false);

        panelGroupe.setVisible(false);
        panelResultat.setVisible(false);
        panel_ID.setVisible(false);
        panelAddress.setVisible(false);

        buttonSave.setVisible(false);

        buttonChercher.addActionListener(e -> chercherPersonne());
        buttonSave.addActionListener(e -> savePersonne());
        addAddress.addActionListener(e -> addAddress());
        add_ID.addActionListener(e -> addID());

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void createUIComponents() {

        fieldID = new CustomJTextField("NUMERIC", false, 8);
        textSexe = new CustomJTextField("UPPER_ALPHABET", false, 1);
    }

    /**
     * Quand activée, affiche les résultats en conséquence : si la personne
     * est trouvé, on l'affiche ses informations sinon, on affiche un message
     * d'erreur
     */
    private void chercherPersonne() {

        if (fieldID.getText().length() == 0) {
            labelErreur.setVisible(true);
            panelResultat.setVisible(false);
        } else {
            int IDinput = Integer.parseInt(fieldID.getText());
            int ID_result = find_Matricule(IDinput, table);

            if (ID_result == -1) {
                labelErreur.setVisible(true);
                panelResultat.setVisible(false);
            } else {
                labelErreur.setVisible(false);
                panelResultat.setVisible(true);
                photo.setVisible(false);
                buttonSave.setVisible(true);
                ID_personne = ID_result;


                /*CREATE QUERY TO RETRIEVE ALL THE INFO CONCERNING THE PERSON*/
                Database_Connection database = new Database_Connection();
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

                /*AFFICHER LES ADDRESSES DE LA PERSONNE S'IL Y A*/
                displayAddress();
            }
        }
    }

    private int find_Matricule(int matricule, String table) {

        Database_Connection database = new Database_Connection();
        try {
            ResultSet data = database.run_Statement_READ("SELECT Matricule, ID_Personne FROM " + table);

            while (data.next()) {
                Integer matriculeData = data.getInt("Matricule");
                if (matriculeData.equals(matricule)) {
                    int ID_result = data.getInt("ID_Personne");
                    database.Database_Deconnection();
                    return ID_result;
                }
            }
            database.Database_Deconnection();
            return -1;
        } catch (SQLException ignore) {
            database.Database_Deconnection();
            return -1;
        }
    }

    public void displayAddress() {

        Database_Connection database = new Database_Connection();

        String sql = "SELECT * FROM adresse WHERE ID_Personne = " + ID_personne;
        ResultSet data = database.run_Statement_READ(sql);
        int totalRows = Display_ResultSet.getRows(data);

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

                tableAdresses.getColumn(" X ").setCellRenderer(new ButtonRenderer());
                tableAdresses.getColumn(" X ").setCellEditor(new ButtonEditorAdresse(new JCheckBox(), this));

                panelAddress.setVisible(true);
            }
            database.Database_Deconnection();
        } catch (SQLException ignore) {
        }
    }

    private void displayID() {
        Database_Connection database = new Database_Connection();
        String sql = "SELECT * FROM identite WHERE ID_Personne = " + ID_personne;
        ResultSet data = database.run_Statement_READ(sql);
        try {
            if (data.next()) {
                add_ID.setVisible(false);
                textCity.setText(String.valueOf(data.getString("Ville_naissance")));
                textDate.setText(data.getString("Date_naissance"));
                textSexe.setText(data.getString("Sexe"));
                textPays.setText(data.getString("Pays_naissance"));
                panel_ID.setVisible(true);
            } else {
                add_ID.setVisible(true);
            }
            database.Database_Deconnection();
        } catch (SQLException ignore) {
        }
    }

    private void addID() {
        GUI_addID add = new GUI_addID(ID_personne, this);
        System.out.println("Enregistré");
        displayAddress();
    }

    private void saveID() {
        Database_Connection database = new Database_Connection();
        String sql = "UPDATE identite SET date_naissance = '" + textDate.getText() +
                "', ville_naissance = '" + textCity.getText() +
                "', pays_naissance = '" + textPays.getText() +
                "', sexe = '" + textSexe.getText() +
                "' WHERE ID_Personne = " + ID_personne;
        //TODO: "'photo= " == ??

        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
    }

    private void saveAddress() {
        Database_Connection database = new Database_Connection();

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

    private void addAddress() {
        GUI_addAddress add = new GUI_addAddress(ID_personne, this);
        System.out.println("Enregistré");
        displayAddress();
    }

    public void deleteAddress(int ID) {
        Database_Connection database = new Database_Connection();
        String sql = "DELETE FROM adresse WHERE Adresse_ID = " + ID;
        database.run_Statement_WRITE(sql);
        database.Database_Deconnection();
        displayAddress();
        System.out.println("Enlevé");
    }

    private void savePersonne() {

        Database_Connection database = new Database_Connection();

        if (textNom.getText().length() == 0 || textPrenom.getText().length() == 0) {
            System.out.println("ERROR IDENTITE");

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

        JOptionPane.showMessageDialog(this, "Bien enregistré");
    }

    private void displayGroup() {
        String sql = "SELECT * from groupe";
        Database_Connection database = new Database_Connection();
        ResultSet groupNames = database.run_Statement_READ(sql);
        try {
            while (groupNames.next()) {
                comboBoxGroupe.addItem(groupNames.getString("Nom"));
                /*int id = groupNames.getInt("Groupe_ID")
                comboBoxGroupe.addItemListener(e->changeGroup(id));*/
            }

            sql = "SELECT groupe.Nom from etudiant INNER JOIN groupe WHERE ID_Personne = " + ID_personne;
            ResultSet currentGroup = database.run_Statement_READ(sql);
            if (currentGroup.next()) {
                comboBoxGroupe.setSelectedItem(currentGroup.getString("Nom"));
            }
            panelGroupe.setVisible(true);
        } catch (SQLException e) {
            labelNoGroup.setVisible(true);
        }
    }

    private void changeGroup(int ID_Group) {
//TODO
    }

    private void saveGroup() {
        String sql = "SELECT Groupe_ID from groupe WHERE Nom = " + comboBoxGroupe.getSelectedItem();
        Database_Connection database = new Database_Connection();
        ResultSet groupID = database.run_Statement_READ(sql);
        try {
            if (groupID.next()) {
                sql = "UPDATE etudiant SET Groupe_ID = " + groupID.getInt("Groupe_ID")
                        + " WHERE Personne_ID = " + ID_personne;
                database.run_Statement_WRITE(sql);
            }
        } catch (SQLException ignore) {
        }

    }
}

