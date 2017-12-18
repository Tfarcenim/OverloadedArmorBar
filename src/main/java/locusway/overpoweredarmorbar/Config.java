package locusway.overpoweredarmorbar;

import locusway.overpoweredarmorbar.proxy.CommonProxy;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Level;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config
{
    private static final String CATEGORY_GENERAL = "general";

    private static String[] defaultColorValues = new String[]{"#FFFFFF", "#AAAA00", "#AA0000"};

    public static String[] colorValues = defaultColorValues;
    public static boolean alwaysShowArmorBar = false;
    public static boolean showEmptyArmorIcons = true;

    public static void readConfig()
    {
        Configuration cfg = CommonProxy.config;
        try
        {
            cfg.load();
            initGeneralConfig(cfg);

            boolean colorError = false;
            Pattern colorPattern = Pattern.compile("^#[0-9A-Fa-f]{6}$");
            for (String color : colorValues)
            {
                Matcher matcher = colorPattern.matcher(color);
                if (!matcher.matches())
                {
                    colorError = true;
                }

            }
            if (colorError)
            {
                OverpoweredArmorBar.logger.log(Level.ERROR, "Config error, ColorValues must be in the format \"#RRGGBB\". There is an invalid value in the config file.");
                colorValues = defaultColorValues;
            }
        } catch (Exception e1)
        {
            OverpoweredArmorBar.logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally
        {
            if (cfg.hasChanged())
            {
                cfg.save();
            }
        }
    }

    private static void initGeneralConfig(Configuration cfg)
    {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");


        Property colorValueProperty = cfg.get(
                CATEGORY_GENERAL,
                "ColorValues",
                defaultColorValues,
                "Colors in #RRGGBB format, Each color is used for 20 armor points");
        colorValues = colorValueProperty.getStringList();

        alwaysShowArmorBar = cfg.getBoolean(
                "AlwaysShowArmorBar",
                CATEGORY_GENERAL,
                false,
                "If set to true, the armor bar will be shown even if the player is not wearing armor");

        showEmptyArmorIcons = cfg.getBoolean(
                "ShowEmptyArmorIcons",
                CATEGORY_GENERAL,
                true,
                "If set to true, show empty icons on the armor bar");
    }
}
