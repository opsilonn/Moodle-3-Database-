package GUI;


import GUI_Components.CustomJFrame;
import GUI_Components.CustomJTextField;
import GUI_Components.DateFunctions;
import UsefulFunctions.Database_Connection;
import com.github.lgooddatepicker.components.DatePicker;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

import static UsefulFunctions.CountRows_TableCell.getRows;


/**
 * Fenêtre dédiée à l'ajout d'une note
 * <p>
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_modifierNote extends CustomJFrame
{
    private static final int DIM_X = 900;
    private static final int DIM_Y = 400;

    private String matricule;

    private JPanel panel;

    private JComboBox<String> comboBoxCours;
    private JComboBox<String> comboBoxGroupe;
    private JComboBox<String> comboBoxMatricule;
    private JComboBox<String> comboBoxNote;
    private JTextField fieldNote;

    private JLabel labelErreur;
    private JLabel labelErreurCours;
    private JLabel labelErreurGroupe;
    private JLabel labelErreurMatricule;

    private JButton buttonValider;
    private DatePicker textDate;


    public GUI_modifierNote(String matricule)
    {
        super("Ajouter note", false, DIM_X, DIM_Y);
        this.matricule = matricule;


        textDate.setSettings(DateFunctions.customDates());

        setCours();
        labelErreur.setVisible(false);

        comboBoxCours.addActionListener(e -> setGroupes());
        comboBoxGroupe.addActionListener(e -> setMatricules());

        if (matricule.equals("-1"))
        {
            //Si l'administration modifie des notes, la date doit bouger également.
            comboBoxNote.addActionListener(e -> moveDate());
        }

        buttonValider.addActionListener(e -> setNote());


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents()
    {
        fieldNote = new CustomJTextField("DECIMAL", false, 5);
        textDate = new DatePicker();
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxCours
     *
     * @return la valeur sélectionnée dans le combobox comboboxCours
     */
    private String ID_COURS()
    {
        String S = String.valueOf(comboBoxCours.getSelectedItem());
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxCours
     *
     * @return la valeur sélectionnée dans le combobox comboboxCours
     */
    private String ID_GROUPE()
    {
        String S = String.valueOf(comboBoxGroupe.getSelectedItem());
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxMatricule
     *
     * @return la valeur sélectionnée dans le combobox comboboxMatricule
     */
    private String ID_MATRICULE() {
        String S = String.valueOf(comboBoxMatricule.getSelectedItem());
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxNote
     *
     * @return la valeur sélectionnée dans le combobox comboboxNote
     */
    private String ID_NOTE()
    {
        String S = String.valueOf(comboBoxNote.getSelectedItem());
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    private String CODE_NOTE()
    {
        String S = String.valueOf(comboBoxNote.getSelectedItem());
        int index = S.indexOf(".");
        return S.substring(0, index);
    }


    /**
     * Met à jour le comboBox contenant les différents cours délivrés par le professeur connecté.
     * S'il n'est pas vide après remplissage, on lance la fonction setGroupe
     */
    private void setCours()
    {
        comboBoxCours.removeAllItems();
        String query = "";
        Database_Connection database = new Database_Connection();

        //Si un professeur ajoute des notes
        if (matricule.equals("-1"))
        {
            //Si l'administration souhaite modifier des notes
            query = "SELECT * FROM cours ORDER BY cours.Nom;";
        }
        else
        {
            query =
                    "SELECT * " +
                    "FROM enseigner, cours " +
                    "WHERE enseigner.Matricule_Prof = " + matricule + " " +
                    "AND enseigner.Code = cours.Code " +
                    "ORDER BY cours.Nom;";
        }

        ResultSet resultat = database.run_Statement_READ(query);

        // On remplit le dropdown contenant le code et nom du cours
        try {
            while (resultat.next())
            {
                comboBoxCours.addItem(
                        resultat.getString("cours.Code") + " : " +
                        resultat.getString("cours.Nom"));
            }
        } catch (SQLException ignore) {
        }
        database.Database_Deconnection();


        // Si le dropdown précédemment rempli est toujours vide, on affiche un message d'erreur
        // Sinon, on appelle la fonction suivante

        boolean result = (comboBoxCours.getItemCount() != 0);
        setDisplay(result, RANK_COURS);

        if (result)
            setGroupes();
    }


    /**
     * Met à jour le comboBox contenant les différents Groupes contenus dans le cours sélectionné.
     * S'il n'est pas vide après remplissage, on lance la fonction setMatricule
     */
    private void setGroupes()
    {
        comboBoxGroupe.removeAllItems();

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM suivre, groupe " +
                "WHERE suivre.Code = " + ID_COURS() + " " +
                "AND suivre.Groupe_ID = groupe.Groupe_ID;";

        ResultSet resultat = database.run_Statement_READ(query);


        // On remplit le dropdown contenant l'ID et le nom des Groupes
        try {
            while (resultat.next()) {
                comboBoxGroupe.addItem(
                        resultat.getString("groupe.Groupe_ID") + " - " +
                                resultat.getString("groupe.Nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.Database_Deconnection();


        // Si le dropdown précédemment remplit est toujours vide, on affiche un message d'erreur
        // Sinon, on appelle la fonction suivante
        boolean result = (comboBoxGroupe.getItemCount() != 0);
        setDisplay(result, RANK_GROUPE);
        if (result)
            setMatricules();
    }


    /**
     * Met à jour le comboBox contenant les différents étudiants contenus dans le Groupe sélectionné.
     * S'il n'est pas vide après remplissage, on lance la fonction setGroupe
     */
    private void setMatricules()
    {
        comboBoxMatricule.removeAllItems();

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM personne, etudiant " +
                        "WHERE etudiant.Groupe_ID = " + ID_GROUPE() + " " +
                        "AND etudiant.ID_Personne = personne.ID ;";

        ResultSet resultat = database.run_Statement_READ(query);


        // On remplit le dropdown contenant l'ID et le nom des étudiants

        try {
            while (resultat.next()) {
                comboBoxMatricule.addItem(
                        resultat.getString("etudiant.Matricule") + " - " +
                                resultat.getString("personne.Prenom") + " " +
                                resultat.getString("personne.Nom").toUpperCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        database.Database_Deconnection();

        boolean result = (comboBoxMatricule.getItemCount() != 0);
        setDisplay(result, RANK_MATRICULE);

        labelErreurMatricule.setVisible(!result);
        comboBoxMatricule.setVisible(result);

        if (result)
            setNotes();
    }


    /**
     * Met à jour le Dropdown contenant la liste des notes de l'étudiant sélectionné.
     */
    private void setNotes() {
        comboBoxNote.removeAllItems();

        //Si l'administration veut modifier une note
        if (matricule.equals("-1")) {
            Database_Connection database = new Database_Connection();
            String query =
                    "SELECT Code, Valeur, Type " +
                            "FROM note " +
                            "WHERE Matricule_Etudiant = " + ID_MATRICULE() + " " +
                            "AND Code = " + ID_COURS() + " ;";

            ResultSet resultat = database.run_Statement_READ(query);

            // On remplit le dropdown contenant le type et la valeur des notes
            try {
                while (resultat.next()) {
                    String note = resultat.getString("Valeur");
                    String code = resultat.getString("Code");

                    switch (resultat.getString("Type")) {
                        case "TP":
                            comboBoxNote.addItem(code + ".TP : " + note);
                            break;

                        case "DE":
                            comboBoxNote.addItem(code + ".DE : " + note);
                            break;

                        case "Projet":
                            comboBoxNote.addItem(code + ".Projet : " + note);
                            break;
                    }
                }
            } catch (SQLException ignore) {
            }
            database.Database_Deconnection();

        } else {
            comboBoxNote.addItem("TP ");
            comboBoxNote.addItem("DE ");
            comboBoxNote.addItem("Projet ");
        }
    }

    /**
     * Bouge la valeur de la date à la date à laquelle l'examen a eu lieue
     */
    void moveDate() {
        String sql = " SELECT from note WHERE Code = " + CODE_NOTE();
        Database_Connection database = new Database_Connection();
        ResultSet data = database.run_Statement_READ(sql);
        if (getRows(data) == 1) {
            try {
                textDate.setDate(DateFunctions.convertDate(data.getString("Date_exam")));
            } catch (SQLException ignore) {
            }
        }
    }


    /**
     * Si la note entrée est correcte, appelle la fonction permettant d'ajouter
     * de modifier une note chez l'Etudiant connecté
     */
    private void setNote() {
        try {
            float inputNote = Float.parseFloat(fieldNote.getText());
            System.out.println(inputNote);

            if (inputNote < 0)
                setLabelErreur("Note trop petite !!");

            else if (20 < inputNote)
                setLabelErreur("Note trop grande !!");

            else {
                Database_Connection database = new Database_Connection();
                String query;

                // Si la note est entrée par un professeur on en ajoute une
                // Sinon, en modifie la note existante
                if (!matricule.equals("-1"))
                    query =
                            "INSERT INTO note (Valeur, Type, Date_Exam, Code, Matricule_Etudiant)" +
                                    "VALUES ('" +
                                    inputNote + "', '" +
                                    ID_NOTE() + "', '" +
                                    textDate.getDate().toString() + "', '" +
                                    ID_COURS() + "', '" +
                                    ID_MATRICULE() + "');";
                else {
                    query =
                            "UPDATE note " +
                                    "SET Valeur = " + inputNote + " " +
                                    "WHERE Code = " + CODE_NOTE() + " ;";
                }
                
                database.run_Statement_WRITE(query);
                database.Database_Deconnection();

                dispose();
            }
        } catch (NumberFormatException e) {
            setLabelErreur("Erreur de syntaxe pour la note !!");
        }

    }


    private void setLabelErreur(String text) {
        labelErreur.setVisible(true);
        labelErreur.setText(text);
    }


    private final static int RANK_COURS = 2;
    private final static int RANK_GROUPE = 1;
    private final static int RANK_MATRICULE = 0;


    private void setDisplay(boolean result, int rank) {
        if (rank >= 2) {
            labelErreurCours.setVisible(!result);

            labelErreurGroupe.setVisible(!result);
            comboBoxGroupe.setVisible(result);
        }
        if (rank >= 1) {
            labelErreurMatricule.setVisible(!result);
            comboBoxMatricule.setVisible(result);
            comboBoxNote.setVisible(result);
        }
        if (rank >= 0) {
            comboBoxNote.setVisible(result);
        }
    }
}
