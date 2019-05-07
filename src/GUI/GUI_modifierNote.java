package GUI;


import GUI_Components.CustomJFrame;
import GUI_Components.CustomJTextField;
import Gestion_admin.Database_Connection;
import recherche.rechercheProfesseur;
import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;


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
    private rechercheProfesseur PROFESSEUR = new rechercheProfesseur();


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


    public GUI_modifierNote(String matricule)
    {
        super("Ajouter note", false, DIM_X, DIM_Y);
        this.matricule = matricule;


        setCours();
        labelErreur.setVisible(false);

        comboBoxCours.addActionListener(e -> setGroupes());
        comboBoxGroupe.addActionListener(e -> setMatricules());

        buttonValider.addActionListener(e -> setNote());


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents()
    {
        fieldNote = new CustomJTextField("DECIMAL", false, 5);
    }




    /**
     * retourne la valeur sélectionnée dans le combobox comboboxCours
     * @return la valeur sélectionnée dans le combobox comboboxCours
     */
    private String ID_COURS()
    {
        String S = String.valueOf( comboBoxCours.getSelectedItem() );
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxCours
     * @return la valeur sélectionnée dans le combobox comboboxCours
     */
    private String ID_GROUPE()
    {
        String S = String.valueOf( comboBoxGroupe.getSelectedItem() );
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxMatricule
     * @return la valeur sélectionnée dans le combobox comboboxMatricule
     */
    private String ID_MATRICULE()
    {
        String S = String.valueOf( comboBoxMatricule.getSelectedItem() );
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }


    /**
     * retourne la valeur sélectionnée dans le combobox comboboxMatricule
     * @return la valeur sélectionnée dans le combobox comboboxMatricule
     */
    private String ID_NOTE()
    {
        String S = String.valueOf( comboBoxNote.getSelectedItem() );
        int index = S.indexOf(" ");
        return S.substring(0, index);
    }




    /**
     * Met à jour le comboBox contenant les différents cours délivré par le professeur connecté.
     * S'il n'est pas vide après remplissage, on lance la fonction setGroupe
     */
    private void setCours()
    {
        comboBoxCours.removeAllItems();

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM professeur, enseigner, cours " +
                "WHERE enseigner.Matricule_Prof = " +  matricule + " " +
                "AND enseigner.Code = cours.Code " +
                "ORDER BY cours.Nom;";

        ResultSet resultat = database.run_Statement_READ(query);


        // On remplit le dropdown contenant l'ID et le nom des Groupes
        try
        {
            while ( resultat.next() )
                comboBoxCours.addItem(
                        resultat.getString("cours.Code") + " : " +
                        resultat.getString("cours.Nom"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        database.Database_Deconnection();


        // Si le dropdown précédemment remplit est toujours vide, on affiche un message d'erreur
        // Sinon, on appelle la fonction suivante

        boolean result = (comboBoxCours.getItemCount() != 0);
        setDisplay(result, RANK_COURS);

        if(result)
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
                "FROM suivre, Groupe " +
                "WHERE suivre.Code = " +  ID_COURS() + " " +
                "AND suivre.Groupe_ID = groupe.Groupe_ID;";

        ResultSet resultat = database.run_Statement_READ(query);


        // On remplit le dropdown contenant l'ID et le nom des Groupes
        try
        {
            while ( resultat.next() )
                comboBoxGroupe.addItem(
                        resultat.getString("groupe.Groupe_ID") + " - " +
                        resultat.getString("groupe.Nom"));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        database.Database_Deconnection();


        // Si le dropdown précédemment remplit est toujours vide, on affiche un message d'erreur
        // Sinon, on appelle la fonction suivante
        boolean result = (comboBoxGroupe.getItemCount() != 0);
        setDisplay(result, RANK_GROUPE);
        if(result)
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
                "WHERE etudiant.Groupe_ID = " +  ID_GROUPE() + " " +
                "AND etudiant.ID_Personne = personne.ID ;";

        ResultSet resultat = database.run_Statement_READ(query);


        // On remplit le dropdown contenant l'ID et le nom des étudiants

        try
        {
            while ( resultat.next() )
            {
                comboBoxMatricule.addItem(
                        resultat.getString("etudiant.Matricule") + " - " +
                        resultat.getString("personne.Prenom") + " " +
                        resultat.getString("personne.Nom").toUpperCase());
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        database.Database_Deconnection();

        boolean result = (comboBoxMatricule.getItemCount() != 0);
        setDisplay(result, RANK_MATRICULE);

        labelErreurMatricule.setVisible(!result);
        comboBoxMatricule.setVisible(result);

        if(result)
            setNotes();
    }


    /**
     * Met à jour le Dropdown contenant la liste des notes de l'étudiant sélectionné.
     */
    private void setNotes()
    {
        comboBoxNote.removeAllItems();

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM note " +
                "WHERE note.Matricule_Etudiant = " +  ID_MATRICULE() + " " +
                "AND note.Code = " + ID_COURS() + " ;";

        ResultSet resultat = database.run_Statement_READ(query);


        // On remplit le dropdown contenant le type et la valeur des notes

        String NOTE_TP = "indéfinie";
        String NOTE_DE = "indéfinie";
        String NOTE_PROJET = "indéfinie";

        try
        {
            while ( resultat.next() )
            {
                String note = resultat.getString("Valeur");

                switch( resultat.getString("note.Type") )
                {
                    case "TP":
                        NOTE_TP = note;
                        break;

                    case "DE":
                        NOTE_DE = note;
                        break;

                    case "Projet":
                        NOTE_PROJET = note;
                        break;

                    default :
                        System.out.println("la note lu possède un type bizarre ...");
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        database.Database_Deconnection();


        comboBoxNote.addItem("TP : " + NOTE_TP);
        comboBoxNote.addItem("DE : " + NOTE_DE);
        comboBoxNote.addItem("Projet : " + NOTE_PROJET);
    }




    /**
     * Si la note entrée est correcte, appelle la fonction permettant d'ajouter
     * de modifier une note chez l'Etudiant connecté
     * */
    private void setNote()
    {
        try
        {
            float inputNote = Float.parseFloat(fieldNote.getText());
            System.out.println(inputNote);

            if (inputNote < 0)
                setLabelErreur("Note trop petite !!");

            else if (20 < inputNote)
                setLabelErreur("Note trop grande !!");

            else
            {
                Database_Connection database = new Database_Connection();
                String query =
                        "UPDATE note, etudiant " +
                        "SET Valeur = " + inputNote + " " +
                        "WHERE Type = '" + ID_NOTE() + "' " +
                        "AND note.Matricule_Etudiant = " + ID_MATRICULE() + " ;";

                System.out.println(query);
                database.run_Statement_WRITE(query);
                database.Database_Deconnection();

                dispose();
            }
        }
        catch (NumberFormatException e)
        {
            setLabelErreur("Erreur de syntaxe pour la note !!");
        }
    }


    private void setLabelErreur(String text)
    {
        labelErreur.setVisible(true);
        labelErreur.setText(text);
    }


    private final static int RANK_COURS     = 2;
    private final static int RANK_GROUPE    = 1;
    private final static int RANK_MATRICULE = 0;


    private void setDisplay(boolean result, int rank)
    {
        if(rank >= 2)
        {
            labelErreurCours.setVisible(!result);

            labelErreurGroupe.setVisible(!result);
            comboBoxGroupe.setVisible(result);
        }
        if(rank >= 1)
        {
            labelErreurMatricule.setVisible(!result);
            comboBoxMatricule.setVisible(result);
            comboBoxNote.setVisible(result);
        }
        if(rank >= 0)
        {
            comboBoxNote.setVisible(result);
        }
    }
}
