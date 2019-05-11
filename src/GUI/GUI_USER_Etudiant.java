package GUI;


import GUI_Components.CustomJFrame;
import UsefulFunctions.Database_Connection;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static UsefulFunctions.CountRows_TableCell.createModel;
import static recherche.Recherche.getPersonne;
import static recherche.RechercheEtudiant.*;


/**
 * Fenêtre dédiée à l'utilisation du logiciel par un Eleve
 * <p>
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
class GUI_USER_Etudiant extends CustomJFrame {
    private static final int DIM_X = 800;
    private static final int DIM_Y = 600;

    private int matricule;

    private JPanel panel;

    private JLabel labelNom;
    private JLabel labelMatricule;
    private JLabel labelGroupe;
    private JLabel labelMoyenne;

    private JButton buttonBulletin;

    private JLabel labelErreur;

    private JScrollPane bulletin;
    private JTable bulletinValeurs;
    private JButton buttonCours;


    private String[] columns = new String[]{"Matière", "Code - Matière", "Moyenne", "Type", "Note", "Coefficient", "ID-Note"};
    private Object[][] DATA;
    private int CURSOR;
    private final static int SIZE_OCCUPIED_BY_COURSE = 4;

    /**
     * Création de l'interface pour un Eleve
     *
     * @param matricule - Matricule de l'élève connecté
     */
    public GUI_USER_Etudiant(int matricule)
    {
        super("Etudiant", true, DIM_X, DIM_Y);
        this.matricule = matricule;


        remplirInformations();
        remplirNotes();

        buttonCours.addActionListener(e -> {
            GUI_consulterCours frame = new GUI_consulterCours();
        });
        buttonBulletin.addActionListener(e -> bulletin());


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    /**
     * Remplissage des champs sur l'information de l'étudiant connecté
     */
    private void remplirInformations() {
        // ON AFFICHE LE PRENOM + NOM
        labelNom.setText(
                getPersonne(matricule, "etudiant", "Prenom") + " " +
                        getPersonne(matricule, "etudiant", "Nom").toUpperCase());


        // ON AFFICHE LE MATRICULE
        labelMatricule.setText(matricule + "");


        // ON AFFICHE LE GROUPE (si aucun trouvé, on affiche "aucun groupe")
        if (possedeGroupe(matricule)) {
            labelGroupe.setText(
                    getGroupe(matricule, "Groupe_ID") + " - " +
                            getGroupe(matricule, "Nom"));
        } else {
            labelGroupe.setText("N'appartient à aucun Groupe");
        }


        // ON AFFICHE LA MOYENNE (si aucun trouvé, on affiche "indéfinie")
        float moyenneGenerale = moyenneGenerale(matricule);
        if (moyenneGenerale == -1)
            labelMoyenne.setText("indéfinie");
        else
            labelMoyenne.setText(String.valueOf(moyenneGenerale));
    }


    /**
     * Remplissage des champs sur les notes de l'étudiant connecté
     */
    private void remplirNotes() {
        String query;
        ResultSet resultat;
        CURSOR = 0;

        int nombreCours = nombreCoursEtudiant(matricule);
        System.out.println(nombreCours);

        if (nombreCours == 0) {
            bulletin.setVisible(false);
            bulletinValeurs.setVisible(false);
        } else {
            labelErreur.setVisible(false);
            DATA = new Object[nombreCours * SIZE_OCCUPIED_BY_COURSE][columns.length];

            Database_Connection database = new Database_Connection();
            query =
                    "SELECT * " +
                            "FROM etudiant, groupe, cours, suivre " +
                            "WHERE etudiant.Matricule = " + matricule + " " +
                            "AND etudiant.Groupe_ID = groupe.Groupe_ID " +
                            "AND groupe.Groupe_ID = suivre.Groupe_ID " +
                            "AND suivre.Code = cours.Code;";

            resultat = database.run_Statement_READ(query);


            try {
                while (resultat.next()) {
                    int coursCode = resultat.getInt("cours.Code");
                    String coursNom = resultat.getString("cours.Nom");
                    String coursCoef = resultat.getString("cours.Coefficient");

                    Map<String, String> coefficients = new HashMap<>();
                    coefficients.put("TP", resultat.getString("cours.TP_Pourcentage"));
                    coefficients.put("DE", resultat.getString("cours.DE_Pourcentage"));
                    coefficients.put("PROJET", resultat.getString("cours.Projet_Pourcentage"));

                    remplirNotesCours(coursCode, coursNom, coursCoef, coefficients);
                }

                bulletinValeurs.setModel(createModel(DATA, columns));
                centrerJTable(bulletinValeurs);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            database.Database_Deconnection();
        }
    }


    /**
     * Remplissage des champs sur les notes de l'étudiant connecté sur une matière spécifique
     *
     * @param coursCode    Code du cours en question
     * @param coursNom     Code du cours en question
     * @param coefficients Tableau des coefficients des notes du cours en question
     */
    private void remplirNotesCours(int coursCode, String coursNom, String coursCoef, Map<String, String> coefficients)
    {
        String TP_note = "Pas de note";
        String DE_note = "Pas de note";
        String PROJET_note = "Pas de note";

        String TP_ID = "";
        String DE_ID = "";
        String PROJET_ID = "";

        Database_Connection database = new Database_Connection();
        String query =
                "SELECT * " +
                "FROM note " +
                "WHERE note.Code = " + coursCode + " " +
                "AND note.Matricule_Etudiant = " + matricule + " ;";

        ResultSet resultat = database.run_Statement_READ(query);

        try
        {
            while (resultat.next())
            {
                String note = resultat.getString("note.Valeur");
                String noteID = resultat.getString("note.ID");

                switch (resultat.getString("note.Type")) {
                    case "TP":
                        TP_note = note;
                        TP_ID = noteID;
                        break;

                    case "DE":
                        DE_note = note;
                        DE_ID = noteID;
                        break;

                    case "Projet":
                        PROJET_note = note;
                        PROJET_ID = noteID;
                        break;

                    default:
                        System.out.println("That's weird");
                        break;
                }
            }


            DATA[CURSOR + 1][0] = coursNom;
            DATA[CURSOR + 1][1] = coursCode;
            float moyenne = moyenne(matricule, coursCode);
            if (moyenne == -1)
                DATA[CURSOR + 1][2] = "indéfinie";
            else
                DATA[CURSOR + 1][2] = moyenne;

            DATA[CURSOR + 2][0] = "coef : " + coursCoef;


            DATA[CURSOR][3] = "TP";
            DATA[CURSOR + 1][3] = "DE";
            DATA[CURSOR + 2][3] = "Projet";

            DATA[CURSOR][4] = TP_note;
            DATA[CURSOR + 1][4] = DE_note;
            DATA[CURSOR + 2][4] = PROJET_note;

            DATA[CURSOR][5] = coefficients.get("TP");
            DATA[CURSOR + 1][5] = coefficients.get("DE");
            DATA[CURSOR + 2][5] = coefficients.get("PROJET");

            DATA[CURSOR][6] = TP_ID;
            DATA[CURSOR + 1][6] = DE_ID;
            DATA[CURSOR + 2][6] = PROJET_ID;

            CURSOR += SIZE_OCCUPIED_BY_COURSE;
        } catch (SQLException e1) {
            e1.printStackTrace();
        }

        database.Database_Deconnection();
    }


    /**
     * Affichage du bulletin officiel du Bulletin de l'élève
     */
    private void bulletin()
    {
        createBulletin();
        /*if (Objects.equals(ETUDIANT.getGroupe(matricule, "Bulletin"), "1")) {
            JOptionPane.showMessageDialog(this, "Votre Bulletin est fini !");
        } else
            JOptionPane.showMessageDialog(this, "Votre Bulletin n'est pas encore fini !");*/
    }


    private void createBulletin()
    {
        String name = "Bulletin_" + matricule + ".pdf";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int index = labelGroupe.getText().indexOf(" ");
        int groupeID = Integer.parseInt(labelGroupe.getText().substring(0, index));
        System.out.println("GROUPE IF+D : " + groupeID);


        Document PDF;
        PdfWriter MyWriter;

        FileOutputStream PDFOutputStream = null;
        PDF = new Document(PageSize.A4, 50, 50, 50, 50);

        try {
            PDFOutputStream = new FileOutputStream(name);
            MyWriter = PdfWriter.getInstance(PDF, PDFOutputStream);
            PDF.open();

            PDF.add(new Paragraph("Bulletin de l'étudiant",
                    new Font(Font.TIMES_ROMAN, 22, Font.BOLD, Color.BLACK)));


            PDF.add(new Paragraph(labelNom.getText() + " - " + labelMatricule.getText()));
            PDF.add(new Paragraph("Année " + year + " - Groupe " + labelGroupe.getText()));

            //PdfPTable table = new PdfPTable(8);

            Table tableau = new Table(4, 1);
            tableau.setAlignment(Element.ALIGN_CENTER);
            tableau.addCell(createCell("Cours", true));
            tableau.addCell(createCell("Moyenne", true));
            tableau.addCell(createCell("Moyenne minimum", true));
            tableau.addCell(createCell("Moyenne maximum", true));
            tableau.endHeaders();

            //Rechercher les cours suivis par le groupe auquel appartient l'étudiant
            ArrayList<Integer> cours = CoursfollowedByGroup(groupeID);

            for (int j = 0; j < cours.size(); j++) {
                tableau.addCell(createCell(getCours(cours.get(j), "Nom"), false));
                //Affichage de la moyenne de l'étudiant dans le cours.
                tableau.addCell(createCell(formatMoy(moyenne(matricule, cours.get(j))), false));
                //Affichage de la moyenne minimum des élèves dans le cours.
                tableau.addCell(createCell(formatMoy(moyenneMinMax(groupeID, false, cours.get(j))), false));
                //Affichage de la moyenne maximum des élèves dans le cours.
                tableau.addCell(createCell(formatMoy(moyenneMinMax(groupeID, true, cours.get(j))), false));
            }


            tableau.addCell(createCell("MOYENNE GENERALE ", false));
            //Affichage de la moyenne générale de l'étudiant.
            tableau.addCell(createCell(formatMoy(moyenneGenerale(matricule)), false));
            //Affichage de la moyenne minimum des élèves dans le cours.
            tableau.addCell(createCell(formatMoy(moyenneGeneraleMinMax(groupeID, false)), false));
            //Affichage de la moyenne maximum des élèves dans le cours.
            tableau.addCell(createCell(formatMoy(moyenneGeneraleMinMax(groupeID, true)), false));


            //Ajouter le tableau au PDF
            PDF.add(tableau);

            Cell cell = new Cell(new Paragraph("Signature du Responsable"));
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            PDF.add(cell);


            PDF.close();
            PDFOutputStream.close();

            Desktop.getDesktop().open(new File(name));


        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private String formatMoy(float moy) {
        if (moy == -1) {
            return "NaN";
        }
        return "" + moy;
    }

    private Cell createCell(String text, boolean header) {
        Cell cell = new Cell(text);
        if (header) {
            cell.setHeader(true);
        }
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
}
