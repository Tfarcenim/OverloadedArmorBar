package tfar.overpoweredarmorbar.overlay;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import tfar.overpoweredarmorbar.ConfigurationHandler;
import tfar.overpoweredarmorbar.RenderGameOverlayEvent;

/*
    Class which handles the render event and hides the vanilla armor bar
 */
public class OverlayEventHandler {
    public static void drawTexturedModalRect(MatrixStack stack,int x, int y, int textureX, int textureY, int width, int height) {
        MinecraftClient.getInstance().inGameHud.drawTexture(stack,x, y, textureX, textureY, width, height);
    }

    public OverlayEventHandler() {
    }

    /*
        Class handles the drawing of the armor bar
     */
    private final static int UNKNOWN_ARMOR_VALUE = -1;
    private int previousArmorValue = UNKNOWN_ARMOR_VALUE;
    private final static int ARMOR_ICON_SIZE = 9;
    //private final static int ARMOR_FIRST_HALF_ICON_SIZE = 5;
    private final static int ARMOR_SECOND_HALF_ICON_SIZE = 4;

    private MinecraftClient mc = MinecraftClient.getInstance();
    private ArmorIcon[] armorIcons;

    // @SubscribeEvent(receiveCanceled = true)
    
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.ARMOR)
            return;
        int scaledWidth = mc.getWindow().getScaledWidth();
        int scaledHeight = mc.getWindow().getScaledHeight();
        renderArmorBar(event.getMatrixStack(),scaledWidth,scaledHeight);
        /* Don't render the vanilla armor bar */
        event.setCanceled(true);
    }

    private int calculateArmorValue() {
        return (int)mc.player.getAttributeValue(EntityAttributes.GENERIC_ARMOR);
    }

    public void renderArmorBar(MatrixStack stack,int screenWidth, int screenHeight) {
        int currentArmorValue = calculateArmorValue();
        int xStart = screenWidth / 2 - 91;
        int yPosition = screenHeight - 49; // ForgeIngameGui.left_height - this doesn't exist on Fabric as Fabric doesn't replace the rendering code;

        //Save some CPU cycles by only recalculating armor when it changes
        if (currentArmorValue != previousArmorValue) {
            //Calculate here
            armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);

            //Save value for next cycle
            previousArmorValue = currentArmorValue;
        }

        //Push to avoid lasting changes
        RenderSystem.pushMatrix();
        //GlStateManager.enableBlend();

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

        //Revert our state back
        color4f(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    private static void color4f(float r, float g, float b, float a){
        RenderSystem.color4f(r,g, b, a);
    }
}