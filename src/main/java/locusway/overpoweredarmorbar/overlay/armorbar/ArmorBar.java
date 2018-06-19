package locusway.overpoweredarmorbar.overlay.armorbar;

import locusway.overpoweredarmorbar.ModConfig;
import locusway.overpoweredarmorbar.overlay.Icon;

/*
    Class manages the calculations required to determine the correct color(s) to use
 */
public class ArmorBar
{
    private static void setArmorIconColor(Icon icon, String[] colors, int scale, int armorValue)
    {
        int currentScale = scale;
        int previousScale = scale - 1;

        if (currentScale > colors.length - 1)
        {
            currentScale = colors.length - 1;
        }

        //Previous scale is -1 between 0 and 20 points of armor, so reset to 0 for sane value
        if (previousScale < 0)
        {
            previousScale = 0;
        }

        //Covers 2 (FULL) and 1 (HALF) - Primary Color
        if (armorValue >= 1)
        {
            //Should be current tier color
            icon.primaryIconColor.setColorFromHex(colors[currentScale]);
        }

        //Covers 1 (HALF) - Secondary Color
        if (armorValue == 1)
        {
            //Should be previous tier color
            icon.secondaryIconColor.setColorFromHex(colors[previousScale]);
        }

        if (armorValue == 0)
        {
            //Should be previous tier color
            icon.primaryIconColor.setColorFromHex(colors[previousScale]);
        }
    }

    public static Icon[] calculateArmorIcons(int playerArmorValue)
    {
        Icon[] icons = new Icon[10];

        //Calculate which color scale to use
        int scale = playerArmorValue / 20;

        //Scale the value down for each position
        int counter = playerArmorValue - (scale * 20);

        //Handle exact wrap around situation
        if(scale > 0 && counter == 0)
        {
            //Show we are maxed out at previous scale
            scale -= 1;
            counter = 20;
        }

        for (int i = 0; i < 10; i++)
        {
            icons[i] = new Icon();
            setArmorIconColor(icons[i], ModConfig.armorColorValues, scale, counter);
            if (counter >= 2)
            {
                //We have at least a full icon to show
                icons[i].iconType = Icon.Type.FULL;
                counter -= 2;
            } else if (counter == 1)
            {
                //We have a half icon to show
                icons[i].iconType = Icon.Type.HALF;
                counter -= 1;
            } else
            {
                //Empty icon
                icons[i].iconType = Icon.Type.NONE;
            }
        }

        return icons;
    }
}
