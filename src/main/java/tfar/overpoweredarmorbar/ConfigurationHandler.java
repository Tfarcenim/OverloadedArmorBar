package tfar.overpoweredarmorbar;

import de.guntram.mcmod.fabrictools.ConfigChangedEvent;
import de.guntram.mcmod.fabrictools.Configuration;
import de.guntram.mcmod.fabrictools.ModConfigurationHandler;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ConfigurationHandler implements ModConfigurationHandler
{
    private static ConfigurationHandler instance;

    private Configuration config;
    private String configFileName;

    private boolean alwaysShowAmorBar, showEmptyArmorIcons,offset;
    private String colorValues;

    public static ConfigurationHandler getInstance() {
        if (instance==null)
            instance=new ConfigurationHandler();
        return instance;
    }
    
    public void load(final File configFile) {
        if (config == null) {
            config = new Configuration(configFile);
            configFileName=configFile.getPath();
            loadConfig();
        }
    }
    
    public static String getConfigFileName() {
        return getInstance().configFileName;
    }

    @Override
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(OverloadedArmorBar.MODNAME)) {
            loadConfig();
        }
    }
    
    @Override
    public void onConfigChanging(ConfigChangedEvent.OnConfigChangingEvent event) {
        if (event.getModID().equals(OverloadedArmorBar.MODNAME)) {
            switch (event.getItem()) {
                case "overloadedarmorbar.config.alwaysshow": alwaysShowAmorBar=(boolean)(Boolean)(event.getNewValue()); break;
                case "overloadedarmorbar.config.showempty": showEmptyArmorIcons=(boolean)(Boolean)(event.getNewValue()); break;
                case "overloadedarmorbar.config.colorlist": colorValues=(String)(event.getNewValue()); break;
            }
        }
    }
    
    private void loadConfig() {
        
        alwaysShowAmorBar = config.getBoolean("overloadedarmorbar.config.alwaysshow",
                Configuration.CATEGORY_CLIENT, false, "overloadedarmorbar.config.tt.alwaysshow");
        showEmptyArmorIcons = config.getBoolean("overloadedarmorbar.config.showempty",
                Configuration.CATEGORY_CLIENT, false, "overloadedarmorbar.config.tt.showempty");
        offset = config.getBoolean("overloadedarmorbar.config.offset",
                Configuration.CATEGORY_CLIENT, true, "overloadedarmorbar.config.tt.offset");
        colorValues = config.getString("overloadedarmorbar.config.colors",
                Configuration.CATEGORY_CLIENT, "#FFFFFF #FF5500 #FFC747 #27FFE3 #00FF00 #7F00FF", "overloadedarmorbar.config.tt.colors");
        
        if (config.hasChanged())
            config.save();
    }
    
    @Override
    public Configuration getConfig() {
        return getInstance().config;
    }
    
    public static boolean alwaysShowArmorBar() { return getInstance().alwaysShowAmorBar; }
    public static boolean showEmptyArmorIcons() { return getInstance().showEmptyArmorIcons; }
    public static List<String> colorValues() { 
        return Arrays.asList(getInstance().colorValues.split(" "));
    };

    public static boolean offset() {
        return getInstance().offset;
    }
}
