package locusway.overpoweredarmorbar;

import net.minecraftforge.common.config.Config;

@Config(modid = OverpoweredArmorBar.MODID)
public class ModConfig
{
    @Config.Name("Armor icon colors")
    @Config.Comment("Colors must be specified in #RRGGBB format")
    public static String[] colorValues = new String[]{"#FFFFFF", "#AAAA00", "#AA0000"};

    @Config.Name("Always show armor bar?")
    public static boolean alwaysShowArmorBar = false;

    @Config.Name("Show empty armor icons?")
    public static boolean showEmptyArmorIcons = true;
}
