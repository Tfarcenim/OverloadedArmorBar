package tfar.overpoweredarmorbar;

import de.guntram.mcmod.fabrictools.ConfigurationProvider;
import net.fabricmc.api.ClientModInitializer;
import tfar.overpoweredarmorbar.overlay.OverlayEventHandler;


public class OverloadedArmorBar implements ClientModInitializer {

    public static final String MODID = "overloadedarmorbar";
    public static final String MODNAME = "Overloaded Armor Bar";
    private static ConfigurationHandler confHandler;
    
    public static boolean drawTextureSuppressed = false;
    public static OverlayEventHandler oeHandler;

    @Override
    public void onInitializeClient() {
        confHandler=ConfigurationHandler.getInstance();
        ConfigurationProvider.register(MODNAME, confHandler);
        confHandler.load(ConfigurationProvider.getSuggestedFile(MODID));
        oeHandler = new OverlayEventHandler();
    }
}