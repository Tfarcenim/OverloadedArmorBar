package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.proxy.ClientProxy;
import locusway.overpoweredarmorbar.proxy.CommonProxy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OverloadedArmorBar.MODID)
public class OverloadedArmorBar {

  public static final String MODID = "overloadedarmorbar";

  public OverloadedArmorBar() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::bakeConfigs);

  }

  public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);
  public static Logger logger = LogManager.getLogger();

  public void setup(final FMLCommonSetupEvent event) {
    proxy.postInit(event);
  }

  @SubscribeEvent
  public void bakeConfigs(ModConfig.ModConfigEvent event)
  {
    if (event.getConfig().getSpec() == Configs.CLIENT_SPEC)
      Configs.bake();
  }


  public static class ConfigChange {
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
      //Only process events for this mod
      if (event.getModID().equals(OverloadedArmorBar.MODID))
        OverloadedArmorBar.proxy.onConfigChanged(event);
    }
  }
}