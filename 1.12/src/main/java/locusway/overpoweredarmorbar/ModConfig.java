package locusway.overpoweredarmorbar;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static locusway.overpoweredarmorbar.OverpoweredArmorBar.*;

@Config(modid = MODID)
public class ModConfig {
    @Config.Name("Armor icon colors")
    @Config.Comment("Colors must be specified in #RRGGBB format")
    public static String[] colorValues = new String[]{
            "#FFFFFF",
            "#FF5500",
            "#FFC747",
            "#27FFE3",
            "#00FF00",
            "#7F00FF"};

    @Config.Name("Always show armor bar?")
    public static boolean alwaysShowArmorBar = false;

    @Config.Name("Show empty armor icons?")
    public static boolean showEmptyArmorIcons = false;

    @Config.Name("Override for Armor shift")
    @Config.Comment("Set to true if the armor bar display's incorrectly")
    public static boolean offset = false;

    @Mod.EventBusSubscriber(modid = MODID)
    public static class EventConfigChanged {

        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            //Only process events for this mod
            if (event.getModID().equals(MODID)) {
                proxy.onConfigChanged(event);
            }
        }
    }
}
