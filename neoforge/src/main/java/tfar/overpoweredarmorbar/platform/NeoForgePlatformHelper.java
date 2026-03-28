package tfar.overpoweredarmorbar.platform;

import net.minecraft.client.gui.Gui;

import net.neoforged.fml.ModList;
import net.neoforged.fml.loading.FMLLoader;
import tfar.overloadedarmorbar.platform.MLConfig;
import tfar.overloadedarmorbar.platform.services.IPlatformHelper;
import tfar.overpoweredarmorbar.Configs;

public class NeoForgePlatformHelper implements IPlatformHelper {

    final MLConfig config = new Configs();

    @Override
    public String getPlatformName() {

        return "NeoForge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.getCurrent().isProduction();
    }

    @Override
    public MLConfig getConfig() {
        return config;
    }

    @Override
    public void offsetLeftHeight(Gui gui, int offset) {
        gui.leftHeight+=offset;
    }

    @Override
    public int getLeftHeight(Gui gui) {
        return gui.leftHeight;
    }
}