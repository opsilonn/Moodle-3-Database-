package GUI;


import GUI_Components.CustomJFrame;
import GUI_Components.CustomJTextField;
import UsefulFunctions.Database_Connection;

import javax.swing.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 * Fenêtre dédiée à la recherche d'un étudiant par le professeur connecté.
 * Seuls les étudiants qui suivent au moins un cours du professeur seront recherchables.
 * <p>
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_chercherEtudiant extends CustomJFrame {
    private static final int DIM_X = 450;
    private static final int DIM_Y = 600;


    private int matricule;
    private JPanel panel;
    private JTextField fieldInput;
    private JButton buttonRechercher;
    private JLabel labelErreur;
    private JPanel panelResult;
    private JLabel labelNom;
    private JLabel labelGroupe;
    private JLabel labelMatricule;
    private JTextArea labelCours;


    /**
     * Création de l'interface pour un Eleve
     *
     * @param matricule - Matricule de l'élève connecté
     */
    public GUI_chercherEtudiant(int matricule) {
        super("Professeur", false, DIM_X, DIM_Y);
        this.matricule = matricule;


        labelErreur.setVisible(false);
        panelResult.setVisible(false);
        labelCours.setEnabled(false);
        buttonRechercher.addActionListener(e -> chercherEtudiant());


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents() {
        fieldInput = new CustomJTextField("NUMERIC", false, 8);
    }


    /**
     * Récupère le matricule entré en input
     *
     * @return le matricule recherché
     */
    private String getInput() {
        String input = fieldInput.getText();

        if (input.length() == 0)
            return "-1";

        return input;
    }


    private boolean isFound;
    private String NOM;
    private String GROUPE;
    private ArrayList<String> COURS;

    /**
     * Lance la recherche d'un étudiant suivant un cours du professeur connecté dont le matricule correspond à l'input
     */
    private void chercherEtudiant() {
        Database_Connection database = new Database_Connection();

        String query =
                "SELECT * " +
                        "FROM personne, etudiant, groupe, suivre, cours, enseigner " +
                        "WHERE personne.ID = etudiant.ID_Personne " +
                        "AND etudiant.Matricule = " + getInput() + " " +
                        "AND etudiant.Groupe_ID = groupe.Groupe_ID " +
                        "AND etudiant.Groupe_ID = suivre.Groupe_ID " +
                        "AND suivre.Code = cours.Code " +
                        "AND cours.Code = enseigner.Code " +
                        "AND enseigner.Matricule_Prof = " + matricule + " ;";

        ResultSet resultSet = database.run_Statement_READ(query);

        try {
            isFound = false;
            if (resultSet.next()) {
                isFound = true;
                NOM = resultSet.getString("Prenom") + " " + resultSet.getString("Nom").toUpperCase();
                GROUPE = resultSet.getString("Groupe_ID") + " - " + resultSet.getString("groupe.Nom");
                COURS = new ArrayList<>();


                String query2 =
                        "SELECT * " +
                                "FROM suivre, cours, enseigner " +
                                "WHERE suivre.Groupe_ID = " + resultSet.getString("Groupe_ID") + " " +
                                "AND suivre.Code = cours.Code " +
                                "AND cours.Code = enseigner.Code " +
                                "AND enseigner.Matricule_Prof = " + matricule + " ;";

                ResultSet resultSet2 = database.run_Statement_READ(query2);
                try {
                    while (resultSet2.next())
                        COURS.add(
                                resultSet2.getString("cours.Code") + " - " +
                                        resultSet2.getString("cours.Nom") + "\n");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        afficherEtudiant();
        database.Database_Deconnection();
    }


    /**
     * Affiche les informations de l'élève recherché si trouvé (true), sinon un message d'erreur (false)
     */
    private void afficherEtudiant() {
        labelErreur.setVisible(!isFound);
        panelResult.setVisible(isFound);

        if (isFound) {
            labelNom.setText(NOM);
            labelMatricule.setText(getInput());
            labelGroupe.setText(GROUPE);

            if (COURS.size() == 0)
                labelCours.setText("Aucun cours avec vous... CE QUI NE DEVRAIT PAS ARRIVER");
            else {
                StringBuilder cours = new StringBuilder();
                for (String s : COURS)
                    cours.append(s);

                labelCours.setText(cours.toString());
            }
        }
    }
}