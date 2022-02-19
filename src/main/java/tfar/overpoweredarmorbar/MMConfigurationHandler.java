package tfar.overpoweredarmorbar;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import de.guntram.mcmod.fabrictools.ConfigurationProvider;
import de.guntram.mcmod.fabrictools.GuiModOptions;

public class MMConfigurationHandler implements ModMenuApi
{

    @Override
    public ConfigScreenFactory<GuiModOptions> getModConfigScreenFactory() {
        return screen -> new GuiModOptions(screen, OverloadedArmorBar.MODNAME, ConfigurationProvider.getHandler(OverloadedArmorBar.MODNAME));
    }
}
