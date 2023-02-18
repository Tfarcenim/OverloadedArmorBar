package tfar.overpoweredarmorbar;

import com.mojang.datafixers.util.Pair;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.fml.IExtensionPoint;
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
            ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class,
                    () -> new IExtensionPoint.DisplayTest(        // Send any version from server to client, since we will be accepting any version as well
                            () -> "dQw4w9WgXcQ",                  // Accept any version on the client, from server or from save
                            (remoteVersion, isFromServer) -> true));
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Configs.CLIENT_SPEC);
        } else {
            System.out.println("Here's an Easter egg");
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void setup(final FMLClientSetupEvent event) {
        OverlayRegistry.OverlayEntry entry = OverlayRegistry.getEntry(ForgeIngameGui.ARMOR_LEVEL_ELEMENT);
        //Register Armor Renderer for events
        OverlayRegistry.registerOverlayAbove(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, entry.getDisplayName(),OverlayEventHandler.overlay);
        OverlayRegistry.enableOverlay(ForgeIngameGui.ARMOR_LEVEL_ELEMENT, false);
    }
}