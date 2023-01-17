package tfar.overpoweredarmorbar.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import tfar.overpoweredarmorbar.Configs;

/*
    Class which handles the render event and hides the vanilla armor bar
 */
public class OverlayEventHandler implements IGuiOverlay {
    public static void drawTexturedModalRect(PoseStack stack, int x, int y, int textureX, int textureY, int width, int height) {
        Minecraft.getInstance().gui.blit(stack,x, y, textureX, textureY, width, height);
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

    private static final Minecraft mc = Minecraft.getInstance();
    private ArmorIcon[] armorIcons;


    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        if (!gui.getMinecraft().options.hideGui && gui.shouldDrawSurvivalElements()) {
            gui.setupOverlayRenderState(true, false);
            renderArmorBar(gui, poseStack, width, height);
        }
    }
    //account for ISpecialArmor, seems to be missing in 1.13+ forge
    private int calculateArmorValue() {
        int currentArmorValue = mc.player.getArmorValue();

    /*    for (ItemStack itemStack : mc.player.getArmorInventoryList()) {
            if (itemStack.getItem() instanceof ISpecialArmor) {
                ISpecialArmor specialArmor = (ISpecialArmor) itemStack.getItem();
                currentArmorValue += specialArmor.getArmorDisplay(mc.player, itemStack, 0);
            }
        }
      */  return currentArmorValue;
    }

    public void renderArmorBar(ForgeGui gui, PoseStack stack, int screenWidth, int screenHeight) {
        int currentArmorValue = calculateArmorValue();
        int xStart = screenWidth / 2 - 91;
        int yPosition = screenHeight - gui.leftHeight;

        //Save some CPU cycles by only recalculating armor when it changes
        if (currentArmorValue != previousArmorValue) {
            //Calculate here
            armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);

            //Save value for next cycle
            previousArmorValue = currentArmorValue;
        }

        //Push to avoid lasting changes
      //  RenderSystem.pushMatrix();
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
                        if (Configs.ClientConfig.showEmptyArmorIcons.get() && (Configs.ClientConfig.alwaysShowArmorBar.get() || currentArmorValue > 0)) {
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
        //GlStateManager.popMatrix();
    }

    private static void color4f(float r, float g, float b, float a){
        RenderSystem.setShaderColor(r,g, b, a);
    }

    public static void renderOverlay(RenderGuiOverlayEvent.Pre e) {
        if (e.getOverlay().id().equals(VanillaGuiOverlay.ARMOR_LEVEL.id())) {
            e.setCanceled(true);
        }
    }
}