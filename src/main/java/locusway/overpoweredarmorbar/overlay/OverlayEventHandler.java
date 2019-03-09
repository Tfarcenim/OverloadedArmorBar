package locusway.overpoweredarmorbar.overlay;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/*
    Class which handles the render event and hides the vanilla armor bar
 */
public class OverlayEventHandler
{
    public OverlayEventHandler(ArmorBarRenderer armorBarRenderer)
    {
        this.armorBarRenderer = armorBarRenderer;
    }

    private ArmorBarRenderer armorBarRenderer;

    @SubscribeEvent(receiveCanceled = true)
    public void renderGameOverlayEvent(RenderGameOverlayEvent event)
    {
        Minecraft mc = Minecraft.getInstance();
        if (event.getType() == RenderGameOverlayEvent.ElementType.ARMOR) {

            int scaledWidth = mc.mainWindow.getScaledWidth();
            int scaledHeight = mc.mainWindow.getScaledHeight();

            armorBarRenderer.renderArmorBar(scaledWidth, scaledHeight);

            /* Don't render the vanilla armor bar */
            event.setCanceled(true);
        }
    }
}