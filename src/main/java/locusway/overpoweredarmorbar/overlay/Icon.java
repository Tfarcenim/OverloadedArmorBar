package locusway.overpoweredarmorbar.overlay;

/*
    Class wraps the information required to draw an individual armor icon
 */
public class Icon
{
    public Type iconType;

    /*
        Type = FULL, Type = NONE:
            The color of the icon
        Type = HALF:
            The color of the left hand side of the icon

     */
    public IconColor primaryIconColor;
    /*
        Type = HALF:
            The color of the right hand side of the icon
     */
    public IconColor secondaryIconColor;


    public Icon()
    {
        iconType = Type.NONE;
        primaryIconColor = new IconColor();
        secondaryIconColor = new IconColor();
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
