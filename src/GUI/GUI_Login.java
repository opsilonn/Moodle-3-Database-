package GUI;


import GUI_Components.*;
import javax.swing.*;
import java.awt.*;
import java.util.Objects;


/**
 * Fenêtre permettant de se connecter au logiciel (version 4).
 *
 * Cette classe hérite de {@link CustomJFrame}
 *
 * @author Hugues
 */
public class GUI_Login extends CustomJFrame
{
    private static final int DIM_X = 500;
    private static final int DIM_Y = 500;

    private JPanel panel;
    private JPanel panelLogo;
    private JPanel panelInputs;
    private JPanel panelForm;

    private JLabel labelLogo;

    public JTextField fieldID;
    public JPasswordField fieldPassword;

    public JButton buttonLogin;
    public JLabel labelIncorrect;

    /**
     * Création de l'interface de login
     */
    public GUI_Login()
    {
        super("Login", true, DIM_X, DIM_Y);

        // Adds the logo image
        ImageIcon imageIcon = new ImageIcon(PATH_LOGO_FULL); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (DIM_X * 0.6), DIM_Y /3,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        labelLogo.setIcon(imageIcon);


        labelIncorrect.setVisible(false);
        buttonLogin.addActionListener( e -> loginVerifier() );


        add(panel);
        pack();
        revalidate();
        setVisible(true);
    }


    private void createUIComponents()
    {
        fieldID = new CustomJTextField("NUMERIC",  false, 8);
        fieldPassword = new CustomJTextField("ALL",  true, 20);
    }





    private void loginVerifier()
    {
        String ID = "12341234";
        String MDP = "azerty";

        String inputID = fieldID.getText();
        String inputMDP = String.valueOf( fieldPassword.getPassword() );
        String message;

        if( Objects.equals(ID, inputID) && Objects.equals(MDP, inputMDP) )
        {
            message = "Bien joué : bons inputs !!";
            labelIncorrect.setVisible(false);
        }
        else
        {
            message = "Dommage : mauvais inputs..";
            labelIncorrect.setVisible(true);
        }

        JOptionPane.showMessageDialog(this, message);
    }
}