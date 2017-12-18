package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.opengl.GL11;

/*
    Class handles the drawing of the armor bar
 */
public class ArmorBarRenderer extends Gui
{
    private int previousArmorValue = -1;

    private final static int BAR_HEIGHT = 9;
    private final static int ARMOR_ICON_SIZE = 9;
    private final static int ARMOR_FIRST_HALF_ICON_SIZE = 5;
    private final static int ARMOR_SECOND_HALF_ICON_SIZE = 4;
    private final static int BAR_SPACING_ABOVE_EXP_BAR = 2 + BAR_HEIGHT;  // pixels between the Armor bar and the XP bar

    private Minecraft mc;
    private ArmorIcon[] armorIcons;

    public ArmorBarRenderer(Minecraft mc)
    {
        this.mc = mc;
    }

    public void renderArmorBar(int screenWidth, int screenHeight)
    {
        int currentArmorValue = mc.player.getTotalArmorValue();

        //Hide armor bar if player is not wearing armor unless they have config requesting it
        if (currentArmorValue == 0 && !Config.alwaysShowArmorBar)
        {
            return;
        }

        //Save some CPU cycles by only recalculating armor when it changes
        if (currentArmorValue != previousArmorValue)
        {
            //Calculate here
            armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);

            //Save value for next cycle
            previousArmorValue = currentArmorValue;
        }

        // we will draw the status bar just above the hotbar.
        //  obtained by inspecting the vanilla hotbar rendering code
        final int vanillaExpLeftX = screenWidth / 2 - 91; // leftmost edge of the experience bar
        final int vanillaExpTopY = screenHeight - 32 + 3;  // top of the experience bar

        //Make sure we don't leak opengl state
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        /* Shift our rendering origin to just above the experience bar
         * The top left corner of the screen is x=0, y=0
         */
        GL11.glTranslatef(vanillaExpLeftX, vanillaExpTopY - BAR_SPACING_ABOVE_EXP_BAR - BAR_HEIGHT, 0);

        int positionCounter = 0;
        for (ArmorIcon icon : armorIcons)
        {
            switch (icon.armorIconType)
            {
                case NONE:
                    ArmorIconColor color = icon.primaryArmorIconColor;
                    GL11.glColor4f(color.Red, color.Green, color.Blue, color.Alpha);
                    if (currentArmorValue > 20)
                    {
                        //Draw the full icon as we have wrapped
                        drawTexturedModalRect(positionCounter, 0, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    }
                    else
                    {
                        if(Config.showEmptyArmorIcons)
                        {
                            //Draw the empty armor icon
                            drawTexturedModalRect(positionCounter, 0, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                        }
                    }
                    break;
                case HALF:
                    ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
                    ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

                    GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
                    drawTexturedModalRect(positionCounter, 0, 25, 9, 5, ARMOR_ICON_SIZE);

                    GL11.glColor4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, secondHalfColor.Alpha);
                    if (currentArmorValue > 20)
                    {
                        //Draw the second half as full as we have wrapped
                        drawTexturedModalRect(positionCounter + 5, 0, 34 + 5, 9, ARMOR_FIRST_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
                    }
                    else
                    {
                        //Draw the second half as empty
                        drawTexturedModalRect(positionCounter + 5, 0, 25 + 5, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
                    }
                    break;
                case FULL:
                    ArmorIconColor fullColor = icon.primaryArmorIconColor;
                    GL11.glColor4f(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
                    drawTexturedModalRect(positionCounter, 0, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    break;
                default:
                    break;
            }
            positionCounter += 8;
        }

        //Revert our state back
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }
}