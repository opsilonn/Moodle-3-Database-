package GUI_Components;


import Gestion_admin.Database_Connection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;


/**
 * CustomJFrame is extending the JFrame class
 *  It contains various optimized prefabs for creating a JFrame, including a setting class iteration.
 * @author Hugues Begeot
 */
public abstract class CustomJFrame extends JFrame
{
    protected final static String PATH_LOGO = "./src/pictures/logo.png";
    protected final static String PATH_LOGO_FULL = "./src/pictures/logoFull.png";
    protected Database_Connection database;

    /**
     * default @CustomJFrame's constructor.
     * @deprecated We'll prefer using the other constructor
     * */
    @Deprecated
    public CustomJFrame()
    {
        setPreferredSize(new Dimension(500, 500));
    }


    /**
     * Regular @CustomJFrame's constructor.
     * <p>
     * @param title Type of the JFrame we want to create.
     * @param closeOnExit if true, the program is closed when we exit the JFrame
     * @param dimX width of the JFrame
     * @param dimY height of the JFrame
     * */
    protected CustomJFrame(String title, boolean closeOnExit, int dimX, int dimY)
    {
        try { setIconImage(ImageIO.read(new File(PATH_LOGO)) ); }
        catch (IOException e) { System.out.println("Icon not found"); }

        setTitle(title);
        setPreferredSize(new Dimension(dimX, dimY));
        setMinimumSize(new Dimension(dimX, dimY));

        //setResizable(false);
        setAlwaysOnTop(false);
        if(closeOnExit) setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }


    /**
     * Regular @CustomJFrame's constructor that includes the JFrame's size.
     * <p>
     * @param title Type of the JFrame we want to create.
     * @param closeOnExit if true, the program is closed when we exit the JFrame
     * @param database the connection to the database
     * @param dimX width of the JFrame
     * @param dimY height of the JFrame
     * */
    protected CustomJFrame(String title, boolean closeOnExit, Database_Connection database, int dimX, int dimY)
    {
        try { setIconImage(ImageIO.read(new File(PATH_LOGO)) ); }
        catch (IOException e) { System.out.println("Icon not found"); }

        setTitle(title);
        setPreferredSize(new Dimension(dimX, dimY));
        setMinimumSize(new Dimension(dimX, dimY));

        this.database = database;

        setAlwaysOnTop(false);
        if(closeOnExit) setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
}