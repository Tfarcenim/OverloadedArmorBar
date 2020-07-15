package tfar.overpoweredarmorbar;


import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class Configs {

    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class ClientConfig {
        public static ForgeConfigSpec.BooleanValue alwaysShowArmorBar;
        public static ForgeConfigSpec.BooleanValue showEmptyArmorIcons;
        public static ForgeConfigSpec.ConfigValue<List<? extends String>> colorValues;

        ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            colorValues = builder
                     .comment("Colors must be specified in #RRGGBB format")
                    .translation("text.overloadedarmorbar.config.colorvalue")
                   .defineList("color values", Lists.newArrayList("#FFFFFF", "#FF5500", "#FFC747", "#27FFE3", "#00FF00", "#7F00FF"), String.class::isInstance);
            alwaysShowArmorBar = builder
                    .comment("Always show armor bar even if empty?")
                    .translation("text.overloadedarmorbar.config.alwaysshowarmorbar")
                    .define("Always show bar", false);
            showEmptyArmorIcons = builder
                    .comment("Show empty armor icons?")
                    .translation("text.overloadedarmorbar.config.showemptyarmoricons")
                    .define("Show empty icons", false);
            builder.pop();
        }
    }
}