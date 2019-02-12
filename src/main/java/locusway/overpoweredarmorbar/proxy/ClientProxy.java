package locusway.overpoweredarmorbar.proxy;

import locusway.overpoweredarmorbar.EventConfigChanged;
import locusway.overpoweredarmorbar.OverpoweredArmorBar;
import locusway.overpoweredarmorbar.overlay.OverlayEventHandler;
import locusway.overpoweredarmorbar.overlay.ArmorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    private static ArmorBarRenderer armorBarRenderer;

    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);

        //Register Armor Renderer for events
        armorBarRenderer = new ArmorBarRenderer(Minecraft.getMinecraft());
        OverlayEventHandler overlay = new OverlayEventHandler(armorBarRenderer);
        MinecraftForge.EVENT_BUS.register(overlay);

        //Register event for configuration change
        EventConfigChanged eventConfigChanged = new EventConfigChanged();
        MinecraftForge.EVENT_BUS.register(eventConfigChanged);
    }

    @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        ConfigManager.sync(OverpoweredArmorBar.MODID, Config.Type.INSTANCE);
        armorBarRenderer.forceUpdate();
    }
}
