package locusway.overpoweredarmorbar.overlay;

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
        switch (event.getType())
        {
            case ARMOR:
                int scaledWidth = event.getResolution().getScaledWidth();
                int scaledHeight = event.getResolution().getScaledHeight();
                armorBarRenderer.renderArmorBar(scaledWidth, scaledHeight);

                /* Don't render the vanilla armor bar */
                event.setCanceled(true);
                break;

            default:
                break;
        }
    }
}