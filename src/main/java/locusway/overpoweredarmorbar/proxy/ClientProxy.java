package locusway.overpoweredarmorbar.proxy;

import locusway.overpoweredarmorbar.overlay.OverlayEventHandler;
import locusway.overpoweredarmorbar.overlay.ArmorBarRenderer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
    private static ArmorBarRenderer armorBarRenderer;

    @Override
    public void preInit(FMLPreInitializationEvent e)
    {
        super.preInit(e);
    }

    @Override
    public void init(FMLInitializationEvent e)
    {
        super.init(e);
    }

    @Override
    public void postInit(FMLPostInitializationEvent e)
    {
        super.postInit(e);

        armorBarRenderer = new ArmorBarRenderer(Minecraft.getMinecraft());
        OverlayEventHandler overlay = new OverlayEventHandler(armorBarRenderer);
        MinecraftForge.EVENT_BUS.register(overlay);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
    }

}
