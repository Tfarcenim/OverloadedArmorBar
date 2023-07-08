package tfar.overpoweredarmorbar.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attributes;
import tfar.overpoweredarmorbar.ConfigurationHandler;

/*
    Class which handles the render event and hides the vanilla armor bar
 */
public class OverlayEventHandler {

    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    public static void drawTexturedModalRect(GuiGraphics stack, int x, int y, int textureX, int textureY, int width, int height) {
        stack.blit(GUI_ICONS_LOCATION,x, y, textureX, textureY, width, height);
    }

    public OverlayEventHandler() {
    }

    /*
        Class handles the drawing of the armor bar
     */
    private final static int UNKNOWN_ARMOR_VALUE = -1;
    private static int previousArmorValue = UNKNOWN_ARMOR_VALUE;
    private final static int ARMOR_ICON_SIZE = 9;
    //private final static int ARMOR_FIRST_HALF_ICON_SIZE = 5;
    private final static int ARMOR_SECOND_HALF_ICON_SIZE = 4;

    private static final Minecraft mc = Minecraft.getInstance();
    private static ArmorIcon[] armorIcons;

    // @SubscribeEvent(receiveCanceled = true)
    
    public static void onRenderGameOverlayEventPre(GuiGraphics event) {
        int scaledWidth = mc.getWindow().getGuiScaledWidth();
        int scaledHeight = mc.getWindow().getGuiScaledHeight();
        renderArmorBar(event,scaledWidth,scaledHeight);
    }
    //account for ISpecialArmor, seems to be missing in 1.13+ forge
    private static int calculateArmorValue() {
        return (int)mc.player.getAttribute(Attributes.ARMOR).getValue();
    }

    public static void renderArmorBar(GuiGraphics stack,int screenWidth, int screenHeight) {
        int currentArmorValue = calculateArmorValue();
        int xStart = screenWidth / 2 - 91;
        int yPosition = getYOffset(screenHeight); // ForgeIngameGui.left_height - this doesn't exist on Fabric as Fabric doesn't replace the rendering code;

        //Save some CPU cycles by only recalculating armor when it changes
        if (currentArmorValue != previousArmorValue) {
            //Calculate here
            armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);

            //Save value for next cycle
            previousArmorValue = currentArmorValue;
        }

        int armorIconCounter = 0;
        for (ArmorIcon icon : armorIcons) {
            int xPosition = xStart + armorIconCounter * 8;
            switch (icon.armorIconType) {
                case NONE:
                    ArmorIconColor color = icon.primaryArmorIconColor;
                    color4f(color.Red, color.Green, color.Blue, color.Alpha);
                    if (currentArmorValue > 20) {
                        //Draw the full icon as we have wrapped
                        drawTexturedModalRect(stack,xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    } else {
                        if (ConfigurationHandler.showEmptyArmorIcons() && (ConfigurationHandler.alwaysShowArmorBar() || currentArmorValue > 0)) {
                            //Draw the empty armor icon
                            drawTexturedModalRect(stack,xPosition, yPosition, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                        }
                    }
                    break;
                case HALF:
                    ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
                    ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

                    color4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
                    drawTexturedModalRect(stack,xPosition, yPosition, 25, 9, 5, ARMOR_ICON_SIZE);

                    color4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, secondHalfColor.Alpha);
                    if (currentArmorValue > 20) {
                        //Draw the second half as full as we have wrapped
                        drawTexturedModalRect(stack,xPosition + 5, yPosition, 39, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
                    } else {
                        //Draw the second half as empty
                        drawTexturedModalRect(stack,xPosition + 5, yPosition, 30, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
                    }
                    break;
                case FULL:
                    ArmorIconColor fullColor = icon.primaryArmorIconColor;
                    color4f(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
                    drawTexturedModalRect(stack,xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                    break;
                default:
                    break;
            }
            armorIconCounter++;
        }

        color4f(1, 1, 1, 1);
    }

    private static int getYOffset(int screenH) {
        int y = screenH - 39;

        double maxHealth = ConfigurationHandler.offset() ? mc.player.getAttributeValue(Attributes.MAX_HEALTH) : 20;
        int absorption = ConfigurationHandler.offset() ? Mth.ceil(mc.player.getAbsorptionAmount()) : 0;
        int offsetRows = Mth.ceil((maxHealth + absorption) / 2.0f / 10.0f);
        int r = Math.max(10 - (offsetRows - 2), 3);
        int s = y - (offsetRows - 1) * r - 10;

        return s;
    }

    private static void color4f(float r, float g, float b, float a) {
        RenderSystem.setShaderColor(r,g, b, a);
    }
}