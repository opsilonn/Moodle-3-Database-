package GUI;


import GUI_Components.BEGEOT_BUNOUF_CustomJFrame;

import javax.swing.*;


/**
 * Fenêtre affichant un message d'erreur lorsque l'on est pas connecté la BDD
 * <p>
 * Cette classe hérite de {@link BEGEOT_BUNOUF_CustomJFrame}
 *
 * @author Hugues
 */
public class BEGEOT_BUNOUF_GUI_noConnexion extends BEGEOT_BUNOUF_CustomJFrame
{
    private static final int DIM_X = 400;
    private static final int DIM_Y = 200;

    private JPanel panel;


    public BEGEOT_BUNOUF_GUI_noConnexion()
    {
        super("Pas de connection !", true, DIM_X, DIM_Y);

        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }
}
