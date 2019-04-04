package Windows;


import javax.swing.*;
import GUI.*;
import java.awt.*;


public class Window_Login  extends CustomJFrame
{
    private JPanel panelTotal;
    private JButton buttonLogin;
    private CustomJTextField fieldID;
    private CustomJTextField fieldPassword;
    private JLabel labelPassword;
    private JLabel labelID;
    private JPanel panelFields;
    private JPanel panelInputs;
    private JPanel panelLogo;
    private JLabel labelLogo;


    public Window_Login(String titre)
    {
        super(titre);

        // Mettre le logo en haut de la fenêtre, et le redimensionner
        ImageIcon imageIcon = new ImageIcon(pathLogoFull); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance((int) (settings.windowMap.get("Login").dimX * 0.3), settings.windowMap.get("Login").dimY/3,  Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        labelLogo.setIcon(imageIcon);

        settings.color.componentsBackground.add(panelTotal);
        settings.color.componentsBackground.add(panelLogo);
        settings.color.componentsBackground.add(panelFields);
        settings.color.componentsBackground.add(panelInputs);
        settings.color.componentsForeground.add(labelID);
        settings.color.componentsForeground.add(labelPassword);
        settings.color.updateColors();

        labelID.setFont( settings.FONT);
        labelPassword.setFont(settings.FONT);

        buttonLogin.addActionListener(e -> System.out.println(fieldID.Field() + " " + fieldPassword.Field()) );

        add(panelTotal);
        pack();
        revalidate();
        setVisible(true);
    }

    private void createUIComponents()
    {
        fieldID = new CustomJTextField("ALPHA_NUMERIC", false, 20, 15,0,5);
        fieldPassword = new CustomJTextField("ALL", true, 20, 15,0,5);
    }
}
