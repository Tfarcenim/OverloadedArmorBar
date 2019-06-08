package locusway.overpoweredarmorbar.proxy;

import locusway.overpoweredarmorbar.OverloadedArmorBar;
import locusway.overpoweredarmorbar.overlay.OverlayEventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@Mod.EventBusSubscriber(CLIENT)
public class ClientProxy extends CommonProxy
{
  public static OverlayEventHandler handler = new OverlayEventHandler();

    @Override
    public void postInit(FMLCommonSetupEvent event)
    {
        super.postInit(event);

        //Register Armor Renderer for events

        MinecraftForge.EVENT_BUS.register(handler);

        //Register event for configuration change
        OverloadedArmorBar.ConfigChange eventConfigChanged = new OverloadedArmorBar.ConfigChange();
        MinecraftForge.EVENT_BUS.register(eventConfigChanged);
    }

  @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        new ConfigChangedEvent.OnConfigChangedEvent(OverloadedArmorBar.MODID,"stuff", false, false);
//        armorBarRenderer.forceUpdate();
    }


}