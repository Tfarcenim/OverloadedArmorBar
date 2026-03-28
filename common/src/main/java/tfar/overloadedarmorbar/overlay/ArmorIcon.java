package tfar.overloadedarmorbar.overlay;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Class wraps the information required to draw an individual armor icon
 */
public class ArmorIcon
{
    public static final Pattern pattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");
    public Type armorIconType;

    /*
        Type = FULL, Type = NONE:
            The color of the icon
        Type = HALF:
            The color of the left hand side of the icon

     */
    public int primaryArmorIconColor;
    /*
        Type = HALF:
            The color of the right hand side of the icon
     */
    public int secondaryArmorIconColor;


    public ArmorIcon()
    {
        armorIconType = Type.NONE;
        primaryArmorIconColor = 0xff000000;
        secondaryArmorIconColor = 0xff000000;
    }

    /*
    Convert from #RRGGBB format.
    If string is not in correct format this function will set the color to black
 */
    public void setPrimaryColor(String colorHex)
    {
        //Check the color hex is valid otherwise default to white
        Matcher matcher = pattern.matcher(colorHex);
        if (matcher.matches()) {
            primaryArmorIconColor = 0xff000000|Integer.decode(colorHex);
        }
    }

    /*
Convert from #RRGGBB format.
If string is not in correct format this function will set the color to black
*/
    public void setSecondaryColor(String colorHex)
    {
        //Check the color hex is valid otherwise default to white
        Matcher matcher = pattern.matcher(colorHex);
        if (matcher.matches()) {
            secondaryArmorIconColor = 0xff000000|Integer.decode(colorHex);
        }
    }


    /*
        The type of armor icon to show.
     */
    public enum Type
    {
        NONE,
        HALF,
        FULL
    }
}
