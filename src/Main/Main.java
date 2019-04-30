package Main;


import GUI.*;
import javax.swing.*;


/**
 * HUB is the central class. F>rom here, we launch the software.
 * @author Hugues Begeot
 */
public class Main
{
    public static void main(String[] argv) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        GUI_Login Login = new GUI_Login();
    }
}