package GUI;


import GUI_Components.CustomJFrame;
import Gestion_admin.Database_Connection;
import recherche.RechercheProfesseur;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Fenêtre dédiée à l'utilisation du logiciel par un Professeur
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_Professeur extends CustomJFrame
{
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;

    private RechercheProfesseur PROFESSEUR = new RechercheProfesseur();
    private String matricule;


    private JPanel panel;

    private JLabel labelNom;
    private JLabel labelMatricule;
    private JLabel labelMatiere;

    private JButton buttonChercherEleve;
    private JButton buttonModifier;

    private JLabel labelErreur;
    private JTable coursTable;
    private JScrollPane coursPane;
    private JButton buttonCours;

    /**
     * Création de l'interface pour un Eleve
     *
     * @param matricule - Matricule de l'élève connecté
     */
    public GUI_Professeur(String matricule)
    {
        super("Professeur", true, DIM_X, DIM_Y);
        this.matricule = matricule;


        remplirInformations();
        remplirCours();


        buttonCours.addActionListener(e -> { GUI_consulterCours frame = new GUI_consulterCours(); });
        buttonModifier.addActionListener(e -> { GUI_modifierNote frame = new GUI_modifierNote(matricule); });


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }




    /**
     * Remplissage des champs sur l'information du professeur connecté
     */
    private void remplirInformations()
    {
        labelNom.setText(
                PROFESSEUR.getPersonne(matricule, "Prenom") + " " +
                PROFESSEUR.getPersonne(matricule, "Nom").toUpperCase() );
        labelMatricule.setText( matricule );
        labelMatiere.setText( "I DONT KNOW");
    }




    private String[] columns = new String[]{ "Cours", "Code", "Groupe - ID","Groupe - Nom", "année" };
    private Object[][] DATA;
    private int CURSOR;

    /**
     * Remplissage des champs sur les cours dispensés du professeur connecté
     */
    private void remplirCours()
    {
        int nombreCours = PROFESSEUR.nombreCours(matricule);


        if(nombreCours == 0)
        {
            labelErreur.setVisible(true);
            coursTable.setVisible(false);
            coursPane.setVisible(false);
        }
        else
        {
            labelErreur.setVisible(false);
            coursTable.setVisible(true);
            coursPane.setVisible(true);

            ArrayList<String> listeCours = PROFESSEUR.getCoursArray(matricule, "Code");
            nombreCours = 0;
            for (String cours : listeCours)
                nombreCours += PROFESSEUR.nombreGroupeSuivantCours(cours) + 1;


            DATA = new Object[nombreCours][columns.length];


            CURSOR = 0;
            for (String cours : listeCours)
                remplirCoursGroupes(cours);


            DefaultTableModel model = new DefaultTableModel(DATA, columns);
            coursTable.setModel(model);
            centrerJTable(coursTable);
        }
    }


    /**
     * Remplissage des champs sur groupes suivant un cours dispensé par le professeur connecté
     * @param coursCode Code du cours en question
     */
    private void remplirCoursGroupes(String coursCode)
    {
        String NOM = PROFESSEUR.getCours(coursCode, "Nom");
        String CODE = PROFESSEUR.getCours(coursCode, "Code");
        String ANNEE = PROFESSEUR.getCours(coursCode, "Annee").substring(0,4);


        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                "FROM cours, suivre, groupe " +
                "WHERE cours.Code = " + coursCode + " " +
                "AND cours.Code = suivre.Code " +
                "AND suivre.Groupe_ID = groupe.Groupe_ID;";

        ResultSet resultat = database.run_Statement_READ(query);

        try
        {
            while ( resultat.next() )
            {
                DATA[CURSOR][0] = NOM;
                DATA[CURSOR][1] = CODE;
                DATA[CURSOR][2] = resultat.getString("groupe.Groupe_ID");
                DATA[CURSOR][3] = resultat.getString("groupe.Nom");;
                DATA[CURSOR][4] = ANNEE;
                CURSOR ++;
            }
            CURSOR++;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        database.Database_Deconnection();
    }
}
