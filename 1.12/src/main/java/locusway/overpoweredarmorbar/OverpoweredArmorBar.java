package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = OverpoweredArmorBar.MODID, name = OverpoweredArmorBar.MODNAME, version = OverpoweredArmorBar.MODVERSION, useMetadata = true, clientSideOnly = true)
public class OverpoweredArmorBar {

    public static final String MODID = "overpoweredarmorbar";
    public static final String MODNAME = "Overloaded Armor Bar";
    public static final String MODVERSION = "@VERSION@";

    @SidedProxy(clientSide = "locusway.overpoweredarmorbar.proxy.ClientProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static OverpoweredArmorBar instance;

    public static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}