package locusway.overpoweredarmorbar.overlay;

/*
    Class representing the color of the armor icon
 */
public class ArmorIconColor
{
    public float Red;
    public float Blue;
    public float Green;
    public float Alpha;

    public ArmorIconColor()
    {
        Red = Blue = Green = Alpha = 1.0f;
    }

    public void setColorFromHex(String colorHex)
    {
        Red = Integer.valueOf(colorHex.substring(1, 3), 16).floatValue() / 255;
        Green = Integer.valueOf(colorHex.substring(3, 5), 16).floatValue() / 255;
        Blue = Integer.valueOf(colorHex.substring(5, 7), 16).floatValue() / 255;
    }
}
