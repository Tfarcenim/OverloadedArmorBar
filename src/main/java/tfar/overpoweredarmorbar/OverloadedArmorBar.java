package tfar.overpoweredarmorbar;

import de.guntram.mcmod.fabrictools.ConfigurationProvider;
import net.fabricmc.api.ClientModInitializer;


public class OverloadedArmorBar implements ClientModInitializer {

    public static final String MODID = "overloadedarmorbar";
    public static final String MODNAME = "Overloaded Armor Bar";
    private static ConfigurationHandler confHandler;
    
    public static boolean drawTextureSuppressed = false;

    @Override
    public void onInitializeClient() {
        confHandler=ConfigurationHandler.getInstance();
        ConfigurationProvider.register(MODNAME, confHandler);
        confHandler.load(ConfigurationProvider.getSuggestedFile(MODID));
    }
}