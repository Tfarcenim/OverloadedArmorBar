package tfar.overpoweredarmorbar;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;
import tfar.overpoweredarmorbar.overlay.OverlayEventHandler;

@Mod(OverloadedArmorBar.MODID)
public class OverloadedArmorBar {

	public static final String MODID = "overloadedarmorbar";

	public OverloadedArmorBar() {

		if (FMLEnvironment.dist == Dist.CLIENT) {
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
		}
	}

	public void setup(final FMLClientSetupEvent event) {
		//Register Armor Renderer for events
		MinecraftForge.EVENT_BUS.register(new OverlayEventHandler());
	}
}