package tfar.overpoweredarmorbar;

import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;
import tfar.overloadedarmorbar.OverloadedArmorBar;
import tfar.overloadedarmorbar.overlay.OverlayRenderer;

@Mod(value = OverloadedArmorBar.MODID,dist = Dist.CLIENT)
public class OverloadedArmorBarNeoForge {

	public static final GuiLayer overlay = (guiGraphics, partialTick) -> {
			OverlayRenderer.renderArmorBar(guiGraphics);
	};

	public OverloadedArmorBarNeoForge(IEventBus bus, ModContainer modContainer) {
		modContainer.registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
		bus.addListener(this::setup);
	}

	public static void disableOverlay(RenderGuiLayerEvent.Pre e) {
		if (e.getName().equals(VanillaGuiLayers.ARMOR_LEVEL)) {
			e.setCanceled(true);
		}
	}

	public void setup(final RegisterGuiLayersEvent event) {
		//Register Armor Renderer for events
		event.registerAbove(VanillaGuiLayers.ARMOR_LEVEL, Identifier.fromNamespaceAndPath(OverloadedArmorBar.MODID,OverloadedArmorBar.MODID),
				overlay);
		NeoForge.EVENT_BUS.addListener(OverloadedArmorBarNeoForge::disableOverlay);
	}
}