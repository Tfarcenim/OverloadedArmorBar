package locusway.overpoweredarmorbar.proxy;

import locusway.overpoweredarmorbar.OverpoweredArmorBar;
import locusway.overpoweredarmorbar.overlay.LavaCharmRenderer;
import locusway.overpoweredarmorbar.overlay.OverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

import static locusway.overpoweredarmorbar.ModConfig.*;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    public static OverlayEventHandler handler = new OverlayEventHandler();
    public static LavaCharmRenderer lavahandler;
    public static boolean lava = false;
    @Override
    public void postInit(FMLPostInitializationEvent event)
    {
        super.postInit(event);

        //Register Armor Renderer for events
        MinecraftForge.EVENT_BUS.register(handler);
        //Register event for configuration change
        MinecraftForge.EVENT_BUS.register(new EventConfigChanged());
        if (Loader.isModLoaded("randomthings")){
            lavahandler = new LavaCharmRenderer();
            MinecraftForge.EVENT_BUS.register(lavahandler);
            lava = true;
        }
    }

    @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        ConfigManager.sync(OverpoweredArmorBar.MODID, Config.Type.INSTANCE);
        handler.forceUpdate();
    }
}
