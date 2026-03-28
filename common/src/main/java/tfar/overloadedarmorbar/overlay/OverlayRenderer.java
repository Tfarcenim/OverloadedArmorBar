package tfar.overloadedarmorbar.overlay;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tfar.overloadedarmorbar.platform.Services;

public class OverlayRenderer {

    private final static int UNKNOWN_ARMOR_VALUE = -1;
    private static int previousArmorValue = UNKNOWN_ARMOR_VALUE;
    private final static int ARMOR_ICON_SIZE = 9;
    private final static int ARMOR_FIRST_HALF_ICON_SIZE = 5;
    private final static int ARMOR_SECOND_HALF_ICON_SIZE = 4;

    private static ArmorIcon[] armorIcons;



    public static void renderArmorBar(GuiGraphicsExtractor graphics) {
        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (entity instanceof Player player && Minecraft.getInstance().gameMode.canHurtPlayer() && !Minecraft.getInstance().options.hideGui) {
            int currentArmorValue = player.getArmorValue();
            int xStart = graphics.guiWidth() / 2 - 91;
            int yPosition = graphics.guiHeight() - Services.PLATFORM.getLeftHeight(Minecraft.getInstance().gui);

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
                            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,Gui.ARMOR_FULL_SPRITE, xPosition, yPosition, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                        } else {
                            if (Services.PLATFORM.getConfig().showEmptyArmorIcons() && (Services.PLATFORM.getConfig().alwaysShowArmorBar() || currentArmorValue > 0)) {
                                //Draw the empty armor icon
                                graphics.blitSprite(RenderPipelines.GUI_TEXTURED,Gui.ARMOR_EMPTY_SPRITE, xPosition, yPosition, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                            }
                        }
                        break;
                    case HALF:
                        ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
                        ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

                        color4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
                        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,Gui.ARMOR_HALF_SPRITE, xPosition, yPosition, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);//half

                        color4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, secondHalfColor.Alpha);
                        if (currentArmorValue > 20) {
                            //Draw the second half as full as we have wrapped
                            graphics.blitSprite(RenderPipelines.GUI_TEXTURED,Gui.ARMOR_FULL_SPRITE,9,9,5,0, xPosition+5, yPosition,ARMOR_SECOND_HALF_ICON_SIZE,ARMOR_ICON_SIZE);
                        } else {
                            //Draw the second half as empty
                    //        graphics.blitSprite(Gui.ARMOR_EMPTY_SPRITE, xPosition + 5, yPosition, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
                        }
                        break;
                    case FULL:
                        ArmorIconColor fullColor = icon.primaryArmorIconColor;
                        color4f(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
                        graphics.blitSprite(RenderPipelines.GUI_TEXTURED,Gui.ARMOR_FULL_SPRITE, xPosition, yPosition, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
                        break;
                    default:
                        break;
                }
                armorIconCounter++;
            }
            color4f(1, 1, 1, 1);
            Services.PLATFORM.offsetLeftHeight(Minecraft.getInstance().gui, 10);
        }
    }

    private static void color4f(float r, float g, float b, float a){
       // RenderSystem.setShaderColor(r,g, b, a);
    }
}
