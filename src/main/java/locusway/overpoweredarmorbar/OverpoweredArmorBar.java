package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.proxy.ClientProxy;
import locusway.overpoweredarmorbar.proxy.CommonProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("overpoweredarmorbar")
public class OverpoweredArmorBar {

    public static final String MODID = "overpoweredarmorbar";

    public OverpoweredArmorBar() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
    }

    public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
    public static Logger logger = LogManager.getLogger();
    public static boolean healthColored = false;

@SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
    //Checks the config to see if a mod is loaded that colors the health. Default is Mantle and Scaling Health
    if (ModList.get().isLoaded("mantle") || ModList.get().isLoaded("scalinghealth"))
        healthColored = true;
        proxy.postInit(event);
    }
}