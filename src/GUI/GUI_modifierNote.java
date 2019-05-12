package GUI;


import GUI_Components.CustomJFrame;
import GUI_Components.CustomJTextField;
import GUI_Components.DateFunctions;
import UsefulFunctions.Database_Connection;
import com.github.lgooddatepicker.components.DatePicker;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static UsefulFunctions.CountRows_TableCell.getRows;


/**
 * Fenêtre dédiée à l'ajout d'une note
 * <p>
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_modifierNote extends CustomJFrame {
    private static final int DIM_X = 1000;
    private static final int DIM_Y = 400;

    private int matricule;

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
    private JLabel labelErreurNote;


    private final ActionListener LISTENER_CALL_GROUPES = e -> setGroupes();
    private final ActionListener LISTENER_CALL_MATRICULES = e -> setMatricules();
    private final ActionListener LISTENER_CALL_NOTES = e -> setNotes();


    public GUI_modifierNote(int matricule) {
        super("Ajouter note", false, DIM_X, DIM_Y);
        this.matricule = matricule;


        textDate.setSettings(DateFunctions.customDates());
        labelErreur.setVisible(false);
        if (matricule == -1) {
            //Si l'administration modifie des notes, on change le titre de la frame.
            setTitle("Modifier note");
            //Si l'administration modifie des notes, la date doit bouger également.
            comboBoxNote.addActionListener(e -> moveDate());

            labelErreurNote.setText("L'étudiant ne possède aucune note dans la matière !");
            GUI_USER_Admin.WindowClosing(this);
        } else {
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    new GUI_USER_Professeur(matricule);
                    dispose();
                }
            });
            labelErreurNote.setText("L'étudiant a déjà toutes ses notes dans la matière !");
        }


        setCours();

        comboBoxCours.addActionListener(LISTENER_CALL_GROUPES);
        comboBoxGroupe.addActionListener(LISTENER_CALL_MATRICULES);
        comboBoxMatricule.addActionListener(LISTENER_CALL_NOTES);

        buttonValider.addActionListener(e -> setNote());


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents() {
        fieldNote = new CustomJTextField("DECIMAL", false, 5);
        textDate = new DatePicker();
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxCours
     *
     * @return la valeur sélectionnée dans le combobox comboboxCours
     */
    private String ID_COURS() {
        String S = String.valueOf(comboBoxCours.getSelectedItem());
        if (S == null || S.length() == 0)
            return "";

        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxCours
     *
     * @return la valeur sélectionnée dans le combobox comboboxCours
     */
    private String ID_GROUPE() {
        String S = String.valueOf(comboBoxGroupe.getSelectedItem());
        if (S == null || S.length() == 0)
            return "";

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
        if (S == null || S.length() == 0)
            return "";

        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la type de la note sélectionnée dans le combobox comboboxNote
     *
     * @return la type de la note sélectionnée dans le combobox comboboxNote
     */
    private String TYPE_NOTE() {
        String S = String.valueOf(comboBoxNote.getSelectedItem());
        if (S == null || S.length() == 0)
            return "";

        if (matricule == -1) {
            int index = S.indexOf(" ");
            return S.substring(0, index);
        } else
            return String.valueOf(comboBoxNote.getSelectedItem());
    }


    /**
     * retourne l'ID de la note sélectionnée dans le combobox comboboxNote
     *
     * @return l'ID de la note sélectionnée dans le combobox comboboxNote
     */
    private String ID_NOTE() {
        String S = String.valueOf(comboBoxNote.getSelectedItem());

        if (S == null || S.length() == 0 || matricule != -1)
            return "";
        else {
            int index = S.indexOf("(") + 1;
            int index2 = S.indexOf(")");
            return S.substring(index, index2);
        }
    }


    /**
     * Met à jour le comboBox contenant les différents cours délivrés par le professeur connecté.
     * S'il n'est pas vide après remplissage, on lance la fonction setGroupe
     */
    private void setCours() {
        comboBoxCours.removeActionListener(LISTENER_CALL_GROUPES);
        comboBoxCours.removeAllItems();
        comboBoxCours.addActionListener(LISTENER_CALL_GROUPES);

        String query;
        Database_Connection database = new Database_Connection();

        if (matricule == -1) //Si l'administration est connecté, on selectionne tous les cours
            query = "SELECT * FROM cours ORDER BY cours.Nom;";

        else //Si un prof est connecté, on selectionne tous ses cours
            query =
                    "SELECT * " +
                            "FROM enseigner, cours " +
                            "WHERE enseigner.Matricule_Prof = " + matricule + " " +
                            "AND enseigner.Code = cours.Code " +
                            "ORDER BY cours.Nom;";

        ResultSet resultat = database.run_Statement_READ(query);

        // On remplit le dropdown contenant le code et nom du cours
        try {
            while (resultat.next()) {
                comboBoxCours.addItem(
                        resultat.getString("cours.Code") + " : " +
                                resultat.getString("cours.Nom"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
    private void setGroupes() {
        comboBoxGroupe.removeActionListener(LISTENER_CALL_MATRICULES);
        comboBoxGroupe.removeAllItems();
        comboBoxGroupe.addActionListener(LISTENER_CALL_MATRICULES);


        Database_Connection database = new Database_Connection();

        // On sélectionne tous les groupes suivant le cours choisi
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


        // Si le dropdown précédemment rempli est toujours vide, on affiche un message d'erreur
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
    private void setMatricules() {
        comboBoxMatricule.removeActionListener(LISTENER_CALL_NOTES);
        comboBoxMatricule.removeAllItems();
        comboBoxMatricule.removeActionListener(LISTENER_CALL_NOTES);

        Database_Connection database = new Database_Connection();

        // On sélectionne tous les étudiant contenus dans le groupe choisi.
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

        if (result)
            setNotes();
    }


    /**
     * Met à jour le Dropdown contenant la liste des notes de l'étudiant sélectionné.
     * Si un admin est connecté, on affiche les notes de l'étudiant ;
     * Si un Professeur est connecté, on affiche les types des notes pas encore rentrées.
     */
    private void setNotes() {
        boolean isTP = false;
        boolean isDE = false;
        boolean isPROJET = false;

        comboBoxNote.removeAllItems();

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                        "FROM note " +
                        "WHERE Matricule_Etudiant = " + ID_MATRICULE() + " " +
                        "AND Code = " + ID_COURS() + " ;";

        ResultSet resultat = database.run_Statement_READ(query);

        // si admin connecté : on affiche les notes
        if (matricule == -1) {
            try {
                while (resultat.next()) {
                    comboBoxNote.addItem(
                            resultat.getString("Type") + " : " +
                                    resultat.getString("Valeur") + " (" + resultat.getString("ID") + ")");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // si prof connecté : on cherche les notes qui sont déjà rentrées pour NE PAS les ajouter par la suite.
        else {
            try {
                while (resultat.next()) {
                    if (Objects.equals(resultat.getString("Type"), "TP")) isTP = true;
                    if (Objects.equals(resultat.getString("Type"), "DE")) isDE = true;
                    if (Objects.equals(resultat.getString("Type"), "Projet")) isPROJET = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (!isTP) comboBoxNote.addItem("TP");
            if (!isDE) comboBoxNote.addItem("DE");
            if (!isPROJET) comboBoxNote.addItem("Projet");
        }

        database.Database_Deconnection();

        boolean result = (comboBoxNote.getItemCount() != 0);
        setDisplay(result, RANK_NOTE);
    }


    /**
     * Bouge la valeur de la date à laquelle l'examen a eu lieu
     */
    private void moveDate() {
        String sql = " SELECT from note WHERE Code = " + ID_COURS();
        Database_Connection database = new Database_Connection();
        ResultSet data = database.run_Statement_READ(sql);
        if (getRows(data) == 1) {
            try {
                textDate.setDate(DateFunctions.convertDate(data.getString("Date_exam")));
            } catch (SQLException ignore) {
                ignore.printStackTrace();
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

            if (inputNote < 0)
                setLabelErreur("Note trop petite !!");

            else if (20 < inputNote)
                setLabelErreur("Note trop grande !!");

            else {
                Database_Connection database = new Database_Connection();
                String query;

                if (matricule != -1) // Si la note est entrée par un professeur on en ajoute une
                    query =
                            "INSERT INTO note (Valeur, Type, Date_Exam, Code, Matricule_Etudiant)" +
                                    "VALUES ('" +
                                    inputNote + "', '" +
                                    TYPE_NOTE() + "', '" +
                                    textDate.getDate().toString() + "', '" +
                                    ID_COURS() + "', '" +
                                    ID_MATRICULE() + "');";

                else // Sinon, en modifie la note existante
                    query =
                            "UPDATE note " +
                                    "SET Valeur = " + inputNote + " " +
                                    "WHERE ID = " + ID_NOTE() + " ;";


                database.run_Statement_WRITE(query);
                database.Database_Deconnection();


                if (matricule != -1) {
                    new GUI_USER_Professeur(matricule);
                } else {
                    new GUI_USER_Admin();
                }
                dispose();
            }
        } catch (NumberFormatException e) {
            setLabelErreur("Erreur de syntaxe pour la note !!");
        }

    }


    /**
     * Règle le message d'erreur à afficher quand la note en entrée est incorrecte
     *
     * @param text Message à mettre dans le label
     */
    private void setLabelErreur(String text) {
        labelErreur.setVisible(true);
        labelErreur.setText(text);
    }


    private final static int RANK_COURS = 3;
    private final static int RANK_GROUPE = 2;
    private final static int RANK_MATRICULE = 1;
    private final static int RANK_NOTE = 0;

    /**
     * Affiche ou non les messages d'erreur et les comboBox
     *
     * @param result vaut true si le comboBox d'un certain niveau est rempli, faux s'il est vide
     * @param rank   Rang du comboBox dont on a précédemment vérifié s'il était vide ou non
     */
    private void setDisplay(boolean result, int rank) {
        if (rank >= 3) {
            labelErreurCours.setVisible(!result);
            comboBoxCours.setVisible(result);
        }
        if (rank >= 2) {
            labelErreurGroupe.setVisible(!result);
            comboBoxGroupe.setVisible(result);
        }
        if (rank >= 1) {
            labelErreurMatricule.setVisible(!result);
            comboBoxMatricule.setVisible(result);
        }
        if (rank >= 0) {
            labelErreurNote.setVisible(!result);
            comboBoxNote.setVisible(result);
        }

        comboBoxNote.setVisible(result);
        buttonValider.setEnabled(result);
    }
}