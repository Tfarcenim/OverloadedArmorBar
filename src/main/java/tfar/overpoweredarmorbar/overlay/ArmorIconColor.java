package tfar.overpoweredarmorbar.overlay;

import net.minecraft.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    Class representing the color of the armor icon
 */
public class ArmorIconColor
{
    public float Red;
    public float Blue;
    public float Green;
    public float Alpha;
    public static final Pattern pattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");



    public ArmorIconColor()
    {
        Red = Blue = Green = Alpha = 1.0f;
    }

    /*
        Convert from #RRGGBB format.
        If string is not in correct format this function will set the color to black
     */
    public void setColorFromHex(String colorHex)
    {
        //Check the color hex is valid otherwise default to white
        Matcher matcher = pattern.matcher(colorHex);
        if (matcher.matches())
        {
            Red = Integer.valueOf(colorHex.substring(1, 3), 16).floatValue() / 255;
            Green = Integer.valueOf(colorHex.substring(3, 5), 16).floatValue() / 255;
            Blue = Integer.valueOf(colorHex.substring(5, 7), 16).floatValue() / 255;
        }
    }
}
