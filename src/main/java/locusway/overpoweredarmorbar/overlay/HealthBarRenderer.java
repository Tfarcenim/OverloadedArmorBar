package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.ModConfig;
import locusway.overpoweredarmorbar.OverpoweredArmorBar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;

import java.util.Random;

/*
    Class handles the drawing of the health bar
 */

public class HealthBarRenderer extends Gui
{
	private Minecraft mc;

	private int updateCounter = 0;
	private int playerHealth = 0;
	private int lastPlayerHealth = 0;
	private long healthUpdateCounter = 0;
	private long lastSystemTime = 0;
	private Random rand = new Random();
	private Icon[] icons;

	private static final ResourceLocation ICON_HEARTS = new ResourceLocation(OverpoweredArmorBar.MODID, "textures/gui/icons.png");
	private static final ResourceLocation ICON_VANILLA = Gui.ICONS;

	private static final float PASS_ONE_ALPHA = 1.0F;
	private static final float PASS_TWO_ALPHA = 0.4F;
	private static final float PASS_THREE_ALPHA = 0.8F;

	private boolean forceUpdateIcons = false;

	public HealthBarRenderer(Minecraft mc)
	{
		this.mc = mc;
	}

	public void forceUpdate()
	{
		forceUpdateIcons = true;
	}

	public void renderHealthBar(int screenWidth, int screenHeight)
	{
		//Push to avoid lasting changes
		GL11.glPushMatrix();
		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

		GlStateManager.enableBlend();

		updateCounter = mc.ingameGUI.getUpdateCounter();

		EntityPlayer entityplayer = (EntityPlayer) this.mc.getRenderViewEntity();
		int health = MathHelper.ceil(entityplayer.getHealth());
		boolean highlight = this.healthUpdateCounter > (long) this.updateCounter && (this.healthUpdateCounter - (long) this.updateCounter) / 3L % 2L == 1L;

		if (health < this.playerHealth && entityplayer.hurtResistantTime > 0)
		{
			this.lastSystemTime = Minecraft.getSystemTime();
			this.healthUpdateCounter = (long) (this.updateCounter + 20);
		}
		else if (health > this.playerHealth && entityplayer.hurtResistantTime > 0)
		{
			this.lastSystemTime = Minecraft.getSystemTime();
			this.healthUpdateCounter = (long) (this.updateCounter + 10);
		}

		if (Minecraft.getSystemTime() - this.lastSystemTime > 1000L)
		{
			this.playerHealth = health;
			this.lastPlayerHealth = health;
			this.lastSystemTime = Minecraft.getSystemTime();
		}

		if(health != this.playerHealth || icons == null || forceUpdateIcons)
		{
			icons = IconStateCalculator.calculateIcons(health, ModConfig.healthColorValues);
			forceUpdateIcons = false;
		}

		this.playerHealth = health;
		int j = this.lastPlayerHealth;
		this.rand.setSeed((long) (this.updateCounter * 312871));
		IAttributeInstance maxHealthAttribute = entityplayer.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		int xStart = screenWidth / 2 - 91;
		int yStart = screenHeight - 39;
		float maxHealth = (float) maxHealthAttribute.getAttributeValue();
		int absorptionAmount = MathHelper.ceil(entityplayer.getAbsorptionAmount());
		int numberOfHealthBars = MathHelper.ceil((maxHealth + (float) absorptionAmount) / 2.0F / 10.0F);
		int i2 = Math.max(10 - (numberOfHealthBars - 2), 3);
		int absorptionRemaining = absorptionAmount;
		int regen = -1;

		if (entityplayer.isPotionActive(MobEffects.REGENERATION))
		{
			regen = this.updateCounter % MathHelper.ceil(maxHealth + 5.0F);
		}

		this.mc.mcProfiler.startSection("health");

		for (int i = 9; i >= 0; --i)
		{
			Icon icon = icons[i];
			IconColor firstHalfColor = icon.primaryIconColor;
			IconColor secondHalfColor = icon.secondaryIconColor;

			int k5 = 16;

			if (entityplayer.isPotionActive(MobEffects.POISON))
			{
				k5 += 36;
			}
			else if (entityplayer.isPotionActive(MobEffects.WITHER))
			{
				k5 += 72;
			}

			int i4 = 0;

			if (highlight)
			{
				i4 = 1;
			}

			int j4 = MathHelper.ceil((float) (i + 1) / 10.0F) - 1;
			int xPosition = xStart + i % 10 * 8;
			int yPosition = yStart - j4 * i2;

			if (health <= 4)
			{
				yPosition += this.rand.nextInt(2);
			}

			if (absorptionRemaining <= 0 && i == regen)
			{
				yPosition -= 2;
			}

			int i5 = 0;

			if (entityplayer.world.getWorldInfo().isHardcoreModeEnabled())
			{
				i5 = 5;
			}

			//Heart background
			this.drawTexturedModalRect(xPosition, yPosition, 16 + i4 * 9, 9 * i5, 9, 9);

			if (highlight)
			{
				if (i * 2 + 1 < j)
				{
					//Draw full highlighted heart
					this.drawTexturedModalRect(xPosition, yPosition, k5 + 54, 9 * i5, 9, 9);
				}

				if (i * 2 + 1 == j)
				{
					//Draw half highlighted heart
					this.drawTexturedModalRect(xPosition, yPosition, k5 + 63, 9 * i5, 9, 9);
				}
			}

			if (absorptionRemaining > 0)
			{
				if (absorptionRemaining == absorptionAmount && absorptionAmount % 2 == 1)
				{
					this.drawTexturedModalRect(xPosition, yPosition, k5 + 153, 9 * i5, 9, 9);
					--absorptionRemaining;
				}
				else
				{
					this.drawTexturedModalRect(xPosition, yPosition, k5 + 144, 9 * i5, 9, 9);
					absorptionRemaining -= 2;
				}
			}
			else
			{
				//if (i * 2 + 1 < health)
				if(icon.iconType == Icon.Type.FULL)
				{
					//Draw full heart

					//Bind our custom texture
					this.mc.getTextureManager().bindTexture(ICON_HEARTS);

					//Draw tinted white heart
					GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, PASS_ONE_ALPHA);
					this.drawTexturedModalRect(xPosition, yPosition, 0, 0, 9, 9);

					//Second pass dark highlights
					GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, PASS_TWO_ALPHA);
					this.drawTexturedModalRect(xPosition, yPosition, 9, 9, 9, 9);

					//Third pass dot highlight
					GL11.glColor4f(1.0F, 1.0F, 1.0F, PASS_THREE_ALPHA);
					this.drawTexturedModalRect(xPosition, yPosition,27, 0, 9, 9);

					//Reset back to normal settings
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.mc.getTextureManager().bindTexture(ICON_VANILLA);
				}

				//if (i * 2 + 1 == health)
				if(icon.iconType == Icon.Type.HALF)
				{
					//Draw Half Heart

					if(health > 20)
					{
						//We have wrapped, Draw both parts of the heart seperately

						//Bind our custom texture
						this.mc.getTextureManager().bindTexture(ICON_HEARTS);

						//Draw first half of tinted white heart
						GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, PASS_ONE_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition, 9, 0, 9, 9);

						//Second pass dark highlights
						GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, PASS_TWO_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition, 9, 9, 9, 9);

						//Third pass dot highlight
						GL11.glColor4f(1.0F, 1.0F, 1.0F, PASS_THREE_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition,27, 0, 9, 9);

						//Draw second half of tinted white heart
						GL11.glColor4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, PASS_ONE_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition, 18, 0, 9, 9);

						//Second pass dark highlights
						GL11.glColor4f(secondHalfColor.Red, secondHalfColor.Green, secondHalfColor.Blue, PASS_TWO_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition, 18, 9, 9, 9);
					}
					else
					{
						//Draw only first half of heart

						//Bind our custom texture
						this.mc.getTextureManager().bindTexture(ICON_HEARTS);

						//Draw tinted white heart
						GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, PASS_ONE_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition, 9, 0, 9, 9);

						//Second pass dark highlights
						GL11.glColor4f(firstHalfColor.Red, firstHalfColor.Green, firstHalfColor.Blue, PASS_TWO_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition, 9, 9, 9, 9);

						//Third pass dot highlight
						GL11.glColor4f(1.0F, 1.0F, 1.0F, PASS_THREE_ALPHA);
						this.drawTexturedModalRect(xPosition, yPosition,27, 0, 9, 9);
					}

					//Reset back to normal settings
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
					this.mc.getTextureManager().bindTexture(ICON_VANILLA);
				}
			}
		}
		GlStateManager.disableBlend();

		//Revert our state back
		GL11.glPopMatrix();
		GL11.glPopAttrib();
		mc.mcProfiler.endSection();
	}
}