package tfar.overpoweredarmorbar;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import tfar.overpoweredarmorbar.overlay.OverlayEventHandler;

@Mod(OverloadedArmorBar.MODID)
public class OverloadedArmorBar {

	public static final String MODID = "overloadedarmorbar";

	public OverloadedArmorBar() {
		if (FMLEnvironment.dist == Dist.CLIENT) {
			ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,() -> new IExtensionPoint.DisplayTest(
					() -> "dQw4w9WgXcQ",     // Send any version from server to client, since we will be accepting any version as well
					(remoteVersion, isFromServer) -> true));// Accept any version on the client, from server or from save
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
			ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
		} else {
			System.out.println("Why is this mod on a dedicated server?");
		}
	}
	public void setup(final RegisterGuiOverlaysEvent event) {
		//Register Armor Renderer for events
		event.registerAbove(VanillaGuiOverlay.ARMOR_LEVEL.id(),MODID,new OverlayEventHandler());
		MinecraftForge.EVENT_BUS.addListener(OverlayEventHandler::disableOverlay);
	}
}