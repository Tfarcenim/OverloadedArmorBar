package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.overlay.OverlayEventHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(OverloadedArmorBar.MODID)
public class OverloadedArmorBar {

  public static final String MODID = "overloadedarmorbar";

  public OverloadedArmorBar() {DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
  });
  }

  public static Logger logger = LogManager.getLogger();

  public void setup(final FMLClientSetupEvent event) {
    //Register Armor Renderer for events
    MinecraftForge.EVENT_BUS.register(new OverlayEventHandler());
  }
}