package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static locusway.overpoweredarmorbar.ModConfig.alwaysShowArmorBar;
import static locusway.overpoweredarmorbar.proxy.ClientProxy.*;

/*
    Class which handles the render event and hides the vanilla armor bar
 */
public class OverlayEventHandler {
  public static void drawTexturedModalRect(float x, float y, int textureX, int textureY, int width, int height) {
    Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, textureX, textureY, width, height);
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

  private Minecraft mc = Minecraft.getMinecraft();
  private ArmorIcon[] armorIcons;

  @SubscribeEvent(receiveCanceled = true)
  public void onRenderGameOverlayEventPre(RenderGameOverlayEvent event) {
    if (event.getType() != RenderGameOverlayEvent.ElementType.ARMOR)
      return;
    int scaledWidth = event.getResolution().getScaledWidth();
    int scaledHeight = event.getResolution().getScaledHeight();
    /* Don't render the vanilla armor bar */
    event.setCanceled(true);
    handler.renderArmorBar(scaledWidth, scaledHeight);
    if (lava) lavahandler.renderLavaCharm(event);

  }

  private int calculateArmorValue() {
    int currentArmorValue = mc.player.getTotalArmorValue();

    for (ItemStack itemStack : mc.player.getArmorInventoryList()) {
      if (itemStack.getItem() instanceof ISpecialArmor) {
        ISpecialArmor specialArmor = (ISpecialArmor) itemStack.getItem();
        currentArmorValue += specialArmor.getArmorDisplay(mc.player, itemStack, 0);
      }
    }
    return currentArmorValue;
  }

  public void renderArmorBar(int screenWidth, int screenHeight) {
    EntityPlayer player = mc.player;
    int currentArmorValue = calculateArmorValue();
    int xStart = screenWidth / 2 - 91;
    int yStart = screenHeight - 39;

    IAttributeInstance playerHealthAttribute = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
    float playerHealth = (float) playerHealthAttribute.getAttributeValue();

    //Fake that the player health only goes up to 20 so that it does not make the bar float above the health bar
    if (!ModConfig.offset && playerHealth > 20) playerHealth = 20;

    float absorptionAmount = MathHelper.ceil(player.getAbsorptionAmount());

    //Clamp the absorption value to 20 so that it doesn't make the bar float above the health bar
    if (!ModConfig.offset && absorptionAmount > 20) absorptionAmount = 20;

    int numberOfHealthBars = (int) Math.ceil(playerHealth / 20) + (int) Math.ceil(absorptionAmount / 20);
    int i2 = Math.max(10 - (numberOfHealthBars - 2), 3);
    int yPosition = yStart - (numberOfHealthBars - 1) * i2 - 10;

    //Save some CPU cycles by only recalculating armor when it changes
    if (currentArmorValue != previousArmorValue) {
      //Calculate here
      armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);

      //Save value for next cycle
      previousArmorValue = currentArmorValue;
    }

    //Push to avoid lasting changes
    GlStateManager.pushMatrix();
    GlStateManager.enableBlend();
    //GlStateManager.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

    int armorIconCounter = 0;
    for (ArmorIcon icon : armorIcons) {
      int xPosition = xStart + armorIconCounter * 8;
      switch (icon.armorIconType) {
        case NONE:
          ArmorIconColor color = icon.primaryArmorIconColor;
          GlStateManager.color(color.Red, color.Green, color.Blue, color.Alpha);
          if (currentArmorValue > 20) {
            //Draw the full icon as we have wrapped
            drawTexturedModalRect(xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
          } else {
            if (ModConfig.showEmptyArmorIcons && (alwaysShowArmorBar || currentArmorValue > 0)) {
              //Draw the empty armor icon
              drawTexturedModalRect(xPosition, yPosition, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
            }
          }
          break;
        case HALF:
          ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
          ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

          GlStateManager.color(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
          drawTexturedModalRect(xPosition, yPosition, 25, 9, 5, ARMOR_ICON_SIZE);

          GlStateManager.color(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, secondHalfColor.Alpha);
          if (currentArmorValue > 20) {
            //Draw the second half as full as we have wrapped
            drawTexturedModalRect(xPosition + 5, yPosition, 39, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
          } else {
            //Draw the second half as empty
            drawTexturedModalRect(xPosition + 5, yPosition, 30, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
          }
          break;
        case FULL:
          ArmorIconColor fullColor = icon.primaryArmorIconColor;
          GlStateManager.color(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
          drawTexturedModalRect(xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
          break;
        default:
          break;
      }
      armorIconCounter++;
    }

    //Revert our state back
    GlStateManager.color(1, 1, 1, 1);
    GlStateManager.popMatrix();
    //GL11.glPopAttrib();
  }

  public void forceUpdate() {
    //Setting to unknown value will cause a refresh next render
    handler.previousArmorValue = UNKNOWN_ARMOR_VALUE;
  }
}