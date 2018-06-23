package locusway.overpoweredarmorbar.overlay;

/*
    Class manages the calculations required to determine the correct color(s) to use
 */
public class IconStateCalculator
{
    private static void setIconColor(Icon icon, String[] colors, int scale, int value)
    {
        int currentScale = scale;
        int previousScale = scale - 1;

        //Ensure we always have a color on the list
        if(colors.length == 0)
        {
            colors = new String[] {"#FFFFFF"};
        }

        //Force last color if we have run out of colors on the list
        if (currentScale > colors.length - 1)
        {
            currentScale = colors.length - 1;
        }
        if(previousScale > colors.length - 1)
        {
            previousScale = colors.length - 1;
        }

        //Previous scale is -1 between 0 and 20 points of armor, so reset to 0 for sane value
        if (previousScale < 0)
        {
            previousScale = 0;
        }

        //Covers 2 (FULL) and 1 (HALF) - Primary Color
        if (value >= 1)
        {
            //Should be current tier color
            icon.primaryIconColor.setColorFromHex(colors[currentScale]);
        }

        //Covers 1 (HALF) - Secondary Color
        if (value == 1)
        {
            //Should be previous tier color
            icon.secondaryIconColor.setColorFromHex(colors[previousScale]);
        }

        if (value == 0)
        {
            //Should be previous tier color
            icon.primaryIconColor.setColorFromHex(colors[previousScale]);
        }
    }

    public static Icon[] calculateIcons(int playerArmorValue, String[] colors)
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
            setIconColor(icons[i], colors, scale, counter);
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
                if(scale > 0)
                {
                    //If scale is greater than 1 we have wrapped so we should use a full heart
                    icons[i].iconType = Icon.Type.FULL;
                }
                else
                {
                    //Empty icon
                    icons[i].iconType = Icon.Type.NONE;
                }
            }
        }

        return icons;
    }
}
