package Main;


import Windows.*;
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
        Window_Login Login = new Window_Login("Login");
        System.out.println(Login);
    }
}