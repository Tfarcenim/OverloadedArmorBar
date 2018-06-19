package locusway.overpoweredarmorbar.overlay.armorbar;

import locusway.overpoweredarmorbar.ModConfig;
import locusway.overpoweredarmorbar.overlay.Icon;
import locusway.overpoweredarmorbar.overlay.IconColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ISpecialArmor;
import org.lwjgl.opengl.GL11;

import java.util.Collection;

/*
    Class handles the drawing of the armor bar
 */
public class ArmorBarRenderer extends Gui
{
	private final static int UNKNOWN_ARMOR_VALUE = -1;
	private int previousArmorValue = UNKNOWN_ARMOR_VALUE;

	private final static int ARMOR_ICON_SIZE = 9;
	private final static int ARMOR_FIRST_HALF_ICON_SIZE = 5;
	private final static int ARMOR_SECOND_HALF_ICON_SIZE = 4;

	private Minecraft mc;
	private ArmorIcon[] armorIcons;

	public ArmorBarRenderer(Minecraft mc)
	{
		this.mc = mc;
	}

	private int calculateArmorValue()
	{
		int currentArmorValue = mc.player.getTotalArmorValue();

		for (ItemStack itemStack : mc.player.getArmorInventoryList())
		{
			if (itemStack.getItem() instanceof ISpecialArmor)
			{
				ISpecialArmor specialArmor = (ISpecialArmor) itemStack.getItem();
				currentArmorValue += specialArmor.getArmorDisplay(mc.player, itemStack, 0);
			}
		}
		return currentArmorValue;
	}

	public void renderArmorBar(int screenWidth, int screenHeight)
	{
		EntityPlayer player = mc.player;
		int currentArmorValue = calculateArmorValue();
		int xStart = screenWidth / 2 - 91;
		int yStart = screenHeight - 39;

		IAttributeInstance playerHealthAttribute = player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float playerHealth = (float) playerHealthAttribute.getAttributeValue();

		int absorptionAmount = MathHelper.ceil(player.getAbsorptionAmount());

		int numberOfHealthBars = MathHelper.ceil((playerHealth + (float) absorptionAmount) / 2.0F / 10.0F);
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
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

		int armorIconCounter = 0;
		for (ArmorIcon icon : armorIcons)
		{
			int xPosition = xStart + armorIconCounter * 8;
			switch (icon.armorIconType)
			{
				case NONE:
					ArmorIconColor color = icon.primaryArmorIconColor;
					GL11.glColor4f(color.Red, color.Green, color.Blue, color.Alpha);
					if (currentArmorValue > 20)
					{
						//Draw the full icon as we have wrapped
						drawTexturedModalRect(xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
					}
					else
					{
						if (ModConfig.showEmptyArmorIcons)
						{
							//Draw the empty armor icon
							drawTexturedModalRect(xPosition, yPosition, 16, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
						}
					}
					break;
				case HALF:
					ArmorIconColor firstHalfColor = icon.primaryArmorIconColor;
					ArmorIconColor secondHalfColor = icon.secondaryArmorIconColor;

					GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, firstHalfColor.Alpha);
					drawTexturedModalRect(xPosition, yPosition, 25, 9, 5, ARMOR_ICON_SIZE);

					GL11.glColor4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, secondHalfColor.Alpha);
					if (currentArmorValue > 20)
					{
						//Draw the second half as full as we have wrapped
						drawTexturedModalRect(xPosition + 5, yPosition, 34 + 5, 9, ARMOR_FIRST_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
					}
					else
					{
						//Draw the second half as empty
						drawTexturedModalRect(xPosition + 5, yPosition, 25 + 5, 9, ARMOR_SECOND_HALF_ICON_SIZE, ARMOR_ICON_SIZE);
					}
					break;
				case FULL:
					ArmorIconColor fullColor = icon.primaryArmorIconColor;
					GL11.glColor4f(fullColor.Red, fullColor.Green, fullColor.Blue, fullColor.Alpha);
					drawTexturedModalRect(xPosition, yPosition, 34, 9, ARMOR_ICON_SIZE, ARMOR_ICON_SIZE);
					break;
				default:
					break;
			}
			armorIconCounter++;
		}

		//Revert our state back
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	public void forceUpdate()
	{
		//Setting to unknown value will cause a refresh next render
		previousArmorValue = UNKNOWN_ARMOR_VALUE;
	}
}