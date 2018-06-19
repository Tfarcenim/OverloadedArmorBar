package locusway.overpoweredarmorbar.overlay;

import locusway.overpoweredarmorbar.ModConfig;
import locusway.overpoweredarmorbar.overlay.armorbar.ArmorBarRenderer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
    public void onRenderGameOverlayEventPre(RenderGameOverlayEvent.Pre event)
    {
        int scaledWidth = event.getResolution().getScaledWidth();
        int scaledHeight = event.getResolution().getScaledHeight();

        switch (event.getType())
        {
            case ARMOR:
                if (!ModConfig.disableArmorBar)
                {
                    armorBarRenderer.renderArmorBar(scaledWidth, scaledHeight);
                    /* Don't render the vanilla armor bar */
                    event.setCanceled(true);
                }
                break;

            default:
                break;
        }
    }
}