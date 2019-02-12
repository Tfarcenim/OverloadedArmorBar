package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.proxy.CommonProxy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = OverpoweredArmorBar.MODID, name = OverpoweredArmorBar.MODNAME, version = OverpoweredArmorBar.MODVERSION, useMetadata = true, clientSideOnly = true)
public class OverpoweredArmorBar
{

    public static final String MODID = "overpoweredarmorbar";
    public static final String MODNAME = "Overloaded Armor Bar";
    public static final String MODVERSION = "1.0.0";

    @SidedProxy(clientSide = "locusway.overpoweredarmorbar.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static OverpoweredArmorBar instance;

    public static Logger logger;
    public static boolean healthColored;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        proxy.postInit(event);
        //If a mod is loaded that colors the health. First one is Baubley Heart Canisters' modid
        healthColored = Loader.isModLoaded("bhc") || Loader.isModLoaded("scalinghealth");
    }
}