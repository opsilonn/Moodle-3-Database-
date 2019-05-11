package GUI;


import GUI_Components.CustomJFrame;
import UsefulFunctions.Database_Connection;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static UsefulFunctions.CountRows_TableCell.createModel;
import static recherche.Recherche.*;
import static recherche.RechercheProfesseur.*;


/**
 * Fenêtre dédiée à l'utilisation du logiciel par un Professeur
 * <p>
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_USER_Professeur extends CustomJFrame
{
    private static final int DIM_X = 600;
    private static final int DIM_Y = 650;

    private int matricule;


    private JPanel panel;

    private JLabel labelNom;
    private JLabel labelMatricule;

    private JButton buttonEtudiant;
    private JButton buttonModifier;

    private JLabel labelErreur;
    private JTable coursTable;
    private JScrollPane coursPane;
    private JButton buttonCours;

    private String[] columns = new String[]{"Cours", "Groupe - ID", "Groupe - Nom", "année"};
    private Object[][] DATA;
    private int CURSOR;


    /**
     * Création de l'interface pour un Professeur
     *
     * @param matricule - Matricule du professeur connecté
     */
    public GUI_USER_Professeur(int matricule)
    {
        super("Professeur", true, DIM_X, DIM_Y);
        this.matricule = matricule;


        remplirInformations();
        remplirCours();


        buttonEtudiant.addActionListener(e -> new GUI_chercherEtudiant(matricule));
        buttonCours.addActionListener(e -> consulteListe());
        buttonModifier.addActionListener(e -> new GUI_modifierNote(matricule));


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void consulteListe() {
        this.setVisible(false);
        new GUI_consulterListes(this);
    }


    /**
     * Remplissage des champs sur l'information du professeur connecté
     */
    private void remplirInformations()
    {
        labelNom.setText(
                getPersonne(matricule, "professeur", "Prenom") + " " +
                getPersonne(matricule, "professeur", "Nom").toUpperCase());
        labelMatricule.setText(matricule + "");
    }


    /**
     * Remplissage des champs sur les cours dispensés du professeur connecté
     */
    private void remplirCours()
    {
        int nombreCours = nombreCoursProfesseur(matricule);
        boolean avoirCours = (nombreCours != 0);

        labelErreur.setVisible(!avoirCours);
        coursTable.setVisible(avoirCours);
        coursPane.setVisible(avoirCours);

        if (avoirCours)
        {
            ArrayList<String> listeCours = getCoursArray(matricule, "Code");
            nombreCours = 0;

            for (String cours : listeCours)
                nombreCours += nombreGroupeSuivantCours(cours) + 1;


            DATA = new Object[nombreCours][columns.length];

            CURSOR = 0;
            for (String cours : listeCours)
                remplirCoursGroupes(cours);

            coursTable.setModel(createModel(DATA, columns));
            centrerJTable(coursTable);
        }
    }


    /**
     * Remplissage des champs sur groupes suivant un cours dispensé par le professeur connecté
     *
     * @param coursCode Code du cours en question
     */
    private void remplirCoursGroupes(String coursCode)
    {
        String NOM = getCours(coursCode, "Code") + " - " + getCours(coursCode, "Nom");
        String ANNEE = getCours(coursCode, "Annee").substring(0, 4);


        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                "FROM suivre, groupe " +
                "WHERE suivre.Code = " + coursCode + " " +
                "AND suivre.Groupe_ID = groupe.Groupe_ID;";


        ResultSet resultat = database.run_Statement_READ(query);

        try {
            while (resultat.next())
            {
                DATA[CURSOR][0] = NOM;
                DATA[CURSOR][1] = resultat.getString("groupe.Groupe_ID");
                DATA[CURSOR][2] = resultat.getString("groupe.Nom");
                DATA[CURSOR][3] = ANNEE;
                CURSOR++;
            }
            CURSOR++;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        database.Database_Deconnection();
    }
}
