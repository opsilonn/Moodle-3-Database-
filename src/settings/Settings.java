package settings;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;


/**
 * Settings class is a central class used to store all values and classes related to setting's properties.
 * @author Hugues Begeot
 */
public class Settings
{
    public SettingsColor color = new SettingsColor();
    public Map<String, SettingsWindow> windowMap = new HashMap<>();

    private final static String FONT_NAME = "Georgia";
    private final static int FONT_STYLE = Font.PLAIN;
    private final static int FONT_SIZE = 14;
    public Font FONT;


    /**
     * Constructs a new <code>Settings</code> iteration.
     */
    public Settings()
    {
        FONT = new Font(FONT_NAME, FONT_STYLE, FONT_SIZE);
        // Windows Parameters
        Window_Fill();
    }

    /**
     * Fills the Map with final values for the label, length and width
     */
    private void Window_Fill()
    {
        windowMap.put("Default", new SettingsWindow("Default"));
        windowMap.put("Login", new SettingsWindow("Login"));
        windowMap.put("Main", new SettingsWindow("Main"));
    }
}