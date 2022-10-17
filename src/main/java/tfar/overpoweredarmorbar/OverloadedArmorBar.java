package tfar.overpoweredarmorbar;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfar.overpoweredarmorbar.overlay.OverlayEventHandler;

@Mod(OverloadedArmorBar.MODID)
public class OverloadedArmorBar {

	public static final String MODID = "overloadedarmorbar";

	public OverloadedArmorBar() {

		if (FMLEnvironment.dist == Dist.CLIENT) {
			//ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
		}
	}
@SuppressWarnings("ConstantConditions")
	public void setup(final FMLClientSetupEvent event) {
	OverlayRegistry.OverlayEntry entry = OverlayRegistry.getEntry(ForgeIngameGui.ARMOR_LEVEL_ELEMENT);
		//Register Armor Renderer for events
		OverlayRegistry.registerOverlayTop(entry.getDisplayName(),new OverlayEventHandler());
		OverlayRegistry.enableOverlay(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, false);
	}
}