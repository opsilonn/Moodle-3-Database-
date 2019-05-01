package GUI;

import javax.swing.*;

public class GUI_Admin extends GUI_Components.CustomJFrame {

    private static final int DIM_X = 400;
    private static final int DIM_Y = 300;

    private JButton buttonSearch;
    private JButton buttonCreate;
    private JButton button3;
    private JButton etudiantButton;
    private JButton professeurButton;
    private JButton responsableButton;
    private JButton coursButton;
    private JPanel panelAdmin;
    private JPanel panelChoice;
    private JButton groupeButton;
    private JPanel panel;

    public GUI_Admin() {
        super("Bienvenue", true, DIM_X, DIM_Y);

        panelChoice.setVisible(false);
        buttonSearch.addActionListener(e -> action(1));

        etudiantButton.addActionListener(e-> action("etudiant"));
        professeurButton.addActionListener(e-> action("professeur"));
        responsableButton.addActionListener(e-> action("responsable"));

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }

    private void action(int mode) {
        panelAdmin.setVisible(false);
        panelChoice.setVisible(true);
    }

    private void action(String table){
        new GUI_chercherPersonne(table);
        dispose();
    }
}
