package settings;


/**
 * Settings_Window class is used to store all measurements of a window, and its label.
 * @author Hugues Begeot
 */
public class SettingsWindow
{
    public String name;
    public int dimX;
    public int dimY;

    private static final String DEFAULT_NAME = "Moodle 3 - Default Page";
    private static final int DEFAULT_DIMX = 300;
    private static final int DEFAULT_DIMY = 300;

    private static final String LOGIN_NAME = "Moodle 3 - Login Page";
    private static final int LOGIN_DIMX = 450;
    private static final int LOGIN_DIMY = 400;

    private static final String MAIN_NAME = "Moodle 3 - Main Page";
    private static final int MAIN_DIMX = 600;
    private static final int MAIN_DIMY = 600;


    /**
     * Constructs a new <code>Settings_Window</code> iteration,
     * with default values ("empty" for the Strings, 0 for the integers).
     */
    SettingsWindow(String type)
    {
        switch(type)
        {
            case "Login":
                name = LOGIN_NAME;
                dimX = LOGIN_DIMX;
                dimY = LOGIN_DIMY;
                break;

            case "Main":
                name = MAIN_NAME;
                dimX = MAIN_DIMX;
                dimY = MAIN_DIMY;
                break;

            default:
                name = DEFAULT_NAME;
                dimX = DEFAULT_DIMX;
                dimY = DEFAULT_DIMY;
                break;
        }
    }

    /**
     * Constructs a new <code>Settings_Window</code> iteration,
     * with values given as parameters
     * <p>
     * @param name Name of the Window
     * @param dimX Length of the Window
     * @param dimY Width of the Window
     * @deprecated We'll prefer to use the other constructor, since he's supposed to have all correct values
     */
    SettingsWindow(String name, int dimX, int dimY)
    {
        this.name = name;
        this.dimX = dimX;
        this.dimY = dimY;
    }


    /**
     * Creates a textual representation of the class's instance
     * @return Textual representation of the instance
     */
    public String toString()
    {
        return String.format(
                "Window #%s : Length : %d, Width : %d",
                name,
                dimX,
                dimY
        );
    }
}
