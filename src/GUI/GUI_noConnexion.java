package GUI;


import GUI_Components.CustomJFrame;
import javax.swing.*;


/**
 * Fenêtre affichant un message d'erreur lorsque l'on est pas connecté la BDD
 * <p>
 * Cette classe hérite de {@link GUI_Components.CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_noConnexion extends CustomJFrame
{
    private static final int DIM_X = 400;
    private static final int DIM_Y = 200;

    private JPanel panel;


    public GUI_noConnexion()
    {
        super("Pas de connection !", true, DIM_X, DIM_Y);

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }
}
