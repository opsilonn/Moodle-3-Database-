package settings;


import java.awt.*;
import java.util.*;


/**
 * Settings_Color class is used to store all values about background and foreground of a window,
 * whether Lightmode or Darkmode.
 * @author Hugues Begeot
 */
public class SettingsColor
{
    public ArrayList<Component> componentsBackground = new ArrayList<>();
    public ArrayList<Component> componentsForeground = new ArrayList<>();

    private static final Color BACKGROUND_LIGHT = new Color(200,200,200);
    private static final Color BACKGROUND_DARK  = new Color(55,55,55);
    private static final Color FOREGROUND_LIGHT = new Color(55,55,55);
    private static final Color FOREGROUND_DARK  = new Color(200,200,200);
    private boolean isLight = true;


    /**
     * Returns the color needed for the background accordingly to the current value of isLight
     * @return the color to be displayed in the background
     */
    private Color getBackground()
    {
        if(isLight) return BACKGROUND_LIGHT;
        return BACKGROUND_DARK;
    }
    /**
     * Returns the color needed for the foreground accordingly to the current value of isLight
     * @return the color to be displayed in the foreground
     */
    private Color getForeground()
    {
        if(isLight) return FOREGROUND_LIGHT;
        return FOREGROUND_DARK;
    }


    /**
     * Sets the Background color of the JFrame (either Light or Night mode)
     * <p>
     * */
    public void updateColors()
    {
        for(Component p : componentsBackground) p.setBackground(getBackground());
        for(Component p : componentsForeground) p.setForeground(getForeground());
    }
}