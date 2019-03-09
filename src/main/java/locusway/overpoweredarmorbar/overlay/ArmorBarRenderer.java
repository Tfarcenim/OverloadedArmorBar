package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.Configs;
import locusway.overpoweredarmorbar.OverpoweredArmorBar;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

import java.util.Arrays;

import static locusway.overpoweredarmorbar.Configs.alwaysShowArmorBar;

/*
    Class handles the drawing of the armor bar
 */

public class ArmorBarRenderer extends Gui
{

	private final int UNKNOWN_ARMOR_VALUE = -1;
	private int previousArmorValue = UNKNOWN_ARMOR_VALUE;

	private static final int ARMOR_ICON_SIZE = 9;
	private static final int ARMOR_FIRST_HALF_ICON_SIZE = 5;
	private static final int ARMOR_SECOND_HALF_ICON_SIZE = 4;

	private Minecraft mc;
	private ArmorIcon[] armorIcons;

	public ArmorBarRenderer(Minecraft mc) {
		this.mc =mc;
	}

	private int calculateArmorValue()
	{
	    //OverpoweredArmorBar.logger.warn("break 1");
		int currentArmorValue = mc.player.getTotalArmorValue();

		//for (ItemStack itemStack : mc.player.getArmorInventoryList())
		//{
			//if (itemStack.getItem() instanceof ISpecialArmor)
			//{
		//		ISpecialArmor specialArmor = (ISpecialArmor) itemStack.getItem();
		//		currentArmorValue += specialArmor.getArmorDisplay(mc.player, itemStack, 0);
		//	}
	//	}
		return currentArmorValue;
	}

	public void renderArmorBar(int screenWidth, int screenHeight)
	{
		EntityPlayer player = mc.player;
		int currentArmorValue = calculateArmorValue();
		int xStart = screenWidth / 2 - 91;
		int yStart = screenHeight - 39;

		IAttributeInstance playerHealthAttribute = player.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float playerHealth = (float) playerHealthAttribute.getValue();

		//Fake that the player health only goes up to 20 so that it does not make the bar float above the health bar
		if (OverpoweredArmorBar.healthColored && playerHealth > 20) playerHealth = 20;

		int absorptionAmount = MathHelper.ceil(player.getAbsorptionAmount());

		//Clamp the absorption value to 20 so that it doesn't make the bar float above the health bar
		if (OverpoweredArmorBar.healthColored && absorptionAmount >20) absorptionAmount=20;

		int numberOfHealthBars = MathHelper.ceil((playerHealth + (float) absorptionAmount) / 20.0F);
		int i2 = Math.max(10 - (numberOfHealthBars - 2), 3);
		int yPosition = yStart - (numberOfHealthBars - 1) * i2 - 10;

		//Save some CPU cycles by only recalculating armor when it changes
		if (currentArmorValue != previousArmorValue)
		{
			//Calculate here
			armorIcons = ArmorBar.calculateArmorIcons(currentArmorValue);

			//Save value for next cycle
			previousArmorValue = currentArmorValue;
		}

		//Push to avoid lasting changes
		GlStateManager.pushMatrix();

		int armorIconCounter = 0;
		for (ArmorIcon icon : armorIcons)
		{
			int xPosition = xStart + armorIconCounter * 8;
			switch (icon.armorIconType)
			{

				case NONE:
					ArmorIconColor color = icon.primaryArmorIconColor;
					GlStateManager.color4f(color.Red, color.Green, color.Blue, color.Alpha);
					if (currentArmorValue > 20)
					{
						//Draw the full icon as we have wrapped
						drawTexturedModalRect(xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
					}
					else
					{
						if (Configs.showEmptyArmorIcons &&( alwaysShowArmorBar || currentArmorValue>0))
						{
							//Draw the empty armor icon
							drawTexturedModalRect(xPosition, yPosition, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
						}
					}
					break;
				case HALF:
					ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
					ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

					GlStateManager.color4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
					drawTexturedModalRect(xPosition, yPosition, 25, 9, 5, ARMOR_ICON_SIZE);

					GlStateManager.color4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, secondHalfColor.Alpha);
					if (currentArmorValue > 20)
					{
						//Draw the second half as full as we have wrapped
						drawTexturedModalRect(xPosition + 5, yPosition, 39, 9, ARMOR_FIRST_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
					}
					else
					{
						//Draw the second half as empty
						drawTexturedModalRect(xPosition + 5, yPosition, 30, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
					}
					break;
				case FULL:
					ArmorIconColor fullColor = icon.primaryArmorIconColor;
					GlStateManager.color4f(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
					drawTexturedModalRect(xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
					break;
				default:
					break;
			}
			armorIconCounter++;
		}
		//Revert our state back
		GlStateManager.popMatrix();
	}

	public void forceUpdate()
	{
		//Setting to unknown value will cause a refresh next render
		previousArmorValue = UNKNOWN_ARMOR_VALUE;
	}
}