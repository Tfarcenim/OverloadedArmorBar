package locusway.overpoweredarmorbar.proxy;

import locusway.overpoweredarmorbar.Config;
import locusway.overpoweredarmorbar.OverpoweredArmorBar;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod.EventBusSubscriber
public class CommonProxy
{

    // Config instance
    public static Configuration config;

    public void preInit(FMLPreInitializationEvent e)
    {
        File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), OverpoweredArmorBar.MODID + ".cfg"));
        Config.readConfig();
    }

    public void init(FMLInitializationEvent e)
    {
    }

    public void postInit(FMLPostInitializationEvent e)
    {
        if (config.hasChanged())
        {
            config.save();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
    }

}
