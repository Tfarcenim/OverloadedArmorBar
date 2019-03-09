package locusway.overpoweredarmorbar.proxy;

import locusway.overpoweredarmorbar.EventConfigChanged;
import locusway.overpoweredarmorbar.OverpoweredArmorBar;
import locusway.overpoweredarmorbar.overlay.ArmorBarRenderer;
import locusway.overpoweredarmorbar.overlay.OverlayEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static net.minecraftforge.api.distmarker.Dist.CLIENT;

@Mod.EventBusSubscriber(CLIENT)
public class ClientProxy extends CommonProxy
{
    private ArmorBarRenderer armorBarRenderer;

    @Override
    public void postInit(FMLCommonSetupEvent event)
    {
        super.postInit(event);

        //Register Armor Renderer for events
        armorBarRenderer = new ArmorBarRenderer(Minecraft.getInstance());
        OverlayEventHandler overlay = new OverlayEventHandler(armorBarRenderer);
        MinecraftForge.EVENT_BUS.register(overlay);

        //Register event for configuration change
        EventConfigChanged eventConfigChanged = new EventConfigChanged();
        MinecraftForge.EVENT_BUS.register(eventConfigChanged);
    }

  @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        new ConfigChangedEvent.OnConfigChangedEvent(OverpoweredArmorBar.MODID,"stuff", false, false);
        armorBarRenderer.forceUpdate();
    }
}