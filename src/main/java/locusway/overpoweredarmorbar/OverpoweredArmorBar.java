package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = OverpoweredArmorBar.MODID, name = OverpoweredArmorBar.MODNAME, version = OverpoweredArmorBar.MODVERSION, useMetadata = true)
public class OverpoweredArmorBar
{

    public static final String MODID = "overpoweredarmorbar";
    public static final String MODNAME = "Overpowered Armor Bar";
    public static final String MODVERSION = "0.2.0";

    @SidedProxy(clientSide = "locusway.overpoweredarmorbar.proxy.ClientProxy", serverSide = "locusway.overpoweredarmorbar.proxy.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static OverpoweredArmorBar instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
    }
}
