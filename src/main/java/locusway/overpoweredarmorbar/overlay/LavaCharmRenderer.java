package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class LavaCharmRenderer {
  public static final Item Lava_Charm = ForgeRegistries.ITEMS.getValue(new ResourceLocation("randomthings:lavacharm"));
  public static final Item Lava_Wader = ForgeRegistries.ITEMS.getValue(new ResourceLocation("randomthings:lavawader"));

  public void renderLavaCharm(RenderGameOverlayEvent event) {
    ItemStack lavaProtector = ItemStack.EMPTY;
    EntityPlayerSP player = Minecraft.getMinecraft().player;
    ItemStack lavaCharm = InventoryUtil.getBauble(Lava_Charm, player);
    if (lavaCharm.isEmpty()) lavaCharm = InventoryUtil.getPlayerInventoryItem(Lava_Charm, player);

    if (!lavaCharm.isEmpty()) lavaProtector = lavaCharm;

    ItemStack boots = player.getItemStackFromSlot(EntityEquipmentSlot.FEET);
    if (!boots.isEmpty() && boots.getItem() == Lava_Wader) lavaProtector = boots;

    if (!lavaProtector.isEmpty()) {
      NBTTagCompound compound = lavaProtector.getTagCompound();
      if (compound != null) {
        float charge = (float)compound.getInteger("charge");
        Minecraft mc = Minecraft.getMinecraft();
        mc.renderEngine.bindTexture(new ResourceLocation("randomthings:textures/gui/lavaCharmBar.png"));
        GuiIngame ingameGui = mc.ingameGUI;
        int width = event.getResolution().getScaledWidth();
        int height = event.getResolution().getScaledHeight();
        int count = (int)Math.floor((double)(charge / 20F));
        int left = 0;
        double absorb = player.getAbsorptionAmount();
        double health = player.getMaxHealth();
        int numberOfBars = ModConfig.offset ? (int)(Math.ceil(health / 20) + Math.ceil(absorb) / 20) : 1 + (absorb > 0 ? 1 : 0);
        int top = height - 50 - 10 * numberOfBars;
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        for(int i = 0; i < count + 1; ++i) {
          if (i == count + 1 - 1) {
            float countFloat = charge / 20F + 10;
            GlStateManager.color(1, 1, 1, countFloat % (float)((int)countFloat));
          }

          ingameGui.drawTexturedModalRect(width / 2 - 92 + left, top, 0, 0, 10, 10);
          left += 8;
          GlStateManager.color(1, 1, 1, 1);
        }

        mc.renderEngine.bindTexture(Gui.ICONS);
        GlStateManager.disableBlend();
      }
    }
  }
}
