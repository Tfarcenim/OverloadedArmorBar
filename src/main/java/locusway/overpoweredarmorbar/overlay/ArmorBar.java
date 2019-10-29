package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.Configs;

import java.util.List;

/*
    Class manages the calculations required to determine the correct color(s) to use
 */
public class ArmorBar
{
    private static void setArmorIconColor(ArmorIcon icon, List<? extends String> colors, int scale, int armorValue)
    {
        int currentScale = scale;
        int previousScale = scale - 1;

        //Force last color if we have run out of colors on the list
        if (currentScale > colors.size() - 1)
        {
            currentScale = colors.size() - 1;
        }
        if (previousScale > colors.size() - 1)
        {
            previousScale = colors.size() - 1;
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
            icon.primaryArmorIconColor.setColorFromHex(colors.get(currentScale));
        }

        //Covers 1 (HALF) - Secondary Color
        if (armorValue == 1)
        {
            //Should be previous tier color
            icon.secondaryArmorIconColor.setColorFromHex(colors.get(previousScale));
        }

        if (armorValue == 0)
        {
            //Should be previous tier color
            icon.primaryArmorIconColor.setColorFromHex(colors.get(previousScale));
        }
    }

    public static ArmorIcon[] calculateArmorIcons(int playerArmorValue)
    {
        ArmorIcon[] armorIcons = new ArmorIcon[10];

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
            armorIcons[i] = new ArmorIcon();
            setArmorIconColor(armorIcons[i], Configs.ClientConfig.colorValues.get(), scale, counter);
            if (counter >= 2)
            {
                //We have at least a full icon to show
                armorIcons[i].armorIconType = ArmorIcon.Type.FULL;
                counter -= 2;
            } else if (counter == 1)
            {
                //We have a half icon to show
                armorIcons[i].armorIconType = ArmorIcon.Type.HALF;
                counter -= 1;
            } else
            {
                //Empty icon
                armorIcons[i].armorIconType = ArmorIcon.Type.NONE;
            }
        }

        return armorIcons;
    }
}
