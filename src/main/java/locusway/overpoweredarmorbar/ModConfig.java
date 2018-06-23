package locusway.overpoweredarmorbar;

import net.minecraftforge.common.config.Config;

@Config(modid = OverpoweredArmorBar.MODID)
public class ModConfig
{
    @Config.Name("Armor icon colors")
    @Config.Comment("Colors must be specified in #RRGGBB format")
    public static String[] armorColorValues = new String[]{"#FFFFFF", "#AAAA00", "#AA0000"};

    @Config.Name("Health icon colors")
    @Config.Comment("Colors must be specified in #RRGGBB format")
    public static String[] healthColorValues = new String[]{"#FF1313", "#13FF13", "#1313FF"};

    @Config.Name("Always show armor bar?")
    public static boolean alwaysShowArmorBar = false;

    @Config.Name("Show empty armor icons?")
    public static boolean showEmptyArmorIcons = true;

	@Config.Name("Disable custom armor bar?")
	public static boolean disableArmorBar = false;

	@Config.Name("Disable custom health bar?")
	public static boolean disableHealthBar = true;
}
