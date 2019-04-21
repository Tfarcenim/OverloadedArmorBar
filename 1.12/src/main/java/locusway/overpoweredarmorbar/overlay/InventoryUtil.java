package locusway.overpoweredarmorbar.overlay;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import baubles.api.BaublesApi;
import baubles.api.cap.IBaublesItemHandler;

public class InventoryUtil {

    public InventoryUtil() {
    }

  public static ItemStack getPlayerInventoryItem(Item item, EntityPlayer player) {
      for(int i = 0; i < player.inventory.getSizeInventory(); ++i) {
        ItemStack is = player.inventory.getStackInSlot(i);
        if (!is.isEmpty() && is.getItem() == item) {
          return is;
        }
      }

      return ItemStack.EMPTY;
    }

    private static ItemStack actuallyGetBauble(Item item, EntityPlayer player) {
      IBaublesItemHandler handler = BaublesApi.getBaublesHandler(player);

      for(int i = 0; i < handler.getSlots(); ++i) {
        ItemStack is = handler.getStackInSlot(i);
        if (!is.isEmpty() && is.getItem() == item) {
          return is;
        }
      }

      return ItemStack.EMPTY;
    }

    public static ItemStack getBauble(Item item, EntityPlayer player) {
      return Loader.isModLoaded("baubles") ? actuallyGetBauble(item, player) : ItemStack.EMPTY;
    }

}

